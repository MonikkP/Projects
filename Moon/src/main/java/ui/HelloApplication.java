package ui;

import business.Service;
import domain.*;
import javafx.application.Application;
import javafx.stage.Stage;
import repository.Repository;
import repository.database.EventDatabaseRepository;
import repository.database.FriendshipDatabaseRepository;
import repository.database.MessageDatabaseRepository;
import repository.database.UserDatabaseRepository;
import utils.Constants;

import java.io.IOException;

public class HelloApplication extends Application {
    public static void main(String[] args) throws IOException {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        UserDatabaseRepository userDatabaseRepository = new UserDatabaseRepository(new DatabaseCredentials(Constants.DATABASE_URL, Constants.DATABASE_USERNAME, Constants.DATABASE_PASSWORD));
        FriendshipDatabaseRepository friendshipDatabaseRepository = new FriendshipDatabaseRepository(new DatabaseCredentials(Constants.DATABASE_URL, Constants.DATABASE_USERNAME, Constants.DATABASE_PASSWORD));
        Repository<Integer, Message> messageDatabaseRepository = new MessageDatabaseRepository(new DatabaseCredentials(Constants.DATABASE_URL, Constants.DATABASE_USERNAME, Constants.DATABASE_PASSWORD));
        EventDatabaseRepository eventRepository = new EventDatabaseRepository(new DatabaseCredentials(Constants.DATABASE_URL, Constants.DATABASE_USERNAME, Constants.DATABASE_PASSWORD));
        Service srv = new Service(userDatabaseRepository, friendshipDatabaseRepository, messageDatabaseRepository,eventRepository);
        ScreenController.setService(srv);
        ScreenController.openPage(ApplicationStages.LOGIN);

    }
}