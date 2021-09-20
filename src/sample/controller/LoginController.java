package sample.controller;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import sample.dao.UserDao;
import sample.util.JavaFXUtil;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    public TextField usernameField;
    public TextField passwordField;
    public Button loginButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void attemptLoginHandler(ActionEvent actionEvent) {
        boolean credentialsValid = UserDao.checkCredentials(usernameField.getText(), passwordField.getText());
        if (credentialsValid) {
            JavaFXUtil.changeWindow(actionEvent,
                    "/sample/view/mainForm.fxml",900, 500);
        }
    }
}
