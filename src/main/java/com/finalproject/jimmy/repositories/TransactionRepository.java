package com.finalproject.jimmy.repositories;

import com.finalproject.jimmy.models.DBCSingleton;
import com.finalproject.jimmy.models.Transaction;

import java.sql.*;


public class TransactionRepository {


    public TransactionRepository() {
    }

    public boolean createTransaction(Transaction transaction) {
        try (Connection connection = DBCSingleton.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO transaction (id, sender, receiver, amount, created, message) VALUES (?, ?, ?, ?, ?, ?)")) {

            statement.setInt(1, transaction.getId());
            statement.setInt(2, transaction.getSender());
            statement.setInt(3, transaction.getReceiver());
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



    public ResultSet fetchTransactionsByCustomer(int customerId, Date startDate, Date endDate) {
        Connection connection = DBCSingleton.getConnection();
        ResultSet rs = null;

        try {
            PreparedStatement ps = connection.prepareStatement(
                    "SELECT * FROM `transaction` " +
                            "WHERE (`sender` IN (SELECT `id` FROM `account` WHERE `customer_id` = ?) " +
                            "OR `receiver` IN (SELECT `id` FROM `account` WHERE `customer_id` = ?)) " +
                            "AND `created` BETWEEN ? AND ? " +
                            "ORDER BY `created` DESC"
            );

            ps.setLong(1, customerId);
            ps.setLong(2, customerId);
            ps.setDate(3, new java.sql.Date(startDate.getTime()));
            ps.setDate(4, new java.sql.Date(endDate.getTime()));

            rs = ps.executeQuery();
        } catch (SQLException e) {
            // handle exception, e.g. printStackTrace()
            e.printStackTrace();
        }

        return rs;
    }







}
