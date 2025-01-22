/*
    Code written and maintained by
    Ugochukwu Precious Onah
    24848777
 */
package org.mjb.webServer;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.mjb.dao.OrdersDAO;
import org.mjb.systemModels.Order;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * The {@code WebOrder} class handles HTTP requests related to customer orders.
 * It manages the functionality for viewing, creating, and deleting orders.
 *
 * This class ensures that only logged-in users or customers can access the order functionality.
 * It integrates with the {@link OrdersDAO} to manage orders in the database.
 */
public class WebOrder implements HttpHandler {
    private WebCookie cookie;

    /**
     * Instantiates a new {@code WebOrder}.
     *
     * @param cookie the {@link WebCookie} object for session management
     */
    public WebOrder(WebCookie cookie){
        this.cookie = cookie;
    }

    /**
     * Handles HTTP requests and routes them based on the request URI path.
     * The following paths are handled:
     * <ul>
     *   <li>{@code /orders}: Loads the orders page for the logged-in user or customer.</li>
     *   <li>{@code /orders/create}: Creates a new order for the logged-in customer.</li>
     *   <li>{@code /orders/delete}: Deletes an order based on the provided ID.</li>
     * </ul>
     * If the user or customer is not logged in, they are redirected to the login page.
     *
     * @param e the {@link HttpExchange} object containing the HTTP request and response details
     * @throws IOException if an I/O error occurs during request handling
     */
    public void handle(HttpExchange e) throws IOException {
        if(cookie.getLoggedCustomer() == null && cookie.getLoggedUser() == null){
            e.getResponseHeaders().add("Location","/login");
            e.sendResponseHeaders(302,0);
            return;
        }
        BufferedWriter w = new BufferedWriter(new OutputStreamWriter(e.getResponseBody()));
        switch(e.getRequestURI().getPath()){
            case "/orders" -> {
                loadPage(w, e);
            }
            case "/orders/create" -> {
                createOrder(w, e);
            }
            case "/orders/delete" -> {
                deleteOrder(w, e);
            }
            default -> {
                w.write("Invalid Path");
            }
        }
        w.close();
    }

    /**
     * Loads and displays the orders page for the logged-in user or customer.
     * The orders are fetched from the database and displayed in a table format.
     * Each order includes a delete button.
     *
     * @param w the {@link BufferedWriter} to write the response body
     * @param e the {@link HttpExchange} object containing the HTTP request and response details
     * @throws IOException if an I/O error occurs while writing the response
     */
    public void loadPage(BufferedWriter w, HttpExchange e) throws IOException {
        StringBuilder t = new StringBuilder();
        for(Order o : cookie.getLoggedUser() != null ? new OrdersDAO().findOrders() : new OrdersDAO().findCustomerOrders(cookie.getLoggedCustomer().getId())){
            t.append(String.format("""
                    <tr>
                        <td>%s</td>
                        <td>%s</td>
                        <td>%s</td>
                        <td>%s</td>
                        <td>%s</td>
                        <td>£%s</td>
                        <td>%s</td>
                    """,o.getId(),o.getInvoiceNo(),o.getItems().size(),
                    o.getCustomer().getName(),o.getCustomer().getAddress().getPostCode(),
                    o.getTotal(),o.getCreatedOn()));
            t.append(String.format("""
                    <td>
                        <a href="/orders/delete?id=%s" class="btn btn-primary me-2 btn-sm">Delete</a>
                    </td>
                    """,o.getId()));
            t.append("</tr>");
        }
        w.write(new WebHelper().buildPage(cookie, """
                <h2>Orders</h2>
                <div class="table-responsive">
                    <table class="table table-hover">
                      <thead>
                        <tr>
                          <th scope="col">#</th>
                          <th scope="col">Order No</th>
                          <th scope="col">Order Items</th>
                          <th scope="col">Customer Name</th>
                          <th scope="col">Post Code</th>
                          <th scope="col">Amount</th>
                          <th scope="col">Date</th>
                        </tr>
                      </thead>
                      <tbody>
                        """,
                t.toString(),
                """
              </tbody>
              </table>
          </div>
          """));
        e.sendResponseHeaders(200,0);
    }

    /**
     * Creates a new order for the logged-in customer if there are items in the customer's cart.
     * The order is created in the database using the {@link OrdersDAO}.
     * After creating the order, the customer is redirected to the orders page.
     *
     * @param w the {@link BufferedWriter} to write the response body
     * @param e the {@link HttpExchange} object containing the HTTP request and response details
     * @throws IOException if an I/O error occurs while writing the response
     */
    public void createOrder(BufferedWriter w, HttpExchange e) throws IOException {
        if(new OrdersDAO().findCartItemsForCustomer(cookie.getLoggedCustomer().getId()).size() > 0){
            new OrdersDAO().createOrder(cookie.getLoggedCustomer().getId());
        }
        e.getResponseHeaders().add("Location","/orders");
        e.sendResponseHeaders(302,0);
    }

    /**
     * Deletes an order based on the provided order ID in the query string.
     * The order is deleted from the database using the {@link OrdersDAO}.
     * After deleting the order, the user is redirected to the orders page.
     *
     * @param w the {@link BufferedWriter} to write the response body
     * @param e the {@link HttpExchange} object containing the HTTP request and response details
     * @throws IOException if an I/O error occurs while writing the response
     */
    public void deleteOrder(BufferedWriter w, HttpExchange e) throws IOException {
        String orderId = new WebHelper().formatQuery(e).get("id");
        if(orderId != null && Integer.parseInt(orderId) > 0){
            new OrdersDAO().deleteOrder(Integer.parseInt(orderId));
        }
        e.getResponseHeaders().add("Location","/orders");
        e.sendResponseHeaders(302,0);
    }
}
