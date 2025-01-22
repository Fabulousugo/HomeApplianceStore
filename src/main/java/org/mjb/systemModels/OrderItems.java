/*
    Code written and maintained by
    Ugochukwu Precious Onah
    24848777
 */

package org.mjb.systemModels;

/**
 * Represents an item in a customer's order.
 * Each `OrderItems` object contains a product (an instance of the `Appliance` class)
 * and the quantity of the product being ordered.
 * It is used to model the items within an order.
 */
public class OrderItems {

    private final Appliance product;
    private final int quantity;

    /**
     * Instantiates a new `OrderItems` object with the specified product and quantity.
     *
     * @param product  The appliance being ordered.
     * @param quantity The quantity of the appliance being ordered.
     */
    public OrderItems(Appliance product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    /**
     * Gets the appliance (product) being ordered.
     *
     * @return The appliance product in the order.
     */
    public Appliance getProduct() {
        return product;
    }

    /**
     * Gets the quantity of the appliance in the order.
     *
     * @return The quantity of the product in the order.
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Calculates the total price for this item, which is the product price multiplied by the quantity.
     *
     * @return The total price for the item.
     */
    public int getTotal() {
        return product.getPrice() * quantity;
    }

    /**
     * Provides a string representation of the `OrderItems` object, which includes the product,
     * quantity, and the summed price for this order item.
     *
     * @return A string representation of the `OrderItems` object.
     */
    @Override
    public String toString() {
        return "OrderItems{" +
                "product=" + product +
                ", quantity=" + quantity +
                ", summedPrice=" + getTotal() +
                '}';
    }
}
