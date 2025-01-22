import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {

    private Customer customer;
    private Address address;

    @BeforeEach
    void setUp() {
        // Create a sample address and customer for testing
        address = new Address("123 Main St", "Apt 4B", "Downtown", "123-456-7890", "USA", "10001");
        customer = new Customer(1, "John Doe", address, "john.doe@example.com", "password123");
    }

    @Test
    void testGetId() {
        // Test the getId method
        assertEquals(1, customer.getId(), "Customer ID should be 1");
    }

    @Test
    void testGetName() {
        // Test the getName method
        assertEquals("John Doe", customer.getName(), "Customer name should be 'John Doe'");
    }

    @Test
    void testGetAddress() {
        // Test the getAddress method
        assertNotNull(customer.getAddress(), "Customer address should not be null");
        assertEquals("123 Main St", customer.getAddress().getAddress1(), "Address line 1 should be '123 Main St'");
    }

    @Test
    void testGetEmail() {
        // Test the getEmail method
        assertEquals("john.doe@example.com", customer.getEmail(), "Customer email should be 'john.doe@example.com'");
    }

    @Test
    void testGetPassword() {
        // Test the getPassword method
        assertEquals("password123", customer.getPassword(), "Customer password should be 'password123'");
    }

    @Test
    void testToString() {
        // Test the toString method
        String expectedString = "Customer{id=1, name='John Doe', address=" + address + ", email='john.doe@example.com', password='password123'}";
        assertEquals(expectedString, customer.toString(), "toString method should return the correct string representation of the customer");
    }
}
