package ui;

import business.Service;
import domain.UserAuthentificationCredentials;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import utils.Constants;

import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ResourceBundle;

public class RegisterController extends AbstractController implements Initializable {

    @FXML
    private ImageView backroundImageView;
    @FXML
    private ImageView moonLogo;
    @FXML
    private Button exitButton;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField countryField;
    @FXML
    private DatePicker dateOfBirthField;
    @FXML
    private TextField passwordField;
    @FXML
    private TextField passwordFieldAgain;
    @FXML
    private Label invaldiregisterLabel;
    @FXML
    private Button registerFromRegisterController;


    public RegisterController(Service srv) {
        super(srv);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void cancelButtonOnAction(ActionEvent event) {
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }

    public void registerFromRegisterControllerAction(ActionEvent event) {

        if (firstNameField.getText().isEmpty() || lastNameField.getText().isEmpty() || dateOfBirthField.getValue()==null
                || countryField.getText().isEmpty() || emailField.getText().isEmpty() || passwordField.getText().isEmpty()
                || passwordFieldAgain.getText().isEmpty()) {
            invaldiregisterLabel.setText("Empty fields!");
            return;
        }
        if (!passwordField.getText().equals(passwordFieldAgain.getText())) {
            invaldiregisterLabel.setText("Passwords are not the same!");
            return;
        }
        if (!service.isValidMail(emailField.getText())) {
            invaldiregisterLabel.setText("You already have an account on this mail!");
            return;
        }
        try {
            String birthDate = dateOfBirthField.getValue().toString();
            LocalDate date = LocalDate.parse(birthDate, Constants.DATE_FORMATTER);
            service.saveRegisteredUser(firstNameField.getText(), lastNameField.getText(), date, countryField.getText()
                    , new UserAuthentificationCredentials(emailField.getText(), passwordField.getText()));

            ScreenController.goBack();
        } catch (DateTimeParseException e) {
            invaldiregisterLabel.setText("Invalid date format try yyyy-MM-dd");
        }
    }
}
