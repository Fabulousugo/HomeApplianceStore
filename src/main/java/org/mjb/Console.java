/*
    Code written and maintained by
    Ugochukwu Precious Onah
    24848777
 */

package org.mjb;

import org.mjb.dao.CustomerDAO;
import org.mjb.dao.ApplianceDAO;
import org.mjb.systemModels.Address;
import org.mjb.systemModels.Appliance;
import org.mjb.systemModels.Customer;

import java.util.Scanner;

/**
 * The Console class provides the user interface for interacting with the Home Appliance store system.
 * It allows users to manage appliances and customer information, including listing, searching,
 * adding, updating, and deleting appliances and customer records.
 * <p>
 * The main menu is displayed to the user in a loop, and depending on the user's input, the corresponding
 * action is executed.
 * </p>
 */
public class Console {

    private static final Scanner scanner = new Scanner(System.in); // Scanner instance for user input

    /**
     * Displays the main menu and handles user input to perform various actions on appliances and customers.
     * The user can choose to list, search, add, update, or delete appliances or customers, or exit the system.
     */
    public void displayAppliance() {
        String input;
        do {
            Menu();
            input = scanner.nextLine(); // Scans the user input
            handleUserChoice(input); // Handle the corresponding action based on user input
        } while (!input.equals("12")); // Exit when user chooses option 12
    }

    /**
     * Prints the main menu with available options for managing appliances and customers.
     */
    private void Menu() {
        System.out.println("--------------------");
        System.out.println("The Home Appliance Store");
        System.out.println("Choose from these options");
        System.out.println("--------------------");
        System.out.println("[1] List Appliances");
        System.out.println("[2] Search product description");
        System.out.println("[3] Add new Appliances");
        System.out.println("[4] Search Appliances id");
        System.out.println("[5] Update Appliances");
        System.out.println("[6] Delete Appliances");
        System.out.println("[7] List customers");
        System.out.println("[8] Add new customer");
        System.out.println("[9] Search for customer");
        System.out.println("[10] Update customer");
        System.out.println("[11] Delete customer");
        System.out.println("[12] Exit");
        System.out.println();
    }

    /**
     * Handles the user's input and performs the corresponding action based on the input.
     * Each input corresponds to a specific action related to appliances or customers.
     *
     * @param input The user's choice from the main menu.
     */
    private void handleUserChoice(String input) {
        switch (input) {
            case "1" -> listAppliances();
            case "2" -> searchApplianceDescription();
            case "3" -> addAppliances();
            case "4" -> searchAppliancesById();
            case "5" -> updateAppliance();
            case "6" -> deleteAppliances();
            case "7" -> listCustomers();
            case "8" -> addCustomer();
            case "9" -> searchCustomerById();
            case "10" -> updateCustomer();
            case "11" -> deleteCustomer();
            case "12" -> System.out.println("Exiting... Thank You!");
            default -> System.out.println("Invalid choice, please try again!.");
        }
    }

    /**
     * Lists all appliances available in the store.
     */
    private void listAppliances() {
        for (Appliance product : new ApplianceDAO().findAppliances(null)) {
            System.out.println(product);
        }
    }

    /**
     * Searches for appliances based on the product description.
     * Displays appliances that match the provided description.
     */
    private void searchApplianceDescription() {
        System.out.println("Enter product description to search:");
        String description = scanner.nextLine();
        for (Appliance product : new ApplianceDAO().findAppliances(description)) {
            System.out.println(product);
        }
    }

    /**
     * Allows the user to add a new appliance to the store's database.
     */
    private void addAppliances() {
        System.out.println("Enter product details:");

        System.out.print("Description: ");
        String description = scanner.nextLine();
        System.out.print("SKU: ");
        String sku = scanner.nextLine();
        System.out.print("Category: ");
        String category = scanner.nextLine();
        System.out.print("Price: ");
        int price = Integer.parseInt(scanner.nextLine());
        System.out.print("Stock: ");
        int stock = Integer.parseInt(scanner.nextLine());
        System.out.print("Expires (YYYY-MM-DD): "); // the expiration date is valid from the day of entry
        String expires = scanner.nextLine();

        Appliance product = new Appliance(description, category, sku, stock, price, expires, 0);
        System.out.println(new ApplianceDAO().createAppliance(product));
    }

    /**
     * Searches for an appliance using its product ID.
     */
    private void searchAppliancesById() {
        System.out.print("Enter product ID to search: ");
        int productId = Integer.parseInt(scanner.nextLine());
        System.out.println(new ApplianceDAO().findAppliance(productId));
    }

    /**
     * Allows the user to update the details of an existing appliance.
     */
    private void updateAppliance() {
        System.out.print("Enter product ID: ");
        int id = Integer.parseInt(scanner.nextLine());

        System.out.print("New Description: ");
        String description = scanner.nextLine();
        System.out.print("New SKU: ");
        String sku = scanner.nextLine();
        System.out.print("New Category: ");
        String category = scanner.nextLine();
        System.out.print("New Price: ");
        int price = Integer.parseInt(scanner.nextLine());
        System.out.print("New Stock: ");
        int stock = Integer.parseInt(scanner.nextLine());
        System.out.print("Expires (YYYY-MM-DD): ");
        String expires = scanner.nextLine();

        Appliance product = new Appliance(description, category, sku, stock, price, expires, id);
        System.out.println(new ApplianceDAO().updateAppliance(product));
    }

    /**
     * Deletes an appliance from the database using its product ID.
     */
    private void deleteAppliances() {
        System.out.print("Enter product ID to delete: ");
        int id = Integer.parseInt(scanner.nextLine());
        System.out.println(new ApplianceDAO().deleteProduct(id));
    }

    /**
     * Lists all customers in the store's database.
     */
    private void listCustomers() {
        for (Customer customer : new CustomerDAO().findCustomers()) {
            System.out.println(customer);
        }
    }

    /**
     * Allows the user to add a new customer to the store's database.
     */
    private void addCustomer() {
        System.out.println("Enter customer details:");

        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Email Address: ");
        String email = scanner.nextLine();
        System.out.print("Preferred Password: ");
        String password = scanner.nextLine();
        System.out.print("Phone Number: ");
        String phone = scanner.nextLine();
        System.out.print("Address Line 1: ");// None of the address is compulsory, but it is advised to include at least one.
        String addressLine1 = scanner.nextLine();
        System.out.print("Address Line 2: ");
        String addressLine2 = scanner.nextLine();
        System.out.print("Address Line 3: ");
        String addressLine3 = scanner.nextLine();
        System.out.print("Post Code: ");
        String postCode = scanner.nextLine();
        System.out.print("Country: ");
        String country = scanner.nextLine();

        Address address = new Address(addressLine1, addressLine2, addressLine3, phone, country, postCode);
        Customer customer = new Customer(0, name, address, email, password);
        System.out.println(new CustomerDAO().createCustomer(customer));
    }

    /**
     * Searches for a customer using their ID.
     */
    private void searchCustomerById() {
        System.out.print("Enter customer ID to search: ");
        int customerId = Integer.parseInt(scanner.nextLine());
        System.out.println(new CustomerDAO().findCustomer(customerId));
    }

    /**
     * Allows the user to update the details of an existing customer.
     */
    private void updateCustomer() {
        System.out.print("Enter customer ID: ");
        int id = Integer.parseInt(scanner.nextLine());

        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Email Address: ");
        String email = scanner.nextLine();
        System.out.print("Preferred Password: ");
        String password = scanner.nextLine();
        System.out.print("Phone Number: ");
        String phone = scanner.nextLine();
        System.out.print("Address Line 1: ");
        String addressLine1 = scanner.nextLine();
        System.out.print("Address Line 2: ");
        String addressLine2 = scanner.nextLine();
        System.out.print("Address Line 3: ");
        String addressLine3 = scanner.nextLine();
        System.out.print("Post Code: ");
        String postCode = scanner.nextLine();
        System.out.print("Country: ");
        String country = scanner.nextLine();

        Address address = new Address(addressLine1, addressLine2, addressLine3, phone, country, postCode);
        Customer customer = new Customer(id, name, address, email, password);
        System.out.println(new CustomerDAO().updateCustomer(customer));
    }

    /**
     * Deletes a customer from the database using their ID.
     */
    private void deleteCustomer() {
        System.out.print("Enter customer ID to delete: ");
        int id = Integer.parseInt(scanner.nextLine());
        System.out.println(new CustomerDAO().deleteCustomer(id));
    }
}
