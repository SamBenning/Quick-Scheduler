package sample.dao;

import javafx.collections.ObservableList;
import sample.model.Contact;

import java.sql.SQLException;

public interface ContactDao {
    public ObservableList<Contact> getAllContacts () throws SQLException;
    public ObservableList<Contact> getContact (String name) throws SQLException;
    public boolean addContact (Contact contact) throws SQLException;
    public boolean updateContact (int existingId, Contact newContact) throws SQLException;
    public boolean deleteContact (Contact contact) throws SQLException;
}
