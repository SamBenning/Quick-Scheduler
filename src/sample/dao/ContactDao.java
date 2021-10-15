package sample.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.model.Contact;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ContactDao {
    /**
     * Queries the Contacts table in the DB and returns an ObservableList of all contacts.*/
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

    /**
     * Searches the Contact table in DB for Contact_Name fields matching passed in String.
     * Uses wildcard characters in query, so if "Roger" is passed in, both "Dan Rogers" and
     * "Roger Daniels" should be returned in ObservableList.*/
    public static ObservableList<Contact> getContact(String name) throws SQLException {
        ObservableList<Contact> contacts = FXCollections.observableArrayList();
        try {
            JDBC.openConnection();
            String query = "select * from contacts where Contact_Name like ? OR Contact_Name like ?;";
            PreparedStatement preparedStatement = JDBC.connection.prepareStatement(query);
            preparedStatement.setString(1, name + "%");
            preparedStatement.setString(2, "%" + name);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Contact newContact = new Contact(resultSet.getInt(1),
                        resultSet.getString(2), resultSet.getString(3));
                contacts.add(newContact);
            }
            JDBC.closeConnection();
            return contacts;
        } catch (Exception e) {
            System.out.println(e);
            return contacts;
        }
    }

    public static boolean addContact(Contact contact) throws SQLException {
        try {
            JDBC.openConnection();
            String name = contact.getContactName();
            String email = contact.getEmail();
            String query = "insert into contacts(Contact_Name, Email) values(?,?);";
            PreparedStatement preparedStatement = JDBC.connection.prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, email);
            preparedStatement.executeUpdate();
            JDBC.closeConnection();
            return true;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }

    public boolean updateContact(int existingId, Contact newContact) throws SQLException {
        try {
            JDBC.openConnection();
            String name = newContact.getContactName();
            String email = newContact.getEmail();

            String query = "update contacts " +
                    "set Contact_Name = ?, Email = ? " +
                    "where Contact_ID = ?;";
            PreparedStatement preparedStatement = JDBC.connection.prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, email);
            preparedStatement.setInt(3, existingId);
            preparedStatement.executeUpdate();
            JDBC.closeConnection();
            return true;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }

    public boolean deleteContact(Contact contact) throws SQLException {
        try {
            JDBC.openConnection();
            String query = "delete from contacts where Contact_ID = ?";
            PreparedStatement preparedStatement = JDBC.connection.prepareStatement(query);
            preparedStatement.setInt(1, contact.getContactId());
            preparedStatement.executeUpdate();
            JDBC.closeConnection();
            return true;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }
}
