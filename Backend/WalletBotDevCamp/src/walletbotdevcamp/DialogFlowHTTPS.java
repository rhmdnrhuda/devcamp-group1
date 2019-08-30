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
        
        String recordKeys = "";     // for insert 
        String recordValues = "";   // for insert
        String recordPairs = "";    // for update
        
        for (Map.Entry<String, JsonElement> entry : params.entrySet()) {
            String key = entry.getKey();
            String val = entry.getValue().getAsString();
            
            recordKeys += key + ",";
            recordValues += "\"" + val + "\",";
            recordPairs += key + "=\"" + val + "\",";
        }
        recordKeys = trimSuffix(recordKeys);
        recordValues = trimSuffix(recordValues);
        recordPairs = trimSuffix(recordPairs);
        
        Database db = new Database();
        String query = db.createQuery(recordKeys, recordValues, recordPairs);
        db.executeUpdate(query);
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
//                case "registerBalance":
//                    processRegister(response.getResult().getParameters());
            }
            for (ResponseMessage msg : response.getResult().getFulfillment().getMessages()) {
                JsonParser parser = new JsonParser();
                JsonObject jsonMessage = (JsonObject) parser.parse(msg.toString());
                
                JsonObject newJson = new JsonObject();
                newJson.add("message", jsonMessage.get("speech"));
                arr.add(newJson);
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