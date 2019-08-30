/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package walletbotdevcamp.httphandler;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            
    @Override
    public String processRequest(int requestIdentifier, String fullRequestBody) {
        JsonArray result = new JsonArray();
        try {
            Map<String, String> params = queryToMap(fullRequestBody);
            
            String userId = params.get("userid");
            String startDate = params.get("startdate");
            String endDate = params.get("enddate");
            
            Date dateStartDate = sdf.parse(startDate);
            Date dateEndDate = sdf.parse(endDate);
            
//            // looping per day
//            Date current = dateStartDate;
//            while(!current.after(dateEndDate)) {
//                Date nextDate = getPrevDate(current);
//                
//                JsonObject dailyData = fetchData(userId, current, nextDate);
//                result.add(dailyData);
//                
//                current = nextDate;
//            }
            Date current = dateEndDate;
            while(!current.before(dateStartDate)) {
                Date prevDate = getPrevDate(current);
                
                JsonObject dailyData = fetchData(userId, current, getNextDate(current));
                if (dailyData != null) {
                    result.add(dailyData);
                }
                
                current = prevDate;
            }
        } catch (Exception ex) {
            logger.error(ex);
        }
        return result.toString();
    }
    
    private Date getNextDate(Date current) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(current);
        calendar.add(Calendar.DATE, 1);
        return calendar.getTime();
    }
    
    private Date getPrevDate(Date current) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(current);
        calendar.add(Calendar.DATE, -1);
        return calendar.getTime();
    }
    
    private JsonObject fetchData(String userId, Date current, Date nextDate) {
        Timestamp currTime = new Timestamp(current.getTime());
        Timestamp nextTime = new Timestamp(nextDate.getTime());
        
        JsonArray records = new Database().getTransactions(userId, currTime, nextTime);
        
        if (records.size() > 0) {
            JsonObject dailyData = new JsonObject();
            dailyData.add("data", records);
            dailyData.addProperty("tanggal", sdf.format(current));
            dailyData.addProperty("total", getDailyTotal(records));
            return dailyData;
        }
        return null;
    }
    
    private double getDailyTotal(JsonArray records) {
        double total = 0;
        for (JsonElement json : records) {
            try {
                JsonObject obj = (JsonObject) json;
                
                Double amount = obj.get("Amount").getAsDouble();
                String type = obj.get("Type").getAsString();

                if (type.equals("Income")) {
                    total += amount;
                } else if (type.equals("Expense")) {
                    total -= amount;
                }
            } catch(Exception ex) {
                logger.error(ex);
            }
        }
        return total;
    }
}
