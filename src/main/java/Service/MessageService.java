package Service;

import DAO.MessageDAO;
import Model.Message;
import java.util.*;

public class MessageService {
    public MessageDAO messageDAO;

    public MessageService() {
        messageDAO = new MessageDAO();
    }

    public MessageService(MessageDAO messageDAO) {
        this.messageDAO = messageDAO;
    }

    public Message newMessage(Message message) {
        if (message.getMessage_text() != "" && message.getMessage_text().length() <= 255) {
            return messageDAO.addMessage(message);
        }
        return null;
    }

    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    public Message getMessageByID(int message_id) {
        return messageDAO.getMessageByID(message_id);
    }

    public Message deleteMessageByID(int message_id) {
        return messageDAO.deleteMessageByID(message_id);
    }

    public Message patchMessageByID(int message_id, Message update_message) {
        if ((update_message.getMessage_text() != "" && update_message.getMessage_text().length() <= 255)) {
            return messageDAO.patchMessageByID(message_id, update_message);
        } 
        return null;
    }

    public List<Message> getMessagesFromUserByID(int account_id) {
        return messageDAO.getMessagesFromUserByID(account_id);
    }
    
}
