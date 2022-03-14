package domain.factories;

import domain.User;
import domain.UserAuthentificationCredentials;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class UserFactory {

    private UserFactory() {}

    public static User createUser(ResultSet set) {
        try {
            UserAuthentificationCredentials credentials = new UserAuthentificationCredentials(set.getString("email"),
                    set.getString("password"));
            credentials.setSalt(set.getString("salt"));
            return createUser(set.getInt("id"), set.getString("first_name"),
                    set.getString("last_name"), LocalDate.parse(set.getString("date_of_birth")),
                    set.getString("country"), credentials);
        } catch (SQLException e) {
            return null;
        }
    }

    public static User createUser(String firstName, String lastName, LocalDate dateOfBirth, String country,UserAuthentificationCredentials userAuthentificationCredentials) {
        return createUser(0, firstName, lastName, dateOfBirth, country,userAuthentificationCredentials);
    }

    public static User createUser(int id, String firstName, String lastName, LocalDate dateOfBirth, String country,UserAuthentificationCredentials userAuthentificationCredentials) {
        User user = new User(firstName, lastName, dateOfBirth, country);
        user.setUserAuthentificationCredentials(userAuthentificationCredentials);
        user.setId(id);
        return user;
    }
}
