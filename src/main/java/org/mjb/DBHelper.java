/*
    Code written and maintained by
    Ugochukwu Precious Onah
    24848777
 */

package org.mjb;

import java.sql.*;
import java.util.Properties;
import java.io.InputStream;
import java.io.*;
/**
 * The DBHelper class provides methods for initializing and destroying SQLite database tables.
 * It handles reading SQL files and executing their contents to create or delete database tables.
 * This class is used for database setup and teardown by executing SQL statements defined in external files.
 */
public class DBHelper {

    /**
     * Reads the content of a file and returns it as a string.
     * The file is read from the classpath using the provided path.
     *
     * @param path the path to the file in the classpath.
     * @return the content of the file as a string.
     * @throws IOException if an I/O error occurs while reading the file.
     */
    public String readFile(String path) throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(path);
        if (inputStream == null) {
            throw new FileNotFoundException("File not found: " + path);
        }

        StringBuilder string = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                string.append(line).append("\n");
            }
        }

        return string.toString();
    }

    /**
     * Initializes the database tables by executing SQL statements from the "create.sql" file.
     * Each SQL statement is executed in sequence to create the necessary tables in the SQLite database.
     *
     * @throws IOException if there is an issue reading the SQL file.
     */
    public void initializeDBTables() {
        try (Connection connector = new Connector().connect()) {
            // Read and execute each SQL statement from the "create.sql" file
            for (String sql : readFile("create.sql").split(";")) {
                if (!sql.trim().isEmpty()) {
                    try {
                        connector.createStatement().execute(sql.trim());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Destroys the database tables by executing SQL statements from the "delete.sql" file.
     * Each SQL statement is executed to remove the corresponding tables in the SQLite database.
     *
     * @throws IOException if there is an issue reading the SQL file.
     */
    public void destroyDBTables() {
        try (Connection c = new Connector().connect()) {
            // Read and execute each SQL statement from the "delete.sql" file
            for (String sql : readFile("delete.sql").split(";")) {
                if (!sql.trim().isEmpty()) {
                    try {
                        c.createStatement().execute(sql.trim());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
