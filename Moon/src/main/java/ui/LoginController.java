package ui;

import business.Service;
import domain.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController extends AbstractController implements Initializable {

    @FXML
    private ImageView moonImageView;
    @FXML
    private ImageView loginIconImageView;
    @FXML
    private ImageView backroundImageView;
    @FXML
    private Button exitButton;
    @FXML
    private TextField usernameTextFiled;
    @FXML
    private TextField passwordTextField;
    @FXML
    private Label invalidLoginMessage;


    public LoginController(Service service) {
        super(service);
    }

    public void cancelButtonOnAction(ActionEvent event) {
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void registerButtonAction(ActionEvent event) {
        ScreenController.openPage(ApplicationStages.REGISTER);
    }


    public void loginButtonAction(ActionEvent event) {
        try {
            if (usernameTextFiled.getText().isEmpty() || passwordTextField.getText().isEmpty()) {
                invalidLoginMessage.setText("Empty fileds!");
            } else if (service.isValidLogin(new UserAuthentificationCredentials(usernameTextFiled.getText(), passwordTextField.getText()))) {
                invalidLoginMessage.setText("Success");
                ScreenController.openPage(ApplicationStages.MAIN);
            } else {
                invalidLoginMessage.setText("Invalid login.Please try Again!");
            }
        } catch (Exception e) {
            System.out.println("Login Exception");
        }
    }
}
