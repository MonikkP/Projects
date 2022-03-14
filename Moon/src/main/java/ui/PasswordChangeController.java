package ui;

import business.Service;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class PasswordChangeController extends  AbstractController implements Initializable {

    @FXML
    private PasswordField currentPasswordTextField;
    @FXML
    private PasswordField newPassword1;
    @FXML
    private PasswordField newPassword2;

    @FXML
    private Button doneButton;
    @FXML
    private Button finishButton;

    @FXML
    private Label warningLabel;

    @FXML
    private ImageView imageViewGoBack;

    public PasswordChangeController(Service service) {
        super(service);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        imageViewGoBack.setOnMouseEntered(event -> {
            doneButton.getScene().setCursor(Cursor.HAND);
        });

        imageViewGoBack.setOnMouseExited(event -> {
            doneButton.getScene().setCursor(Cursor.DEFAULT);
        });
    }

    public void doneButtonOnAction(ActionEvent event) {
        if(!service.getLoggedUser().getUserAuthentificationCredentials().checkIfPasswordMatches(currentPasswordTextField.getText())) {
            warningLabel.setText("Incorrect Password");
            warningLabel.setVisible(true);
        } else {
            currentPasswordTextField.setVisible(false);
            newPassword1.setVisible(true);
            newPassword2.setVisible(true);
            doneButton.setVisible(false);
            finishButton.setVisible(true);
            }
    }

    public void finishButtonOnAction(ActionEvent event) {
        if(!newPassword1.getText().equals(newPassword2.getText())) {
            warningLabel.setText("Different passwords!");
            warningLabel.setVisible(true);
        } else {
            service.updatePassword(service.getLoggedUser().getId(), newPassword2.getText());
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("Password changed successfully");
            alert.show();
        }
    }

}
