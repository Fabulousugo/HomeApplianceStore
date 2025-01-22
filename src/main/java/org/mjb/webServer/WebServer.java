/*
    Code written and maintained by
    Ugochukwu Precious Onah
    24848777
 */

package org.mjb.webServer;

import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * The WebServer class sets up an HTTP server that listens for incoming HTTP requests on a specified port.
 * It routes requests to different contexts (URLs) for handling various functionalities such as product
 * listing, customer management, login, cart, and order processing.
 * <p>
 * The server is configured with multiple request contexts, each handled by a specific HttpHandler.
 * </p>
 *
 * <p>
 * Example usage:
 * <pre>
 * WebServer server = new WebServer(8080);
 * </pre>
 * This will start the server on port 8080.
 * </p>
 *
 * <p>
 * The server will output messages to the console for successful startup or any errors encountered during initialization.
 * </p>
 */
public class WebServer {

    /**
     * Instantiates a new WebServer that listens on the provided port and sets up request contexts for various routes.
     *
     * @param port the port number on which the server will listen for incoming requests
     * @throws IOException if an I/O error occurs during the creation or initialization of the server
     * @throws Exception if an unexpected error occurs while setting up server contexts or starting the server
     */
    public WebServer(int port) {
        try {
            WebCookie cookie = new WebCookie(null, null);
            HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

            server.setExecutor(null); // Use default executor

            try {
                // Create contexts for different routes
                server.createContext("/", new WebProduct(cookie));
                server.createContext("/customers", new WebCustomer(cookie));
                server.createContext("/login", new WebLogin(cookie));
                server.createContext("/cart", new WebCart(cookie));
                server.createContext("/orders", new WebOrder(cookie));
            } catch (Exception e) {
                System.err.println("Error while creating server contexts: " + e.getMessage());
                e.printStackTrace();
            }

            // Start the server
            server.start();
            System.out.println("Server running on port: " + port);

        } catch (IOException e) {
            System.err.println("Error initializing the web server on port " + port + ": " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
