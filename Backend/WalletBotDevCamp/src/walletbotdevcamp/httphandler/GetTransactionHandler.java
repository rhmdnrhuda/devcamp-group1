/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package walletbotdevcamp.httphandler;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import walletbotdevcamp.database.Database;
import walletbotdevcamp.processor.ChatProcessor;

/**
 *
 * @author cifran
 */
public class GetTransactionHandler extends SimpleHttpHandler {
    @Override
    public String processRequest(int requestIdentifier, String fullRequestBody) {
        JsonArray array = new JsonArray();
        try {
            Map<String, String> params = queryToMap(fullRequestBody);
            
            String userId = params.get("userid");
            String startDate = params.get("startdate");
            String endDate = params.get("enddate");
            
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date dateStartDate = sdf.parse(startDate);
            Date dateEndDate = sdf.parse(endDate);
            Timestamp timestampStartDate = new Timestamp(dateStartDate.getTime());
            Timestamp timestampEndDate = new Timestamp(dateEndDate.getTime());
            
            array = new Database().getTransactions(userId, timestampStartDate, timestampEndDate);
        } catch (Exception ex) {
            logger.error(ex);
        }
        return array.toString();
    }
}
