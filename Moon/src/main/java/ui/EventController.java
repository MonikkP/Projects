package ui;

import business.Service;
import domain.Event;
import domain.NotificationSubscription;
import domain.Observer;
import domain.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import ui.styleImage.CustomStyle;
import utils.Constants;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;

public class EventController extends AbstractNotifiableController implements Initializable, Observer {

    @FXML
    private Button addEvent;
    @FXML
    private FlowPane flowPane;
    @FXML
    private VBox participantsVBox;
    @FXML
    private TextArea textArea;

    public EventController(Service service) {
        super(service);
        service.addObserver(this);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        CustomStyle.setStyleExitButton(exitButton);
        CustomStyle.setStyleImage(profileImageButton);
        CustomStyle.setStyleImage(moonLogo);
        logoOnAction();
        profilePageOnAction();
        goBack.setOnMouseEntered(event -> {
            goBack.getScene().setCursor(Cursor.HAND);
        });
        goBack.setOnMouseExited(event -> {
            goBack.getScene().setCursor(Cursor.DEFAULT);
        });
        participantsVBox.setVisible(false);
        textArea.setVisible(false);
        textArea.setId("textArea");
        loadData();
    }

    public void saveEvent(Event event) {

        VBox vBox = new VBox();
        vBox.setId("vbox");

        Label name = new Label(event.getName());
        name.setId("name-label");

        Label descr = new Label(event.getDescription());
        descr.setId("description-label");

        Label location = new Label(event.getLocation());
        location.setId("location-label");

        Label owner = new Label("Owned by: " + event.getOwner().getFullName());
        owner.setId("owner-label");

        Label date = new Label(event.getDateTime().format(Constants.DATE_TIME_FORMATTER));
        date.setId("date-label");

        Label participants = new Label("There are " + event.getParticipants().size() + " participants.");
        participants.setId("participants-label");
        participants.setOnMouseEntered(e -> {
            if (event.getParticipants().size() == 0)
                return;
            participantsVBox.setVisible(true);
            participantsVBox.toFront();
            participantsVBox.setLayoutX(e.getSceneX());
            participantsVBox.setLayoutY(e.getSceneY());
            for (User user : event.getParticipants()) {
                Label userLabel = new Label(user.getFullName());
                userLabel.setId("user-label");
                participantsVBox.getChildren().add(new Group(userLabel));
            }
        });
        participants.setOnMouseExited(e -> {
            participantsVBox.setVisible(false);
            participantsVBox.getChildren().clear();
        });

        descr.setOnMouseEntered(e -> {
            if (event.getDescription().length()<35)
                return;
            textArea.setVisible(true);
            textArea.toFront();
            textArea.setLayoutX(e.getSceneX());
            textArea.setLayoutY(e.getSceneY());
           textArea.setText(event.getDescription());
        });
        descr.setOnMouseExited(e -> {
            textArea.setVisible(false);
            textArea.clear();
        });

        Button register = new Button();
        if (!event.getParticipants().contains(service.getLoggedUser())) {
            register.setText("Attend");
        } else {
            register.setText("Cancel attending");
        }
        register.setOnMouseClicked(e -> {
           if (!event.getParticipants().contains(service.getLoggedUser())) {
               register.setText("Cancel attending");
               service.saveEventParticipation(event);
           } else {
               register.setText("Attend");
               service.deleteEventParticipation(event);
           }
        });
        register.setId("button");


        vBox.getChildren().add(date);
        vBox.getChildren().add(name);
        vBox.getChildren().add(owner);
        vBox.getChildren().add(descr);
        vBox.getChildren().add(location);
        vBox.getChildren().add(participants);
        vBox.getChildren().add(register);
        if (event.getParticipants().contains(service.getLoggedUser())) {
            final Button subscribeButton = new Button();
            subscribeButton.setId("button");
            if (service.findNotificationSubscriptionStatus(event) == NotificationSubscription.SUBSCRIBED) {
                subscribeButton.setText("Unsubscribe");
            } else {
                subscribeButton.setText("Subscribe");
            }
            subscribeButton.setOnMouseClicked(e -> {
                if (service.findNotificationSubscriptionStatus(event) == NotificationSubscription.SUBSCRIBED) {
                    subscribeButton.setText("Subscribed");
                    service.unsubscribeFromNotifications(event);
                } else {
                    subscribeButton.setText("Unsubscribed");
                    service.subscribeToNotification(event);
                }
            });
            vBox.getChildren().add(subscribeButton);
        }

        flowPane.getChildren().add(vBox);

    }

    private void loadData() {
        flowPane.getChildren().clear();
        for(Event e : service.findAllEvents()) {
            saveEvent(e);
        }
    }

    public void addEventOnAction() {
        ScreenController.openPage(ApplicationStages.NEW_EVENT);
    }

    @Override
    public void update() {
        loadData();
    }
}