package ui;

import business.Service;
import business.ServiceException;
import domain.Observer;
import domain.Tuple;
import domain.User;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.util.Callback;
import ui.styleImage.CustomStyle;
import utils.Constants;

import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class MainViewController extends AbstractNotifiableController implements Initializable, Observer {

    ObservableList<User> modelUser = FXCollections.observableArrayList();
    @FXML
    TableColumn<User, String> tableColumnFirstName;
    @FXML
    TableColumn<User, String> tableColumnLastName;
    @FXML
    TableColumn<User, String> tableColumnAction;
    @FXML
    TableView<User> tableViewUsers;
    @FXML
    private ImageView backImageView;
    @FXML
    private TextField searchBar;
    @FXML
    private ImageView acceptRejectPane;
    @FXML
    private Button acceptButton;
    @FXML
    private Button rejectButton;
    @FXML
    private Label labelAcceptReject;
    @FXML
    private ImageView topBar;
    @FXML
    private ImageView leftBar;
    @FXML
    private ImageView newMessageImage;
    @FXML
    private ImageView eventImageView;
    @FXML
    private ImageView reportsImageView;

    public MainViewController(Service service) {
        super(service);
        service.addObserver(this);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        tableViewUsers.setVisible(false);
        setVisibilityOnAcceptRejectPane(false);
        notificationsListView.setVisible(false);
        CustomStyle.setStyleExitButton(exitButton);
        CustomStyle.setStyleImage(profileImageButton);
        CustomStyle.setStyleImage(moonLogo);
        CustomStyle.setStyleImage(newMessageImage);
        CustomStyle.setStyleImage(eventImageView);
        CustomStyle.setStyleImage(reportsImageView);
        tableColumnFirstName.setResizable(false);
        tableColumnLastName.setResizable(false);
        tableColumnAction.setResizable(false);
        moonLogo.setPickOnBounds(true);
        profilePageOnAction();
        messageImageOnAction();
        loadData();
        service.scheduleNotificationsForAllEvents();
        tableViewUsers.setFixedCellSize(30);
        tableViewUsers.prefHeightProperty().bind(Bindings.size(tableViewUsers.getItems()).multiply(tableViewUsers.getFixedCellSize()));
    }

    private void loadData() {
        tableColumnFirstName.setCellValueFactory(new PropertyValueFactory<User, String>("firstName"));
        tableColumnLastName.setCellValueFactory(new PropertyValueFactory<User, String>("lastName"));

        tableViewUsers.setEditable(true);

        Callback<TableColumn<User, String>, TableCell<User, String>> cellFactory = (param) -> {
            final TableCell<User, String> cell = new TableCell<User, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {

                        final Button processFriendRequest = new Button(Constants.MOON_EMOJI);

                        processFriendRequest.setOnMouseEntered(event -> {
                            User clickedUser = getTableView().getItems().get(getIndex());
                            if (service.checkIfIsFriendWithTheLoggedUser(clickedUser)) {
                                processFriendRequest.setText(Constants.REMOVE_FRIEND_EMOJI);
                            } else {
                                processFriendRequest.setText(Constants.SEND_FRIEND_REQUEST_ROCKET_EMOJI);
                            }
                            if (service.checkIfIsTheLoggedUser(clickedUser)) {
                                processFriendRequest.setText(Constants.PROFILE_EMOJI);
                            }
                            if (service.isFriendshipPending(service.getLoggedUser().getId(), clickedUser.getId())) {
                                if (service.isRequestSentByLoggedUserTo(clickedUser.getId())) {
                                    processFriendRequest.setText(Constants.SENT_REQUEST_EMOJI);
                                } else {
                                    processFriendRequest.setText(Constants.PENDING_HOURGLASS_EMOJI);
                                }
                            }
                        });

                        processFriendRequest.setOnMouseClicked(event -> {
                            User clickedUser = getTableView().getItems().get(getIndex());
                            if (service.checkIfIsFriendWithTheLoggedUser(clickedUser)) {
                                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                alert.setContentText("Are you sure you want to delete " + clickedUser.getFullName() + " from your list of friends?");
                                alert.showAndWait().ifPresent(response -> {
                                    if (!response.equals(ButtonType.CANCEL)) {
                                        //delete friendships
                                        service.deleteFriendship(service.findOneFriendship(new Tuple<>(clickedUser.getId(), service.getLoggedUser().getId())));
                                        processFriendRequest.setText(Constants.MOON_EMOJI);
                                    }
                                });
                                return;
                            }
                            if (service.isFriendshipPending(service.getLoggedUser().getId(), clickedUser.getId())) {
                                if (service.isRequestSentByLoggedUserTo(clickedUser.getId())) {
                                    service.deleteFriendship(service.findOneFriendship(new Tuple<>(clickedUser.getId(), service.getLoggedUser().getId())));
                                    processFriendRequest.setText(Constants.SEND_FRIEND_REQUEST_ROCKET_EMOJI);
                                    return;

                                }
                                setVisibilityOnAcceptRejectPane(true);
                                CustomStyle.setStyleImageButtons(acceptRejectPane);
                                //accept or reject
                                acceptButton.setOnAction(event1 -> {
                                    service.acceptFriendRequest(clickedUser.getId(), service.getLoggedUser().getId());
                                    processFriendRequest.setText(Constants.REMOVE_FRIEND_EMOJI);
                                    setVisibilityOnAcceptRejectPane(false);

                                });
                                rejectButton.setOnAction(event1 -> {
                                    service.rejectFriendRequest(clickedUser.getId(), service.getLoggedUser().getId());
                                    processFriendRequest.setText(Constants.SEND_FRIEND_REQUEST_ROCKET_EMOJI);
                                    setVisibilityOnAcceptRejectPane(false);

                                });

                            } else {
                                if (service.checkIfIsTheLoggedUser(clickedUser)) {
                                    ScreenController.openPage(ApplicationStages.PROFILE);
                                } else {
                                    try {
                                        service.saveFriendshipRequest(service.getLoggedUser().getId(), clickedUser.getId());
                                        processFriendRequest.setText(Constants.SENT_REQUEST_EMOJI);
                                    } catch (ServiceException e) {
                                        Alert alert = new Alert(Alert.AlertType.WARNING);
                                        alert.setContentText("You can not send friend request to " + clickedUser.getFullName() + " because the user has previously rejected your friend request!");
                                        alert.show();
                                    }
                                }
                            }

                        });
                        setGraphic(processFriendRequest);
                        setText(null);

                    }
                }
            };

            return cell;
        };

        tableColumnAction.setCellFactory(cellFactory);

        modelUser.setAll(service.findListAllUsers());

        tableViewUsers.setItems(modelUser);
        searchBar.textProperty().addListener(o -> handleFilter());
        tableViewUsers.setPlaceholder(new Label("\uD83E\uDD15❓"));
    }

    public void setVisibilityOnAcceptRejectPane(boolean boo) {
        if (boo) {
            acceptRejectPane.setVisible(true);
            acceptButton.setVisible(true);
            rejectButton.setVisible(true);
            labelAcceptReject.setVisible(true);
        } else {
            acceptRejectPane.setVisible(false);
            acceptButton.setVisible(false);
            rejectButton.setVisible(false);
            labelAcceptReject.setVisible(false);
        }

    }
    public void messageImageOnAction() {
        newMessageImage.setOnMouseClicked(event -> {
            ScreenController.openPage(ApplicationStages.MESSENGER);
        });
    }

    public void setSearchBarOnAction() {
        tableViewUsers.setVisible(true);

    }

    public void imageOnAction() {
        tableViewUsers.setVisible(false);

    }

    private void handleFilter() {
        Predicate<User> p1 = n -> n.getFirstName().toUpperCase().startsWith(searchBar.getText().toUpperCase(Locale.ROOT));
        modelUser.setAll(service.findListAllUsers()
                .stream()
                .filter(p1)
                .collect(Collectors.toList()));
    }

    public void eventOnAction() {
        ScreenController.openPage(ApplicationStages.EVENT);
    }

    public void reportsOnAction() {
        ScreenController.openPage(ApplicationStages.REPORTS);
    }

    @Override
    public void logoOnAction() {
        super.logoOnAction();
        tableViewUsers.setVisible(false);
        searchBar.clear();
        backImageView.requestFocus();
    }

    @Override
    public void update() {
        notificationsListView.setVisible(false);
        notificationsListView.toBack();
    }
}
