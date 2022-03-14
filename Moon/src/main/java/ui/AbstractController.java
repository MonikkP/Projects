package ui;

import business.Service;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class AbstractController {
    protected Service service;

    @FXML
    protected ImageView profileImageButton;
    @FXML
    protected ImageView moonLogo;
    @FXML
    protected Button exitButton;
    @FXML
    protected ImageView goBack;

    private double xOffset;
    private double yOffset;

    public AbstractController(Service service) {
        this.service = service;
    }

    public void logoOnAction() {
        ScreenController.openPage(ApplicationStages.LOGIN);
    }

    protected void profilePageOnAction() {
        profileImageButton.setOnMouseClicked(event -> {
            ScreenController.openPage(ApplicationStages.PROFILE);
        });
    }

    public void exitButtonOnAction(ActionEvent event) {
        ScreenController.closeApplication();
    }

    public void goBack() {
        ScreenController.goBack();
    }

    public void setService(Service service) {
        this.service = service;
    }

    public void reactWhenPressed(MouseEvent event) {
        xOffset = event.getSceneX();
        yOffset = event.getSceneY();
    }

    public void reactWhenDragged(MouseEvent event) {
        Stage thisStage = ScreenController.getCurrentStage();
        thisStage.setX(event.getScreenX() - xOffset);
        thisStage.setY(event.getScreenY() - yOffset);
    }
}
