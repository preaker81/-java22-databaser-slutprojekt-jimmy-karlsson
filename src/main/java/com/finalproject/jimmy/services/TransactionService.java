package com.finalproject.jimmy.services;

import com.finalproject.jimmy.models.Account;
import com.finalproject.jimmy.models.Transaction;
import com.finalproject.jimmy.repositories.AccountRepository;
import com.finalproject.jimmy.repositories.TransactionRepository;

import java.sql.Timestamp;

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

}
