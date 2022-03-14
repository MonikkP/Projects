package utils;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface PreparedStatementSetInterface {

    public void set(PreparedStatement statement, int argumentIndex, Object argument) throws SQLException;
}
