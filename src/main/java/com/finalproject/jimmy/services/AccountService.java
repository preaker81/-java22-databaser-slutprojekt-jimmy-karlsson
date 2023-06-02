package com.finalproject.jimmy.services;

import java.util.Random;

public class AccountService {

    //    Generates a random account number
    private final int MIN_ACCOUNT_NUMBER = 100000000;
    private final int MAX_ACCOUNT_NUMBER = 999999999;
    public String generateAccountNumber() {
        Random random = new Random();
        int accountNumber = random.nextInt(MAX_ACCOUNT_NUMBER - MIN_ACCOUNT_NUMBER + 1) + MIN_ACCOUNT_NUMBER;
        return String.format("%09d", accountNumber);
    }

}
