/*
    Code written and maintained by
    Ugochukwu Precious Onah
    24848777
 */

package org.mjb.systemModels;

import java.util.ArrayList;

/**
 * Represents an order placed by a customer in the system.
 * The order contains details such as the invoice number, customer information,
 * a list of items in the order, and the date when the order was created.
 */
public class Order {
    private final String invoiceNo;
    private final Customer customer;
    private final ArrayList<OrderItems> items;
    private final String createdOn;
    private final int id;

    /**
     * Instantiates a new Order.
     *
     * @param invoiceNo the invoice number for the order.
     * @param customer the customer placing the order.
     * @param items the list of items in the order.
     * @param createdOn the date when the order was created.
     * @param id the unique identifier for the order.
     */
    public Order(String invoiceNo, Customer customer, ArrayList<OrderItems> items, String createdOn, int id) {
        this.invoiceNo = invoiceNo;
        this.customer = customer;
        this.items = items;
        this.createdOn = createdOn;
        this.id = id;
    }

    /**
     * Gets the unique identifier for the order.
     *
     * @return the order ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the invoice number for the order.
     *
     * @return the invoice number.
     */
    public String getInvoiceNo() {
        return invoiceNo;
    }

    /**
     * Gets the customer who placed the order.
     *
     * @return the customer object.
     */
    public Customer getCustomer() {
        return customer;
    }

    /**
     * Gets the list of items in the order.
     *
     * @return the list of order items.
     */
    public ArrayList<OrderItems> getItems() {
        return items;
    }

    /**
     * Gets the creation date of the order.
     *
     * @return the creation date of the order.
     */
    public String getCreatedOn() {
        return createdOn;
    }

    /**
     * Provides a string representation of the Order object.
     *
     * @return a string containing the details of the order.
     */
    @Override
    public String toString() {
        return "Order{" +
                "invoiceNo='" + invoiceNo + '\'' +
                ", customer=" + customer +
                ", items=" + items +
                ", createdOn='" + createdOn + '\'' +
                ", id=" + id +
                '}';
    }

    /**
     * Calculates the total cost of all the items in the order.
     *
     * @return the total price of the items in the order.
     */
    public int getTotal() {
        int t = 0;
        for (OrderItems item : items) {
            t += item.getTotal();
        }
        return t;
    }
}
