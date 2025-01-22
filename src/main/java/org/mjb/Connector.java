/*
    Code written and maintained by
    Ugochukwu Precious Onah
    24848777
 */

package org.mjb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * The Connector class is responsible for establishing a connection to an SQLite database.
 * It provides a method to connect to the database and handle potential exceptions that may arise
 * during the connection process.
 * <p>
 * Example usage:
 * <pre>
 * Connector connector = new Connector();
 * try {
 *     Connection connection = connector.connect();
 *     // Interact with the database using the connection
 * } catch (Exception e) {
 *     // Handle connection failure
 * }
 * </pre>
 * </p>
 */
public class Connector {
    // Path to the SQLite database file
    private static final String DATABASE_URL = "jdbc:sqlite:src/main/resources/db.sqlite"; // The dbMain is the database in use

    /**
     * Establishes and returns a connection to the SQLite database.
     * <p>
     * This method attempts to load the SQLite JDBC driver and establish a connection to the
     * database at the specified URL. If successful, it returns the {@link Connection} object
     * to interact with the database. If any error occurs during the process, it throws the
     * respective exception.
     * </p>
     *
     * @return the {@link Connection} object to interact with the SQLite database
     * @throws Exception if the connection fails due to a missing JDBC driver, database connection error,
     *                   or other unforeseen issues
     */
    public Connection connect() throws Exception {
        try {
            // Load the SQLite JDBC driver
            Class.forName("org.sqlite.JDBC");

            // Attempts to establish a connection
            Connection connection = DriverManager.getConnection(DATABASE_URL);

            if (connection == null) {
                throw new SQLException("Failed to establish a database connection.");
            }

            return connection;
        } catch (ClassNotFoundException e) {
            // If the SQLite JDBC driver is not found, the catch block throws an exception
            throw new ClassNotFoundException("SQLite JDBC driver not found. Ensure the driver is correctly included in the project.", e);
        } catch (SQLException e) {
            // Handle SQL-specific exceptions
            throw new SQLException("An error occurred while connecting to the database: " + e.getMessage(), e);
        }
    }
}
