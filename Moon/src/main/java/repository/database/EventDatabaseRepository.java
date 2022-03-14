package repository.database;

import domain.DatabaseCredentials;
import domain.Event;
import domain.NotificationSubscription;
import domain.User;
import domain.factories.EventFactory;
import domain.factories.UserFactory;
import repository.RepositoryException;
import utils.Constants;
import utils.PreparedStatementFactory;
import utils.SQLQueryLoader;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EventDatabaseRepository extends AbstractDatabaseRepository<Integer, Event>{

    public EventDatabaseRepository(DatabaseCredentials credentials) {
        super(credentials);
        try {
            sqlQueryLoader = new SQLQueryLoader(Constants.QUERIES_FILE_NAME);
        } catch (IOException e) {
            System.out.println("bau bau");
        }
    }

    private User findOneUser(Integer userID) {
        try {
            ensureConnection();
            PreparedStatement statement = PreparedStatementFactory.prepareStatement(connection,
                    sqlQueryLoader.loadProperty("FindOneUserByID"), userID);
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next())
                return null;
            return UserFactory.createUser(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RepositoryException();
        }
    }

    public List<User> findEventParticipants(Integer eventID) {
        try {
            ensureConnection();
            PreparedStatement statement = PreparedStatementFactory.prepareStatement(connection,
                    sqlQueryLoader.loadProperty("FindAllParticipantsToEvent"), eventID);
            ResultSet resultSet = statement.executeQuery();
            List<User> participants = new ArrayList<>();
            while (resultSet.next()) {
                participants.add(findOneUser(resultSet.getInt("id_user")));
            }
            return participants;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    @Override
    public Event findOne(Integer eventID) {
        try {
            ensureConnection();
            PreparedStatement statement = PreparedStatementFactory.prepareStatement(connection,
                    sqlQueryLoader.loadProperty("FindOneEvent"), eventID);
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next())
                return null;
            return EventFactory.createEvent(resultSet, findOneUser(resultSet.getInt("id_owner")),
                    findEventParticipants(eventID));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    @Override
    public Iterable<Event> findAll() {
        try {
            ensureConnection();
            PreparedStatement statement = PreparedStatementFactory.prepareStatement(connection,
                    sqlQueryLoader.loadProperty("FindAllEvents"));
            ResultSet resultSet = statement.executeQuery();
            List<Event> events = new ArrayList<>();
            while (resultSet.next()) {
                events.add(findOne(resultSet.getInt("id")));
            }
            return events;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    @Override
    public Event save(Event entity) {
        try {
            ensureConnection();
            PreparedStatement statement = PreparedStatementFactory.prepareStatement(connection,
                    sqlQueryLoader.loadProperty("SaveOneEvent"), entity.getOwner().getId(), entity.getName(),
                    entity.getDescription(), entity.getLocation(), entity.getDateTime().toString());
            statement.executeUpdate();
            return null;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    @Override
    public Event delete(Event entity) {
        try {
            ensureConnection();
            PreparedStatement statement = PreparedStatementFactory.prepareStatement(connection,
                    sqlQueryLoader.loadProperty("DeleteOneEvent"), entity.getId(), entity.getId());
            statement.executeUpdate();
            return null;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    @Override
    public Event update(Event entity) {
        return null;
    }

    @Override
    public void syncContent() {

    }

    public void saveParticipation(User user, Event event) {
        try {
            ensureConnection();
            PreparedStatement statement = PreparedStatementFactory.prepareStatement(connection,
                    sqlQueryLoader.loadProperty("SaveOneParticipation"), user.getId(), event.getId());
            statement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void deleteParticipation(User user, Event event) {
        try {
            ensureConnection();
            PreparedStatement statement = PreparedStatementFactory.prepareStatement(connection,
                    sqlQueryLoader.loadProperty("DeleteOneParticipation"), user.getId(), event.getId());
            statement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public NotificationSubscription findStatusSubscription(User user, Event event) {
        try {
            ensureConnection();
            PreparedStatement statement = PreparedStatementFactory.prepareStatement(connection,
                    sqlQueryLoader.loadProperty("FindOneSubscriptionStatus"), user.getId(), event.getId());
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next())
                return null;
            return NotificationSubscription.valueOf(resultSet.getString("status_subscription"));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    public void updateSubscription(User user, Event event, NotificationSubscription subscriptionStatus) {
        try {
            ensureConnection();
            PreparedStatement statement = PreparedStatementFactory.prepareStatement(connection,
                    sqlQueryLoader.loadProperty("UpdateSubscriptionStatus"), subscriptionStatus.toString(),
                    user.getId(), event.getId());
            statement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
