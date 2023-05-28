package com.finalproject.jimmy;

import com.finalproject.jimmy.repositories.AccountRepository;
import com.finalproject.jimmy.repositories.CustomerRepository;
import com.finalproject.jimmy.services.CustomerService;
import com.finalproject.jimmy.services.PasswordService;
import com.finalproject.jimmy.services.PopulateDatabaseService;
import com.finalproject.jimmy.view.ConsoleInterface;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
//        Built in classes
        Scanner scanner = new Scanner(System.in);

//        Repositories
        CustomerRepository customerRepository = new CustomerRepository();
        AccountRepository accountRepository = new AccountRepository();

//        Services
        PasswordService passwordService = new PasswordService();
        PopulateDatabaseService populateDatabaseService = new PopulateDatabaseService(
                passwordService);

        CustomerService customerService = new CustomerService(
                customerRepository,
                passwordService);

//        Console Interface
        ConsoleInterface consoleInterface = new ConsoleInterface(
                customerRepository,
                accountRepository,
                populateDatabaseService,
                passwordService,
                customerService,
                scanner);


//        Starting the program
        consoleInterface.startMenu();
    }
}