package DAO;

import Model.Account;
import Util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// import com.azul.crs.client.Result;

public class AccountDAO {

    public Account createAccount(Account account) {
        if (accountExists(account.getUsername()) == true) {
            System.out.println("DAO: Account already exists! ");
            return null;
        }
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "insert into account(username, password) values (?, ?);" ;
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());
            
            preparedStatement.executeUpdate();
            ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
            if(pkeyResultSet.next()){
                int generated_account_id = (int) pkeyResultSet.getLong(1);
                return new Account(generated_account_id, account.getUsername(), account.getPassword());
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public boolean login(Account account) {
        boolean isValidLogin = false;
        return isValidLogin;
    }

    private boolean accountExists(String username) {

        Connection connection = ConnectionUtil.getConnection();
        boolean isAccount = false;
        try {
            String sql = "select username from account where username = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                if (rs.getString("username") == username) {
                    isAccount = true;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return isAccount;
    }
    
}
