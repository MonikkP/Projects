package repository.database;

import domain.DatabaseCredentials;
import domain.User;
import domain.factories.UserFactory;
import repository.RepositoryException;
import utils.Constants;
import utils.PreparedStatementFactory;
import utils.SQLQueryLoader;
import java.io.IOException;
import java.sql.*;
import java.util.HashSet;

import java.util.Set;

public class UserDatabaseRepository extends AbstractDatabaseRepository<Integer, User> {

    public UserDatabaseRepository(DatabaseCredentials credentials) {
        super(credentials);
        try {
            sqlQueryLoader = new SQLQueryLoader(Constants.QUERIES_FILE_NAME);
        } catch (IOException e) {
            System.out.println("bau bau");
        }
    }

    public User findOneByEmail(String email) {

        try {
            ensureConnection();
            PreparedStatement statement = PreparedStatementFactory.prepareStatement(connection,
                    sqlQueryLoader.loadProperty("FindOneUserByEmail"), email);
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
    public User findOne(Integer ID) {
        try {
            ensureConnection();
            PreparedStatement statement = PreparedStatementFactory.prepareStatement(connection,
                    sqlQueryLoader.loadProperty("FindOneUserByID"), ID);
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
    public Iterable<User> findAll() {
        try {
            ensureConnection();
            PreparedStatement statement = PreparedStatementFactory.prepareStatement(connection,
                    sqlQueryLoader.loadProperty("FindAllUsers"));
            ResultSet resultSet = statement.executeQuery();
            Set<User> users = new HashSet<>();
            while (resultSet.next()) {
                users.add(UserFactory.createUser(resultSet));
            }
            return users;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RepositoryException();
        }
    }

    @Override
    public User save(User entity) {
        try {
            ensureConnection();
            PreparedStatement statement = PreparedStatementFactory.prepareStatement(connection,
                    sqlQueryLoader.loadProperty("SaveOneUser"), entity.getFirstName(),
                    entity.getLastName(), entity.getDateBirth().toString(), entity.getCountry(),entity.getUserAuthentificationCredentials().getUserName(),
                    entity.getUserAuthentificationCredentials().getPassword(), entity.getUserAuthentificationCredentials().getSalt());
            statement.executeUpdate();
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RepositoryException();
        }
    }

    @Override
    public User delete(User entity) {
        try {
            ensureConnection();
            PreparedStatement statement = PreparedStatementFactory.prepareStatement(connection,
                    sqlQueryLoader.loadProperty("DeleteAllFriendshipsOfUser"), entity.getId(), entity.getId());
            statement.executeUpdate();
            statement = PreparedStatementFactory.prepareStatement(connection,
                    sqlQueryLoader.loadProperty("DeleteAllMessagesSentToUser"), entity.getId());
            statement.executeUpdate();
            statement = PreparedStatementFactory.prepareStatement(connection,
                    sqlQueryLoader.loadProperty("DeleteAllMessagesSentFromUser"), entity.getId(), entity.getId());
            statement.executeUpdate();
            statement = PreparedStatementFactory.prepareStatement(connection,
                    sqlQueryLoader.loadProperty("DeleteOneUser"), entity.getId());
            statement.executeUpdate();
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RepositoryException();
        }
    }

    @Override
    public User update(User entity) {
        return null;
    }

    public void updateCountry(User entity) {
        try {
            ensureConnection();
            PreparedStatement statement = PreparedStatementFactory.prepareStatement(connection,
                    sqlQueryLoader.loadProperty("UpdateCountry"), entity.getCountry(),
                    entity.getId());
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RepositoryException();
        }
    }

    public void updateDate(User entity) {
        try {
            ensureConnection();
            PreparedStatement statement = PreparedStatementFactory.prepareStatement(connection,
                    sqlQueryLoader.loadProperty("UpdateDate"), entity.getDateBirth().toString(),
                    entity.getId());
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RepositoryException();
        }
    }

    public void updateEmail(User entity) {
        try {
            ensureConnection();
            PreparedStatement statement = PreparedStatementFactory.prepareStatement(connection,
                    sqlQueryLoader.loadProperty("UpdateEmail"), entity.getUserAuthentificationCredentials().getUserName(),
                    entity.getId());
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RepositoryException();
        }
    }

    public void updatePassword(User entity) {
        try {
            ensureConnection();
            PreparedStatement statement = PreparedStatementFactory.prepareStatement(connection,
                    sqlQueryLoader.loadProperty("UpdatePassword"), entity.getUserAuthentificationCredentials().getPassword(),
                    entity.getUserAuthentificationCredentials().getSalt(), entity.getId());
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RepositoryException();
        }
    }

    @Override
    public void syncContent() {

    }

}
