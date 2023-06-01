package com.finalproject.jimmy.repositories;

import com.finalproject.jimmy.models.Account;
import com.finalproject.jimmy.models.DBCSingleton;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// This is the repository class for Account. It contains methods for interacting with the 'account' table in the database.
public class AccountRepository {

    // This method is for creating a new account in the database.
    public boolean createAccount(Account account) {
        try (Connection connection = DBCSingleton.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO account (account_name, created, customer_id, balance, account_number) VALUES (?, ?, ?, ?, ?)")) {

            // Set parameters for the prepared statement from the account object.
            statement.setString(1, account.getAccount_name());
            statement.setTimestamp(2, account.getCreated());
            statement.setInt(3, account.getCustomer_id());
            statement.setInt(4, account.getBalance());
            statement.setString(5, account.getAccount_number());

            // Execute the statement and check if the operation affected any rows.
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // This method is for deleting an account from the database by its account number.
    public boolean deleteAccountByAccountNumber(String accountNumber) {
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

    // This method is for retrieving a list of accounts for a specific customer by their customer ID.
    public List<Account> getAccountsByCustomerID(int inputCustomerId) {
        List<Account> accountList = new ArrayList<>();

        try (Connection connection = DBCSingleton.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT * FROM account WHERE customer_id = ?")) {

            statement.setInt(1, inputCustomerId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    // Map the current row of the result set to an Account object.
                    Account account = new Account(
                            resultSet.getInt("id"),
                            resultSet.getString("account_name"),
                            resultSet.getTimestamp("created"),
                            resultSet.getInt("customer_id"),
                            resultSet.getInt("balance"),
                            resultSet.getString("account_number")
                    );

                    // Add the account object to the list.
                    accountList.add(account);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return accountList;
    }

    // This method is for retrieving a single account by its account number.
    public Account getAccountByAccountNumber(String accountNumber) {
        Account account = null;

        try (Connection connection = DBCSingleton.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT * FROM account WHERE account_number = ?")) {

            statement.setString(1, accountNumber);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    // Map the current row of the result set to an Account object.
                    account = new Account(
                            resultSet.getInt("id"),
                            resultSet.getString("account_name"),
                            resultSet.getTimestamp("created"),
                            resultSet.getInt("customer_id"),
                            resultSet.getInt("balance"),
                            resultSet.getString("account_number")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return account;
    }

    // This method checks if an account exists in the database by its account number.
    public boolean accountExists(String accountNumber) {
        try (Connection connection = DBCSingleton.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT COUNT(*) FROM account WHERE account_number = ?")) {

            statement.setString(1, accountNumber);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    // If count is greater than 0, the account exists.
                    return resultSet.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // This method updates the balance of an account by its account number.
    public boolean updateBalance(String accountNumber, int newBalance) {
        try (Connection connection = DBCSingleton.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "UPDATE account SET balance = ? WHERE account_number = ?")) {

            statement.setInt(1, newBalance);
            statement.setString(2, accountNumber);

            int rowsAffected = statement.executeUpdate();
            // Return true when the balance update is successful.
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            // Return false when there is an exception.
            return false;
        }
    }


}
