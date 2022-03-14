package repository.database;

import domain.*;
import domain.factories.MessageFactory;
import domain.factories.UserFactory;
import repository.RepositoryException;
import utils.Constants;
import utils.PreparedStatementFactory;
import utils.SQLQueryLoader;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MessageDatabaseRepository extends AbstractDatabaseRepository<Integer, Message> {

    public MessageDatabaseRepository(DatabaseCredentials credentials) {
        super(credentials);
        try {
            sqlQueryLoader = new SQLQueryLoader(Constants.QUERIES_FILE_NAME);
        } catch (IOException e) {
            System.out.println("bau bau");
        }
    }

    public User findOneUser(Integer userID) {
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

    @Override
    public Message findOne(Integer messageID) {
        try {
            ensureConnection();
            PreparedStatement statement = PreparedStatementFactory.prepareStatement(connection,
                    sqlQueryLoader.loadProperty("FindOneMessage"), messageID);
            ResultSet messageDetails = statement.executeQuery();
            if (!messageDetails.next())
                return null;
            User sender = findOneUser(messageDetails.getInt("id_from_user"));
            statement = PreparedStatementFactory.prepareStatement(connection,
                    sqlQueryLoader.loadProperty("FindReceiversOfMessage"), messageID);
            ResultSet messageReceivers = statement.executeQuery();
            List<User> receivers = new ArrayList<>();
            while (messageReceivers.next())
                receivers.add(findOneUser(messageReceivers.getInt("id_to_user")));
            Message reply = null;
            if (messageDetails.getInt("id_reply") != 0)
                reply = findOne(messageDetails.getInt("id_reply"));
            return MessageFactory.createMessage(messageDetails.getInt("id"), sender, receivers,
                    messageDetails.getString("message"),
                    LocalDateTime.parse(messageDetails.getString("send_date")), reply);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RepositoryException();
        }
    }

    @Override
    public Iterable<Message> findAll() {
        try {
            ensureConnection();
            PreparedStatement statement = PreparedStatementFactory.prepareStatement(connection,
                    sqlQueryLoader.loadProperty("FindAllMessages"));
            ResultSet messageIDs = statement.executeQuery();
            Set<Message> messages = new HashSet<>();
            while (messageIDs.next())
                messages.add(findOne(messageIDs.getInt("id")));
            return messages;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RepositoryException();
        }
    }

    @Override
    public Message save(Message entity) {

        try {
            ensureConnection();
            PreparedStatement statement = PreparedStatementFactory.prepareStatement(connection,
                    sqlQueryLoader.loadProperty("SaveOneMessage"), entity.getMessage(),
                    entity.getDate().toString(), entity.getFrom().getId(),
                    (entity.getReply() == null ? 0 : entity.getReply().getId()));
            statement.executeUpdate();
            for (User user : entity.getTo()) {
                statement = PreparedStatementFactory.prepareStatement(connection,
                        sqlQueryLoader.loadProperty("SaveOneRecipient"), user.getId());
                statement.executeUpdate();
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RepositoryException();
        }
    }

    @Override
    public Message delete(Message entity) {
        try {
            ensureConnection();
            PreparedStatement statement = PreparedStatementFactory.prepareStatement(connection,
                    sqlQueryLoader.loadProperty("DeleteOneMessage"), entity.getId());
            statement.executeUpdate();
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RepositoryException();
        }
    }

    @Override
    public Message update(Message entity) {
        return null;
    }

    @Override
    public void syncContent() {

    }

}
