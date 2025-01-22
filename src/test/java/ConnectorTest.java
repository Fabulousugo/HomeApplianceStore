package org.mjb;

import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class ConnectorTest {

    @Test
    public void testSingletonConnection() throws SQLException {
        // Retrieve two connections
        Connection connection1 = Connector.connect();
        Connection connection2 = Connector.connect();

        // Assert that both connections are the same instance
        assertSame(connection1, connection2, "The connections should be the same instance");
    }

    @Test
    public void testDatabaseConnection() throws SQLException {
        // Retrieve a connection and check if it is valid
        Connection connection = Connector.connect();
        assertNotNull(connection, "The connection should not be null");
        assertTrue(connection.isValid(1), "The connection should be valid");
    }
}
