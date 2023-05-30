package com.finalproject.jimmy.repositories;

import com.finalproject.jimmy.models.Account;
import com.finalproject.jimmy.models.DBCSingleton;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountRepository {


    public boolean createAccount(Account account) {
        try (Connection connection = DBCSingleton.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO account (account_name, created, customer_id, balance, account_number) VALUES (?, ?, ?, ?, ?)")) {

            statement.setString(1, account.getAccount_name());
            statement.setTimestamp(2, account.getCreated());
            statement.setInt(3, account.getCustomer_id());
            statement.setInt(4, account.getBalance());
            statement.setString(5, account.getAccount_number());

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteAccount(String accountNumber) {
        try (Connection connection = DBCSingleton.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "DELETE FROM account WHERE account_number = ?")) {

            statement.setString(1, accountNumber);

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Account> getAccountsByCustomerID(int inputCustomerId) {
        List<Account> accountList = new ArrayList<>();

        try (Connection connection = DBCSingleton.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT * FROM account WHERE customer_id = ?")) {

            statement.setInt(1, inputCustomerId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String account_name = resultSet.getString("account_name");
                    Timestamp created = resultSet.getTimestamp("created");
                    int customer_id = resultSet.getInt("customer_id");
                    int balance = resultSet.getInt("balance");
                    String account_number = resultSet.getString("account_number");

                    Account account = new Account(id, account_name, created, customer_id, balance, account_number);
                    accountList.add(account);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return accountList;
    }

    public Account getAccountByAccountNumber(String accountNumber) {
        Account account = null;

        try (Connection connection = DBCSingleton.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT * FROM account WHERE account_number = ?")) {

            statement.setString(1, accountNumber);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String account_name = resultSet.getString("account_name");
                    Timestamp created = resultSet.getTimestamp("created");
                    int customer_id = resultSet.getInt("customer_id");
                    int balance = resultSet.getInt("balance");
                    String account_number = resultSet.getString("account_number");

                    account = new Account(id, account_name, created, customer_id, balance, account_number);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return account;
    }



    public boolean accountExists(String accountNumber) {
        try (Connection connection = DBCSingleton.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT COUNT(*) FROM account WHERE account_number = ?")) {

            statement.setString(1, accountNumber);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }





}
