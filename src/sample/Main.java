package sample;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.dao.JDBC;
import sample.dao.impl.ContactDaoImpl;
import sample.model.Contact;

public class Main extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("view/sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }


    public static void main(String[] args) throws Exception {
        Class.forName("sample.dao.impl.ContactDaoImpl");
        for (Contact contact : test.getcon()) {
            System.out.println(contact.getContactName());
        }

        test.updatecon();

        for (Contact contact : test.getcon()) {
            System.out.println(contact.getContactName());
        }
        launch(args);

    }
}
