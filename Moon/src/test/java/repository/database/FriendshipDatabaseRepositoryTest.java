package repository.database;

import domain.Friendship;
import domain.factories.FriendshipFactory;
import domain.FriendshipStatus;
import domain.Tuple;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class FriendshipDatabaseRepositoryTest extends AbstractDatabaseTester {

    public FriendshipDatabaseRepositoryTest() {
        super();
        repository = new FriendshipDatabaseRepository(credentials);
    }

    @Test
    void findOne() {
        Tuple id = new Tuple<Integer>(4, 3);
        assertEquals(dataSet.getFriendships().get(id), repository.findOne(id));
    }

    @Test
    void findAll() {
        assertEquals(2, repository.findAll().spliterator().estimateSize());
    }

    @Test
    void save() {
        Tuple<Integer> id = new Tuple<>(2, 3);
        Friendship friendship = FriendshipFactory.createFriendship(2, 3, LocalDateTime.now(),
                FriendshipStatus.ACCEPTED);
        repository.save(friendship);
        assertEquals(repository.findOne(id), friendship);
    }

    @Test
    void delete() {
        Tuple id = new Tuple<Integer>(1, 2);
        Friendship friendship = (Friendship) repository.findOne(id);
        assertEquals(null, repository.delete(friendship));
    }

    @Test
    void update() {
        Tuple id = new Tuple<Integer>(2, 4);
        Friendship friendship = FriendshipFactory.createFriendship(2, 4, LocalDateTime.parse("2021-11-28T13:27:35.682"),
                FriendshipStatus.ACCEPTED);
        repository.update(friendship);
        assertEquals(friendship, repository.findOne(id));

    }

}