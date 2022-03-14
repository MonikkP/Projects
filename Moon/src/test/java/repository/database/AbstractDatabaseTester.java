package repository.database;

import domain.DatabaseCredentials;
import org.junit.jupiter.api.BeforeAll;
import repository.Repository;
import utils.Constants;
import utils.SQLQueryLoader;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class AbstractDatabaseTester {

    protected final DatabaseCredentials credentials = new DatabaseCredentials(Constants.DATABASE_URL_TEST, Constants.DATABASE_USERNAME,
            Constants.DATABASE_PASSWORD);

    protected Repository repository = null;

    protected TestDataSet dataSet = new TestDataSet();

    @BeforeAll
    public static void prepareDatabase() {
        try (Connection connection = DriverManager.getConnection(Constants.DATABASE_URL_TEST, Constants.DATABASE_USERNAME, Constants.DATABASE_PASSWORD)) {
            SQLQueryLoader loader = new SQLQueryLoader(Constants.TEST_QUERIES_FILE_NAME);
            PreparedStatement query = connection.prepareStatement(loader.loadProperty("PrepareDatabase"));
            query.executeUpdate();
        } catch (SQLException | IOException throwables) {
            throwables.printStackTrace();
        }
    }
}
