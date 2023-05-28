package com.finalproject.jimmy.view;


import com.finalproject.jimmy.models.Customer;
import com.finalproject.jimmy.services.CustomerService;
import com.finalproject.jimmy.services.PopulateDatabaseService;

import java.util.Scanner;

public class ConsoleInterface {

    private final PopulateDatabaseService populateDatabaseService;
    private final CustomerService customerService;
    private final Scanner scanner;

    public ConsoleInterface(PopulateDatabaseService populateDatabaseService, CustomerService customerService, Scanner scanner) {
        this.populateDatabaseService = populateDatabaseService;
        this.customerService = customerService;
        this.scanner = scanner;
    }

    public void startMenu() {
//        These calls will create tables and template users if not exist.
        populateDatabaseService.createDatabaseAndTables();
        populateDatabaseService.createTemplateCustomers();

        int choice;

        do {
            System.out.println("*******************************************************************");
            System.out.println("----------------------- === Start Menu === ----------------------- ");
            System.out.println("*******************************************************************");
            System.out.println("1. Logg in.");
            System.out.println("2. Create customer account.");
            System.out.println("3. Close application.");
            System.out.println("0. Exit");

            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    loginMenu();
                    break;
                case 2:

                    break;
                case 3:

                    break;
                case 0:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 0);
    }

    private void loginMenu() {
        System.out.println("*******************************************************************");
        System.out.println("--------------------------- Login Menu ---------------------------");
        System.out.println("You have two template customer logins you can use.");
        System.out.println("Customer 1: Anders Andersson, SSN = 20000101-0101, password = 1111.");
        System.out.println("Customer 2: Berit Bengtsson, SSN = 20000202-0202, password = 2222.");
        System.out.println("*******************************************************************");

//        Scanner scanner = new Scanner(System.in);
        boolean loggedIn = false;

        while (!loggedIn) {
            System.out.print("SSN: ");
            String SSN = scanner.nextLine();

            System.out.print("Password: ");
            String password = scanner.nextLine();

            Customer customer = customerService.authenticateUser(SSN, password);

            if (customer != null) {
                loggedIn = true;
                loggedInMenu(customer);
            } else {
                System.out.println("Login failed. Please try again or enter 'exit' to quit.");
                System.out.print("Enter 'exit' to quit or any key to retry: ");
                String input = scanner.nextLine();

                if (input.equalsIgnoreCase("exit")) {
                    break; // Exit the loop if the user chooses to quit
                }
            }
        }
    }



    private void loggedInMenu(Customer customer) {
        int choice;

        do {
            System.out.println("=== Submenu ===");
            System.out.println("1. Create balance account.");
            System.out.println("2. Delete balance account.");
            System.out.println("3. Show all balance accounts.");
            System.out.println("4. Show user information.");
            System.out.println("5. Transfer money.");
            System.out.println("0. Go back");

            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline character

            switch (choice) {
                case 1:
//                    createBalanceAccountMenu(customer);
                    break;
                case 2:
//                    deleteBalanceAccountMenu(customer);
                    break;
                case 3:
//                    showAllBalanceAccountMenu(customer);
                    break;
                case 4:
//                    showUserInfoMenu(customer);
                    break;
                case 5:
//                    transferMoneyMenu(customer);
                    break;
                case 0:
                    System.out.println("Going back to the previous menu...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 0);
    }

    private void createCustomerAccount(){

    }

}

/*
* Template submenu.
* */

//    public static void submenu(Scanner scanner) {
//        int choice;
//
//        do {
//            System.out.println("=== Submenu ===");
//            System.out.println("1. Suboption 1");
//            System.out.println("2. Suboption 2");
//            System.out.println("0. Go back");
//
//            System.out.print("Enter your choice: ");
//            choice = scanner.nextInt();
//            scanner.nextLine(); // Consume newline character
//
//            switch (choice) {
//                case 1:
//                    // Handle Suboption 1
//                    System.out.println("Suboption 1 selected.");
//                    break;
//                case 2:
//                    // Handle Suboption 2
//                    System.out.println("Suboption 2 selected.");
//                    break;
//                case 0:
//                    System.out.println("Going back to the previous menu...");
//                    break;
//                default:
//                    System.out.println("Invalid choice. Please try again.");
//            }
//        } while (choice != 0);
//    }