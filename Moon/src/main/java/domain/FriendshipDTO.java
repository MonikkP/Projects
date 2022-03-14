package domain;

import utils.Constants;

public class FriendshipDTO {
    private final User user;
    private final Friendship friendship;
    private final String statusText;

    public FriendshipDTO(User user, Friendship friendship, String status) {
        this.user = user;
        this.friendship = friendship;
        this.statusText = status;
    }

    public String getFullName() {
        return user.getFullName();
    }

    public String getDate() {
        return friendship.getDateOfFriendship().format(Constants.DATE_FORMATTER);
    }

    public String getStatus() {
        return statusText;
    }

    public User getUser() {
        return user;
    }
}
