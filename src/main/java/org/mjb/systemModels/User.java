/*
    Code written and maintained by
    Ugochukwu Precious Onah
    24848777
 */

package org.mjb.systemModels;

/**
 * The User class represents a user in the system.
 * It holds the user's information, including their unique identifier (ID), username, and password.
 * The class provides getter methods to retrieve these details.
 * <p>
 * Example usage:
 * <pre>
 * User user = new User(1, "john_doe", "password123");
 * int userId = user.getId();
 * String username = user.getUsername();
 * String password = user.getPassword();
 * </pre>
 * </p>
 */
public class User {
    private final int id;
    private final String username;
    private final String password;

    /**
     * Instantiates a new User with the specified ID, username, and password.
     *
     * @param id       the unique identifier of the user
     * @param username the username associated with the user
     * @param password the password associated with the user
     */
    public User(int id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    /**
     * Gets the unique ID of the user.
     *
     * @return the unique ID of the user
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the username of the user.
     *
     * @return the username associated with the user
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets the password of the user.
     *
     * @return the password associated with the user
     */
    public String getPassword() {
        return password;
    }
}
