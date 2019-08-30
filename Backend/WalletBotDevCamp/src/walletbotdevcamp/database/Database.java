/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package walletbotdevcamp.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
    
    public void insertTransaction() {
        
    }
    
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
            util.closePreparedStatement(ps);
            util.closeConnection(conn);
            util.closeResultSet(rs);
        }
        logger.debug(query+", "+userId+" Result: "+totalBalance);
        return totalBalance;
    }
}
