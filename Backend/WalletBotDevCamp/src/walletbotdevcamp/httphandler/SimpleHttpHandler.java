/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package walletbotdevcamp.httphandler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 *
 * @author cifran
 */
public class SimpleHttpHandler implements HttpHandler {
    final Logger logger = Logger.getLogger(SimpleHttpHandler.class);

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        int requestIdentifier = httpExchange.hashCode();
        logger.debug(requestIdentifier + " => got hit " + new Timestamp(System.currentTimeMillis()));

        String fullRequestBody = getRequestBody(httpExchange.getRequestBody(), httpExchange.getRequestMethod(), httpExchange.getRequestURI().getQuery());
        logger.debug(requestIdentifier + " => receive request = " + fullRequestBody);

        String response = processRequest(requestIdentifier, fullRequestBody);
        logger.debug(requestIdentifier + " => send response = "+response);

        closeStream(httpExchange, response);
    }

    private String getRequestBody(InputStream inputStream, String method, String query) {
        String fullRequestBody = "";
        
        if (method.equals("POST")) {
            InputStreamReader isr = new InputStreamReader(inputStream);
            BufferedReader reader = new BufferedReader(isr);

            try {
                String requestBody = null;
                while((requestBody = reader.readLine()) != null) {
                    fullRequestBody += requestBody;
                }
            } catch (IOException ex) {
                logger.error(ex);
            }

            try { reader.close(); } catch (IOException ex) { logger.error(ex); }
            try { isr.close(); } catch (IOException ex) { logger.error(ex); }
            try { inputStream.close(); } catch (IOException ex) { logger.error(ex); }
        } else {
            fullRequestBody = query;
        }

        return fullRequestBody;
    }
    
    private void closeStream(HttpExchange httpExchange, String response) throws IOException {
        httpExchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");

        if (httpExchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
            httpExchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, OPTIONS");
            httpExchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type,Authorization");
            httpExchange.sendResponseHeaders(204, -1);
            return;
        }
    
        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
        httpExchange.close();
    }
    
    public Map<String, String> queryToMap(String query) {
        Map<String, String> result = new HashMap<>();
        for (String param : query.split("&")) {
            String[] entry = param.split("=");
            if (entry.length > 1) {
                result.put(entry[0], entry[1]);
            }else{
                result.put(entry[0], "");
            }
        }
        return result;
    }

    public String processRequest(int requestIdentifier, String fullRequestBody) {
        return "internal error";
    }
}
