package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.dao.JDBC;

import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

public class Main extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("view/loginForm.fxml")));
        primaryStage.setTitle("Quick Scheduler");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
    }


    public static void main(String[] args) throws Exception {
        /*ResourceBundle resourceBundle = ResourceBundle.getBundle("sample/util/Login", Locale.FRENCH);
        if(Locale.FRENCH.getLanguage().equals("fr")) {
            System.out.println(resourceBundle.getString("Quick Scheduler"));
        }*/
        //Locale.setDefault(Locale.FRENCH);
        JDBC.openConnection();
        launch(args);
        JDBC.closeConnection();
    }
}
