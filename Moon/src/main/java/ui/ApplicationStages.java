package ui;

import business.Service;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public enum ApplicationStages {
    LOGIN,
    REGISTER,
    MAIN,
    PROFILE,
    PASSWORD_CHANGE,
    MESSENGER,
    NEW_CONVERSATION,
    EVENT,
    NEW_EVENT,
    REPORTS;

    public static Stage getStage(ApplicationStages selectedStage, Service service) {
        String fxmlFileString = "/fxml/";
        AbstractController controller = null;
        switch (selectedStage) {
            case LOGIN:
                fxmlFileString += "login-view.fxml";
                controller = new LoginController(service);
                break;
            case REGISTER:
                fxmlFileString += "register-view.fxml";
                controller = new RegisterController(service);
                break;
            case MAIN:
                fxmlFileString += "main-view.fxml";
                controller = new MainViewController(service);
                break;
            case PROFILE:
                fxmlFileString += "profile-view.fxml";
                controller = new ProfileController(service);
                break;
            case PASSWORD_CHANGE:
                fxmlFileString += "change-password.fxml";
                controller = new PasswordChangeController(service);
                break;
            case MESSENGER:
                fxmlFileString += "messenger.fxml";
                controller = new MessengerController(service);
                break;
            case NEW_CONVERSATION:
                fxmlFileString += "new-conversation.fxml";
                controller = new ConversationController(service);
                break;
            case EVENT:
                fxmlFileString += "event-view.fxml";
                controller = new EventController(service);
                break;
            case NEW_EVENT:
                fxmlFileString += "new-event.fxml";
                controller = new NewEventController(service);
                break;
            case REPORTS:
                fxmlFileString +="reports.fxml";
                controller = new ReportsController(service);
                break;
        }
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(LoginController.class.getResource(fxmlFileString));
            AbstractController finalController = controller;
            fxmlLoader.setControllerFactory(c -> finalController);

            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(scene);
            return stage;
        } catch (IOException i) {
            i.printStackTrace();
            return null;
        }
    }
}
