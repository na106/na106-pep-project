package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.*;
import java.util.*;

import Model.Message;
import Util.ConnectionUtil;

public class MessageDAO {
    
    public List<Message> getAllMessages(){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();

        try{
            String sql = "Select * from message";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                messages.add(message);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return messages;
    }

    public Message getMessageById(int id){
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "Select * from message where message_id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                Message message = new Message(rs.getInt("message_id"), 
                rs.getInt("posted_by"),
                rs.getString("message_text"),
                rs.getLong("time_posted_epoch")
                );
                return message;
            }           
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public List<Message> getMessagesByAccId(int posted){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        String sql = "SELECT * FROM message WHERE posted_by = ?";

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
                ps.setInt(1, posted);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    Message message = new Message();
                    message.setMessage_id(rs.getInt("message_id"));
                    message.setPosted_by(rs.getInt("posted_by"));
                    message.setMessage_text(rs.getString("message_text"));
                    message.setTime_posted_epoch(rs.getLong("time_posted_epoch"));
                    messages.add(message);
                }
            } catch (SQLException e) 
            {
                System.out.println(e.getMessage());
            }
        return messages;
    }

    public Message addMessage(Message message){
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "Insert into message (posted_by, message_text, time_posted_epoch) values (?,?,?)" ;
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            //write preparedStatement's setString and setInt methods here.
            preparedStatement.setInt(1, message.posted_by);
            preparedStatement.setString(2, message.message_text);
            preparedStatement.setLong(3, message.time_posted_epoch);

            preparedStatement.executeUpdate();
            ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
            if(pkeyResultSet.next()){
                int generated_message_id = (int) pkeyResultSet.getLong(1);
                return new Message(generated_message_id, message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch());
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Message updateMessage(int id, Message message){
        if(message.getMessage_text().isEmpty()){
            return null;
        }
        Connection connection = ConnectionUtil.getConnection();

        try{
            String sql = "UPDATE message SET message_text = ? where message_id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, message.getMessage_text());
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch(SQLException e){
            System.out.println(e.getMessage());
            return null;
        }
        Message msg = getMessageById(id);
        return msg;
    }

    public void deleteMessage(int msg_id){
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "Delete from message where message_id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, msg_id);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

}
