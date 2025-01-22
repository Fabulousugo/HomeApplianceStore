// Code written and maintained by
// Ugochukwu Precious Onah
// 24848777
package org.mjb.webServer;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.mjb.dao.OrdersDAO;
import org.mjb.systemModels.OrderItems;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;


/**
 * Handles the cart operations for the web server.
 * <p>
 * This class is responsible for managing the shopping cart functionality, including displaying the cart page,
 * adding products to the cart, and removing items from the cart. It interacts with the {@link OrdersDAO}
 * for database operations related to cart items and utilizes {@link WebCookie} for managing user sessions.
 * </p>
 */
public class WebCart implements HttpHandler {
    private WebCookie cookie;

    /**
     * Handles the HTTP requests for cart operations.
     * <p>
     * Based on the requested URI path, this method delegates the handling to the appropriate method
     * to load the cart page, add an item to the cart, or remove an item from the cart.
     * </p>
     *
     * @param  {@link HttpExchange} object containing the request and response data.
     * @throws IOException if an I/O error occurs during request handling.
     */
    public WebCart(WebCookie cookie){
        this.cookie = cookie;
    }
    public void handle(HttpExchange e) throws IOException {
        if(cookie.getLoggedCustomer() == null || cookie.getLoggedUser() != null){
            e.getResponseHeaders().add("Location","/login");
            e.sendResponseHeaders(302,0);
            return;
        }
        BufferedWriter w = new BufferedWriter(new OutputStreamWriter(e.getResponseBody()));
        switch(e.getRequestURI().getPath()){
            case "/cart"->{
                loadPage(w,e);
            }
            case "/cart/add"->{
                addToCart(w,e);
            }
            case "/cart/remove"->{
                removeFromCart(w,e);
            }
            default -> {
                w.write("Invalid Path");
            }
        }
        w.close();
    }

    /**
     * Load page.
     *
     * @param w the w
     * @param e the e
     * @throws IOException the io exception
     */
    public void loadPage(BufferedWriter w, HttpExchange e) throws IOException {
        StringBuilder t = new StringBuilder();
        for(OrderItems o : new OrdersDAO().findCartItemsForCustomer(cookie.getLoggedCustomer().getId())){
            t.append(String.format("""
                    <tr>
                        <td>%s</td>
                        <td>%s</td>
                        <td>%s</td>
                        <td>%s</td>
                        <td>%s</td>
                        <td>%s</td>
                        <td>£%s</td>
                        <td>£%s</td>
                    """,o.getProduct().getId(),o.getProduct().getSku(),o.getProduct().getDescription(),
                    o.getProduct().getCategory(),o.getProduct().getExpiresOn(),o.getQuantity(),
                    o.getProduct().getPrice(),o.getTotal()));
            t.append(String.format("""
                    <td>
                        <a href="/cart/remove?id=%s" class="btn btn-primary me-2 btn-sm">Remove</a>
                    </td>
                    """,o.getProduct().getId()));
            t.append("</tr>");
        }
        w.write(new WebHelper().buildPage(cookie, """
                <h2>Cart</h2>
                <div>
                    <a href="/orders/create" class="btn btn-primary">Create Order</a>
                </div>
                <div class="table-responsive">
                    <table class="table table-hover">
                      <thead>
                        <tr>
                          <th scope="col">#</th>
                          <th scope="col">SKU</th>
                          <th scope="col">Description</th>
                          <th scope="col">Category</th>
                          <th scope="col">Expires</th>
                          <th scope="col">Quantity</th>
                          <th scope="col">Unit Price</th>
                          <th scope="col">Total Price</th>
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
     * Add to cart.
     *
     * @param w the w
     * @param e the e
     * @throws IOException the io exception
     */
    public void addToCart(BufferedWriter w, HttpExchange e) throws IOException {
        String productId = new WebHelper().formatQuery(e).get("id");
        if(productId != null && Integer.parseInt(productId) > 0){
            new OrdersDAO().addApplianceToCart(Integer.parseInt(productId),cookie.getLoggedCustomer().getId());
        }
        e.getResponseHeaders().add("Location","/");
        e.sendResponseHeaders(302,0);
    }

    /**
     * Remove from cart.
     *
     * @param w the w
     * @param e the e
     * @throws IOException the io exception
     */
    public void removeFromCart(BufferedWriter w, HttpExchange e) throws IOException {
        String productId = new WebHelper().formatQuery(e).get("id");
        if(productId != null && Integer.parseInt(productId) > 0){
            new OrdersDAO().removeApplianceFromCart(Integer.parseInt(productId),cookie.getLoggedCustomer().getId());
        }
        e.getResponseHeaders().add("Location","/cart");
        e.sendResponseHeaders(302,0);
    }
}
