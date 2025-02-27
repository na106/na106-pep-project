package Service;

import java.util.*;

import DAO.AccountDAO;
import DAO.MessageDAO;
import Model.Account;
import Model.Message;

public class MessageService {
    
    MessageDAO messageDAO;
    AccountDAO accountDAO;

    public MessageService(){
        messageDAO = new MessageDAO();
    }

    public MessageService(MessageDAO messageDAO, AccountDAO accountDAO){
        this.messageDAO = messageDAO;
        this.accountDAO = accountDAO;
    }

    public List<Message> getAllMessages(){
        return messageDAO.getAllMessages();
    }

    public Message getMessageById(int message_id){
            return messageDAO.getMessageById(message_id);
    }
    
    public List<Message> getMessagesByAccId(int accountId) {
        return messageDAO.getMessagesByAccId(accountId);
    }

    public Message addMessage(Message message) {
        if(message.getMessage_text().isEmpty() == true || 
        messageDAO.getMessageById(message.getMessage_id()) != null)
        {
            return null;
        }
        return messageDAO.addMessage(message);
        
    }

   public Message updateMessage(int message_id, Message message) {
        Message msg = messageDAO.getMessageById(message_id);
        if(msg == null){
            return null;
        }
        return messageDAO.updateMessage(message_id, message);
    }

    public Message deleteMessage(int id) {
        Message message = messageDAO.getMessageById(id);
        if (message == null){
            return null;
        }
        messageDAO.deleteMessage(id);
        return message;
    }
}
