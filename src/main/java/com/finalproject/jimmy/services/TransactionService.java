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
import java.util.ArrayList;
import java.util.Date;
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
        sender.setBalance(sender.getBalance() - amount);
        receiver.setBalance(receiver.getBalance() + amount);

        // Save changes, exit if unsuccessful
        if (!accountRepository.updateBalance(senderAccountNumber, sender.getBalance())
                || !accountRepository.updateBalance(receiverAccountNumber, receiver.getBalance())) {
            return false;
        }

        // Create a new transaction
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
        Transaction transaction = new Transaction(0, sender.getId(), receiver.getId(), amount, currentTimestamp, message);

        // Save transaction, exit if unsuccessful
        if (!transactionRepository.createTransaction(transaction)) {
            return false;
        }

        // Transaction successfully performed
        return true;
    }

    public List<Transaction> processTransactions(int customerId, java.util.Date startDate, java.util.Date endDate) {
        java.sql.Date sqlStartDate = new java.sql.Date(startDate.getTime());
        java.sql.Date sqlEndDate = new java.sql.Date(endDate.getTime());
        ResultSet rs = transactionRepository.fetchTransactionsByCustomer(customerId, sqlStartDate, sqlEndDate);
        return extractTransactionsFromResultSet(rs);
    }


    private List<Transaction> extractTransactionsFromResultSet(ResultSet rs) {
        List<Transaction> transactions = new ArrayList<>();

        try {
            while (rs != null && rs.next()) {
                Transaction transaction = new Transaction();
                transaction.setId(rs.getInt("id"));
                transaction.setSender(rs.getInt("sender"));
                transaction.setReceiver(rs.getInt("receiver"));
                transaction.setAmount(rs.getInt("amount"));
                transaction.setCreated(rs.getTimestamp("created"));
                transaction.setMessage(rs.getString("message"));

                transactions.add(transaction);
            }
        } catch (SQLException e) {
            // handle exception, e.g. printStackTrace()
            e.printStackTrace();
        }

        return transactions;
    }

    public boolean isValidDateFormat(String dateStr) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(dateStr);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }


}
