/*
    Code written and maintained by
    Ugochukwu Precious Onah
    24848777
 */
package org.mjb.webServer;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.mjb.dao.CustomerDAO;
import org.mjb.dao.UserDAO;
import org.mjb.systemModels.Customer;
import org.mjb.systemModels.User;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;

/**
 * The {@code WebLogin} class handles HTTP requests for the login page and login attempts in the web server.
 * It interacts with the {@link WebCookie} class to manage session data for logged-in users and customers.
 * This class implements the {@link com.sun.net.httpserver.HttpHandler} interface and processes both
 * {@code GET} and {@code POST} HTTP requests to provide login functionalities.
 */
public class WebLogin implements HttpHandler {
    private WebCookie cookie;

    /**
     * Constructs a new {@code WebLogin} instance with the given {@code WebCookie}.
     *
     * @param cookie the {@code WebCookie} instance that will manage the session for logged-in users and customers.
     */
    public WebLogin(WebCookie cookie) {
        this.cookie = cookie;
    }

    public void handle(HttpExchange e) throws IOException {
        BufferedWriter w = new BufferedWriter(new OutputStreamWriter(e.getResponseBody()));
        switch (e.getRequestMethod()) {
            case "GET" -> {
                loadPage(w, e);
            }
            case "POST" -> {
                postMapping(w, e);
            }
            default -> {
                w.write("Invalid Request Method");
            }
        }
        w.close();
    }

    /**
     * Loads and renders the login page for {@code GET} requests.
     * This method clears any previously logged-in session data and sends back the login form.
     *
     * @param w the {@code BufferedWriter} used to send the response body.
     * @param e the {@link HttpExchange} representing the HTTP request and response.
     * @throws IOException if an I/O error occurs while sending the response.
     */

    public void loadPage(BufferedWriter w, HttpExchange e) throws IOException {
        cookie.setLoggedUser(null);
        cookie.setLoggedCustomer(null);
        w.write(new WebHelper().buildPage(cookie, """
                <div class="row" >
                    <div class="col-md-6 offset-md-3">
                        <h2>Login to Ugochukwu Appliances</h2>
                        <form action="/login" method="POST">
                          <div class="form-group">
                            <label for="email">Email/Username</label>
                            <input type="text" name="email" class="form-control" id="email">
                          </div>
                          <div class="form-group">
                            <label for="password">Password</label>
                            <input type="password" name="password" class="form-control" id="password">
                          </div>
                          <div class="mt-4">
                              <button type="submit" class="btn btn-primary">Try Login</button>
                          </div>
                        </form>
                    </div>
                </div>
                """)
        );
        e.sendResponseHeaders(200,0);
    }

    /**
     * Processes the {@code POST} request containing login credentials.
     * This method attempts to authenticate the user and customer with the provided email and password.
     *
     * <p>If both user and customer login attempts fail, the user is redirected back to the login page.</p>
     * <p>If the login is successful, the user is redirected to the home page.</p>
     *
     * @param w the {@code BufferedWriter} used to send the response body.
     * @param e the {@link HttpExchange} representing the HTTP request and response.
     * @throws IOException if an I/O error occurs while processing the request or sending the response.
     */

    public void postMapping(BufferedWriter w, HttpExchange e) throws IOException {
        HashMap<String, String> body = new WebHelper().formatRequest(e);
        User user = new UserDAO().loginAsUser(body.get("email"),body.get("password"));
        Customer customer = new CustomerDAO().logInAsCustomer(body.get("email"),body.get("password"));
        cookie.setLoggedUser(user);
        cookie.setLoggedCustomer(customer);
        if(cookie.getLoggedUser() == null && cookie.getLoggedCustomer() == null){
            e.getResponseHeaders().add("Location","/login");
            e.sendResponseHeaders(302,0);
            return;
        }
        e.getResponseHeaders().add("Location","/");
        e.sendResponseHeaders(302,0);
    }
}
