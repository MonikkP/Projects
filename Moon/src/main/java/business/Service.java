package business;


import domain.*;
import domain.Observable;
import domain.factories.*;
import domain.validators.FriendshipValidator;
import domain.validators.UserValidator;
import repository.Repository;
import repository.database.EventDatabaseRepository;
import repository.database.FriendshipDatabaseRepository;
import repository.database.UserDatabaseRepository;
import utils.DurationCalculator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;


public class Service extends Observable {

    private UserDatabaseRepository userRepository;

    private FriendshipDatabaseRepository friendshipRepository;

    private Repository<Integer, Message> messageRepository;

    private EventDatabaseRepository eventRepository;

    private UserValidator userValidator;

    private FriendshipValidator friendshipValidator;

    private User loggedUser;

    private ScheduledExecutorService scheduledNotificationsExecutor;

    private final List<Integer> minutesBefore = new ArrayList<>(Arrays.asList(10, 20, 30));

    public Service(UserDatabaseRepository userRepository, FriendshipDatabaseRepository friendshipRepository,
                   Repository<Integer, Message> messageRepository, EventDatabaseRepository eventRepository) {
        this.userRepository = userRepository;
        this.friendshipRepository = friendshipRepository;
        this.messageRepository = messageRepository;
        this.eventRepository = eventRepository;
        this.userValidator = new UserValidator();
        this.friendshipValidator = new FriendshipValidator();
    }

    /**
     * @param userID -the id of the User to be returned
     *               id must not be null
     * @return the entity with the specified id
     * or null - if there is no entity with the given id
     * @throws IllegalArgumentException if id is null.
     */
    public User findOneUser(Integer userID) {
        return userRepository.findOne(userID);
    }

    /**
     * Finds one user by email
     * @param email string email
     * @return one user entity by email or null
     */
    public User findOneUserByEmail(String email) {
        return userRepository.findOneByEmail(email);
    }

    /**
     * Sets new logged user if it is valid (credentials match)
     * @param userAuthentificationCredentials -  credentials to check
     * @return true if logged in false otherwise
     */
    public boolean isValidLogin(UserAuthentificationCredentials userAuthentificationCredentials) {
        User loggedUser = findOneUserByEmail(userAuthentificationCredentials.getUserName());
        if (loggedUser == null)
            return false;
        if(!loggedUser.getUserAuthentificationCredentials().checkIfPasswordMatches(userAuthentificationCredentials.getPassword()))
            return false;
        this.loggedUser = loggedUser;
        notifyObservers();
        rescheduleNotificationsForAllEvents();
        return true;
    }

    /**
     * Checks if friendship has pending status
     * @param id1 - id of first user
     * @param id2 - id of second user
     * @return true if status is pending false otherwise
     */
    public boolean isFriendshipPending(int id1, int id2) {
        Friendship friendship = friendshipRepository.findOne(new Tuple<>(id1, id2));
        if (friendship != null) {
            FriendshipStatus friendshipStatus = friendship.getStatus();
            return friendshipStatus.equals(FriendshipStatus.PENDING);
        }
        return false;
    }

    /**
     * Updates the country of a user
     * @param ID - id of the user
     * @param country - string the new country
     */
    public void updateCountry(int ID, String country) {
        User user = userRepository.findOne(ID);
        user.setCountry(country);
        userRepository.updateCountry(user);
    }

    /**
     * Updates the date of a user
     * @param ID - id of the user
     * @param date - date the new date
     */
    public void updateDate(int ID, LocalDate date) {
        User user = userRepository.findOne(ID);
        user.setDateBirth(date);
        userRepository.updateDate(user);
    }

    /**
     * Updates the email of a user
     * @param ID - id of the user
     * @param email - string the new email
     */
    public void updateEmail(int ID, String email) {
        User user = userRepository.findOne(ID);
        user.setUserAuthentificationCredentials(new UserAuthentificationCredentials(email, user.getUserAuthentificationCredentials().getPassword()));
        userRepository.updateEmail(user);
    }

    /**
     * Updates the password of a user
     * @param ID - id of the user
     * @param password - string the new password
     */
    public void updatePassword(int ID, String password) {
        User user = userRepository.findOne(ID);
        UserAuthentificationCredentials credentials = new UserAuthentificationCredentials(user.getUserAuthentificationCredentials().getUserName(), password);
        credentials.hashYourOwnPassword();
        user.setUserAuthentificationCredentials(credentials);
        userRepository.updatePassword(user);
    }

    /**
     * Finds the logged user
     * @return User current logged user
     */
    public User getLoggedUser() {
        return this.loggedUser;
    }

    /**
     * Checks if a user is the current logged user
     * @param clickedUser - user to be checked
     * @return true if it's the same false otherwise
     */
    public boolean checkIfIsTheLoggedUser(User clickedUser) {
        return getLoggedUser().getUserAuthentificationCredentials().getUserName().equals(clickedUser.getUserAuthentificationCredentials().getUserName());
    }

    /**
     * Checks if a user is friends with the logged user
     * @param clickedUser - user to be checked
     * @return true if it's friends otherwise false
     */
    public boolean checkIfIsFriendWithTheLoggedUser(User clickedUser) {
        return findAllFriendsList(getLoggedUser()).contains(clickedUser);
    }

    /**
     * @param friendshipID -the Tuple of id s of the Friendship to be returned
     *                     id must not be null
     * @return the entity with the specified id
     * or null - if there is no entity with the given id
     * @throws IllegalArgumentException if id is null.
     */
    public Friendship findOneFriendship(Tuple<Integer> friendshipID) {
        return friendshipRepository.findOne(friendshipID);
    }

    /**
     * @return all Users
     */
    public Iterable<User> findAllUsers() {
        return userRepository.findAll();
    }

    /**
     * @return list of all Users
     */
    public List<User> findListAllUsers() {
        List<User> users = new ArrayList<>();
        findAllUsers().forEach(users::add);
        return users;
    }

    /**
     * @return Number of Users
     */
    public int getNumberOfUsers() {
        return (int) findAllUsers().spliterator().estimateSize();
    }

    /**
     * @return all Friendships
     */
    public Iterable<Friendship> findIterableOfAllFriendships() {
        return friendshipRepository.findAll();
    }

    /**
     * @return all Friendships as ArrayList
     */
    public List<Friendship> findListOfAllFriendships() {
        List<Friendship> friendshipList = new ArrayList<>();
        findIterableOfAllFriendships().forEach(friendshipList::add);
        return friendshipList;
    }

    /**
     * @param firstName   must be not null
     * @param lastName    must be not null
     * @param dateOfBirth must respect date_time_formatter from Constants
     * @param country     must not be null
     * @throws ServiceException if the user with this id already exists
     */
    public void saveRegisteredUser(String firstName, String lastName, LocalDate dateOfBirth, String country, UserAuthentificationCredentials userAuthentificationCredentials) {
        userAuthentificationCredentials.hashYourOwnPassword();
        User newUser = UserFactory.createUser(firstName, lastName, dateOfBirth, country, userAuthentificationCredentials);
        userRepository.save(newUser);
    }

    /**
     * @param id1 part of the tuple that identifies one Friendship
     * @param id2 part of the tuple that identifies one Friendship
     * @throws ServiceException if idd1 equals idd2
     *                          if the friendship identified by Tuple (idd1,idd2) already exists
     *                          if the users identified by idd1 or idd2 doesn't exist
     */
    public void saveFriendshipRequest(int id1, int id2) {
        Tuple<Integer> id = new Tuple<>(id1, id2);
        Friendship pr = friendshipRepository.findOne(id);
        if (pr == null) {
            pr = FriendshipFactory.createFriendship(id1, id2, FriendshipStatus.PENDING);
            friendshipRepository.save(pr);
            notifyObservers();
        } else {
            if (pr.getStatus() == FriendshipStatus.PENDING)
                throw new ServiceException("There is already a pending friend request between these 2 people!");
            if (pr.getStatus() == FriendshipStatus.REJECTED && pr.getId().getId2() == id2)  // id2 a mai rejectat odata
                throw new ServiceException("The friend request has already been rejected!");
            if (pr.getStatus() == FriendshipStatus.REJECTED && pr.getId().getId2() == id1) {  // id2 a rejectat in trecut
                // si acum da cerere
                friendshipRepository.delete(pr);
                pr = FriendshipFactory.createFriendship(id1, id2, FriendshipStatus.PENDING);
                friendshipRepository.save(pr);
                notifyObservers();
            }
        }
    }

    private void parseFriendRequeset(int id1, int id2, FriendshipStatus marker) {
        Tuple<Integer> id = new Tuple<>(id1, id2);
        Friendship pr = friendshipRepository.findOne(id);
        if (pr == null)
            throw new ServiceException("The friend request between those 2 users has not been registered!");
        if (pr.getStatus() == FriendshipStatus.ACCEPTED)
            throw new ServiceException("The two users are already friends!");
        if (pr.getStatus() == FriendshipStatus.REJECTED)
            throw new ServiceException("The friend request has already been rejected!");
        // pending
        if (pr.getId().getId2() == id1)
            throw new ServiceException("The sender of a friend request cannot " + marker.toString() +
                    " the friend request.");
        pr.setStatus(marker);
        friendshipRepository.update(pr);
        notifyObservers();
    }

    /**
     * Accept a friend request sent by id1 to id2
     *
     * @param id1 sender of friend request
     * @param id2 receiver of friend request
     */
    public void acceptFriendRequest(int id1, int id2) {
        parseFriendRequeset(id1, id2, FriendshipStatus.ACCEPTED);
    }

    /**
     * Accept a friend request sent by id1 to id2
     *
     * @param id1 sender of friend request
     * @param id2 receiver of friend request
     */
    public void rejectFriendRequest(int id1, int id2) {
        parseFriendRequeset(id1, id2, FriendshipStatus.REJECTED);

    }


    /**
     * @param userID that will be erased
     * @throws ServiceException if entity is null
     *                          if the entity doesn't exist in users list
     */
    public void deleteUser(int userID) {
        User userToDelete = userRepository.findOne(userID);
        if (userToDelete == null)
            throw new ServiceException("There is no user with this id!");
        userRepository.delete(userToDelete);
    }

    /**
     * @param entity that will be removed
     * @throws ServiceException if entity is null
     */
    public void deleteFriendship(Friendship entity) {
        friendshipRepository.delete(entity);
        notifyObservers();
    }

    /**
     * Check if logged user sent friend request to another user
     * @param id - id of the other user
     * @return true if user sent requets otherwise false
     */
    public boolean isRequestSentByLoggedUserTo(int id) {
        Friendship fr = findOneFriendship(new Tuple<>(getLoggedUser().getId(), id));
        return fr.getId().getId1().equals(getLoggedUser().getId());
    }

    /**
     * Finds the list of friends of a user
     * @param user - user whose friends we find
     * @return - list of User who are friends with user
     */
    public List<User> findAllFriendsList(User user) {
        ArrayList<User> userFriends = new ArrayList<>();
        Stream<Friendship> friendsID = findAllFriendshipsOfUser(user.getId());
        friendsID.forEach(friendship -> {
            if (Objects.equals(friendship.getId().getId1(), user.getId())) {
                userFriends.add(findOneUser(friendship.getId().getId2()));
            } else {
                userFriends.add(findOneUser(friendship.getId().getId1()));
            }
        });
        return userFriends;

    }

    /**
     * Finds all friendships of user in stream format
     * @param userID - id of the user we check for
     * @return Stream of Friendships
     */
    public Stream<Friendship> findAllFriendshipsOfUser(int userID) {
        return findListOfAllFriendships().stream().filter(friendship -> friendship.isUserBelongingToFriendship(userID) &&
                friendship.getStatus().equals(FriendshipStatus.ACCEPTED));

    }

    /**
     * Finds all friendships of user in specific month
     * @param userID - user to check
     * @param date - contains the month
     * @return stream of friendships from that month
     */
    public Stream<Friendship> findAllFriendshipsOfUserByMonth(int userID, LocalDate date) {

        return findAllFriendshipsOfUser(userID).filter(friendship ->
                friendship.getDateOfFriendship().getMonth().equals(date.getMonth()));
    }

    /**
     * Finds all friendships of user in a specific interval
     * @param user - user to find friendships of
     * @param from -  initial date
     * @param to - end date
     * @return - list of user belonging to friendships in interval
     */
    public List<User> findAllFriendsOfUserInInterval(User user, LocalDate from, LocalDate to) {
        List<User> friendsInInterval = new ArrayList<>();
        Predicate<Friendship> p1 = n -> {

            if((n.getDateOfFriendship().toLocalDate().isAfter(from) || n.getDateOfFriendship().toLocalDate().equals(from))
                    && (n.getDateOfFriendship().toLocalDate().isBefore(to) || n.getDateOfFriendship().toLocalDate().equals(to)))
                return true;
            else {
                return false;
            }
        };
        List<Friendship> friendships = findAllFriendshipsOfUser(user.getId()).filter(p1).toList();
        for(Friendship f : friendships) {
            if(f.getId().getId1().equals(getLoggedUser().getId())){
                friendsInInterval.add(userRepository.findOne(f.getId().getId2()));
            } else {
                friendsInInterval.add(userRepository.findOne(f.getId().getId1()));
            }
        }
        return friendsInInterval;
    }

    /**
     * Finds all messages stored
     * @return Iterable of Messages with all messages
     */
    public Iterable<Message> findAllMessages() {
        return messageRepository.findAll();
    }

    /**
     * Converts ids to User entities
     * @param ids - list of ids to be converted
     * @return - list of Users corresponding to ID
     */
    private List<User> convertIDsToUsers(List<Integer> ids) {
        return ids.stream().map(this::findOneUser).collect(Collectors.toList());
    }

    /**
     * Parses message details to validate them
     * @param idFromUser - id of sender
     * @param idsToUsers - id of receivers
     * @param message - message to be sent
     */
    private void parseMessageDetails(int idFromUser, List<Integer> idsToUsers, String message) {
        User messageSender = userRepository.findOne(idFromUser);
        if (messageSender == null)
            throw new ServiceException("The user trying to send the message doesn't exist!");
        if (idsToUsers.isEmpty())
            throw new ServiceException("You can't send a message to no one!");
        if (message.equals(""))
            throw new ServiceException("You can't send an empty message!");
        List<User> messageReceivers = convertIDsToUsers(idsToUsers);
        if (messageReceivers.stream().anyMatch(Objects::isNull))
            throw new ServiceException("The user trying to receive the message doesn't exist!");
        if (!messageReceivers.stream().allMatch(new HashSet<>()::add))
            throw new ServiceException("Can not send the same message to duplicated ids !");
        if (messageReceivers.contains(messageSender))
            throw new ServiceException("U can't chat with yourself!\n");
    }

    /**
     * Saves a new message
     * @param idFromUser - id of sender
     * @param idsToUsers - ids of receivers
     * @param messageText - message to be sent
     */
    public void saveMessage(int idFromUser, List<Integer> idsToUsers, String messageText) {
        parseMessageDetails(idFromUser, idsToUsers, messageText);
        Message newMessage = MessageFactory.createMessage(findOneUser(idFromUser), convertIDsToUsers(idsToUsers),
                messageText, LocalDateTime.now());
        messageRepository.save(newMessage);
    }

    /**
     * Saves a new reply
     * @param idFromUser - id from user
     * @param idsToUsers - id to user
     * @param messageText - message to be sent
     * @param messageReplyID - id of reply
     */
    public void saveReply(int idFromUser, List<Integer> idsToUsers, String messageText, Integer messageReplyID) {
        parseMessageDetails(idFromUser, idsToUsers, messageText);
        Message messageReply = messageRepository.findOne(messageReplyID);
        if (messageReply == null)
            throw new ServiceException("The message you're trying to answer doesn't exist!");
        Message newMessage = MessageFactory.createMessage(findOneUser(idFromUser), convertIDsToUsers(idsToUsers),
                messageText, LocalDateTime.now(), messageReply);
        messageRepository.save(newMessage);
    }

    /**
     * Finds conversation between two users
     * @param id1 - id of user1
     * @param id2 - id of user2
     * @return Iterable of Message describing conversation
     */
    public Iterable<Message> findConversation(int id1, int id2) {
        if (userRepository.findOne(id1) == null || userRepository.findOne(id2) == null) {
            throw new ServiceException("Users specified don't exist!");
        }
        Iterable<Message> messages = messageRepository.findAll();
        Set<Message> result = new TreeSet<>(Comparator.comparingInt(Entity::getId));
        for (Message message : messages) {
            if (message.getFrom().getId() == id1 && message.getTo().stream().anyMatch(u -> u.getId() == id2)) {
                result.add(message);
            } else if (message.getFrom().getId() == id2 && message.getTo().stream().anyMatch(u -> u.getId() == id1)) {
                result.add(message);
            }
        }
        return result;
    }

    /**
     * Checks if mail is valid
     * @param mail - string of mail
     * @return true if is valid false otherwise
     */
    public boolean isValidMail(String mail) {
        return findOneUserByEmail(mail) == null;
    }

    /**
     * Finds requests that contain user
     * @param userID - id of user to be checked
     * @return Stream of Friendships that are requests
     */
    public Stream<Friendship> findRequestsContainingUser(int userID) {
        return findListOfAllFriendships().stream().filter(friendship -> friendship.isUserBelongingToFriendship(userID) &&
                friendship.getStatus().equals(FriendshipStatus.PENDING));
    }

    /**
     * Finds users which have pending request by loggeed user
     * @param loggedUser - current logged user
     * @return List of users
     */
    public List<User> findUsersInPendingSentByLoggedUser(User loggedUser) {
        List<User> pendingRequests = new ArrayList<>();
        Stream<Friendship> pendingFriendships = findRequestsContainingUser(loggedUser.getId());
        for (Friendship f : pendingFriendships.toList()) {
            if (f.getId().getId1().equals(loggedUser.getId())) {
                if (isRequestSentByLoggedUserTo(f.getId().getId2())) {
                    pendingRequests.add(findOneUser(f.getId().getId2()));
                }
            } else {
                if (isRequestSentByLoggedUserTo(f.getId().getId1())) {
                    pendingRequests.add(findOneUser(f.getId().getId1()));
                }

            }
        }
        return pendingRequests;
    }

    /**
     * Finds users who rejected friend requests from a user
     * @param user - user to be checked
     * @return list of users
     */
    public List<User> findUsersWhoRejectedFriendRequestsFrom(User user) {
        List<User> friendsWhoRejected = new ArrayList<>();
        for(Friendship f : findListOfAllFriendships()) {
            if(f.getStatus() == FriendshipStatus.REJECTED) {
                if(f.getId().getId1().equals(user.getId())){
                    if(isRequestSentByLoggedUserTo(f.getId().getId2())){
                        friendsWhoRejected.add(findOneUser(f.getId().getId2()));
                    }
                } else {
                    if(isRequestSentByLoggedUserTo(f.getId().getId1())){
                        friendsWhoRejected.add(findOneUser(f.getId().getId1()));
                    }
                }

            }
        }
        return friendsWhoRejected;

    }

    /**
     * Convert a stream of Friendships to a list of FriendshipDTO
     * @param friendshipStream - stream of Friendships
     * @return List of FreindshipDTO
     */
    private List<FriendshipDTO> convertFriendshipStreamToFriendshipDTOList(Stream<Friendship> friendshipStream) {
        Function<Friendship, FriendshipDTO> convertFriendshipToDTO = friendship -> {
            if (friendship.getId().getId1() == getLoggedUser().getId()) {
                if (friendship.getStatus() == FriendshipStatus.PENDING)
                    return new FriendshipDTO(findOneUser(friendship.getId().getId2()), friendship, "REQUEST SENT");
                else if (friendship.getStatus() == FriendshipStatus.ACCEPTED)
                    return new FriendshipDTO(findOneUser(friendship.getId().getId2()), friendship, "ACCEPTED");
                else
                    return new FriendshipDTO(findOneUser(friendship.getId().getId2()), friendship, "REQUEST REJECTED");
            } else {
                if (friendship.getStatus() == FriendshipStatus.PENDING)
                    return new FriendshipDTO(findOneUser(friendship.getId().getId1()), friendship, "REQUEST RECEIVED");
                else if(friendship.getStatus() == FriendshipStatus.ACCEPTED)
                    return new FriendshipDTO(findOneUser(friendship.getId().getId1()), friendship, "ACCEPTED");
                else
                    return null;
            }
        };
        return friendshipStream.map(convertFriendshipToDTO).filter(Objects::nonNull).collect(Collectors.toList());
    }

    /**
     * Finds friends and requests of logged user
     * @return List of FriendshipDTO
     */
    public List<FriendshipDTO> findFriendsAndRequestsOfLoggedUser() {
        return convertFriendshipStreamToFriendshipDTOList(Stream.concat(findAllFriendshipsOfUser(getLoggedUser().getId()),
                        findRequestsContainingUser(getLoggedUser().getId())));
    }

    /**
     * Finds friends and requests of logged user in paginated form
     * @param page current page
     * @param rowsPerPage how many rows per page
     * @return list of friendship dto
     */
    public List<FriendshipDTO> findFriendsAndRequestsOfLoggedUserPaginated(int page, int rowsPerPage) {
        return convertFriendshipStreamToFriendshipDTOList(friendshipRepository.findFriendshipsOfUserPaginated(getLoggedUser(),
                page, rowsPerPage).stream());
    }

    /**
     * Finds number of pages
     * @param rowsPerPage - how many rows per page
     * @return - number of pages
     */
    public int findNumberOfPages(int rowsPerPage) {
        int numberOfFriendsAndRequests = friendshipRepository.findNumberOfFriendsAndRequestsOfUser(getLoggedUser());
        if (numberOfFriendsAndRequests % rowsPerPage == 0 && numberOfFriendsAndRequests > 0)
            return friendshipRepository.findNumberOfFriendsAndRequestsOfUser(getLoggedUser()) / rowsPerPage;
        return friendshipRepository.findNumberOfFriendsAndRequestsOfUser(getLoggedUser()) / rowsPerPage + 1;
    }

    /**
     * Finds count of friends of logged user
     * @return - long count of friends
     */
    public long findCountOfFriendsOfLoggedUser() {
        List<Friendship> list = findAllFriendshipsOfUser(getLoggedUser().getId()).collect(Collectors.toList());
        return list.size();
    }

    /**
     * Finds conversations of logged user
     * @return List fo COnversationMembers of logged user
     */
    public List<ConversationMembers> findConversationsOfLoggedUser() {
        Set<ConversationMembers> conversationMembersSet = new HashSet<>();
        for (Message message : messageRepository.findAll()) {
            if (message.containsUser(getLoggedUser())) {
                conversationMembersSet.add(ConversationMembersFactory.createConversation(message));
            }
        }
        return conversationMembersSet.stream().toList();
    }

    /**
     * Finds message of conversation
     * @param conversationMembers - members of conversation of whom messages we are looking for
     * @return List of messages belonging to that conversation
     */
    public List<Message> findMessageOfConversation(ConversationMembers conversationMembers) {
        return StreamSupport.stream(messageRepository.findAll().spliterator(), false)
                .filter(message -> ConversationMembersFactory.createConversation(message).equals(conversationMembers))
                .sorted(new Comparator<Message>() {
                    @Override
                    public int compare(Message o1, Message o2) {
                        return o1.getId() - o2.getId();
                    }
                })
                .collect(Collectors.toList());
    }


    /**
     *
     * @param loggedUser user who receive the messages
     * @param fromUser user who sent the messages
     * @param from date from
     * @param to date to
     * @return list of received messages from user
     */

    public List<List<Message>> nrOfReceivedMessagesInIntervalFromUser(User loggedUser, User fromUser, LocalDate from, LocalDate to) {
        List<Message> messages = new ArrayList<>();
        List<List<Message>> listOfLists = new ArrayList<>();
        findAllMessages().forEach(messages::add);
        List<Message> messagesReceived = new ArrayList<>();
        List<Message> messagesSent = new ArrayList<>();
        Predicate<Message> p1 = n -> {

            if ((n.getDate().toLocalDate().isAfter(from) || n.getDate().toLocalDate().equals(from))
                    && (n.getDate().toLocalDate().isBefore(to) || n.getDate().toLocalDate().equals(to)))
                return true;
            else {
                return false;
            }
        };

        for(Message m : messages.stream().filter(p1).toList()){
            if(m.getTo().contains(loggedUser) && m.getFrom().equals(fromUser)){
               messagesReceived.add(m);
            }
            if(m.getFrom().equals(loggedUser) && m.getTo().contains(fromUser)){
                messagesSent.add(m);
            }
    }
        listOfLists.add(messagesReceived);
        listOfLists.add(messagesSent);
        return listOfLists;

    }

    /**
     * number of received messages by user in a date interval
     * @param user for whom we want to see the received messages
     * @param to date to
     * @param from date from
     * @return the messages from date from to date to received by user
     */
    public int nrOfReceivedMessagesInInterval(User user,LocalDate to, LocalDate from) {
        List<Message> messages = new ArrayList<>();
       findAllMessages().forEach(messages::add);
        Predicate<Message> p1 = n -> {

            if ((n.getDate().toLocalDate().isAfter(from) || n.getDate().toLocalDate().equals(from))
                    && (n.getDate().toLocalDate().isBefore(to) || n.getDate().toLocalDate().equals(to)))
                return true;
            else {
                return false;
            }
        };
        int nr = 0;

       for(Message m :  messages.stream().filter(p1).toList()){
           if(m.getTo().contains(user)){
               nr++;
           }
       }
       return nr;

    }

    /**
     * saves a message in a possible new conversation if the conversation doesn't exist
     * @param conversationMembers members of the conversation
     * @param messageText the text
     */
    public void saveMessageInPossibleNewConversation(ConversationMembers conversationMembers, String messageText) {
        saveMessageInConversation(conversationMembers, messageText);
        notifyObservers();
    }

    /**
     * saves a message in a conversation
     * @param conversationMembers the members of a conversation
     * @param messageText the text
     */
    public void saveMessageInConversation(ConversationMembers conversationMembers, String messageText) {
        saveMessage(getLoggedUser().getId(),
                conversationMembers.getUsersWithoutSpecificUser(getLoggedUser())
                        .stream()
                        .map(Entity::getId)
                        .collect(Collectors.toList()),
                messageText
        );
    }

    /**
     * find all friends of the logged user
     * @return a list of all friends od the user
     */
    public List<User> findAllFriendsOfLoggedUser() {
        return findAllFriendsList(getLoggedUser());
    }

    /**
     *  saves an event
     * @param name of the event
     * @param description of the event
     * @param location of the event
     * @param dateTime date and time of the event
     */
    public void saveEvent(String name, String description, String location, LocalDateTime dateTime) {
        eventRepository.save(EventFactory.createEvent(getLoggedUser(), name, description, location, dateTime));
        notifyObservers();
    }

    /**
     * save an event participation
     * @param event for which we save a participation
     */
    public void saveEventParticipation(Event event) {
        eventRepository.saveParticipation(getLoggedUser(), event);
        notifyObservers();
        rescheduleNotificationsForAllEvents();
    }

    /**
     * deletes one event
     * @param event we want to delete
     */
    public void deleteEvent(Event event) {
        if (event.getOwner().equals(getLoggedUser())) {
            eventRepository.delete(event);
            notifyObservers();
        }
        else
            throw new ServiceException("You cannot delete an event that you did not create!!!!!!!");
    }

    /**
     * find one event with the ID id
     * @param Id of the event we want to find
     * @return an event with ID id
     */
    public Event findOneEvent(int Id) {
        return eventRepository.findOne(Id);
    }

    /**
     * find all events
     * @return all the events
     */
    public ArrayList<Event> findAllEvents() {
        ArrayList<Event> events = new ArrayList<>();
        for (Event e : eventRepository.findAll()) {
            events.add(e);
        }
        return events;
    }

    /**
     * delete am event participation
     * @param event participation which will be deleted
     */
    public void deleteEventParticipation(Event event) {
        eventRepository.deleteParticipation(getLoggedUser(), event);
        notifyObservers();
    }

    /**
     * schedule notifications for all events
     */
    public void scheduleNotificationsForAllEvents() {
        scheduledNotificationsExecutor = Executors.newScheduledThreadPool(1);
        for (Event event : eventRepository.findAll()) {
            if (event.getParticipants().contains(getLoggedUser()) && findNotificationSubscriptionStatus(event) == NotificationSubscription.SUBSCRIBED) {
                scheduleNotificationsForOneEvent(event);
            }
        }
    }

    /**
     * schedule Notifications for an event
     * @param event for which we schedule notification
     */
    public void scheduleNotificationsForOneEvent(Event event) {
        for (Integer delay : minutesBefore) {
            long secondsUntilEvent = DurationCalculator.findDurationBetweenDatesInSeconds(LocalDateTime.now(), event.getDateTime());
            if (secondsUntilEvent > delay) {
                scheduledNotificationsExecutor.schedule(new PendingNotification(new Notification(event, delay)),
                        secondsUntilEvent - delay, TimeUnit.SECONDS);
            }
        }
    }

    /**
     * cancel scheduled notifications
     */
    public void cancelScheduledNotifications() {
        if (scheduledNotificationsExecutor != null)
            scheduledNotificationsExecutor.shutdownNow();
    }

    /**
     * rescheduleNotification for all events
     */
    public void rescheduleNotificationsForAllEvents() {
        cancelScheduledNotifications();
        scheduleNotificationsForAllEvents();
    }

    private class PendingNotification implements Runnable {

        private Notification notification;

        public PendingNotification(Notification notification) {
            this.notification = notification;
        }

        @Override
        public void run() {
            if (findNotificationSubscriptionStatus(notification.getAttendingEvent()) == NotificationSubscription.SUBSCRIBED) {
                notifyNotificationObservers(notification);
            }
        }
    }

    /**
     * close the threads
     */
    public void close() {
        scheduledNotificationsExecutor.shutdownNow();
    }

    /**
     * notify Notification Observers about read
     */
    public void notificationsHaveBeenRead() {
        notifyNotificationObserversAboutRead();
    }

    /**
     *
     * @param event from which we get the status of subscription
     * @return the status of subscription
     */
    public NotificationSubscription findNotificationSubscriptionStatus(Event event) {
        return eventRepository.findStatusSubscription(getLoggedUser(), event);
    }

    /**
     * unsubsribe from an event
     * @param event we want to unsubscribe
     */
    public void unsubscribeFromNotifications(Event event) {
        eventRepository.updateSubscription(getLoggedUser(), event, NotificationSubscription.UNSUBSCRIBED);
        rescheduleNotificationsForAllEvents();
    }

    /**
     * subscribe to an event
     * @param event  we want to subscribe
     */
    public void subscribeToNotification(Event event) {
        eventRepository.updateSubscription(getLoggedUser(), event, NotificationSubscription.SUBSCRIBED);
        rescheduleNotificationsForAllEvents();
    }
}
