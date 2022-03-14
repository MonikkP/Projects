package domain.factories;

import domain.Message;
import domain.User;

import java.time.LocalDateTime;
import java.util.List;

public class MessageFactory {

    private MessageFactory() {}

    public static Message createMessage(int messageID, User messageSender, List<User> messageReceivers, String messageText,
                                        LocalDateTime dateTime, Message reply) {
        Message message = new Message(messageSender, messageReceivers, messageText, dateTime, reply);
        message.setId(messageID);
        return message;
    }

    public static Message createMessage(User messageSender, List<User> messageReceivers, String messageText,
                                        LocalDateTime dateTime, Message reply) {
        return createMessage(0, messageSender, messageReceivers, messageText, dateTime, reply);
    }

    public static Message createMessage(int messageID, User messageSender, List<User> messageReceivers, String messageText,
                                        LocalDateTime dateTime) {
        return createMessage(messageID, messageSender, messageReceivers, messageText, dateTime, null);
    }

    public static Message createMessage(User messageSender, List<User> messageReceivers, String messageText,
                                        LocalDateTime dateTime) {
        return createMessage(0, messageSender, messageReceivers, messageText, dateTime, null);
    }

}
