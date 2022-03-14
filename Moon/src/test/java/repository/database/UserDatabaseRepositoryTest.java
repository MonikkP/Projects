package repository.database;

import domain.User;
import domain.UserAuthentificationCredentials;
import domain.factories.UserFactory;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserDatabaseRepositoryTest extends AbstractDatabaseTester {

    public UserDatabaseRepositoryTest() {
        super();
        repository = new UserDatabaseRepository(credentials);
    }

    @Test
    void findOne() {
        int testID = 1;
        assertEquals(dataSet.getUsers().get(testID), repository.findOne(testID));
    }

    @Test
    void findAll() {
        assertEquals(repository.findAll().spliterator().estimateSize(), 3);
    }

    @Test
    void save() {
        User userTest = UserFactory.createUser(5, "First-Name 5", "Last-Name 5",
                LocalDate.of(2021, 11, 28), "Romania",new UserAuthentificationCredentials("email","pass"));
        repository.save(userTest);
        assertEquals(userTest, repository.findOne(5));
    }

    @Test
    void delete() {
        User user = (User) repository.delete(repository.findOne(3));
        assertEquals(user, null);
    }
}