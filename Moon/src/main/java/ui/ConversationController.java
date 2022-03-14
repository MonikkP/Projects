package ui;

import business.Service;
import domain.Observer;
import domain.User;
import domain.factories.ConversationMembersFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ConversationController extends AbstractController implements Initializable, Observer {

    @FXML
    private TextField searchBarTextField;

    @FXML
    private ListView<User> searchResultsListView;

    @FXML
    private FlowPane receiversFlowPane;

    @FXML
    private TextArea messageTextArea;

    private ObservableList<User> userModel = FXCollections.observableArrayList();

    private List<User> selectedUsers = new ArrayList<>();

    public ConversationController(Service service) {
        super(service);
        service.addObserver(this);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        hideUserSearchResults();
        searchResultsListView.toFront();
        searchResultsListView.setCellFactory(new Callback<ListView<User>, ListCell<User>>() {
            @Override
            public ListCell<User> call(ListView<User> param) {
                ListCell<User> cell = new ListCell<User>() {
                    @Override
                    public void updateItem(User item, boolean empty) {
                        super.updateItem(item, empty);
                        setStyle("-fx-background-color: rgb(0,0,0,0);");
                        if (empty) {
                            setText(null);
                        } else {
                            setText(item.getFullName());
                            //setGraphic(this);
                        }
                    }
                };
                return cell;
            }
        });
        handleFilter();
        searchResultsListView.setItems(userModel);
        searchBarTextField.textProperty().addListener(o -> handleFilter());
        receiversFlowPane.setVgap(2);
        receiversFlowPane.setHgap(2);

    }

    private void handleFilter() {
        Predicate<User> p1 = n -> n.getFirstName().toUpperCase().startsWith(searchBarTextField.getText().toUpperCase(Locale.ROOT));
        userModel.setAll(service.findAllFriendsOfLoggedUser()
                .stream()
                .filter(p1)
                .collect(Collectors.toList()));
    }

    public void showUserSearchResults() {
        searchResultsListView.setVisible(true);
    }

    public void hideUserSearchResults() {
        searchResultsListView.setVisible(false);
    }

    public void searchedUserSelected() {
        User currentlySelectedUser = searchResultsListView.getSelectionModel().getSelectedItem();
        if (currentlySelectedUser != null && !selectedUsers.contains(currentlySelectedUser)) {
            TextFlow userTextFlow = convertUserToTextflow(currentlySelectedUser);
            receiversFlowPane.getChildren().add(userTextFlow);
            selectedUsers.add(currentlySelectedUser);
        }
        hideUserSearchResults();
    }

    private TextFlow convertUserToTextflow(User toConvert) {
        Text text = new Text(toConvert.getFullName());
        TextFlow textFlow = new TextFlow(text);
        textFlow.setStyle("-fx-color: rgb(239, 242, 255); "
                + "-fx-background-color: rgb(199, 199, 204);"
                + "-fx-background-radius: 20px");
        textFlow.setPadding(new Insets(5, 10, 5, 10));
        text.setFill(Color.BLACK);
        textFlow.setOnMouseClicked(e -> {
            receiversFlowPane.getChildren().remove(textFlow);
            selectedUsers.remove(toConvert);
        });
        return textFlow;
    }

    public void sendMessage() {
        if (messageTextArea.getText().strip().equals("")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("You cannot send an empty message!");
            alert.show();
            return;
        }
        if (selectedUsers.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please choose message recipients!");
            alert.show();
            return;
        }
        selectedUsers.add(service.getLoggedUser());
        service.saveMessageInPossibleNewConversation(ConversationMembersFactory.createConversation(selectedUsers),
                messageTextArea.getText());
        goBack();
    }

    @Override
    public void update() {
        receiversFlowPane.getChildren().clear();
        selectedUsers.clear();
        messageTextArea.clear();
    }
}
