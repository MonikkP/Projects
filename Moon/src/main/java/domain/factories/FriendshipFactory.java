package domain.factories;

import domain.Friendship;
import domain.FriendshipStatus;
import domain.Tuple;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class FriendshipFactory {
    private FriendshipFactory() {}

    public static Friendship createFriendship(int ID1, int ID2, LocalDateTime date, FriendshipStatus status) {
        Tuple<Integer> friendshipID = new Tuple<>(ID1, ID2);
        Friendship friendship = new Friendship(date, status);
        friendship.setId(friendshipID);
        return friendship;
    }

    public static Friendship createFriendship(int ID1, int ID2, FriendshipStatus status) {
        return createFriendship(ID1, ID2, LocalDateTime.now(), status);
    }

    public static Friendship createFriendship(ResultSet resultSet) {
        try {
            return createFriendship(resultSet.getInt("id1"), resultSet.getInt("id2"),
                    LocalDateTime.parse(resultSet.getString("date_of_friendship")),
                    FriendshipStatus.valueOf(resultSet.getString("status")));
        } catch (SQLException e) {
            return null;
        }
    }
}
