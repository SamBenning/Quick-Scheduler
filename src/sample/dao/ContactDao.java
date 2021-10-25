package sample.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.model.Contact;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Handles sending queries and updates to the contacts table in the database.*/
public class ContactDao {
    /**
     * Queries the Contacts table in the DB and returns an ObservableList of all contacts.
     * @return An observable list containing contact objects.*/
    public static ObservableList<Contact> getAllContacts() {
        ObservableList<Contact> contacts = FXCollections.observableArrayList();
        try {
            String query = "SELECT * FROM contacts;";
            Statement statement = JDBC.connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                Contact newContact = new Contact(resultSet.getInt(1),
                        resultSet.getString(2), resultSet.getString(3));
                contacts.add(newContact);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (NullPointerException e) {
            System.out.println("null ptr exception");
        }
        return contacts;
    }
}
