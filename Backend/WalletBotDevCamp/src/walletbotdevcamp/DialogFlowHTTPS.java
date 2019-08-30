/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package walletbotdevcamp;

import ai.api.AIConfiguration;
import ai.api.AIDataService;
import ai.api.AIServiceException;
import ai.api.model.AIContext;
import ai.api.model.AIEvent;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import ai.api.model.ResponseMessage;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import walletbotdevcamp.database.Database;

/**
 *
 * @author cifran
 */

public class DialogFlowHTTPS {
    private final Logger logger = Logger.getLogger(DialogFlowHTTPS.class);
    
    private final String protocolVersion = "20170712";
    private final String timeZone = "Asia/Jakarta";
    private final String languange = "id";
    private String sessionId = "";
    private String clientKey = "f790581ea3fc4d96b05548c56aec14dc";
    private AIDataService dataService = null;
    private final String ALL_EXPENSES = "tagihan,belanja,hiburan,makanan";
    private final String ALL_INCOMES = "gaji,hadiah,penjualan,tabungan";
    
    public DialogFlowHTTPS(String sessionId) {
        this.sessionId = sessionId;
        
        AIConfiguration config = new AIConfiguration(clientKey);
        config.setProtocolVersion(protocolVersion);
        this.dataService = new AIDataService(config);
    }
    
    public JsonArray submitQueryAI(String message) {
        return submitQueryAI(message, null, false, null);
    }
    
    private void processInsert(HashMap<String, JsonElement> params) {
        if (params.size() < 1) {return;}    // should have at least 1 params
        
        String recordKeys = "userid,";     // for insert 
        String recordValues = "\""+this.sessionId+"\",";   // for insert
        
        for (Map.Entry<String, JsonElement> entry : params.entrySet()) {
            
            String key = entry.getKey();
            String val = entry.getValue().getAsString();
            
            String[] res1 = putToQuery(key, val);
            recordKeys += res1[0];
            recordValues += res1[1];
            
            if (key.equals("Category")) {
                String[] res2 = putToQuery("Type", convertCategory(val));
                recordKeys += res2[0];
                recordValues += res2[1];
            }
        }
        recordKeys = trimSuffix(recordKeys);
        recordValues = trimSuffix(recordValues);
        
        Database db = new Database();
        String query = db.createQuery(recordKeys, recordValues);
        db.executeUpdate(query);
    }

    private String convertCategory(String val) {
        if (ALL_EXPENSES.toLowerCase().contains(val.toLowerCase())) {
            return "Expense";
        } else if (ALL_INCOMES.toLowerCase().contains(val.toLowerCase())) {
            return "Income";
        } else {
            return "Expense";
        }
    }
    
    private String[] putToQuery(String key, String val) {
        String recordKeys = "";
        String recordValues = "";
        
        recordKeys += key + ",";
        if (key.equals("Timestamp")) {

            if (val.contains("T")) {
                String newVal = val.substring(0, 19).replaceAll("T", " ");
                recordValues += "\"" + newVal + "\",";
            } else {
                recordValues += "NOW(),";
            }

        } else {
            recordValues += "\"" + val + "\",";
        }
        return new String[] {recordKeys, recordValues};
    }
    
    public JsonArray submitQueryAI(String message, List<AIContext> initialContexts, Boolean resetContext, String eventName) {
        JsonArray arr = new JsonArray();
        try {
            AIRequest request = new AIRequest();
            
            if (eventName != null) {
                request.setEvent(new AIEvent(eventName));
            } else {
                request.setQuery(message);
            }
            request.setSessionId(sessionId);
            request.setLanguage(languange);
            request.setTimezone(timeZone);
            request.setResetContexts(resetContext);
            
            if (initialContexts != null && initialContexts.size() > 0) {
                request.setContexts(initialContexts);
            }
            AIResponse response = dataService.request(request);
            
            
            switch(response.getResult().getAction()) {
                case "insertRecord": 
                    processInsert(response.getResult().getParameters());
            }
            
            for (ResponseMessage msg : response.getResult().getFulfillment().getMessages()) {
                
                JsonParser parser = new JsonParser();
                JsonObject jsonMessage = (JsonObject) parser.parse(msg.toString());
                
                String msgString = jsonMessage.get("speech").getAsString();
                
                
                if ("getReport".equals(response.getResult().getAction())) {
                    
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//                    msgString = msgString.replaceFirst("\\$dateTime.startDate", sdf.format(new Date()));
//                    msgString = msgString.replaceFirst("\\$dateTime.endDate", sdf.format(new Date()));
//                    
//                    try {
//                        String[] parsed = msgString.split("\\,");
//                        String mmsgString = parsed[0].trim() + "," + parsed[1].trim().substring(0, 10) + "," + parsed[2].trim().substring(0, 10);
//                        msgString = mmsgString;
//                    } catch(Exception ex) {
//                        logger.error(ex);
//                    }


                    Calendar c = Calendar.getInstance();   // this takes current date
                    c.set(Calendar.DAY_OF_MONTH, 1);
    
                    String[] parsedMsg = msgString.split(",");
                    String startDate = sdf.format(c.getTime());
                    String endDate = sdf.format(new Date());
                    try {
                        if (parsedMsg.length >= 1) {
                            String[] parsedDateTime = parsedMsg[1].split("/");

                            if (parsedDateTime.length >= 1) {
                                startDate = parsedDateTime[0];
                            }
                            if (parsedDateTime.length >= 2) {
                                endDate = parsedDateTime[1];
                            }
                        }
                    } catch(Exception ex) {
                        logger.error(ex);
                    }
                    msgString = parsedMsg[0].trim() + "," + startDate.trim() + "," + endDate.trim();
                }
                
                arr.add(msgString);
            }
        } catch (AIServiceException ex) {
            logger.error(ex);
        }
        return arr;
    }
    
    private String trimSuffix(String str) {
        return str.substring(0, str.length()-1);
    }
}