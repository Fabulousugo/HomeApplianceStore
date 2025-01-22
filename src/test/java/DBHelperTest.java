package org.mjb;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DBHelperTest {

    private DBHelper dbHelper;

    @BeforeEach
    public void setup() {
        dbHelper = new DBHelper();
    }

    @Test
    public void testInitializeDBTables() {
        // Test if the DBHelper correctly initializes the tables
        try {
            dbHelper.initializeDBTables();
        } catch (Exception e) {
            fail("Exception thrown during table initialization: " + e.getMessage());
        }
    }

    @Test
    public void testReadFile() {
        // Test if the readFile method correctly reads the content of the SQL file
        try {
            String sqlContent = dbHelper.readFile("create.sql");
            assertNotNull(sqlContent, "The SQL content should not be null");
            assertTrue(sqlContent.contains("CREATE TABLE"), "The SQL content should contain 'CREATE TABLE'");
        } catch (Exception e) {
            fail("Exception thrown during file reading: " + e.getMessage());
        }
    }
}
