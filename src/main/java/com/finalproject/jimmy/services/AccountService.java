package com.finalproject.jimmy.services;

import com.finalproject.jimmy.repositories.AccountRepository;

public class AccountService {
    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }
}
