package sample.model;

/**
 * Model object class to represent contact records from the database.*/
public class Contact {
    private int contactId;
    private String contactName;
    private String email;


    public Contact(String contactName, String email) {
        this.contactName = contactName;
        this.email = email;
    }

    public Contact(int contactId, String contactName, String email) {
        this.contactId = contactId;
        this.contactName = contactName;
        this.email = email;
    }

    public int getContactId() {
        return contactId;
    }

    public String getContactName() {
        return contactName;
    }

    @Override
    public String toString() {
        return "(ID: " + contactId + ") " + contactName;
    }
}
