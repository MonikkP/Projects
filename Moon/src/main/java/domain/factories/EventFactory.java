package domain.factories;

import domain.Event;
import domain.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class EventFactory {

    private EventFactory() {}

    public static Event createEvent(Integer eventID, User owner, String name, String description, String location,
                                    LocalDateTime dateTime, List<User> members) {
        Event newEvent = new Event(owner, name, description, location, dateTime, members);
        newEvent.setId(eventID);
        return newEvent;
    }

    public static Event createEvent(ResultSet resultSet, User owner, List<User> participants) {
        try {
            return createEvent(resultSet.getInt("id"), owner, resultSet.getString("name"),
                    resultSet.getString("description"), resultSet.getString("location"),
                    LocalDateTime.parse(resultSet.getString("date_time")), participants);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    public static Event createEvent(User owner, String name, String description, String location, LocalDateTime dateTime) {
        return createEvent(0, owner, name, description, location, dateTime, null);
    }
}
