/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package walletbotdevcamp.database;

import org.apache.log4j.Logger;

/**
 *
 * @author cifran
 */
public class DatabaseUtility {

    private final Logger logger = Logger.getLogger(DatabaseUtility.class);

    public final void closeResultSet(java.sql.ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (java.sql.SQLException ex) {
                logger.error("SQL Exception in Utility.closeResultSet method:", ex);
            }
        }
    }

    public final void closePreparedStatement(java.sql.PreparedStatement ps) {
        if (ps != null) {
            try {
                ps.close();
            } catch (java.sql.SQLException ex) {
                logger.error("SQL Exception in Utility.closePreparedStatement method:", ex);
            }
        }
    }

    public final void closeConnection(java.sql.Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (java.sql.SQLException ex) {
                logger.error("SQL Exception in Utility.closeConnection method:", ex);
            }
        }
    }
}
