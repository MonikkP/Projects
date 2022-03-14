package repository.database;

import domain.*;
import domain.factories.FriendshipFactory;
import repository.RepositoryException;
import utils.Constants;
import utils.PreparedStatementFactory;
import utils.SQLQueryLoader;

import javax.xml.transform.Result;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FriendshipDatabaseRepository extends AbstractDatabaseRepository<Tuple<Integer>, Friendship> {

    public FriendshipDatabaseRepository(DatabaseCredentials credentials) {
        super(credentials);
        try {
            sqlQueryLoader = new SQLQueryLoader(Constants.QUERIES_FILE_NAME);
        } catch (IOException e) {
            System.out.println("bau bau");
        }
    }

    @Override
    public Friendship findOne(Tuple<Integer> FriendshipID) {
        try {
            ensureConnection();
            PreparedStatement statement = PreparedStatementFactory.prepareStatement(connection,
                    sqlQueryLoader.loadProperty("FindOneFriendship"), FriendshipID.getId1(),
                    FriendshipID.getId2(), FriendshipID.getId2(), FriendshipID.getId1());
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next())
                return null;
            return FriendshipFactory.createFriendship(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RepositoryException();
        }
    }

    @Override
    public Iterable<Friendship> findAll() {
        try {
            ensureConnection();
            PreparedStatement statement = PreparedStatementFactory.prepareStatement(connection,
                    sqlQueryLoader.loadProperty("FindAllFriendships"));
            ResultSet resultSet = statement.executeQuery();
            Set<Friendship> friendships = new HashSet<>();
            while (resultSet.next())
                friendships.add(FriendshipFactory.createFriendship(resultSet));
            return friendships;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RepositoryException();
        }
    }

    @Override
    public Friendship save(Friendship entity) {
        try {
            ensureConnection();
            PreparedStatement statement = PreparedStatementFactory.prepareStatement(connection,
                    sqlQueryLoader.loadProperty("SaveOneFriendship"), entity.getId().getId1(),
                    entity.getId().getId2(), entity.getDateOfFriendship().toString(), entity.getStatus().toString());
            statement.executeUpdate();
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RepositoryException();
        }
    }

    @Override
    public Friendship delete(Friendship entity) {
        try {
            ensureConnection();
            PreparedStatement statement = PreparedStatementFactory.prepareStatement(connection,
                    sqlQueryLoader.loadProperty("DeleteOneFriendship"), entity.getId().getId1(),
                    entity.getId().getId2(), entity.getId().getId2(), entity.getId().getId1());
            statement.executeUpdate();
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RepositoryException();
        }
    }

    @Override
    public Friendship update(Friendship entity) {
        try {
            ensureConnection();
            PreparedStatement statement = PreparedStatementFactory.prepareStatement(connection,
                    sqlQueryLoader.loadProperty("UpdateFriendship"), entity.getStatus().toString(),
                    entity.getId().getId1(), entity.getId().getId2(), entity.getId().getId2(), entity.getId().getId1());
            statement.executeUpdate();
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RepositoryException();
        }
    }

    @Override
    public void syncContent() {

    }

    public List<Friendship> findFriendshipsOfUserPaginated(User user, int page, int rowsPerPage) {
        try {
            ensureConnection();
            PreparedStatement statement = PreparedStatementFactory.prepareStatement(connection,
                    sqlQueryLoader.loadProperty("FindAllFriendshipsPaginated"), user.getId(),
                    user.getId(), rowsPerPage, (page) * rowsPerPage);
            ResultSet resultSet = statement.executeQuery();
            List<Friendship> friendshipList = new ArrayList<>();
            while (resultSet.next()) {
                friendshipList.add(FriendshipFactory.createFriendship(resultSet));
            }
            return friendshipList;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RepositoryException();
        }
    }

    public int findNumberOfFriendsAndRequestsOfUser(User user) {
        try {
            ensureConnection();
            PreparedStatement statement = PreparedStatementFactory.prepareStatement(connection,
                    sqlQueryLoader.loadProperty("FindCountOfAllFriendships"), user.getId(), user.getId());
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next())
                return 0;
            int a = resultSet.getInt("count");
            return a;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
