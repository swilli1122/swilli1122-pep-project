package DAO;

import Util.ConnectionUtil;
import Model.Message;

import java.sql.*;
import java.util.*;

public class MessageDAO {

    public Message addMessage(Message message) {

        if (accountExists(message.getPosted_by()) != true) {
            return null; 
        }

        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "insert into message(posted_by, message_text, time_posted_epoch) values (?, ?, ?);" ;
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setInt(1, message.getPosted_by());
            preparedStatement.setString(2, message.getMessage_text());
            preparedStatement.setLong(3, message.getTime_posted_epoch());
            
            preparedStatement.executeUpdate();
            ResultSet rs = preparedStatement.getGeneratedKeys();
            if(rs.next()){
                int generated_message_id = (int) rs.getLong(1);
                return new Message(generated_message_id, message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch());
            }
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    private boolean accountExists(int account_id) {
        Connection connection = ConnectionUtil.getConnection();
        boolean isAccount = false;
        try {
            String sql = "select account_id from account where account_id = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, account_id);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                if (rs.getInt("account_id") == account_id) {
                    isAccount = true;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return isAccount;
    }

    public List<Message> getAllMessages() {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try {
            String sql = "select * from message;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                messages.add(message);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return messages;
    }

    public Message getMessageByID(int message_id) {

        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "select * from message where message_id = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, message_id);
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()){
                return new Message(message_id, rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch"));
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Message deleteMessageByID(int message_id) {
        Connection connection = ConnectionUtil.getConnection();

        Message deletedMessage = getMessageByID(message_id);

        try {
            if (deletedMessage == null) {
                return null;
            } 

            String sql = "delete from message where message_id = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, message_id);
            
            preparedStatement.executeUpdate();
            
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return deletedMessage;
    }

    public Message patchMessageByID(int message_id, Message update_message) {
        Connection connection = ConnectionUtil.getConnection();

        String updated_text = update_message.getMessage_text();

        Message updatedMessage = getMessageByID(message_id);

        try {
            if (updatedMessage == null) {
                return null;
            } 

            String sql = "update message set message_text = ? where message_id = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, updated_text);
            preparedStatement.setInt(2, message_id);
            
            preparedStatement.executeUpdate();
            updatedMessage.setMessage_text(updated_text);
            
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return updatedMessage;
    }

    public List<Message> getMessagesFromUserByID(int account_id) {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> userMessages = new ArrayList<>();
        try {
            String sql = "select * from message where posted_by = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, account_id);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                userMessages.add(message);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return userMessages;
    }
    
}
