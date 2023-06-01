package com.finalproject.jimmy.view;

import com.finalproject.jimmy.repositories.AccountRepository;
import com.finalproject.jimmy.repositories.CustomerRepository;
import com.finalproject.jimmy.repositories.TransactionRepository;
import com.finalproject.jimmy.services.*;

import java.util.Scanner;

public class Swosh {
    public void startApp(){

        Scanner scanner = new Scanner(System.in);

//        Repositories
        AccountRepository accountRepository = new AccountRepository();
        CustomerRepository customerRepository = new CustomerRepository();
        TransactionRepository transactionRepository = new TransactionRepository();

//        Services
        PasswordService passwordService = new PasswordService();
        CustomerService customerService = new CustomerService(
                customerRepository,
                passwordService
        );
        AccountService accountService = new AccountService();
        PopulateDatabaseService populateDatabaseService = new PopulateDatabaseService(
                accountService,
                passwordService
        );
        TransactionService transactionService = new TransactionService(
                accountRepository,
                transactionRepository
        );

//        Console Interface
        ConsoleInterface consoleInterface = new ConsoleInterface(
                customerRepository,
                accountRepository,
                populateDatabaseService,
                passwordService,
                customerService,
                accountService,
                transactionService,
                scanner
        );

        consoleInterface.setupDatabase();
        consoleInterface.startMenu();
    }
}
