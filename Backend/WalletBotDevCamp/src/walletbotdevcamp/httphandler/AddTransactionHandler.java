/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package walletbotdevcamp.httphandler;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import walletbotdevcamp.processor.ChatProcessor;

/**
 *
 * @author cifran
 */
public class AddTransactionHandler extends SimpleHttpHandler  {

    @Override
    public String processRequest(int requestIdentifier, String fullRequestBody) {
        JsonObject parameters = new JsonObject();
        try {
            parameters = (JsonObject) new JsonParser().parse(fullRequestBody);
        } catch(Exception ex) {
            logger.error(ex);
        }
        ChatProcessor processor = new ChatProcessor(requestIdentifier, parameters);
        if (processor.isRequestValid()) {
            return processor.process();
        }
        return "internal error";
    }
    
}
