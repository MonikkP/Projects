package domain.factories;

import domain.ConversationMembers;
import domain.Message;
import domain.User;

import java.util.ArrayList;
import java.util.List;

public class ConversationMembersFactory {

    private ConversationMembersFactory() {

    }

    public static ConversationMembers createConversation(Message message) {
        ConversationMembers conversationMembers = new ConversationMembers();
        conversationMembers.addUser(message.getFrom());
        conversationMembers.addUser(message.getTo());
        return conversationMembers;
    }

    public static ConversationMembers createConversation(List<User> userList) {
        ConversationMembers conversationMembers = new ConversationMembers();
        conversationMembers.addUser(userList);
        return conversationMembers;
    }
}
