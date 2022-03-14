package business;

import repository.database.*;

public class AbstractServiceTest extends AbstractDatabaseTester {

    protected Service service;

    public AbstractServiceTest() {
        super();
        service = new Service(new UserDatabaseRepository(credentials), new FriendshipDatabaseRepository(credentials),
                new MessageDatabaseRepository(credentials), new EventDatabaseRepository(credentials));
    }
}
