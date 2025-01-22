/*
 * Code written and maintained by
 * Ugochukwu Precious Onah
 * 24848777
 */
package org.mjb.webServer;

import com.sun.net.httpserver.HttpExchange;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

/**
 * The {@code WebHelper} class provides utility functions to assist with handling HTTP requests
 * and generating dynamic HTML content. It includes methods for generating HTML headers and footers,
 * processing HTTP query strings, and formatting request parameters into a map.
 * <p>
 * This class is primarily used to generate content for a web server, including customer interactions,
 * product display, cart management, and login status handling.
 * </p>
 */
public class WebHelper {

    /**
     * Generates the HTML header for a web page, including the navigation bar and links.
     * The header content changes based on the user's login status and the information stored
     * in the {@code WebCookie}.
     *
     * @param cookie an instance of {@link WebCookie} containing session information
     * @return a string representing the HTML header, including the navigation bar
     */
    public String getHeader(WebCookie cookie) {
        return String.format(""" 
            <!DOCTYPE html>
            <html lang="en" >
            <head>
              <meta charset="UTF-8">
              <title>Ugochukwu Store</title>
              <link rel='stylesheet' href='https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css'>  
                <script src='https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js'></script>
            </head>
            <body>
            <nav class="navbar navbar-expand-lg navbar-dark" style="background-color: #071d2f;">
              <div class="container-fluid">
                <a class="navbar-brand" href="/">Ugochukwu Store</a>
                <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation style="background-color: #FF0000; color: #FFFFFF;">
                  <span class="navbar-toggler-icon"></span>
                </button>
                <div class="collapse navbar-collapse" id="navbarSupportedContent">
                  <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                    <li class="nav-item dropdown">
                      <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                        Products
                      </a>
                      <ul class="dropdown-menu">
                        <li><a class="dropdown-item" href="/">All Products</a></li>
                        <li>
                          <hr class="dropdown-divider">
                        </li>
                        <li><a class="dropdown-item" href="/?expired">Expired Products</a></li>
                      </ul>
                    </li>
                    %s
                  </ul>
                </div>
              </div>
            </nav>
            <div class="container pa-5 p-5">
            """, cookie.getLoggedCustomer() != null ? """
                <li class="nav-item">
                  <a href="/orders" class="nav-link">Orders</a>
                </li>
                 <li class="nav-item">
                   <a href="/cart" class="nav-link">Cart</a>
                 </li>
                <li class="nav-item">
                  <a href="/login" class="nav-link">Logout</a>
                </li>
                """ : (
                cookie.getLoggedUser() != null ? """
                            <li class="nav-item">
                              <a href="/customers" class="nav-link">Customers</a>
                            </li>
                            <li class="nav-item">
                              <a href="/orders" class="nav-link">Orders</a>
                            </li>
                            <li class="nav-item">
                              <a href="/login" class="nav-link">Logout</a>
                            </li>
                            """: """
                                <li class="nav-item">
                                  <a href="/login" class="nav-link">Login</a>
                                </li>
                            """
        ));
    }

    /**
     * Generates the HTML footer for a web page, including any necessary styles for page animations.
     *
     * @param cookie an instance of {@link WebCookie} containing session information
     * @return a string representing the HTML footer of the page
     */
    public String getFooter(WebCookie cookie) {
        return """
                <style>
                    .dropdown-menu {
                      animation: 0.5s slideup;
                    }
                    
                    @keyframes slideup {
                      from {
                        transform: translateY(10%);
                      }
                    
                      to {
                        transform: translateY(0);
                      }
                    }
                </style> 
            </div>
            </body>
            </html>
            """;
    }

    /**
     * Builds a complete HTML page by concatenating the header, provided content, and the footer.
     * This method generates the full page structure dynamically based on session data and additional content.
     *
     * @param cookie an instance of {@link WebCookie} used to generate the header and footer
     * @param arguments a variable number of strings representing content to be inserted between the header and footer
     * @return a string representing the complete HTML page
     */
    public String buildPage(WebCookie cookie, String... arguments) {
        StringBuilder s = new StringBuilder();
        s.append(getHeader(cookie));
        for (String i : arguments) {
            s.append(i);
        }
        s.append(getFooter(cookie));
        return s.toString();
    }

    /**
     * Converts a URL-encoded query string into a {@link HashMap} of key-value pairs.
     * Decodes the keys and values to handle special characters such as spaces and symbols.
     *
     * @param s the URL-encoded query string to be formatted
     * @return a {@link HashMap} containing the decoded key-value pairs from the query string
     */
    public HashMap<String, String> formatStringToMap(String s) {
        HashMap<String, String> map = new HashMap<String, String>();
        String[] pairs = s.toString().split("&");
        for (String pair : pairs) {
            String[] kv = pair.split("=");
            String key = kv[0];
            key = URLDecoder.decode(key, StandardCharsets.UTF_8);

            String value;
            try {
                value = kv[1];
            } catch (Exception e) {
                value = "";
            }
            value = URLDecoder.decode(value, StandardCharsets.UTF_8);

            map.put(key, value);
        }
        return map;
    }

    /**
     * Extracts the request body from the HTTP exchange and formats it into a {@link HashMap}
     * of key-value pairs. This is used for POST data submitted via forms.
     *
     * @param exchange the {@link HttpExchange} object representing the HTTP request
     * @return a {@link HashMap} containing the formatted request parameters
     * @throws IOException if an error occurs while reading the request body
     */
    public HashMap<String, String> formatRequest(HttpExchange exchange) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(exchange.getRequestBody()));
        String line;
        StringBuilder request = new StringBuilder();
        while ((line = in.readLine()) != null) {
            request.append(line);
        }
        return formatStringToMap(request.toString());
    }

    /**
     * Extracts and formats the query string from the URL of an HTTP request into a {@link HashMap}
     * of key-value pairs.
     *
     * @param exchange the {@link HttpExchange} object representing the HTTP request
     * @return a {@link HashMap} containing the formatted query parameters
     * @throws IOException if an error occurs while processing the query string
     */
    public HashMap<String, String> formatQuery(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();
        HashMap<String, String> map = new HashMap<String, String>();
        if (query == null) {
            return map;
        }
        return formatStringToMap(query);
    }
}
