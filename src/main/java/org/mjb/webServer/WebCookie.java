// Code written and maintained by
// Ugochukwu Precious Onah
// 24848777
package org.mjb.webServer;

import org.mjb.systemModels.Customer;
import org.mjb.systemModels.User;

/**
 * The type WebCookie represents the session information for the logged-in user and customer.
 * It stores the user and customer data and provides methods to access and update the session details.
 */
public class WebCookie {
    private User loggedUser;
    private Customer loggedCustomer;

    /**
     * Instantiates a new WebCookie with the logged user and customer details.
     *
     * @param loggedUser     the logged user
     * @param loggedCustomer the logged customer
     */
    public WebCookie(User loggedUser, Customer loggedCustomer) {
        this.loggedUser = loggedUser;
        this.loggedCustomer = loggedCustomer;
    }

    /**
     * Gets the logged user.
     *
     * @return the logged user
     */
    public User getLoggedUser() {
        return loggedUser;
    }

    /**
     * Gets the logged customer.
     *
     * @return the logged customer
     */
    public Customer getLoggedCustomer() {
        return loggedCustomer;
    }

    /**
     * Sets the logged user.
     *
     * @param loggedUser the logged user
     */
    public void setLoggedUser(User loggedUser) {
        this.loggedUser = loggedUser;
    }

    /**
     * Sets the logged customer.
     *
     * @param loggedCustomer the logged customer
     */
    public void setLoggedCustomer(Customer loggedCustomer) {
        this.loggedCustomer = loggedCustomer;
    }
}
