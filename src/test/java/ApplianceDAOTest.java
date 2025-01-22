package org.mjb.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mjb.Connector;
import org.mjb.systemModels.Appliance;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class ApplianceDAOTest {

    private ApplianceDAO applianceDAO;
    private Connection connection;

    @BeforeEach
    public void setup() throws SQLException {
        // Initialize ApplianceDAO and test database
        applianceDAO = new ApplianceDAO();
        connection = Connector.connect();
        connection.createStatement().execute("CREATE TABLE IF NOT EXISTS products (id INTEGER PRIMARY KEY, category TEXT, description TEXT, sku TEXT, price INTEGER, stock INTEGER, expiresOn TEXT);");
    }

    @Test
    public void testCreateAppliance() {
        // Create a sample appliance
        Appliance appliance = new Appliance("Washing Machine", "Appliance", "SKU123", 10, 500, "2025-12-31", 1);

        // Test create method
        assertTrue(applianceDAO.createAppliance(appliance), "Appliance should be created successfully");

        // Verify the appliance is in the database
        ArrayList<Appliance> appliances = applianceDAO.findAppliances(null);
        assertEquals(1, appliances.size(), "There should be 1 appliance in the database");
    }

    @Test
    public void testFindAppliances() {
        // Create and add some appliances to the database
        applianceDAO.createAppliance(new Appliance("Dishwasher", "Appliance", "SKU456", 5, 300, "2024-12-31", 2));
        applianceDAO.createAppliance(new Appliance("Refrigerator", "Appliance", "SKU789", 8, 700, "2026-01-01", 3));

        // Test findAppliances method
        ArrayList<Appliance> appliances = applianceDAO.findAppliances("Dishwasher");
        assertEquals(1, appliances.size(), "There should be 1 appliance matching the search");
        assertEquals("Dishwasher", appliances.get(0).getDescription(), "The appliance should be a Dishwasher");
    }

    @Test
    public void testDeleteAppliance() {
        // Create and add an appliance to the database
        applianceDAO.createAppliance(new Appliance("Oven", "Appliance", "SKU000", 3, 400, "2025-05-10", 4));

        // Test delete method
        assertTrue(applianceDAO.deleteProduct(4), "The appliance should be deleted successfully");

        // Verify the appliance is removed
        ArrayList<Appliance> appliances = applianceDAO.findAppliances(null);
        assertEquals(0, appliances.size(), "There should be no appliances in the database");
    }

    @Test
    public void testFindExpiredAppliances() {
        // Create and add expired appliances to the database
        applianceDAO.createAppliance(new Appliance("Expired Washing Machine", "Appliance", "SKU123", 0, 500, "2020-01-01", 5));
        applianceDAO.createAppliance(new Appliance("Expired Refrigerator", "Appliance", "SKU124", 0, 600, "2022-01-01", 6));

        // Test findExpiredAppliances method
        ArrayList<Appliance> appliances = applianceDAO.findExpiredAppliances(null);
        assertTrue(appliances.size() >= 2, "There should be at least 2 expired appliances");
    }
}
