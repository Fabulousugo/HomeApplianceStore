// Code written and maintained by
// Ugochukwu Precious Onah
// 24848777

package org.mjb.webServer;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.mjb.dao.CustomerDAO;
import org.mjb.systemModels.Address;
import org.mjb.systemModels.Customer;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;

/// The WebServer customer.
public class WebCustomer implements HttpHandler {
    private WebCookie cookie;

    /**
     * Handles HTTP requests for customer-related actions.
     * It processes GET, POST, and DELETE requests and routes them based on the requested path and method.
     *
     * @param cookie the HttpExchange object representing the HTTP request and response
     * @throws IOException if an I/O error occurs while handling the request
     */
    public WebCustomer(WebCookie cookie){
        this.cookie = cookie;
    }
    public void handle(HttpExchange e) throws IOException {
        if(cookie.getLoggedUser() == null){
            e.getResponseHeaders().add("Location","/login");
            e.sendResponseHeaders(302,0);
            return;
        }
        BufferedWriter w = new BufferedWriter(new OutputStreamWriter(e.getResponseBody()));
        switch(e.getRequestMethod()){
            case "GET"->{
                switch(e.getRequestURI().getPath()){
                    case "/customers"->{
                        loadPage(w,e);
                    }
                    case "/customers/new","/customers/edit"->{
                        savePage(w,e);
                    }
                    case "/customers/delete"->{
                        deleteMapping(w,e);
                    }
                    default -> {
                        w.write("Invalid Path");
                    }
                }
            }
            case "POST"->{
                postMapping(w,e);
            }
            default -> {
                w.write("Invalid Request Method");
            }
        }
        w.close();
    }

    /**
     * Loads the customer list page.
     * Fetches the customer data from the database and generates an HTML table to display the customer information.
     *
     * @param w the BufferedWriter object used to write the response
     * @param e the HttpExchange object representing the HTTP request and response
     * @throws IOException if an I/O error occurs while writing the response
     */
    public void loadPage(BufferedWriter w, HttpExchange e) throws IOException {
        StringBuilder t = new StringBuilder();
        for(Customer c : new CustomerDAO().findCustomers()){
            t.append(String.format("""
                    <tr>
                        <td>%s</td>
                        <td>%s</td>
                        <td>%s</td>
                        <td>%s</td>
                        <td>%s</td>
                        <td>%s</td>
                        <td>%s</td>
                        <td>%s</td>
                        <td>%s</td>
                    """,c.getId(),c.getName(),c.getEmail(),
                    c.getAddress().getPhone(),c.getAddress().getAddress1(),
                    c.getAddress().getAddress2(),c.getAddress().getAddress3(),
                    c.getAddress().getPostCode(),c.getAddress().getCountry()));
            t.append(String.format("""
                <td>
                    <a href="/customers/edit?id=%s" class="btn btn-primary me-2 btn-sm style =">Edit</a>
                    <a href="/customers/delete?id=%s" class="btn btn-danger btn-sm">Delete</a>
                </td>
                """,c.getId(),c.getId()));
            t.append("</tr>");
        }

        w.write(new WebHelper().buildPage(cookie, """
                <h2>Customers</h2>
                <div>
                    <a href="/customers/new" class="btn btn-primary">Create New Customer</a>
                </div>
                <div class="table-responsive">
                    <table class="table table-hover">
                      <thead>
                        <tr>
                          <th scope="col">#</th>
                          <th scope="col">Name</th>
                          <th scope="col">Email</th>
                          <th scope="col">Phone</th>
                          <th scope="col">Address 1</th>
                          <th scope="col">Address 2</th>
                          <th scope="col">Address 3</th>
                          <th scope="col">Post Code</th>
                          <th scope="col">Country</th>
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
     * Displays the form for creating or editing customer data.
     * If the request is for editing, it loads the existing customer data into the form.
     *
     * @param w the BufferedWriter object used to write the response
     * @param e the HttpExchange object representing the HTTP request and response
     * @throws IOException if an I/O error occurs while writing the response
     */

    public void savePage(BufferedWriter w, HttpExchange e) throws IOException {
        String id = new WebHelper().formatQuery(e).get("id");
        Customer customer = null;
        if(id != null && Integer.parseInt(id) > 0){
            customer = new CustomerDAO().findCustomer(Integer.parseInt(id));
        }
        w.write(new WebHelper().buildPage(cookie, String.format("""
                <div class="row">
                    <div class="col-md-6 offset-md-3">
                        <h2>Customer</h2>
                        <form action="/customers" method="POST">
                          <div class="form-group">
                            <label for="name">Name</label>
                            <input type="text" name="name" class="form-control" id="name" value="%s">
                          </div>
                          <div class="form-group">
                            <label for="email">Email</label>
                            <input type="text" name="email" class="form-control" id="email" value="%s">
                          </div>
                          <div class="form-group">
                            <label for="password">Password</label>
                            <input type="password" name="password" class="form-control" id="password" value="%s">
                          </div>
                          <div class="form-group">
                            <label for="phone">Phone</label>
                            <input type="tel" name="phone" class="form-control" id="phone" value="%s">
                          </div>
                          <div class="form-group">
                            <label for="address1">Address 1</label>
                            <input type="text" name="address1" class="form-control" id="address1" value="%s">
                          </div>
                          <div class="form-group">
                            <label for="address2">Address 2</label>
                            <input type="text" name="address2" class="form-control" id="address2" value="%s">
                          </div>
                          <div class="form-group">
                            <label for="address3">Address 3</label>
                            <input type="text" name="address3" class="form-control" id="address3" value="%s">
                          </div>
                          <div class="form-group">
                            <label for="postCode">Post Code</label>
                            <input type="text" name="postCode" class="form-control" id="postCode" value="%s">
                          </div>
                          <div class="form-group">
                            <label for="country">Country</label>
                            <input type="text" name="country" class="form-control" id="country" value="%s">
                          </div>
                            <input type="hidden" name="id" value="%s">
                          <div class="mt-4">
                              <button type="submit" class="btn btn-primary">Save</button>
                          </div>
                        </form>
                    </div>
                </div>
                """,customer != null ? customer.getName() : "",customer != null ? customer.getEmail() : ""
                ,customer != null ? customer.getPassword() : "",customer != null ? customer.getAddress().getPhone() : ""
                ,customer != null ? customer.getAddress().getAddress1() : "",customer != null ? customer.getAddress().getAddress2() : ""
                ,customer != null ? customer.getAddress().getAddress3() : "",customer != null ? customer.getAddress().getPostCode() : ""
                ,customer != null ? customer.getAddress().getCountry() : "",customer != null ? customer.getId() : "0"
        )));
        e.sendResponseHeaders(200,0);
    }

    /**
     * Handles POST requests for customer creation or updates.
     * It processes form data and either creates a new customer or updates an existing one.
     *
     * @param w the BufferedWriter object used to write the response
     * @param e the HttpExchange object representing the HTTP request and response
     * @throws IOException if an I/O error occurs while writing the response
     */

    public void postMapping(BufferedWriter w, HttpExchange e) throws IOException {
            HashMap<String, String> body = new WebHelper().formatRequest(e);
            Customer customer = new Customer(body.get("id") != null && Integer.parseInt(body.get("id")) > 0 ? Integer.parseInt(body.get("id")) : 0 ,
                    body.get("name"),new Address(body.get("address1"),body.get("address2"),body.get("address3"),body.get("phone"),
                    body.get("country"),body.get("postCode")),body.get("email"),body.get("password"));
            if(customer.getId() > 0){
                new CustomerDAO().updateCustomer(customer);
            }else{
                new CustomerDAO().createCustomer(customer);
            }
            e.getResponseHeaders().add("Location","/customers");
            e.sendResponseHeaders(302,0);
    }

    /**
     * Handles the deletion of a customer.
     * It retrieves the customer ID from the request and deletes the customer from the database.
     *
     * @param w the BufferedWriter object used to write the response
     * @param e the HttpExchange object representing the HTTP request and response
     * @throws IOException if an I/O error occurs while writing the response
     */
    public void deleteMapping(BufferedWriter w, HttpExchange e) throws IOException {
        String id = new WebHelper().formatQuery(e).get("id");
        if(id != null && Integer.parseInt(id) > 0){
            new CustomerDAO().deleteCustomer(Integer.parseInt(id));
        }
        e.getResponseHeaders().add("Location","/customers");
        e.sendResponseHeaders(302,0);
    }
}
