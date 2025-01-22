/*
    Code written and maintained by
    Ugochukwu Precious Onah
    24848777
 */
package org.mjb.webServer;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.mjb.dao.ApplianceDAO;
import org.mjb.systemModels.Appliance;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;

/**
 * This class handles HTTP requests related to product management (adding, editing, deleting, listing)
 * for a web-based appliance store. It uses the HttpHandler interface to manage HTTP requests and
 * interacts with the appliance data through the ApplianceDAO class.
 */
public class WebProduct implements HttpHandler {

    private WebCookie cookie;

    /**
     * Constructs a new WebProduct handler with the specified cookie.
     *
     * @param cookie the WebCookie that holds session and user data
     */
    public WebProduct(WebCookie cookie){
        this.cookie = cookie;
    }

    /**
     * Handles HTTP requests and processes them according to their method (GET, POST).
     * Routes requests based on the request URI to the corresponding method for page loading,
     * saving, or deleting product data.
     *
     * @param e the HttpExchange object containing the HTTP request and response
     * @throws IOException if an I/O error occurs
     */
    public void handle(HttpExchange e) throws IOException {
        BufferedWriter w = new BufferedWriter(new OutputStreamWriter(e.getResponseBody()));

        // Process different HTTP methods
        switch(e.getRequestMethod()){
            case "GET" -> {
                switch(e.getRequestURI().getPath()){
                    case "/" -> {
                        loadPage(w, e); // Load the product list page
                    }
                    case "/product/new", "/product/edit" -> {
                        savePage(w, e); // Load the page for creating or editing a product
                    }
                    case "/product/delete" -> {
                        deleteMapping(w, e); // Handle product deletion
                    }
                    default -> {
                        w.write("Invalid Path"); // Invalid URI
                    }
                }
            }
            case "POST" -> {
                postMapping(w, e); // Handle product creation or updates
            }
            default -> {
                w.write("Invalid Request Method"); // Invalid HTTP method
            }
        }
        w.close();
    }

    /**
     * Loads the page displaying a list of products, optionally filtered by search or expiration date.
     * The page also includes options for editing or deleting products if the user is logged in.
     *
     * @param w the BufferedWriter to send the response
     * @param e the HttpExchange object containing the HTTP request
     * @throws IOException if an I/O error occurs
     */
    public void loadPage(BufferedWriter w, HttpExchange e) throws IOException {
        String s = new WebHelper().formatQuery(e).get("s");
        String ex = new WebHelper().formatQuery(e).get("expired");
        StringBuilder t = new StringBuilder();

        // Fetch appliances based on search or expired status
        for(Appliance p : ex != null ? new ApplianceDAO().findExpiredAppliances(s) : new ApplianceDAO().findAppliances(s)){
            t.append(String.format(""" 
                    <tr>
                        <td>%s</td>
                        <td>%s</td>
                        <td>%s</td>
                        <td>%s</td>
                        <td>%s</td>
                        <td>%s</td>
                        <td>£%s</td>
                    """, p.getId(), p.getSku(), p.getDescription(), p.getCategory(),
                    p.getExpiresOn(), p.getStock(), p.getPrice()));

            // Add edit and delete options if logged in as a user
            if(cookie.getLoggedUser() != null){
                t.append(String.format("""
                        <td>
                            <a href="/product/edit?id=%s" class="btn btn-primary me-2 btn-sm style">Edit</a>
                            <a href="/product/delete?id=%s" class="btn btn-danger btn-sm">Delete</a>
                        </td>
                        """, p.getId(), p.getId()));
            }

            // Add add-to-cart button if logged in as a customer
            if(cookie.getLoggedCustomer() != null){
                t.append(String.format("""
                        <td>
                            <a href="/cart/add?id=%s" class="btn btn-primary me-2 btn-sm">Add to Cart</a>
                        </td>
                        """, p.getId()));
            }
            t.append("</tr>");
        }

        // Construct and send the page with product list
        w.write(new WebHelper().buildPage(cookie, """
                <h2>Products</h2>
                """,
                cookie.getLoggedUser() != null ? """
                        <div>
                            <a href="/product/new" class="btn btn-primary">Create New Product</a>
                        </div>
                        """ : "",
                """
                <div>
                    <form class="d-flex justify-content-end" role="search" action="/">
                        <input class="form-control me-2 w-50" type="search" name="s" placeholder="Search for a product" aria-label="Search">
                        <button class="btn btn-outline-primary" type="submit">Search</button>
                      </form>
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
                          <th scope="col">Stock</th>
                          <th scope="col">Price</th>
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
        e.sendResponseHeaders(200, 0);
    }

    /**
     * Loads the page for adding or editing a product. If a product ID is provided, it loads the
     * details of the specified product for editing.
     *
     * @param w the BufferedWriter to send the response
     * @param e the HttpExchange object containing the HTTP request
     * @throws IOException if an I/O error occurs
     */
    public void savePage(BufferedWriter w, HttpExchange e) throws IOException {
        if(cookie.getLoggedUser() == null){
            e.getResponseHeaders().add("Location", "/login");
            e.sendResponseHeaders(302, 0);
            return;
        }

        String id = new WebHelper().formatQuery(e).get("id");
        Appliance product = null;
        if(id != null && Integer.parseInt(id) > 0){
            product = new ApplianceDAO().findAppliance(Integer.parseInt(id));
        }

        // Build the page for creating or editing a product
        w.write(new WebHelper().buildPage(cookie, String.format("""
                <div class="row">
                    <div class="col-md-6 offset-md-3">
                        <h2>Product</h2>
                        <form action="/product" method="POST">
                          <div class="form-group">
                            <label for="sku">Stock Keeping Unit</label>
                            <input type="text" name="sku" class="form-control" id="sku" value="%s">
                          </div>
                          <div class="form-group">
                            <label for="description">Description</label>
                            <input type="text" name="description" class="form-control" id="description" value="%s">
                          </div>
                          <div class="form-group">
                            <label for="category">Category</label>
                            <input type="text" name="category" class="form-control" id="category" value="%s">
                          </div>
                          <div class="form-group">
                            <label for="price">Price</label>
                            <input type="number" name="price" class="form-control" id="price" value="%s">
                          </div>
                          <div class="form-group">
                            <label for="stock">Stock</label>
                            <input type="number" name="stock" class="form-control" id="stock" value="%s">
                          </div>
                          <div class="form-group">
                            <label for="expiresOn">Expires On</label>
                            <input type="text" name="expiresOn" class="form-control" id="expiresOn" placeholder="YYYY-MM-DD" value="%s">
                          </div>
                            <input type="hidden" name="id" value="%s">
                          <div class="mt-4">
                              <button type="submit" class="btn btn-primary">Save</button>
                          </div>
                        </form>
                    </div>
                </div>
                """, product != null ? product.getSku() : "",
                product != null ? product.getDescription() : "",
                product != null ? product.getCategory() : "",
                product != null ? product.getPrice() : "",
                product != null ? product.getStock() : "",
                product != null ? product.getExpiresOn() : "",
                product != null ? product.getId() : 0
        )));

        e.sendResponseHeaders(200, 0);
    }

    /**
     * Handles POST requests to save a new or updated product. It extracts the product data from the
     * request body and stores it in the database. If the product already exists (identified by ID),
     * it is updated; otherwise, a new product is created.
     *
     * @param w the BufferedWriter to send the response
     * @param e the HttpExchange object containing the HTTP request
     * @throws IOException if an I/O error occurs
     */
    public void postMapping(BufferedWriter w, HttpExchange e) throws IOException {
        if(cookie.getLoggedUser() == null){
            e.getResponseHeaders().add("Location", "/login");
            e.sendResponseHeaders(302, 0);
            return;
        }

        HashMap<String, String> body = new WebHelper().formatRequest(e);
        Appliance product = new Appliance(
                body.get("description"),
                body.get("category"),
                body.get("sku"),
                Integer.parseInt(body.get("stock")),
                Integer.parseInt(body.get("price")),
                body.get("expiresOn"),
                body.get("id") != null && Integer.parseInt(body.get("id")) > 0 ? Integer.parseInt(body.get("id")) : 0
        );

        // If product ID exists, update the product; otherwise, create a new one
        if(product.getId() > 0){
            new ApplianceDAO().updateAppliance(product);
        } else {
            new ApplianceDAO().createAppliance(product);
        }

        e.getResponseHeaders().add("Location", "/");
        e.sendResponseHeaders(302, 0);
    }

    /**
     * Handles DELETE requests to remove a product from the system. The product is identified by
     * the ID in the query string.
     *
     * @param w the BufferedWriter to send the response
     * @param e the HttpExchange object containing the HTTP request
     * @throws IOException if an I/O error occurs
     */
    public void deleteMapping(BufferedWriter w, HttpExchange e) throws IOException {
        if(cookie.getLoggedUser() == null){
            e.getResponseHeaders().add("Location", "/login");
            e.sendResponseHeaders(302, 0);
            return;
        }

        String id = new WebHelper().formatQuery(e).get("id");
        if(id != null && Integer.parseInt(id) > 0){
            new ApplianceDAO().deleteProduct(Integer.parseInt(id)); // Delete the appliance by ID
        }
        e.getResponseHeaders().add("Location", "/");
        e.sendResponseHeaders(302, 0);
    }
}
