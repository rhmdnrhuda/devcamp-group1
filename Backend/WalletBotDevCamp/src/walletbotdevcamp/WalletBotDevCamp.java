/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package walletbotdevcamp;

import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import org.apache.log4j.Logger;
import walletbotdevcamp.httphandler.AddTransactionHandler;
import walletbotdevcamp.httphandler.BalanceHandler;
import walletbotdevcamp.httphandler.GetTransactionHandler;

/**
 *
 * @author cifran
 */
public class WalletBotDevCamp {
    private static final Logger logger = Logger.getLogger(WalletBotDevCamp.class);

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            logger.info("Starting up HTTP server localhost");
            
            HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
            server.createContext("/getBalance", new BalanceHandler());
            server.createContext("/addTransaction", new AddTransactionHandler());
            server.createContext("/getTransactions", new GetTransactionHandler());
            
            server.setExecutor(Executors.newFixedThreadPool(10));
            server.start();
            logger.info("HTTP Server started ==>   http://localhost:8000/");
            
        } catch (IOException ex) {
            logger.error(ex);
        }
    }
    
}
