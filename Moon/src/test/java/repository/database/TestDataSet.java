package repository.database;

import domain.*;
import domain.factories.FriendshipFactory;
import domain.factories.MessageFactory;
import domain.factories.UserFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class TestDataSet {

    private Map<Integer, User> users = new HashMap<>();

    private Map<Tuple<Integer>, Friendship> friendships = new HashMap<>();

    private Map<Integer, Message> messages = new HashMap<>();

    public TestDataSet() {
        for (int i = 1; i <= 4; i++)
            users.put(i, UserFactory.createUser(i, "First-Name " + i, "Last-Name " + i,
                    LocalDate.of(2021, 11, 28), "Romania",new UserAuthentificationCredentials("e","p")));
        friendships.put(new Tuple<>(1, 2), FriendshipFactory.createFriendship(1, 2,
                LocalDateTime.parse("2021-11-28T13:27:35.682"), FriendshipStatus.valueOf("ACCEPTED")));
        friendships.put(new Tuple<>(4, 3), FriendshipFactory.createFriendship(4, 3,
                LocalDateTime.parse("2021-11-28T13:27:35.682"), FriendshipStatus.valueOf("REJECTED")));
        friendships.put(new Tuple<>(2, 4), FriendshipFactory.createFriendship(2, 4,
                LocalDateTime.parse("2021-11-28T13:27:35.682"), FriendshipStatus.valueOf("PENDING")));
        for (int i = 1; i <= 3; i++)
            messages.put(i, MessageFactory.createMessage(i, users.get(i), null, "Message " + i,
                    LocalDateTime.parse("2021-11-28T11:34:27.519568400")));
        users.get(2);
        messages.get(1).setTo(new ArrayList<User>(Arrays.asList(users.get(2), users.get(3))));
        messages.get(2).setTo(new ArrayList<User>(Arrays.asList(users.get(1), users.get(3))));
        messages.get(3).setTo(new ArrayList<User>(Arrays.asList(users.get(2))));
    }

    public Map<Integer, User> getUsers() {
        return users;
    }

    public Map<Tuple<Integer>, Friendship> getFriendships() {
        return friendships;
    }

    public Map<Integer, Message> getMessages() {
        return messages;
    }
}
