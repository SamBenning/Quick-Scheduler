package sample.model;

/**
 * Model object class to represent customer records from the database.*/
public class Customer {

    private int customerId;
    private final String customerName;
    private final String address;
    private final String postalCode;
    private final String phone;
    private final int divisionId;

    public Customer(int customerId, String customerName, String address, String postalCode, String phone, int divisionId) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.address = address;
        this.postalCode = postalCode;
        this.phone = phone;
        this.divisionId = divisionId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getAddress() {
        return address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getPhone() {
        return phone;
    }

    public int getDivisionId() {
        return divisionId;
    }

    @Override
    public String toString() {
        return customerName + " (ID: " + customerId + ")";
    }
}
