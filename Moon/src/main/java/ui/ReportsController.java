package ui;

import business.Service;
import domain.Friendship;
import domain.Message;
import domain.Observer;
import domain.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.effect.ImageInput;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import ui.styleImage.CustomStyle;


import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class ReportsController extends AbstractController implements Initializable, Observer {

    @FXML
    private Button button1;
    @FXML
    private Button button2;
    @FXML
    private Label description1Report;
    @FXML
    private Label description2Report;
    @FXML
    private ChoiceBox<String> chooseBoxFriends;
    @FXML
    private ScrollPane mergiterog;
    @FXML
    private DatePicker fromDatePicker;
    @FXML
    private DatePicker toDatePicker;
    @FXML
    private FlowPane friendsFlowPane;
    @FXML
    private Label warningDates;
    @FXML
    private TextField fileName;

    public ReportsController(Service service) {
        super(service);
        service.addObserver(this);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        customButton1();
        customButton2();
        CustomStyle.setStyleImage(goBack);
        goBack.setOnMouseEntered(event -> {
            goBack.getScene().setCursor(Cursor.HAND);
        });
        goBack.setOnMouseExited(event -> {
            goBack.getScene().setCursor(Cursor.DEFAULT);
        });
        initDataCheckBox();
        mergiterog.setVisible(false);
        friendsFlowPane.setVisible(false);
        toDatePicker.setOnMouseClicked(event -> {

            friendsFlowPane.getChildren().clear();
            friendsFlowPane.setVisible(false);
        });

        friendsFlowPane.setMaxWidth(100);
    }

    @Override
    public void goBack() {
        super.goBack();
        reinitialisePage();
    }

    private void initDataCheckBox() {
        chooseBoxFriends.getItems().clear();
        for (User u : service.findAllFriendsOfLoggedUser()) {
            chooseBoxFriends.getItems().add(u.getFullName() + " " + u.getUserAuthentificationCredentials().getUserName());
        }

    }

    private void customButton1() {
        button1.setOnMouseEntered(event -> {
            description1Report.setVisible(true);
        });
        button1.setOnMouseExited(event -> {
            description1Report.setVisible(false);
        });
    }

    private void customButton2() {
        button2.setOnMouseEntered(event -> {
            description2Report.setVisible(true);
        });
        button2.setOnMouseExited(event -> {
            description2Report.setVisible(false);
        });
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

    public void button2OnClick() {
        friendsFlowPane.getChildren().clear();
        friendsFlowPane.setVisible(false);
        fileName.setVisible(false);
        if (isNotValidDate()) {
            return;
        }
        if (chooseBoxFriends.getValue() == null) {
            warningDates.setText("Choose a friend!!");
            warningDates.setVisible(true);
            return;
        } else {
            warningDates.setVisible(false);
        }
        String selectedUser = chooseBoxFriends.getValue().toString();
        String email = selectedUser.substring(selectedUser.lastIndexOf(" ") + 1).trim();
        User from = service.findOneUserByEmail(email);
        List<List<Message>> sentReceivedMessagesFromUser = service.nrOfReceivedMessagesInIntervalFromUser(service.getLoggedUser(), from, fromDatePicker.getValue(), toDatePicker.getValue());
        Integer nrOfMessagesReceived = sentReceivedMessagesFromUser.get(0).size();
        Integer nrOfMessagesSend = sentReceivedMessagesFromUser.get(1).size();
        Label nrReceived = new Label("Number of messages received from " + selectedUser + ": " + nrOfMessagesReceived);
        Label nrSent = new Label("Number of messages sent to " + selectedUser + ": " + nrOfMessagesSend);
        nrReceived.setId("nr-received");
        nrSent.setId("nr-sent");
        friendsFlowPane.getChildren().add(nrReceived);
        friendsFlowPane.getChildren().add(nrSent);
        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                        new PieChart.Data("RECEIVED", nrOfMessagesReceived),
                        new PieChart.Data("SENT", nrOfMessagesSend));
        final PieChart chart = new PieChart(pieChartData);
        chart.setMaxWidth(307);
        chart.setMaxHeight(200);
        Button generatePDF = new Button("Generate PDF \uD83D\uDCC1");
        generatePDF.setOpacity(0.76);
        generatePDF.setAlignment(Pos.CENTER);

        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Save");

        generatePDF.setOnMouseClicked(event -> {
            fileName.setVisible(true);
            if (fileName.getText().isEmpty()) {
                warningDates.setText("Please select a name for your file! (.pdf)");
                warningDates.setVisible(true);
                return;
            } else {
                warningDates.setVisible(false);
            }

            File selectedDirectory = directoryChooser.showDialog((Stage) button1.getScene().getWindow());

            //savepdf
            PDDocument document = new PDDocument();
            PDPage page = new PDPage();
            document.addPage(page);

            PDFont font = PDType1Font.HELVETICA_BOLD;
            PDFont font1 = PDType1Font.TIMES_ROMAN;
            try {

                PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true, true);

                contentStream.beginText();
                contentStream.setFont(font, 20);
                contentStream.moveTextPositionByAmount(100, 650);
                contentStream.drawString("Reports between " + fromDatePicker.getValue() + " and " + toDatePicker.getValue());
                contentStream.endText();
                contentStream.beginText();
                contentStream.setFont(font1, 10);
                contentStream.setLeading(15f);
                contentStream.newLineAtOffset(25, 600);
                contentStream.drawString("Messages received: ");


                for (Message m : sentReceivedMessagesFromUser.get(0)) {
                    contentStream.newLine();
                    String text = m.toStringForPDF();
                    text = text.replace("\n", "");
                    contentStream.drawString(text);
                }
                contentStream.endText();
                contentStream.close();

                PDImageXObject pdImage = PDImageXObject.createFromFile("src/main/resources/images/logoMoon.jpg", document);
                PDPageContentStream contents = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true);
                contents.drawImage(pdImage, 0, 710, pdImage.getWidth() / 5, pdImage.getHeight() / 5);
                contents.close();

                WritableImage image = friendsFlowPane.snapshot(new SnapshotParameters(), null);

                BufferedImage awtImage = SwingFXUtils.fromFXImage(image, null);
                PDImageXObject pdImageXObject = LosslessFactory.createFromImage(document, awtImage);
                PDPageContentStream contentStream2 = new PDPageContentStream(document, document.getPage(0), PDPageContentStream.AppendMode.APPEND, true, true);
                contentStream2.drawImage(pdImageXObject, 50, 50, awtImage.getWidth(), awtImage.getHeight());
                contentStream2.close();


                if (selectedDirectory != null) {
                    document.save(new File(selectedDirectory.getAbsolutePath() + "/" + fileName.getText() + ".pdf"));
                } else {
                    System.out.println("Error saving");
                }

                document.close();
            } catch (IOException e) {
                e.printStackTrace();
            }


        });
        friendsFlowPane.getChildren().add(generatePDF);
        friendsFlowPane.getChildren().add(chart);
        friendsFlowPane.setVisible(true);
        mergiterog.setVisible(true);
    }

    public void button1OnClick() {
        friendsFlowPane.getChildren().clear();
        friendsFlowPane.setVisible(false);
        fileName.setVisible(false);
        if (isNotValidDate()) {
            return;
        }
        List<User> friendsListInInterval = service.findAllFriendsOfUserInInterval(service.getLoggedUser(),
                fromDatePicker.getValue(), toDatePicker.getValue());

        String shownText = "You have created: " + friendsListInInterval.size() + " friend";
        if (friendsListInInterval.size() > 1)
            shownText += "s";
        shownText += "";
        String shownText1 = " from " +
                fromDatePicker.getValue().toString() + " to " + toDatePicker.getValue().toString() + ":";
        String finalString = shownText + shownText1;
        Label newFriendsNumber = new Label(finalString);
        newFriendsNumber.setId("new-friends-number");
        friendsFlowPane.getChildren().add(newFriendsNumber);
        for (User u : friendsListInInterval) {
            friendsFlowPane.getChildren().add(convertUserToTextflow(u));
        }
        Region p = new Region();
        p.setPrefSize(300, 0.0);
        friendsFlowPane.getChildren().add(p);
        Label pending = new Label("Friends who have not accepted yet your friend request: ");
        pending.setId("pending-label");
        friendsFlowPane.getChildren().add(pending);
        List<User> pendingFriends = service.findUsersInPendingSentByLoggedUser(service.getLoggedUser());
        for (User u : pendingFriends) {
            friendsFlowPane.getChildren().add(convertUserToTextflow(u));
        }
        p = new Region();
        p.setPrefSize(300, 0.0);
        friendsFlowPane.getChildren().add(p);
        Label rejected = new Label("Users who rejected your friend request: ");
        rejected.setId("rejected-label");
        friendsFlowPane.getChildren().add(rejected);
        List<User> rejects = service.findUsersWhoRejectedFriendRequestsFrom(service.getLoggedUser());
        for (User u : rejects) {
            friendsFlowPane.getChildren().add(convertUserToTextflow(u));
        }
        p = new Region();
        p.setPrefSize(300, 0.0);
        friendsFlowPane.getChildren().add(p);
        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                        new PieChart.Data("ACCEPTED", friendsListInInterval.size()),
                        new PieChart.Data("PENDING", pendingFriends.size()),
                        new PieChart.Data("REJECTED", rejects.size()));
        final PieChart chart = new PieChart(pieChartData);
        chart.setMaxWidth(307);
        chart.setMaxHeight(200);
        friendsFlowPane.getChildren().add(chart);
        int nrOfReceivedMessages = service.nrOfReceivedMessagesInInterval(service.getLoggedUser(), fromDatePicker.getValue(), toDatePicker.getValue());
        Label nrMess = new Label("Number of mesaages you have recived: " + nrOfReceivedMessages);
        nrMess.setId("nr-mess");
        friendsFlowPane.getChildren().add(nrMess);
        Button generatePDF = new Button("Generate PDF \uD83D\uDCC1");
        generatePDF.setOpacity(0.76);
        generatePDF.setAlignment(Pos.CENTER);

        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Save");

        generatePDF.setOnMouseClicked(event -> {
            fileName.setVisible(true);
            if (fileName.getText().isEmpty()) {
                warningDates.setText("Please select a name for your file! (.pdf)");
                warningDates.setVisible(true);
                return;
            } else {
                warningDates.setVisible(false);
            }
            File selectedDirectory = directoryChooser.showDialog((Stage) button1.getScene().getWindow());

            //savepdf
            PDDocument document = new PDDocument();
            PDPage page = new PDPage();
            document.addPage(page);

            PDFont font = PDType1Font.HELVETICA_BOLD;

            try {

                PDPageContentStream contentStream = new PDPageContentStream(document, page);

                contentStream.beginText();
                contentStream.setFont(font, 20);
                contentStream.moveTextPositionByAmount(100, 650);
                contentStream.drawString("Reports between " + fromDatePicker.getValue() + " and " + toDatePicker.getValue());
                contentStream.endText();
                contentStream.close();

                PDImageXObject pdImage = PDImageXObject.createFromFile("src/main/resources/images/logoMoon.jpg", document);
                PDPageContentStream contents = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true);
                contents.drawImage(pdImage, 0, 710, pdImage.getWidth() / 5, pdImage.getHeight() / 5);
                contents.close();

                WritableImage image = friendsFlowPane.snapshot(new SnapshotParameters(), null);
                BufferedImage awtImage = SwingFXUtils.fromFXImage(image, null);
                PDImageXObject pdImageXObject = LosslessFactory.createFromImage(document, awtImage);
                PDPageContentStream contentStream2 = new PDPageContentStream(document, document.getPage(0), PDPageContentStream.AppendMode.APPEND, true, true);
                contentStream2.drawImage(pdImageXObject, 50, 50, awtImage.getWidth(), awtImage.getHeight());
                contentStream2.close();

                if (selectedDirectory != null) {
                    document.save(new File(selectedDirectory.getAbsolutePath() + "/" + fileName.getText() + ".pdf"));
                } else {
                    System.out.println("Error saving");
                }

                document.close();
            } catch (IOException e) {
                e.printStackTrace();
            }


        });

        friendsFlowPane.getChildren().add(generatePDF);
        friendsFlowPane.setVisible(true);
        mergiterog.setVisible(true);

    }


    private boolean isNotValidDate() {
        if (fromDatePicker.getValue() == null || toDatePicker.getValue() == null) {
            warningDates.setText("Please pick a date!");
            warningDates.setVisible(true);
            return true;
        } else {
            warningDates.setVisible(false);
            return false;
        }
    }

    private void reinitialisePage() {
        fromDatePicker.setValue(null);
        toDatePicker.setValue(null);
        initDataCheckBox();
        friendsFlowPane.getChildren().clear();
        friendsFlowPane.setVisible(false);
        fileName.setVisible(false);
    }

    @Override
    public void update() {
        reinitialisePage();
    }
}
