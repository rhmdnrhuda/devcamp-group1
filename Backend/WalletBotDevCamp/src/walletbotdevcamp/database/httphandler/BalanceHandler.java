/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package walletbotdevcamp.database.httphandler;

import com.google.gson.JsonArray;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Map;
import walletbotdevcamp.database.Database;

/**
 *
 * @author cifran
 */
public class BalanceHandler extends SimpleHttpHandler {
    
    @Override
    public String processRequest(int requestIdentifier, String fullRequestBody) {
        Map<String, String> params = queryToMap(fullRequestBody);
        
        double balance = new Database().getBalance(params.get("userid"));
        return String.valueOf(balance);
    }
}
