package utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public class PreparedStatementFactory {

    private static Map<Class<?>, PreparedStatementSetInterface> setters = new HashMap<>();
    static {
        setters.put(Integer.class, (ps, index, arg) -> ps.setInt(index, (Integer) arg));
        setters.put(Long.class, (ps, index, arg) -> ps.setLong(index, (Long) arg));
        setters.put(String.class, (ps, index, arg) -> ps.setString(index, (String) arg));
    }

    private PreparedStatementFactory() {}

    public static PreparedStatement prepareStatement(Connection connection, String SQLStatement, Object... arguments)
            throws SQLException {
        PreparedStatement statement = connection.prepareStatement(SQLStatement);
        int argumentIndex = 1;
        for (Object argument : arguments) {
            setArgument(statement, argumentIndex, argument);
            argumentIndex++;
        }
        return statement;
    }

    private static void setArgument(PreparedStatement statement, int argumentIndex, Object argument) throws SQLException {
        setters.get(argument.getClass()).set(statement, argumentIndex, argument);
    }
}
