package DAO;

import java.sql.*;

import Model.Account;
import Util.ConnectionUtil;

public class AccountDAO {

    public Account register(Account account){
        Connection connection = ConnectionUtil.getConnection();
        
        try{
            String sql = "Insert into account (username, password) values (?,?)";
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        
            ps.setString(1, account.getUsername());
            ps.setString(2, account.getPassword());

            ps.executeUpdate();
            ResultSet pKeyResultSet = ps.getGeneratedKeys();
            if(pKeyResultSet.next()){
                int generated_acc_id = (int) pKeyResultSet.getLong(1);
                return new Account(generated_acc_id, account.getUsername(), account.getPassword());
            }
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Account loginAccount(String username, String password){
        Connection connection = ConnectionUtil.getConnection();
        try{
           String sql = "Select * from account where username = ? AND password = ?";
           PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS); 
        
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            ps.getGeneratedKeys();
            while(rs.next()){
                Account account = new Account(
                    rs.getInt("account_id"),
                    rs.getString("username"),
                    rs.getString("password"));
                return account;
            }
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Account getAccByUsername(String username){
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "Select * from message where username = ?";
            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Account acc = new Account(rs.getInt("account_id"), 
                rs.getString("username"),
                rs.getString("password")
                );
                return acc;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

}

