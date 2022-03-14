package ui;

import business.Service;
import domain.Observer;
import domain.User;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class NewEventController extends AbstractController implements Initializable, Observer {

    @FXML
    private Button createButton;
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField descriptionTextField;
    @FXML
    private TextField locationTextField;
    @FXML
    private DatePicker dateTime;
    @FXML
    private Slider timeSlider;
    @FXML
    private Label timeLabel;

    public NewEventController(Service service) {
        super(service);
        service.addObserver(this);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        timeSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                int sliderValue = (int)(timeSlider.getValue());
                String hour = String.valueOf(sliderValue/60);
                String minute = String.valueOf(sliderValue%60);
                if (sliderValue / 60 < 10)
                    hour = "0" + hour;
                if (sliderValue % 60 < 10)
                    minute = "0" + minute;
                timeLabel.setText(hour + ":" + minute);
            }
        });

    }

    public void createButtonOnClick() {
        String name = nameTextField.getText();
        String desc = descriptionTextField.getText();
        String location = locationTextField.getText();
        int sliderValue = (int)(timeSlider.getValue());
        LocalDateTime dateTimeEvent = dateTime.getValue().atTime(sliderValue/60,sliderValue%60);
        service.saveEvent(name,desc,location,dateTimeEvent);
        resetPage();
        ScreenController.goBack();
    }

    private void resetPage() {
        nameTextField.clear();
        descriptionTextField.clear();
        locationTextField.clear();
        dateTime.setValue(LocalDate.now());
        timeSlider.setValue(0);
    }
    @Override
    public void update() {
        resetPage();
    }
}
