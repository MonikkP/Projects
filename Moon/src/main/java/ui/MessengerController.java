package ui;

import business.Service;
import domain.ConversationMembers;
import domain.Message;
import domain.Observer;
import domain.User;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.util.Callback;
import ui.styleImage.CustomStyle;
import utils.Constants;

import java.net.URL;
import java.util.ResourceBundle;

public class MessengerController extends AbstractNotifiableController implements Initializable, Observer {

    private final double ORIGINAL_CHAT_VERTICAL_BOX_HEIGHT = 506.4;
    @FXML
    private ImageView backgroundImageView;
    @FXML
    private ImageView sendMessageImageView;
    @FXML
    private ImageView newConversationImageView;
    @FXML
    private ListView<ConversationMembers> conversationsListView;
    @FXML
    private ScrollPane chatScrollPanel;
    @FXML
    private VBox chatVerticalBox;
    @FXML
    private TextArea chatTextArea;

    private ObservableList<ConversationMembers> conversationsModel = FXCollections.observableArrayList();

    private User lastDisplayedUser;


    public MessengerController(Service service) {
        super(service);
        service.addObserver(this);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        conversationsListView.setCellFactory(new Callback<ListView<ConversationMembers>, ListCell<ConversationMembers>>() {
            @Override
            public ListCell<ConversationMembers> call(ListView<ConversationMembers> param) {
                ListCell<ConversationMembers> cell = new ListCell<ConversationMembers>() {
                    @Override
                    public void updateItem(ConversationMembers item, boolean empty) {
                        super.updateItem(item, empty);
                        setStyle("-fx-background-color: rgb(0,0,0,0);");
                        if (empty) {
                            setBorder(Border.EMPTY);
                        } else {
                            setGraphic(convertConversationMembersToFlowPane(item));
                            setId("conversation");
                        }
                    }
                };
                cell.setOnMouseClicked(e -> {
                    setConversation(conversationsListView.getSelectionModel().getSelectedItem());
                });
                return cell;
            }
        });
        loadData();
        conversationsListView.setItems(conversationsModel);
        chatTextArea.setOnKeyReleased(e -> {
            if (e.getCode() == KeyCode.ENTER)
                sendMessage();
        });
        chatVerticalBox.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (newValue.doubleValue() > ORIGINAL_CHAT_VERTICAL_BOX_HEIGHT)
                    chatScrollPanel.setVvalue(newValue.doubleValue());
                else
                    chatScrollPanel.setVvalue(0);
            }
        });

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
    }

    private void loadData() {
        conversationsModel.setAll(service.findConversationsOfLoggedUser());
    }

    private FlowPane convertConversationMembersToFlowPane(ConversationMembers users) {
        FlowPane conversationFlowPane = new FlowPane();
        conversationFlowPane.setStyle("-fx-background-color: rgb(0,0,0,0);");
        for (User user : users.getUsers()) {
            if (!user.equals(service.getLoggedUser()))
                conversationFlowPane.getChildren().add(convertUserToTextflow(user));
        }
        conversationFlowPane.setMaxWidth(conversationsListView.getWidth());
        conversationFlowPane.setPadding(new Insets(10, 0, 10, 0));
        conversationFlowPane.setVgap(2);
        conversationFlowPane.setHgap(2);
        return conversationFlowPane;
    }

    private TextFlow convertUserToTextflow(User toConvert) {
        Text text = new Text(toConvert.getFullName());
        TextFlow textFlow = new TextFlow(text);
        textFlow.setStyle("-fx-color: rgb(239, 242, 255); "
                + "-fx-background-color: rgb(199, 199, 204);"
                + "-fx-background-radius: 20px");
        textFlow.setPadding(new Insets(5, 10, 5, 10));
        text.setFill(Color.BLACK);
        return textFlow;
    }

    private void setConversation(ConversationMembers conversationMembers) {
        chatVerticalBox.getChildren().clear();
        lastDisplayedUser = null;
        chatScrollPanel.setVvalue(chatScrollPanel.getVmin());
        service.findMessageOfConversation(conversationMembers)
                .forEach(this::addMessageToConversationChat);
        chatTextArea.setDisable(conversationMembers.getUsers().size() == 2 && !service.checkIfIsFriendWithTheLoggedUser(conversationMembers.getUsersWithoutSpecificUser(service.getLoggedUser()).get(0)));
    }

    private void addMessageToConversationChat(Message message) {
        if (!message.getFrom().equals(lastDisplayedUser))
            chatVerticalBox.getChildren().add(convertUserToHBox(message.getFrom()));
        chatVerticalBox.getChildren().add(convertMessageToHBox(message));
        lastDisplayedUser = message.getFrom();
    }

    private HBox convertUserToHBox(User user) {
        HBox hbox = new HBox();
        if (service.getLoggedUser().equals(user))
            hbox.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        else
            hbox.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
        hbox.setPadding(new Insets(5, 5, 0, 10));
        hbox.getChildren().add(convertUserToLabel(user));
        return hbox;
    }

    private Label convertUserToLabel(User user) {
        Label userFullNameLabel = new Label();
        userFullNameLabel.setText(user.getFullName());
        userFullNameLabel.setStyle("-fx-font-weight: bold");
        return userFullNameLabel;
    }

    private HBox convertMessageToHBox(Message messageToConvert) {
        HBox hbox = new HBox();
        if (service.getLoggedUser().equals(messageToConvert.getFrom()))
            hbox.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        else
            hbox.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
        hbox.setPadding(new Insets(5, 5, 5, 10));
        hbox.setSpacing(3);
        hbox.setFillHeight(true);
        hbox.getChildren().add(convertMessageToDateLabel(messageToConvert));
        hbox.getChildren().get(0).setVisible(false);
        hbox.getChildren().add(convertMessageToTextFlow(messageToConvert));
        hbox.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                hbox.getChildren().get(0).setVisible(!hbox.getChildren().get(0).isVisible());
            }
        });
        return hbox;
    }

    private Label convertMessageToDateLabel(Message messageToConvert) {
        Label dateTimeLabel = new Label();
        dateTimeLabel.setText(messageToConvert.getDate().format(Constants.DATE_TIME_FORMATTER));
        dateTimeLabel.managedProperty().bind(dateTimeLabel.visibleProperty());
        dateTimeLabel.setAlignment(Pos.CENTER);
        dateTimeLabel.setMaxHeight(Double.MAX_VALUE);
        return dateTimeLabel;
    }

    private TextFlow convertMessageToTextFlow(Message messageToConvert) {
        Text text = new Text(messageToConvert.getMessage());
        TextFlow textFlow = new TextFlow(text);
        textFlow.setStyle("-fx-color: rgb(239, 242, 255); "
                + "-fx-background-color: rgb(0, 0, 0);"
                + "-fx-background-radius: 20px");
        text.setFill(Color.WHITESMOKE);
        textFlow.setPadding(new Insets(5, 10, 5, 10));
        textFlow.setMaxWidth(chatVerticalBox.getWidth() / 2);
        textFlow.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
        return textFlow;
    }

    public void sendMessage() {
        if (conversationsListView.getSelectionModel().getSelectedItem() == null)
            return;
        if (!chatTextArea.getText().strip().equals("")) {
            service.saveMessageInConversation(conversationsListView.getSelectionModel().getSelectedItem(),
                    chatTextArea.getText().strip());
            setConversation(conversationsListView.getSelectionModel().getSelectedItem());
            chatTextArea.setText("");
        }
    }

    public void addNewConversation() {
        ScreenController.openPage(ApplicationStages.NEW_CONVERSATION);
    }

    @Override
    public void update() {
        loadData();
    }
}
