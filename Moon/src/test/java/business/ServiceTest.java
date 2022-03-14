package business;

import domain.*;
import domain.factories.FriendshipFactory;
import domain.factories.MessageFactory;
import domain.factories.UserFactory;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.MethodName.class)
class ServiceTest extends AbstractServiceTest {

    @Test
    void findOneExistingUser() {
        int testID = 1;
        assertEquals(dataSet.getUsers().get(testID), service.findOneUser(testID));
    }

    @Test
    void findOneNonExistingUser() {
        int testID = 7;
        assertEquals(dataSet.getUsers().get(testID), service.findOneUser(testID));
    }

    @Test
    void findOneFriendship() {
        Tuple<Integer> testID = new Tuple<>(1, 2);
        assertEquals(dataSet.getFriendships().get(testID), service.findOneFriendship(testID));
    }

    @Test
    void findAllUsers() {
        assertEquals(4, service.findAllUsers().spliterator().estimateSize());
    }

    @Test
    void getNumberOfUsers() {
        assertEquals(4, service.getNumberOfUsers());
    }

    @Test
    void saveRegisteredUser() {
        int id = 5;
        String firstName = "First-Name 5";
        String lastName = "Last-Name 5";
        LocalDate date = LocalDate.of(2021, 11, 28);
        String country = "Romania";
        String email = "p@gmail.com";
        String password = "as";
        User userTest = UserFactory.createUser(id, firstName, lastName, date, country,new UserAuthentificationCredentials(email,password));
        service.saveRegisteredUser(firstName, lastName, date, country,new UserAuthentificationCredentials(email,password));
        assertEquals(userTest, service.findOneUser(5));
    }

    @Test
    void saveFriendshipRequest() {
        Tuple<Integer> testID = new Tuple<>(2, 3);
        Friendship friendshipTest = FriendshipFactory.createFriendship(2, 3, FriendshipStatus.PENDING);
        service.saveFriendshipRequest(2, 3);
        assertEquals(friendshipTest, service.findOneFriendship(testID));
    }

    @Test
    void acceptFriendRequest() {
        Tuple<Integer> testID = new Tuple<>(2, 4);
        Friendship friendshipTest = service.findOneFriendship(testID);
        service.acceptFriendRequest(2, 4);
        friendshipTest.setStatus(FriendshipStatus.ACCEPTED);
        assertEquals(friendshipTest, service.findOneFriendship(testID));
    }

    @Test
    void zDeleteUser() {
        int testID = 1;
        service.deleteUser(testID);
        assertEquals(null, service.findOneUser(testID));
    }

    @Test
    void findAllFriendshipsOfUser() {
        Tuple<Integer> testID = new Tuple<>(1, 2);
        assertEquals(dataSet.getFriendships().get(testID), service.findAllFriendshipsOfUser(1)
                .collect(Collectors.toList()).get(0));
    }

    @Test
    void findAllFriendshipsOfUserByMonth() {
        Tuple<Integer> testID = new Tuple<>(1, 2);
        assertEquals(dataSet.getFriendships().get(testID), service.findAllFriendshipsOfUserByMonth(1,
                        LocalDate.of(1, 11, 1)).collect(Collectors.toList()).get(0));
    }

    @Test
    void findAllMessages() {
        assertEquals(3, service.findAllMessages().spliterator().estimateSize());
    }

    @Test
    void saveMessage() {
        String messageText = "Message 4";
        service.saveMessage(2, new ArrayList<>(Arrays.asList(3, 4)), messageText);
        assertEquals(4, service.findAllMessages().spliterator().estimateSize());
    }

    @Test
    void saveReply() {
        String messageText = "Message 5";
        service.saveReply(3, new ArrayList<>(Arrays.asList(2)), messageText, 4);
        assertEquals(5, service.findAllMessages().spliterator().estimateSize());
    }

    @Test
    void findConversation() {
        assertEquals(2, service.findConversation(2, 3).spliterator().estimateSize());
    }
}