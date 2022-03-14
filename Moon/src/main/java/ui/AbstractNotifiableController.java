package ui;

import business.Service;
import domain.Notification;
import domain.NotificationsObserver;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;
import javafx.scene.text.Text;
import javafx.util.Callback;

import java.net.URL;
import java.util.ResourceBundle;

public abstract class AbstractNotifiableController extends AbstractController implements NotificationsObserver, Initializable {

    @FXML
    public ImageView notificationsImageView;
    @FXML
    public ListView<Notification> notificationsListView;

    private ObservableList<Notification> model = FXCollections.observableArrayList();

    public AbstractNotifiableController(Service service) {
        super(service);
        service.addNotificationObserver(this);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        notificationsListView.setCellFactory(new Callback<ListView<Notification>, ListCell<Notification>>() {
            @Override
            public ListCell<Notification> call(ListView<Notification> param) {
                ListCell<Notification> cell = new ListCell<>() {
                    @Override
                    public void updateItem(Notification item, boolean empty) {
                        super.updateItem(item, empty);
                        if (!empty) {
                            Text label = new Text(item.getNotification());
                            label.setWrappingWidth(notificationsListView.getWidth() - 15);
                            label.setId("notificationText");
                            setGraphic(label);
                            setId("notificationViewTextCell");
                        } else {
                            setBorder(Border.EMPTY);
                        }
                    }
                };
                return cell;
            }
        });
        notificationsListView.setItems(model);
        notificationsListView.setVisible(false);
        notificationsListView.toBack();
    }

    public void showNotifications() {
        if (notificationsListView.isVisible()) {
            notificationsListView.setVisible(false);
            notificationsListView.toBack();
        } else {
            notificationsImageView.setImage(new Image("/images/bell-static.png"));
            notificationsListView.setVisible(true);
            notificationsListView.toFront();
            service.notificationsHaveBeenRead();
        }
    }

    @Override
    public void updateNotifications(Notification notification) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                model.add(notification);
                if (!notificationsListView.isVisible())
                    notificationsImageView.setImage(new Image("/images/bell-dynamic.gif"));
                else
                    service.notificationsHaveBeenRead();
            }
        });
    }

    @Override
    public void updateReadStatus() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                notificationsImageView.setImage(new Image("/images/bell-static.png"));
            }
        });
    }
}
