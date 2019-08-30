/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package walletbotdevcamp.database;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import org.apache.log4j.Logger;

/**
 *
 * @author cifran
 */
public class Database {
    private final Logger logger = Logger.getLogger(Database.class);
    private final String tableName = "transaction";
    private final String dbUrl = "jdbc:mysql://localhost:9000/devcamp";
    private final String dbUser = "devcamp";
    private final String dbPass = "devcamp2019";
    private final String dbDriver = "com.mysql.jdbc.Driver";
    
    public double getBalance(String userId) {
        double totalBalance = 0;
        
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = "SELECT Amount, Type FROM "+tableName+" WHERE userId = ?";
        try {
            Class.forName(dbDriver).newInstance();
            conn = DriverManager.getConnection(dbUrl,dbUser,dbPass);  
            ps = conn.prepareStatement(query);
            ps.setObject(1, userId);
            rs = ps.executeQuery();
            
            while(rs.next()) {
                Double amount = rs.getDouble("Amount");
                String type = rs.getString("Type");
                
                if (type.equals("Income")) {
                    totalBalance += amount;
                } else if (type.equals("Expense")) {
                    totalBalance -= amount;
                }
            }
            
        } catch (Exception ex) {
            logger.error(ex);
        } finally {
            DatabaseUtility util = new DatabaseUtility();
            util.closeResultSet(rs);
            util.closePreparedStatement(ps);
            util.closeConnection(conn);
        }
        logger.debug(query+", "+userId+" Result: "+totalBalance);
        return totalBalance;
    }
    
    public JsonArray getTransactions(String userId, Timestamp startDate, Timestamp endDate) {
        JsonArray array = new JsonArray();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = 
                "SELECT * FROM "+tableName+" WHERE userid = ? "
                + " AND Timestamp >= ?"
                + " AND Timestamp <= ?"
                + " ORDER BY Timestamp DESC";
        
        final String[] paramNames = {"Category", "Amount", "Type", "Timestamp", "Note"};
        try {
            Class.forName(dbDriver).newInstance();
            conn = DriverManager.getConnection(dbUrl,dbUser,dbPass);  
            ps = conn.prepareStatement(query);
            
            ps.setObject(1, userId);
            ps.setObject(2, startDate);
            ps.setObject(3, endDate);
            
            rs = ps.executeQuery();
            
            while(rs.next()) {
                JsonObject obj = new JsonObject();
                
                for (String key: paramNames) {
                    if (key.equals("Timestamp")) {
                        Timestamp ts = new Timestamp(rs.getTimestamp(key).getTime());
                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                        obj.addProperty(key, sdf.format(ts));
                        
                    } else {
                        try {
                            int nom = Integer.parseInt(rs.getString(key));
                            obj.addProperty(key, nom);
                        } catch(Exception ex) {
                            obj.addProperty(key, rs.getString(key));
                        }
                    }
                }
                array.add(obj);
            }
            
        } catch (Exception ex) {
            logger.error(ex);
        } finally {
            DatabaseUtility util = new DatabaseUtility();
            util.closeResultSet(rs);
            util.closePreparedStatement(ps);
            util.closeConnection(conn);
        }
        logger.debug(query+","+userId+","+startDate+","+endDate+" === "+array.toString());
        return array;
    }

    public String createQuery(String recordKeys, String recordValues) {
        return "INSERT INTO " + tableName
                + " ("+recordKeys+") VALUES"
                + " ("+recordValues+")";
    }

    public void executeUpdate(String query) {
        Connection conn = null;
        PreparedStatement ps = null;
        int result = -1;
        try {
            Class.forName(dbDriver).newInstance();
            conn = DriverManager.getConnection(dbUrl,dbUser,dbPass);  
            ps = conn.prepareStatement(query);
            result = ps.executeUpdate();
        } catch (Exception ex) {
            logger.error(ex);
        } finally {
            DatabaseUtility util = new DatabaseUtility();
            util.closePreparedStatement(ps);
            util.closeConnection(conn);
        }
        logger.debug(query+", Result: "+result);
    }
}
