/*
 Code written and maintained by
 Ugochukwu Precious Onah
 24848777
*/
package org.mjb.systemModels;

/**
 * The Customer class represents a customer in the system.
 * It encapsulates customer-related information including their name,
 * address, email, and password. It also provides methods to access
 * individual attributes of the customer.
 *
 * <p>The class contains the following fields:</p>
 * <ul>
 *   <li>id: A unique identifier for the customer (int)</li>
 *   <li>name: The name of the customer (String)</li>
 *   <li>address: The address of the customer (Address)</li>
 *   <li>email: The email address of the customer (String)</li>
 *   <li>password: The password for the customer's account (String)</li>
 * </ul>
 *
 * <p>The class includes a constructor to initialize these fields and
 * getter methods to retrieve the customer's details. Additionally, the
 * {@code toString()} method provides a string representation of the customer.</p>
 *
 * <p>Example usage:</p>
 * <pre>
 *   Address address = new Address("123 Main St", "Apt 4B", "Downtown", "123-456-7890", "USA", "10001");
 *   Customer customer = new Customer(1, "John Doe", address, "john.doe@example.com", "password123");
 *   System.out.println(customer.getName()); // Output: John Doe
 *   System.out.println(customer.getEmail()); // Output: john.doe@example.com
 * </pre>
 *
 * <p>Note for self: Be cautious with handling sensitive information such as passwords.</p>
 */
public class Customer {
    private final int id;
    private final String name;
    private final Address address;
    private final String email;
    private final String password;

    /**
     * Instantiates a new Customer.
     *
     * @param id       the id
     * @param name     the name
     * @param address  the address
     * @param email    the email
     * @param password the password
     */
    public Customer(int id, String name, Address address, String email, String password) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.email = email;
        this.password = password;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets address.
     *
     * @return the address
     */
    public Address getAddress() {
        return address;
    }

    /**
     * Gets email.
     *
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Gets password.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address=" + address +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
