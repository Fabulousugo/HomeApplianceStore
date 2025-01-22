/*
    Code written and maintained by
    Ugochukwu Precious Onah
    24848777
 */
package org.mjb;

import org.mjb.dao.UserDAO;
import org.mjb.systemModels.User;
import org.mjb.webServer.WebServer;

import java.util.Scanner;


/**
 * The main entry point of the application.
 * Handles initialization, user interaction, and starting the appropriate mode (Console or Web Server).
 */
public class Main {
    public static void main(String[] args) {
        try {
            // Step 1: This is used to Initialize the sqlite database tables
            //
            // System.out.println("Initializing database tables...");
            new DBHelper().initializeDBTables();

            // Optional debug step: Uncomment to test fetching customers from the database
            // CustomerDAO customerDAO = new CustomerDAO();
            // customerDAO.debugFindCustomers();

            // Step 2: The user can access the system using the options made available to them...
            Scanner scanner = new Scanner(System.in);
            System.out.println("Choose the application mode:");
            System.out.println("[1] Console Mode"); // accessing the system in the console wouldn't let you appreciate my html skills.
            System.out.println("[2] Web Server Mode");
            System.out.print("Enter your choice: ");
            String input = scanner.nextLine();

            // Step 3: Handles user selection
            switch (input) {
                case "1" -> launchConsoleMode();
                case "2" -> launchWebServerMode();
                default -> System.out.println("Invalid choice. Please restart the application.");
            }
        } catch (Exception e) {
            // Log any unexpected errors
            System.err.println("An unexpected error occurred:");
            e.printStackTrace();
        }
    }

    /**
     * Launches the application in Console mode.
     * Displays the console menu and interacts with the user.
     */
    private static void launchConsoleMode() {
        System.out.println("Starting Console Mode...");
        new Console().displayAppliance();
    }

    /**
     * Launches the application in Web Server mode.
     * Ensures an admin user exists and starts the web server.
     *
     * @throws Exception if any error occurs during initialization.
     */
    private static void launchWebServerMode() throws Exception {
        System.out.println("Starting Web Server Mode...");

        // Admin credentials
        String Username = "admin";
        String Password = "admin";

        // Ensure admin user exists
        // The system is designed to either create a new admin user, if none presently exist or alert the user that an admin user exists.
        // Guards against redundacy of sorts.
        UserDAO userDAO = new UserDAO();
        if (userDAO.loginAsUser(Username, Password) == null) {
            userDAO.createUser(new User(0, Username, Password));
            System.out.println("Admin user created.");
        } else {
            System.out.println("Admin user already exists; if you are the admin, enter your credentials.");
        }

        // Start the web server
        new WebServer(5000); // Port 8080 could have worked just fine too.
        System.out.printf("""
                Web Server  has started successfully.
                Use these for the Admin credentials:
                Username: %s
                Password: %s
                """, Username, Password);
    }
}
