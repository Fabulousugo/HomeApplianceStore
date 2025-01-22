// Code written and maintained by
// Ugochukwu Precious Onah
// 24848777
package org.mjb.dao;

import org.mjb.Connector;
import org.mjb.systemModels.Address;
import org.mjb.systemModels.Customer;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;

public class CustomerDAO {
    /**
     * Retrieves a list of all customers from the database.
     *
     * @return List of customers
     */
    public ArrayList<Customer> findCustomers() {
        ArrayList<Customer> customers = new ArrayList<>();
        try (Connection connection = new Connector().connect()) {
            ResultSet result = connection.createStatement().executeQuery("SELECT * FROM customers;");
            while (result.next()) {

                // Create Address object from ResultSet
                Address address = new Address(result.getString("address1"), result.getString("address2"), result.getString("address3"),
                        result.getString("phone"), result.getString("country"), result.getString("postCode"));

                // Add new customer to the list
                customers.add(new Customer(result.getInt("id"), result.getString("name"), address, result.getString("email"), result.getString("password")));
            }
        } catch (Exception e) {
            e.printStackTrace(); // Handles exceptions by printing stack trace
        }
        return customers;
    }

    /**
     * Allows user to Find a customer by their ID.
     *
     * @param customerId The ID of the customer to find
     * @return The customer object if found, otherwise null
     */
    public Customer findCustomer(int customerId) {
        Customer customer = null;

        try (Connection connection = new Connector().connect()) {
            // Querying the database for customer by ID
            ResultSet result = connection.createStatement().executeQuery("SELECT * FROM customers WHERE id = " + customerId);
            if (result.next()) {

                // Creating an Address object from the ResultSet
                Address address = new Address(result.getString("address1"), result.getString("address2"), result.getString("address3"),
                        result.getString("phone"), result.getString("country"), result.getString("postCode"));

                // Creating a Customer object from ResultSet and assigning it
                customer = new Customer(result.getInt("id"), result.getString("name"), address, result.getString("email"), result.getString("password"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return customer;
    }

    /**
     * This code block Creates a new customer in the database.
     *
     * @param customer The customer object to insert
     * @return true if the customer is successfully created, false otherwise
     */
    public boolean createCustomer(Customer customer) {
        try (Connection connection = new Connector().connect()) {

            // SQL query to insert a new customer into the database
            String sql = String.format("INSERT INTO customers (name,email,address1,address2,address3,country,postCode,phone,password) VALUES ('%s','%s','%s','%s','%s','%s','%s','%s','%s')",
                    customer.getName(), customer.getEmail(), customer.getAddress().getAddress1(), customer.getAddress().getAddress2(),
                    customer.getAddress().getAddress3(), customer.getAddress().getCountry(), customer.getAddress().getPostCode(),
                    customer.getAddress().getPhone(), customer.getPassword());
            connection.createStatement().execute(sql);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Updates an existing customer's information in the database.
     *
     * @param customer The customer object with updated information
     * @return true if the update is successful, false otherwise
     */

    public boolean updateCustomer(Customer customer) {
        try (Connection connection = new Connector().connect()) {
            String sql = String.format("UPDATE customers SET name = '%s',email = '%s',address1 = '%s',address2 = '%s',address3 = '%s',country = '%s',postCode = '%s',phone = '%s',password = '%s' WHERE id = '%d'",
                    customer.getName(), customer.getEmail(), customer.getAddress().getAddress1(), customer.getAddress().getAddress2(),
                    customer.getAddress().getAddress3(), customer.getAddress().getCountry(), customer.getAddress().getPostCode(),
                    customer.getAddress().getPhone(), customer.getPassword(), customer.getId());

            connection.createStatement().execute(sql); // Execute the insertion
            return true; // Return true if insertion was successful
        } catch (Exception e) {
            e.printStackTrace(); // Print the error if insertion fails
        }
        return false; // Return false if something went wrong
    }

    /**
     * Deletes a customer from the database by their ID.
     *
     * @param customerId The ID of the customer to delete
     * @return true if the deletion is successful, false otherwise
     */

    public boolean deleteCustomer(int customerId) {
        try (Connection connection = new Connector().connect()) {
            // SQL query to delete customer by ID
            connection.createStatement().execute("DELETE FROM customers WHERE id = " + customerId);
            return true;  // Return true if deletion was successful
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false; // Return false if something went wrong
    }

    /**
     * Authenticates a customer by their email and password.
     *
     * @param email The email of the customer
     * @param password The password of the customer
     * @return The customer object if credentials match, otherwise null
     */

    public Customer logInAsCustomer(String email, String password) {
        Customer customer = null;
        try (Connection connection = new Connector().connect()) {
            // SQL query to authenticate the customer
            ResultSet result = connection.createStatement().executeQuery("SELECT * FROM customers WHERE email = '" + email + "' AND password = '" + password + "'");
            if (result.next()) {
                // Create Address object from ResultSet
                Address address = new Address(result.getString("address1"), result.getString("address2"), result.getString("address3"),
                        result.getString("phone"), result.getString("country"), result.getString("postCode"));

                // Create and return Customer object if credentials match
                customer = new Customer(result.getInt("id"), result.getString("name"), address, result.getString("email"), result.getString("password"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return customer;
    }
}
