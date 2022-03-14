package repository.database;

import domain.DatabaseCredentials;
import domain.Entity;
import repository.Repository;
import utils.SQLQueryLoader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class AbstractDatabaseRepository<ID, E extends Entity<ID>> implements Repository<ID, E> {

    private DatabaseCredentials credentials;
    protected Connection connection;
    protected SQLQueryLoader sqlQueryLoader;


    public AbstractDatabaseRepository(DatabaseCredentials credentials) {
        this.credentials = credentials;
        this.connection = null;
    }

    protected void ensureConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(credentials.getURL(), credentials.getUserName(),
                    credentials.getPassword());
        }
    }
}