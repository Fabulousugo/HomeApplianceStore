// Code written and maintained by
// Ugochukwu Precious Onah
// 24848777
package org.mjb.dao;

import org.mjb.Connector;
import org.mjb.systemModels.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * The OrdersDAO class handles database interactions related to orders, such as
 * retrieving cart items for a customer, creating orders, adding/removing items
 * from the cart, and deleting orders. It makes use of SQL queries to communicate with
 * the database and returns relevant data in the form of objects like Order,
 * OrderItems, and Customers
 *
 * For brevity, the Appliance is also called the product.
 * It is used interchangeably throughout the class and project as a whole.
 */
public class OrdersDAO {

    /**
     * Finds all the items in the cart for a given customer.
     *
     * @param customerId the ID of the customer whose cart items are to be fetched
     * @return a list of OrderItems representing the customer's cart
     */
    public ArrayList<OrderItems> findCartItemsForCustomer(int customerId){
        ArrayList<OrderItems> items = new ArrayList<OrderItems>();
        try{
            // Establishes a connection to the database
            Connection connection = new Connector().connect();

            // Execute SQL query to fetch cart items for the given customer
            ResultSet result = connection.createStatement().executeQuery(String.format(
                    "SELECT cart.quantity, products.id, products.sku, products.category, products.description, products.price, " +
                            "products.expiresOn, products.stock FROM cart INNER JOIN products on products.id = cart.productId WHERE cart.customerId = '%s'",
                    customerId
            ));

            // Process the results and add each item to the list
            while(result.next()){
                items.add(new OrderItems(new Appliance(
                        result.getString("description"), result.getString("category"), result.getString("sku"),
                        result.getInt("stock"), result.getInt("price"), result.getString("expiresOn"), result.getInt("id")
                ), result.getInt("quantity")));
            }

            // Close the result set and connection
            result.close();
            connection.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return items;
    }

    /**
     * Creates an order for a given customer and transfers items from the cart to the order.
     *
     * @param customerId the ID of the customer placing the order
     * @return a boolean indicating whether the order was successfully created
     */
    public boolean createOrder(int customerId){
        try{
            Connection connection = new Connector().connect();

            // Generate a random order number for the new order
            int inv = (int) Math.floor(Math.random() * (1000000000 - 100000000 + 1) + 3000);

            // Insert a new record into the orders table
            connection.createStatement().execute(String.format(
                    "INSERT INTO orders (createdOn, customerId, orderNo) VALUES (DATE('now'), '%s', '%s')",
                    customerId, inv
            ));

            // Retrieve the ID of the newly created order
            int orderId = connection.createStatement().executeQuery(String.format("SELECT * FROM orders WHERE orderNo = '%s'", inv)).getInt("id");

            // Add items from the cart to the order
            for(OrderItems item : findCartItemsForCustomer(customerId)){
                connection.createStatement().execute(String.format(
                        "INSERT INTO orderItems (orderId, productId, quantity) VALUES ('%s', '%s', '%s')",
                        orderId, item.getProduct().getId(), item.getQuantity()
                ));
            }

            // Clear the cart after the order is created
            connection.createStatement().execute("DELETE FROM cart WHERE customerId = " + customerId);
            connection.close();
            return true;
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Retrieves all the items associated with a given order.
     *
     * @param orderId the ID of the order whose items are to be fetched
     * @return a list of OrderItems representing the products in the order
     */
    public ArrayList<OrderItems> findOrderItems(int orderId){
        ArrayList<OrderItems> items = new ArrayList<OrderItems>();
        try{
            Connection connection = new Connector().connect();

            // Execute SQL query to fetch items for the given order
            ResultSet result = connection.createStatement().executeQuery(String.format(
                    "SELECT products.id as productId, products.sku, products.category, products.description, products.price, " +
                            "products.expiresOn, products.stock, orderItems.quantity, orderItems.orderId FROM orderItems " +
                            "INNER JOIN products on products.id = orderItems.productId INNER JOIN orders on orders.id = orderItems.orderId " +
                            "WHERE orders.id = '%s' GROUP BY products.id", orderId
            ));

            // Process the results and add each item to the list
            while(result.next()){
                items.add(new OrderItems(new Appliance(
                        result.getString("description"), result.getString("category"), result.getString("sku"),
                        result.getInt("stock"), result.getInt("price"), result.getString("expiresOn"), result.getInt("productId")
                ), result.getInt("quantity")));
            }

            result.close();
            connection.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return items;
    }

    /**
     * Retrieves all the orders placed by a given customer.
     *
     * @param customerId the ID of the customer whose orders are to be fetched
     * @return a list of Order objects representing the customer's orders
     */
    public ArrayList<Order> findCustomerOrders(int customerId){
        ArrayList<Order> items = new ArrayList<Order>();
        try{
            Connection connection = new Connector().connect();

            // Execute SQL query to fetch orders for the given customer
            ResultSet result = connection.createStatement().executeQuery(String.format(
                    "SELECT orders.*, customers.name as name, customers.address1 as address1, customers.address2 as address2, " +
                            "customers.address3 as address3, customers.country as country, customers.postCode as postCode, customers.phone as phone, " +
                            "customers.email as email, customers.password as password FROM orders INNER JOIN customers on customers.id = orders.customerId " +
                            "WHERE orders.customerId = '%s'", customerId
            ));

            // Process the results and create Order objects
            while(result.next()){
                Address address = new Address(result.getString("address1"), result.getString("address2"), result.getString("address3"),
                        result.getString("phone"), result.getString("country"), result.getString("postCode"));
                Customer customer = new Customer(result.getInt("customerId"), result.getString("name"), address,
                        result.getString("email"), result.getString("password"));
                items.add(new Order(
                        result.getString("orderNo"), customer, findOrderItems(result.getInt("id")), result.getString("createdOn"), result.getInt("id")
                ));
            }
            connection.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return items;
    }

    /**
     * Retrieves all the orders in the system.
     *
     * @return a list of all orders in the system
     */
    public ArrayList<Order> findOrders(){
        ArrayList<Order> items = new ArrayList<Order>();
        try{
            Connection connection = new Connector().connect();

            // Execute SQL query to fetch all orders
            ResultSet result = connection.createStatement().executeQuery(
                    "SELECT orders.*, customers.name as name, customers.address1 as address1, customers.address2 as address2, " +
                            "customers.address3 as address3, customers.country as country, customers.postCode as postCode, customers.phone as phone, " +
                            "customers.email as email, customers.password as password FROM orders INNER JOIN customers on customers.id = orders.customerId"
            );

            // Process the results and create Order objects
            while(result.next()){
                Address address = new Address(result.getString("address1"), result.getString("address2"), result.getString("address3"),
                        result.getString("phone"), result.getString("country"), result.getString("postCode"));
                Customer customer = new Customer(result.getInt("customerId"), result.getString("name"), address,
                        result.getString("email"), result.getString("password"));
                items.add(new Order(
                        result.getString("orderNo"), customer, findOrderItems(result.getInt("id")), result.getString("createdOn"), result.getInt("id")
                ));
            }
            connection.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return items;
    }

    /**
     * Deletes an order from the database based on its ID.
     *
     * @param orderId the ID of the order to be deleted
     * @return a boolean indicating whether the order was successfully deleted
     */
    public boolean deleteOrder(int orderId){
        try{
            Connection connection = new Connector().connect();
            connection.createStatement().execute(String.format("DELETE FROM orders WHERE id = %s", orderId));
            connection.close();
            return true;
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Adds a product to the customer's cart.
     *
     * @param productId  the ID of the product to be added
     * @param customerId the ID of the customer adding the product to the cart
     * @return a boolean indicating whether the product was successfully added to the cart
     */
    public boolean addApplianceToCart(int productId, int customerId){
        try{
            OrderItems item = null;
            Connection connection = new Connector().connect();

            // Check if the product already exists in the customer's cart
            ResultSet result = connection.createStatement().executeQuery(String.format(
                    "SELECT cart.quantity, products.id, products.sku, products.category, products.description, products.price, " +
                            "products.expiresOn, products.stock FROM cart INNER JOIN products on products.id = cart.productId " +
                            "WHERE cart.customerId = '%s' AND cart.productId = '%s'", customerId, productId
            ));

            while(result.next()){
                item = new OrderItems(new Appliance(
                        result.getString("description"), result.getString("category"), result.getString("sku"),
                        result.getInt("stock"), result.getInt("price"), result.getString("expiresOn"), result.getInt("id")
                ), result.getInt("quantity"));
            }
            result.close();

            // If the product is not already in the cart, insert it, otherwise update the quantity
            if(item == null){
                connection.createStatement().execute(String.format(
                        "INSERT INTO cart (quantity, customerId, productId) VALUES ('%s','%s','%s')", 1, customerId, productId
                ));
            }else{
                connection.createStatement().execute(String.format(
                        "UPDATE cart SET quantity = '%s' WHERE customerId = '%s' AND productId = '%s'",
                        item.getQuantity() + 1, customerId, productId
                ));
            }
            connection.close();
            return true;
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Removes a product from the customer's cart.
     *
     * @param productId  the ID of the product to be removed
     * @param customerId the ID of the customer removing the product from the cart
     * @return a boolean indicating whether the product was successfully removed from the cart
     */
    public boolean removeApplianceFromCart(int productId, int customerId){
        try{
            Connection connection = new Connector().connect();

            // Execute SQL query to delete the product from the cart
            connection.createStatement().execute(String.format(
                    "DELETE FROM cart WHERE customerId = '%s' AND productId = '%s'", customerId, productId
            ));
            connection.close();
            return true;
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }
}
