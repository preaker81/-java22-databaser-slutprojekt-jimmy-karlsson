package com.finalproject.jimmy.view;


import com.finalproject.jimmy.models.Account;
import com.finalproject.jimmy.models.Customer;
import com.finalproject.jimmy.repositories.AccountRepository;
import com.finalproject.jimmy.repositories.CustomerRepository;
import com.finalproject.jimmy.services.CustomerService;
import com.finalproject.jimmy.services.PasswordService;
import com.finalproject.jimmy.services.PopulateDatabaseService;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Scanner;

public class ConsoleInterface {

    private final CustomerRepository customerRepository;
    private final AccountRepository accountRepository;

    private final PopulateDatabaseService populateDatabaseService;
    private final PasswordService passwordService;
    private final CustomerService customerService;
    private final Scanner scanner;

    public ConsoleInterface(
            CustomerRepository customerRepository,
            AccountRepository accountRepository,
            PopulateDatabaseService populateDatabaseService,
            PasswordService passwordService,
            CustomerService customerService,
            Scanner scanner) {
        this.customerRepository = customerRepository;
        this.accountRepository = accountRepository;
        this.populateDatabaseService = populateDatabaseService;
        this.passwordService = passwordService;
        this.customerService = customerService;
        this.scanner = scanner;
    }

    public void startMenu() {
        // These calls will create tables and template users if not exist.
        populateDatabaseService.createDatabaseAndTables();
        populateDatabaseService.createTemplateCustomers();

        int choice;

        do {
            System.out.println("*******************************************************************");
            System.out.println("----------------------- === Start Menu === ----------------------- ");
            System.out.println("*******************************************************************");
            System.out.println("1. Log in.");
            System.out.println("2. Create customer account.");
            System.out.println("3. Close application.");
            System.out.println("0. Exit");

            System.out.print("Enter your choice: ");

            // Check if there is input available
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline character

                switch (choice) {
                    case 1:
                        loginMenu();
                        break;
                    case 2:
                        createCustomerAccountMenu();
                        break;
                    case 3:

                        break;
                    case 0:
                        System.out.println("Exiting...");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } else {
                // Consume invalid input
                scanner.nextLine();
                System.out.println("Invalid choice. Please try again.");
                choice = -1; // Set choice to an invalid value to repeat the loop
            }
        } while (choice != 0);
    }


    private void createCustomerAccountMenu() {
        System.out.println("*******************************************************************");
        System.out.println("--------------- === Create new customer account === -------------- ");
        System.out.println("*******************************************************************");

        System.out.println("Enter customer details:");
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("SSN (YYYYMMDD-XXXX): ");
        String SSN = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Phone: ");
        String phone = scanner.nextLine();
        System.out.print("Password: ");
        String password = passwordService.hashPassword(scanner.nextLine());

        // Validate SSN format
        if (!customerService.isValidSSN(SSN)) {
            System.out.println("Invalid SSN format. Customer not added to the database.");
            return;
        }

        // Create a new customer object
        Customer customer = new Customer(0, name, SSN, email, phone, password);

        // Insert the customer into the database
        int customerId = customerRepository.createCustomer(customer);
        if (customerId != -1) {
            System.out.println("Customer added successfully.");

            // Create default account values
            String accountName = "Default Account";
            BigDecimal initialBalance = BigDecimal.ZERO;
            String accountNumber = accountRepository.generateAccountNumber();

            // Create the account object
            Account account = new Account(0, accountName, Timestamp.from(Instant.now()), customerId, initialBalance, accountNumber);

            // Insert the account into the database
            if (accountRepository.createAccount(account)) {
                System.out.println("Default account created successfully.");
            } else {
                System.out.println("Failed to create default account.");
            }
        } else {
            System.out.println("Failed to add customer to the database.");
        }
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
            System.out.println("*******************************************************************");
            System.out.println("--------------- === Welcome " + customer.getName() + " === -------------- ");
            System.out.println("*******************************************************************");
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

    private void balanceAccountMenu() {
    }

    private void createBalanceAccountMenu() {
    }

    private void deleteBalanceAccountMenu() {
    }

    private void checkBalanceStatusMenu() {
    }

    private void transferBalanceMenu() {
    }

    private void updateUserMenu() {
    }

    private void deleteUserAccountMenu() {
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