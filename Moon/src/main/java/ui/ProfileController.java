package ui;

import business.Service;
import business.ServiceException;
import domain.FriendshipDTO;
import domain.Observer;
import domain.Tuple;
import domain.User;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;
import ui.styleImage.CustomStyle;
import utils.Constants;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class ProfileController extends AbstractController implements Initializable, Observer {

    public ProfileController(Service service) {
        super(service);
        service.addObserver(this);
    }


    @FXML
    private ImageView imageViewGoBack;

    @FXML
    private Label labelUnlockTabel;

    @FXML
    private Label labelCountFriends;

    @FXML
    private Pagination pagination;

    @FXML
    private TableColumn<FriendshipDTO, String> tableColumnFullName;

    @FXML
    private TableColumn<FriendshipDTO, String> tableColumnDate;

    @FXML
    private TableColumn<FriendshipDTO, String> tableColumnStatus;

    @FXML
    private TableView<FriendshipDTO> tableViewFriendships;

    @FXML
    TableColumn<FriendshipDTO, String> tableColumnAction;

    @FXML
    private ImageView acceptRejectPane;
    @FXML
    private Button acceptButton;
    @FXML
    private Button rejectButton;
    @FXML
    private Label labelAcceptReject;

    @FXML
    private Label fullName;

    @FXML
    private TextField userCountry;
    @FXML
    private DatePicker userDate;
    @FXML
    private TextField userEmail;
    @FXML
    private ImageView backInfoImage;
    @FXML
    private Label editPassword;

    private final int ROWS_PER_PAGE = 5;


    private ObservableList<FriendshipDTO> model = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tableViewFriendships.setVisible(false);
        setVisibilityOnAcceptRejectPane(false);
        CustomStyle.setStyleImageButtons(backInfoImage);

        model.addListener(new ListChangeListener<FriendshipDTO>() {
            @Override
            public void onChanged(Change<? extends FriendshipDTO> c) {
                long countOfFriends = service.findCountOfFriendsOfLoggedUser();
                String shownText = "You have " + service.findCountOfFriendsOfLoggedUser() + " friend";
                if (countOfFriends > 1)
                    shownText += "s";
                shownText += ".";
                labelCountFriends.setText(shownText);

            }
        });
        pagination.setPageFactory(this::createPage);
        setData();
        tableViewFriendships.setPlaceholder(new Label("\uD83E\uDD15❓"));

    }
    private void reloadTable() {
        pagination.setMaxPageIndicatorCount(service.findNumberOfPages(ROWS_PER_PAGE));
        model.setAll(service.findFriendsAndRequestsOfLoggedUserPaginated(pagination.getCurrentPageIndex(), ROWS_PER_PAGE));
        tableViewFriendships.setItems(model);
    }

    public void setData() {
        tableColumnFullName.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        tableColumnDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        tableColumnStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        tableViewFriendships.setEditable(true);
        //duplicated code consider refactoring
        Callback<TableColumn<FriendshipDTO, String>, TableCell<FriendshipDTO, String>> cellFactory = (param) -> {
            final TableCell<FriendshipDTO, String> cell = new TableCell<FriendshipDTO,String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {

                        final Button processFriendRequest = new Button(Constants.MOON_EMOJI);

                        processFriendRequest.setOnMouseEntered(event -> {
                            User clickedUser = getTableView().getItems().get(getIndex()).getUser();
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
                            User clickedUser = getTableView().getItems().get(getIndex()).getUser();
                            if (service.checkIfIsFriendWithTheLoggedUser(clickedUser)) {
                                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                alert.setContentText("Are you sure you want to delete " + clickedUser.getFullName() + " from your list of friends?");
                                alert.showAndWait().ifPresent(response -> {
                                    if (!response.equals(ButtonType.CANCEL)) {
                                        //delete friendships
                                        service.deleteFriendship(service.findOneFriendship(new Tuple<>(clickedUser.getId(), service.getLoggedUser().getId())));
                                        processFriendRequest.setText(Constants.MOON_EMOJI);
                                        reloadTable();
                                    }
                                });
                                return;
                            }
                            if (service.isFriendshipPending(service.getLoggedUser().getId(), clickedUser.getId())) {
                                if (service.isRequestSentByLoggedUserTo(clickedUser.getId())) {
                                    service.deleteFriendship(service.findOneFriendship(new Tuple<>(clickedUser.getId(), service.getLoggedUser().getId())));
                                    processFriendRequest.setText(Constants.SEND_FRIEND_REQUEST_ROCKET_EMOJI);
                                    reloadTable();
                                    return;

                                }
                                setVisibilityOnAcceptRejectPane(true);
                                CustomStyle.setStyleImageButtons(acceptRejectPane);
                                //accept or reject
                                acceptButton.setOnAction(event1 -> {
                                    service.acceptFriendRequest(clickedUser.getId(), service.getLoggedUser().getId());
                                    processFriendRequest.setText(Constants.REMOVE_FRIEND_EMOJI);
                                    setVisibilityOnAcceptRejectPane(false);
                                    reloadTable();

                                });
                                rejectButton.setOnAction(event1 -> {
                                    service.rejectFriendRequest(clickedUser.getId(), service.getLoggedUser().getId());
                                    processFriendRequest.setText(Constants.SEND_FRIEND_REQUEST_ROCKET_EMOJI);
                                    setVisibilityOnAcceptRejectPane(false);
                                    reloadTable();

                                });

                            } else {
                                    try {
                                        service.saveFriendshipRequest(service.getLoggedUser().getId(), clickedUser.getId());
                                        processFriendRequest.setText(Constants.SENT_REQUEST_EMOJI);
                                        reloadTable();
                                    } catch (ServiceException e) {
                                        Alert alert = new Alert(Alert.AlertType.WARNING);
                                        alert.setContentText("You can not send friend request to " + clickedUser.getFullName() + " because the user has previously rejected your friend request!");
                                        alert.show();
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
        loadData();
        tableViewFriendships.setItems(model);

    }

    private void loadData() {
        fullName.setText(service.getLoggedUser().getFullName());
        userCountry.setText(service.getLoggedUser().getCountry());
        userDate.setValue(service.getLoggedUser().getDateBirth());
        userEmail.setText(service.getLoggedUser().getUserAuthentificationCredentials().getUserName());
        reloadTable();
    }

    private Node createPage(int pageIndex) {
        reloadTable();
        return new BorderPane(tableViewFriendships);
    }

    //duplicated code

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
    ///////

    public void setEditButtonCountryOnAction(ActionEvent event) {
        String country = userCountry.getText();
        service.updateCountry(service.getLoggedUser().getId(),country);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("You successfully updated your country in " + country);
        alert.show();
    }
    public void setEditButtonDateOnAction(ActionEvent event) {
        LocalDate date = userDate.getValue();
        service.updateDate(service.getLoggedUser().getId(),date);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("You successfully updated your date birth in " + date.toString());
        alert.show();
    }
    public void setEditButtonEmailOnAction(ActionEvent event) {
        String email = userEmail.getText();
        if(!service.isValidMail(userEmail.getText())) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("You already have an account on " + email);
            alert.show();
        } else {
            service.updateEmail(service.getLoggedUser().getId(), email);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("You successfully updated your email in " + email);
            alert.show();
        }
    }
    public void hoverOnItem() {
        tableViewFriendships.getScene().setCursor(Cursor.HAND);
    }

    public void hoverOnItemIsOver() {
        tableViewFriendships.getScene().setCursor(Cursor.DEFAULT);
    }

    public void hoverOnSeeDetails() {
        hoverOnItem();
        labelUnlockTabel.setUnderline(true);
    }

    public void hoverOnEditPassword() {
        hoverOnItem();
        editPassword.setUnderline(true);
    }

    public void hoverOnSeeDetailsOver() {
        hoverOnItemIsOver();
        labelUnlockTabel.setUnderline(false);
    }

    public void hoverOnEditPasswordOver() {
        hoverOnItemIsOver();
        editPassword.setUnderline(false);
    }

    public void clickOnSeeDetails() {
        tableViewFriendships.setVisible(!tableViewFriendships.isVisible());
        pagination.setVisible(!pagination.isVisible());
    }

    public void editPasswordOnAction() {
        ScreenController.openPage(ApplicationStages.PASSWORD_CHANGE);
    }

    private void hideFriendsTable() {
        tableViewFriendships.setVisible(false);
        pagination.setVisible(false);
    }

    @Override
    public void update() {
        hideFriendsTable();
        loadData();
    }
}
