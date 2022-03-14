package domain;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ConversationMembers {

    private List<User> users;

    public ConversationMembers() {
        users = new ArrayList<>();
    }

    public List<User> getUsers() {
        return users;
    }

    public void addUser(User user) {
        users.add(user);
    }

    public void addUser(List<User> userList) {
        for (User user : userList)
            addUser(user);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConversationMembers that = (ConversationMembers) o;
        return hashCode() == that.hashCode();
    }

    @Override
    public int hashCode() {
        List<Integer> ids = users.stream().map(u -> u.getId()).collect(Collectors.toList());
        ids.sort(Comparator.naturalOrder());
        return Objects.hash(ids);
    }

    public List<User> getUsersWithoutSpecificUser(User userToRemove) {
        return getUsers().stream()
                .filter(user -> !user.equals(userToRemove))
                .collect(Collectors.toList());
    }
}
