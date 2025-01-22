package org.mjb.systemModels;

/**
 * The Address class represents a physical address with various components such as street address, phone number,
 * country, and postal code.
 * It provides accessors for these components and a string representation of the address.
 *
 * <p>Code written and maintained by Ugochukwu Precious Onah, 24848777.</p>
 */
public class Address {
    private final String address1;
    private final String address2;
    private final String address3;
    private final String phone;
    private final String country;
    private final String postCode;

    /**
     * Instantiates a new Address with the provided details.
     *
     * @param address1 the first line of the address
     * @param address2 the second line of the address
     * @param address3 the third line of the address
     * @param phone the phone number associated with the address
     * @param country the country of the address
     * @param postCode the postal code of the address
     */
    public Address(String address1, String address2, String address3, String phone, String country, String postCode) {
        this.address1 = address1;
        this.address2 = address2;
        this.address3 = address3;
        this.phone = phone;
        this.country = country;
        this.postCode = postCode;
    }

    /**
     * Returns a string representation of the address including all components.
     *
     * @return a string representing the address in the format:
     *         Address{address1='...', address2='...', address3='...', phone='...', country='...', postCode='...'}
     */
    @Override
    public String toString() {
        return "Address{" +
                "address1='" + address1 + '\'' +
                ", address2='" + address2 + '\'' +
                ", address3='" + address3 + '\'' +
                ", phone='" + phone + '\'' +
                ", country='" + country + '\'' +
                ", postCode='" + postCode + '\'' +
                '}';
    }

    /**
     * Gets the first line of the address.
     *
     * @return the first line of the address
     */
    public String getAddress1() {
        return address1;
    }

    /**
     * Gets the second line of the address.
     *
     * @return the second line of the address
     */
    public String getAddress2() {
        return address2;
    }

    /**
     * Gets the third line of the address.
     *
     * @return the third line of the address
     */
    public String getAddress3() {
        return address3;
    }

    /**
     * Gets the phone number associated with the address.
     *
     * @return the phone number
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Gets the country of the address.
     *
     * @return the country
     */
    public String getCountry() {
        return country;
    }

    /**
     * Gets the postal code of the address.
     *
     * @return the postal code
     */
    public String getPostCode() {
        return postCode;
    }
}
