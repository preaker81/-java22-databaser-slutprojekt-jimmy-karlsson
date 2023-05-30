package com.finalproject.jimmy.repositories;

import com.finalproject.jimmy.models.Account;


public class TransactionRepository {
    private final AccountRepository accountRepository;

    public TransactionRepository(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;

    }

    public void addBalance(String accountNumber, int amount){

    }

    public void removeBalance (String accountNumber, int amount){

    }
}
