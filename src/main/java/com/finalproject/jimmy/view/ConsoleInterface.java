package com.finalproject.jimmy.view;


import com.finalproject.jimmy.repositories.CustomerRepository;
import com.finalproject.jimmy.services.PopulateDatabaseService;

import java.util.Scanner;

public class ConsoleInterface {

    private final PopulateDatabaseService populateDatabaseService;
    private final Scanner scanner;

    public ConsoleInterface(PopulateDatabaseService populateDatabaseService, Scanner scanner) {
        this.populateDatabaseService = populateDatabaseService;
        this.scanner = scanner;
    }

    public void startMenu() {
        populateDatabaseService.createDatabaseAndTables();
        populateDatabaseService.createTemplateCustomers();

        System.out.println("""
                *******************************************************************
                -------------------------Welcome to Swosh.-------------------------
                *******************************************************************
                Press:
                1. Logg in.
                2. Create customer account.
                3. Close application.
                """);
        System.out.println("Input: ");
        String input = scanner.next();

        switch (input) {
            case "1":
                loginMenu();
                break;

            case "2":
                // code to be executed if variable == constant2;
                break;

            // You can have any number of case statements.
            case "3":
                System.exit(0);
                break;

            // Optional
            default:
                // code to be executed if variable doesn't match any constant;
        }


    }

    private void loginMenu(){
        System.out.println("""
                *******************************************************************
                --------------------------- Login Menu. ---------------------------
                You have two template customer logins you can use.
                Customer 1: Anders Andersson, SSN = 20000101-0101, password = 1111.
                Customer 2: Berit Bengtsson, SSN = 20000202-0202, password = 2222.
                *******************************************************************
                
                """);

        System.out.println("SSN: ");
        String ssn = scanner.next();
        System.out.println("Password: ");
        String password = scanner.next();

    }

}
