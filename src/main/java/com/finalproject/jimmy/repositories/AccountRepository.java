package com.finalproject.jimmy.repositories;

import com.finalproject.jimmy.models.Account;
import com.finalproject.jimmy.models.DBCSingleton;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;

public class AccountRepository {


    public boolean createAccount(Account account) {
        try (Connection connection = DBCSingleton.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO account (account_name, created, customer_id, balance, account_number) VALUES (?, ?, ?, ?, ?)")) {

            statement.setString(1, account.getAccount_name());
            statement.setTimestamp(2, account.getCreated());
            statement.setInt(3, account.getCustomer_id());
            statement.setBigDecimal(4, account.getBalance());
            statement.setString(5, account.getAccount_number());

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    //    Generates a random account number
    private final int MIN_ACCOUNT_NUMBER = 100000000;
    private final int MAX_ACCOUNT_NUMBER = 999999999;
        public String generateAccountNumber() {
            Random random = new Random();
            int accountNumber = random.nextInt(MAX_ACCOUNT_NUMBER - MIN_ACCOUNT_NUMBER + 1) + MIN_ACCOUNT_NUMBER;
            return String.format("%09d", accountNumber);
    }

}
