package repository.database;

import domain.Message;
import domain.factories.MessageFactory;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class MessageDatabaseRepositoryTest extends AbstractDatabaseTester {

    public MessageDatabaseRepositoryTest() {
        super();
        repository = new MessageDatabaseRepository(credentials);
    }

    @Test
    void findOne() {
        int testID = 1;
        assertEquals(dataSet.getMessages().get(testID), repository.findOne(testID));
    }

    @Test
    void findAll() {
        assertEquals(3, repository.findAll().spliterator().estimateSize());
    }

    @Test
    void save() {
        Message message = MessageFactory.createMessage(4, dataSet.getUsers().get(4),
                new ArrayList<>(Arrays.asList(dataSet.getUsers().get(1))), "TEST", LocalDateTime.now());
        repository.save(message);
        assertEquals(message, repository.findOne(4));
    }

}