package com.finalproject.jimmy.repositories;

import com.finalproject.jimmy.models.Account;
import com.finalproject.jimmy.models.DBCSingleton;
import com.finalproject.jimmy.models.Transaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;


public class TransactionRepository {
    private final AccountRepository accountRepository;

    public TransactionRepository(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;

    }

    public boolean createTransaction(Transaction transaction) {
        try (Connection connection = DBCSingleton.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO transaction (id, sender, receiver, amount, created, message) VALUES (?, ?, ?, ?, ?, ?)")) {

            statement.setInt(1, transaction.getId());
            statement.setInt(2, transaction.getSender());
            statement.setInt(3, transaction.getReciever());
            statement.setInt(4, transaction.getAmount());
            statement.setTimestamp(5, transaction.getCreated());
            statement.setString(6, transaction.getMessage());

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }







}
