package DAO;

import Model.Account;
import Util.ConnectionUtil;

import java.sql.*;

public class AccountDAO {

    public Account createAccount(Account account) {
        if (accountExists(account.getUsername()) == true) {
            return null; // do not want to create an account that has already been created
        }
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "insert into account(username, password) values (?, ?);" ;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());
            
            preparedStatement.executeUpdate();
            ResultSet rs = preparedStatement.getGeneratedKeys();
            if(rs.next()){
                int generated_account_id = (int) rs.getLong(1);
                return new Account(generated_account_id, account.getUsername(), account.getPassword());
            }
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Account login(Account account) {
        if (accountExists(account.getUsername()) != true) {
            return null; // can not login if account does not exist
        }
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "select * from account where username = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, account.getUsername());
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) { // some pretty messy login logic below
                if (rs.getString("username").equals(account.getUsername())) {
                    if (rs.getString("password").equals(account.getPassword())) {
                        return new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    // checks if the account id requested is already in database
    private boolean accountExists(String username) {
        Connection connection = ConnectionUtil.getConnection();
        boolean isAccount = false;
        try {
            String sql = "select username from account where username = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                if (rs.getString("username").equals(username)) {
                    isAccount = true;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return isAccount;
    }
    
}
