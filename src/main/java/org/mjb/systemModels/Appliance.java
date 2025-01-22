/*
    Code written and maintained by
    Ugochukwu Precious Onah
    24848777
 */
/**
 * The {@code Appliance} class represents an appliance product in the system.
 * It contains details about the appliance, such as its description, category,
 * SKU (stock-keeping unit), stock quantity, price, expiration date, and a unique identifier ({@code id}).
 * The class is also referred to as "product" in the project and is used interchangeably.
 *
 * <p> This class is part of the system model for managing product information in a store or inventory system.
 * It allows for tracking product details and supporting inventory management functions.
 *
 * @see Address
 */
package org.mjb.systemModels;

public class Appliance {

    private final String description;
    private final String category;
    private final String sku;
    private final int id;
    private final int stock;
    private final int price;
    private final String expiresOn;

    /**
     * Instantiates a new Appliance.
     *
     * @param description the description of the appliance
     * @param category    the category of the appliance
     * @param sku         the SKU (stock-keeping unit) of the appliance
     * @param stock       the quantity of the appliance in stock
     * @param price       the price of the appliance
     * @param expiresOn   the expiration date of the appliance, if applicable
     * @param id          the unique identifier for the appliance
     */
    public Appliance(String description, String category, String sku, int stock, int price, String expiresOn, int id) {
        this.description = description;
        this.category = category;
        this.sku = sku;
        this.stock = stock;
        this.price = price;
        this.expiresOn = expiresOn;
        this.id = id;
    }

    /**
     * Gets the description of the appliance.
     *
     * @return the description of the appliance
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets the category of the appliance.
     *
     * @return the category of the appliance
     */
    public String getCategory() {
        return category;
    }

    /**
     * Gets the SKU (stock-keeping unit) of the appliance.
     *
     * @return the SKU of the appliance
     */
    public String getSku() {
        return sku;
    }

    /**
     * Gets the quantity of the appliance in stock.
     *
     * @return the stock quantity of the appliance
     */
    public int getStock() {
        return stock;
    }

    /**
     * Gets the price of the appliance.
     *
     * @return the price of the appliance
     */
    public int getPrice() {
        return price;
    }

    /**
     * Gets the expiration date of the appliance.
     *
     * @return the expiration date of the appliance
     */
    public String getExpiresOn() {
        return expiresOn;
    }

    /**
     * Gets the unique identifier for the appliance.
     *
     * @return the ID of the appliance
     */
    public int getId() {
        return id;
    }

    /**
     * Returns a string representation of the appliance, including all its attributes.
     * This method is useful for logging and debugging purposes.
     *
     * @return a string representation of the appliance
     */
    @Override
    public String toString() {
        return "Appliance{" +
                "description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", sku='" + sku + '\'' +
                ", stock=" + stock +
                ", price=" + price +
                ", expiresOn='" + expiresOn + '\'' +
                ", id=" + id +
                '}';
    }

    /**
     * Generates an HTML table row for the appliance. This method is designed to be extended
     * and may return an empty string depending on the context, such as whether the user is an admin.
     *
     * @param isAdmin a flag indicating if the current user is an administrator
     * @return an HTML formatted string representing the appliance in a table row format
     */
    public String toHTMLTableItem(boolean isAdmin) {
        return "";
    }
}
