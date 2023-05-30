package com.finalproject.jimmy;

import com.finalproject.jimmy.repositories.AccountRepository;
import com.finalproject.jimmy.repositories.CustomerRepository;
import com.finalproject.jimmy.repositories.TransactionRepository;
import com.finalproject.jimmy.services.*;
import com.finalproject.jimmy.view.ConsoleInterface;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
//        Built in classes
        Scanner scanner = new Scanner(System.in);

//        Repositories
        AccountRepository accountRepository = new AccountRepository();
        CustomerRepository customerRepository = new CustomerRepository();
        TransactionRepository transactionRepository = new TransactionRepository(
                accountRepository
        );

//        Services
        PasswordService passwordService = new PasswordService();
        PopulateDatabaseService populateDatabaseService = new PopulateDatabaseService(
                passwordService
        );

        AccountService accountService = new AccountService();

        CustomerService customerService = new CustomerService(
                customerRepository,
                passwordService
        );

        TransactionService transactionService = new TransactionService();

//        Console Interface
        ConsoleInterface consoleInterface = new ConsoleInterface(
                customerRepository,
                accountRepository,
                populateDatabaseService,
                passwordService,
                accountService,
                customerService,
                scanner
        );


//        Starting the program
        consoleInterface.startMenu();
    }
}