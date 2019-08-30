/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package walletbotdevcamp.processor;

import com.google.gson.JsonObject;
import org.apache.log4j.Logger;
import walletbotdevcamp.DialogFlowHTTPS;

/**
 *
 * @author cifran
 */

public class ChatProcessor {
    private final Logger logger = Logger.getLogger(ChatProcessor.class);
    private int requestIdentifier = 0;
    private String sessionIdentifier = "";
    private String message = "";
    
    public ChatProcessor(int requestIdentifier, JsonObject parameters) {
        this.requestIdentifier = requestIdentifier;
        try { this.sessionIdentifier = parameters.get("userid").getAsString(); } catch(Exception ex){ logger.error(ex); }
        try { this.message = parameters.get("message").getAsString(); } catch(Exception ex){ logger.error(ex); }
    }

    public boolean isRequestValid() {
        return !(message.isEmpty() || sessionIdentifier.isEmpty());
    }

    public String process() {
        DialogFlowHTTPS dialogflow = new DialogFlowHTTPS(this.sessionIdentifier);
        return dialogflow.submitQueryAI(message).toString();
    }
            
}
