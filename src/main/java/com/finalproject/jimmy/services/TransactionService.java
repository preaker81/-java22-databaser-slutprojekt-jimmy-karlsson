package com.finalproject.jimmy.services;

import com.finalproject.jimmy.models.Account;
import com.finalproject.jimmy.models.Transaction;
import com.finalproject.jimmy.repositories.AccountRepository;
import com.finalproject.jimmy.repositories.TransactionRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TransactionService {
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public TransactionService(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    public boolean performTransaction(String senderAccountNumber, String receiverAccountNumber, int amount, String message) {
        // Retrieve sender and receiver accounts
        Account sender = accountRepository.getAccountByAccountNumber(senderAccountNumber);
        Account receiver = accountRepository.getAccountByAccountNumber(receiverAccountNumber);

        // Validate sender, receiver, and sender's balance
        if (sender == null || receiver == null || sender.getBalance() < amount) {
            return false;
        }

        // Update sender and receiver balances
        int updatedSenderBalance = sender.getBalance() - amount;
        int updatedReceiverBalance = receiver.getBalance() + amount;

        // Try to update balances in the repository
        boolean isSenderBalanceUpdated = accountRepository.updateBalance(senderAccountNumber, updatedSenderBalance);
        boolean isReceiverBalanceUpdated = accountRepository.updateBalance(receiverAccountNumber, updatedReceiverBalance);

        // Check if both updates were successful
        if (!isSenderBalanceUpdated || !isReceiverBalanceUpdated) {
            return false;
        }

        // If we reached this point, it means both balances were successfully updated
        // We can proceed with creating a new transaction
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
        Transaction transaction = new Transaction(0, sender.getId(), receiver.getId(), amount, currentTimestamp, message);

        // Return the result of transaction creation directly
        return transactionRepository.createTransaction(transaction);
    }



    public List<Transaction> processTransactions(int customerId, LocalDate startDate, LocalDate endDate) {
        // Convert startDate and endDate into LocalDateTime at start and end of respective days
        LocalDateTime startDateTime = getStartOfDay(startDate);
        LocalDateTime endDateTime = getEndOfDay(endDate);

        // Fetch transactions from repository and extract them into a List
        ResultSet resultSet = transactionRepository.fetchTransactionsByCustomer(customerId, startDateTime, endDateTime);
        List<Transaction> transactions = resultSetToTransactions(resultSet);

        return transactions;
    }

    private List<Transaction> resultSetToTransactions(ResultSet resultSet) {
        List<Transaction> transactions = new ArrayList<>();

        try {
            // Iterate over each row in the result set
            while (resultSet != null && resultSet.next()) {
                // Create a new Transaction object and set its properties from the result set
                Transaction transaction = new Transaction();
                transaction.setId(resultSet.getInt("id"));
                transaction.setSender(resultSet.getInt("sender"));
                transaction.setReceiver(resultSet.getInt("receiver"));
                transaction.setAmount(resultSet.getInt("amount"));
                transaction.setCreated(resultSet.getTimestamp("created"));
                transaction.setMessage(resultSet.getString("message"));

                // Add the transaction to the list
                transactions.add(transaction);
            }
        } catch (SQLException e) {
            // Print stack trace for debugging purposes. In production code, consider using logging.
            e.printStackTrace();
        }

        return transactions;
    }

    public LocalDateTime getStartOfDay(LocalDate date) {
        return date.atStartOfDay();
    }

    public LocalDateTime getEndOfDay(LocalDate date) {
        return date.plusDays(1).atStartOfDay().minusNanos(1);
    }

}
