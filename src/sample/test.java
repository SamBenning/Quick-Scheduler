package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.dao.impl.ContactDaoImpl;
import sample.model.Contact;

import java.sql.SQLException;

public class test {
    ContactDaoImpl contactDao = new ContactDaoImpl();

    public static ObservableList<Contact> getcon(){

        ObservableList<Contact> contacts = FXCollections.observableArrayList();
        try {
            ContactDaoImpl inst = new ContactDaoImpl();
            contacts =  inst.getAllContacts();
            return contacts;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return contacts;
        }
    }

    public static ObservableList<Contact> getconname(String name) throws Exception{
        ObservableList<Contact> contacts = FXCollections.observableArrayList();
        try {
            ContactDaoImpl inst = new ContactDaoImpl();
            contacts = inst.getContact(name);
            return contacts;
        } catch (Exception e) {
            System.out.println(e);
            return contacts;
        }
    }

    public static void addcon() throws Exception {

        ContactDaoImpl inst = new ContactDaoImpl();
        Contact con = new Contact("Tim Conklin", "tim@tim.com");
        inst.addContact(con);
    }

    public static void deletecon(Contact contact) throws Exception {
        ContactDaoImpl inst = new ContactDaoImpl();
        inst.deleteContact(contact);
    }

    public static void updatecon() throws Exception {
        ContactDaoImpl inst = new ContactDaoImpl();
        Contact con = new Contact("Samuel Benning", "sam@ben.com");
        System.out.println(inst.updateContact(4, con));
    }



}
