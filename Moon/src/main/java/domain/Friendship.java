package domain;

import utils.Constants;

import java.time.LocalDateTime;
import java.util.Objects;

public class Friendship extends Entity<Tuple<Integer>>{

    private LocalDateTime dateOfFriendship;

    private FriendshipStatus status;

    public Friendship(LocalDateTime dateOfFriendship, FriendshipStatus status) {
        this.dateOfFriendship = dateOfFriendship;
        this.status = status;
    }

    public FriendshipStatus getStatus() {
        return status;
    }

    public void setStatus(FriendshipStatus status) {
        this.status = status;
    }

    public Friendship() {
        this.dateOfFriendship = LocalDateTime.now();
    }

    public LocalDateTime getDateOfFriendship() {
        return dateOfFriendship;
    }

    public void setDateOfFriendship(LocalDateTime dateOfFriendship) {
        this.dateOfFriendship = dateOfFriendship;
    }

    public boolean isUserBelongingToFriendship(int userID) {
        return getId().getId1() == userID || getId().getId2() == userID;
    }
    @Override
    public String toString() {
        return "Prietenie: " + "id1-" + getId().getId1() +  " id2-" + getId().getId2() +
                " date-" + getDateOfFriendship().format(Constants.DATE_FORMATTER);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return this.getId().equals(((Friendship) o).getId());

    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), dateOfFriendship);
    }
}
