package com.finalproject.jimmy.view;


import com.finalproject.jimmy.models.Account;
import com.finalproject.jimmy.models.Customer;

import com.finalproject.jimmy.repositories.AccountRepository;
import com.finalproject.jimmy.repositories.CustomerRepository;

import com.finalproject.jimmy.services.AccountService;
import com.finalproject.jimmy.services.CustomerService;
import com.finalproject.jimmy.services.PasswordService;
import com.finalproject.jimmy.services.PopulateDatabaseService;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Scanner;

public class ConsoleInterface {

    private final CustomerRepository customerRepository;
    private final AccountRepository accountRepository;
    private final PopulateDatabaseService populateDatabaseService;
    private final PasswordService passwordService;
    private final AccountService accountService;
    private final CustomerService customerService;
    private final Scanner scanner;

    public ConsoleInterface(
            CustomerRepository customerRepository,
            AccountRepository accountRepository,
            PopulateDatabaseService populateDatabaseService,
            PasswordService passwordService,
            AccountService accountService,
            CustomerService customerService,
            Scanner scanner) {
        this.customerRepository = customerRepository;
        this.accountRepository = accountRepository;
        this.populateDatabaseService = populateDatabaseService;
        this.passwordService = passwordService;
        this.accountService = accountService;
        this.customerService = customerService;
        this.scanner = scanner;
    }

    public void startMenu() {
        // These calls will create tables and template users if not exist.
        populateDatabaseService.createDatabaseAndTables();
        populateDatabaseService.createTemplateCustomers();

        int choice;

        do {
            System.out.println(ConsoleColors.CYAN);
            System.out.println("*******************************************************************");
            System.out.println("----------------------- === Start Menu === ----------------------- ");
            System.out.println("*******************************************************************");
            System.out.println(ConsoleColors.RESET);
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
        System.out.println(ConsoleColors.CYAN);
        System.out.println("*******************************************************************");
        System.out.println("--------------- === Create new customer account === -------------- ");
        System.out.println("*******************************************************************");
        System.out.println(ConsoleColors.RESET);

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
            int initialBalance = 1000;
            String accountNumber = accountService.generateAccountNumber();

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
        System.out.println(ConsoleColors.CYAN);
        System.out.println("*******************************************************************");
        System.out.println("--------------------------- Login Menu ---------------------------");
        System.out.println("You have two template customer logins you can use.");
        System.out.println("Customer 1: Anders Andersson, SSN = 20000101-0101, password = 1111.");
        System.out.println("Customer 2: Berit Bengtsson, SSN = 20000202-0202, password = 2222.");
        System.out.println("*******************************************************************");
        System.out.println(ConsoleColors.RESET);

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
            System.out.println(ConsoleColors.CYAN);
            System.out.println("*******************************************************************");
            System.out.println("--------------------- === Logged in menu === --------------------- ");
            System.out.println("*******************************************************************");
            System.out.println(ConsoleColors.RESET);
            System.out.println("1. " + customer.getName() + "´s Balance accounts.");
            System.out.println("2. Show user information.");
            System.out.println("3. Update customer information");
            System.out.println("4. Empty");
            System.out.println("5. Transfer money.");
            System.out.println("0. Go back");
            System.out.println("");

            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline character

            switch (choice) {
                case 1:
                    balanceAccountMenu(customer);
                    break;
                case 2:
                    showUserInfoMenu(customer);
                    break;
                case 3:
                    updateCustomerInfo(customer);
                    break;
                case 4:
                    transferMoneyMenu(customer);
                    break;
                case 5:

                    break;
                case 0:
                    System.out.println("Going back to the previous menu...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 0);
    }

    private void balanceAccountMenu(Customer customer) {
        int choice;

        do {
            System.out.println(ConsoleColors.CYAN);
            System.out.println("*******************************************************************");
            System.out.println("------------------ === Balance account menu === ------------------ ");
            System.out.println("*******************************************************************");
            System.out.println(ConsoleColors.RESET);
            System.out.println("1. Create a new balance account");
            System.out.println("2. Delete a existing balance account");
            System.out.println("0. Go back");
            System.out.println("");

            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline character

            switch (choice) {
                case 1:
                    createBalanceAccountMenu(customer);
                    break;
                case 2:
                    deleteBalanceAccountMenu(customer);
                    break;
                case 0:
                    System.out.println("Going back to the previous menu...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 0);
    }

    private void createBalanceAccountMenu(Customer customer) {
        System.out.println(ConsoleColors.CYAN);
        System.out.println("*******************************************************************");
        System.out.println("--------------- === Create balance account menu === -------------- ");
        System.out.println("*******************************************************************");
        System.out.println(ConsoleColors.RESET);
        System.out.println("Enter the account details...");
        System.out.println("");
        System.out.println("Account name: ");
        String accountName = scanner.nextLine();
        System.out.println("How much money should it have initially: ");
        int initialBalance = scanner.nextInt();


        Account account = new Account(0, accountName, Timestamp.from(Instant.now()), customer.getId(), initialBalance, accountService.generateAccountNumber());

        // Insert the account into the database
        if (accountRepository.createAccount(account)) {
            System.out.println("Default account created successfully.");
        } else {
            System.out.println("Failed to create default account.");
        }
    }

    private void deleteBalanceAccountMenu(Customer customer) {
        System.out.println(ConsoleColors.CYAN);
        System.out.println("*******************************************************************");
        System.out.println("--------------- === Delete balance account menu === -------------- ");
        System.out.println("*******************************************************************");
        System.out.println(ConsoleColors.RESET);
        System.out.println("!WARNING! This action is irreversible !WARNING!");
        System.out.println("");
        System.out.println("Enter the account number [#########]");
        String accountNumber = scanner.nextLine();
        System.out.println("Enter 'DELETE [#########]' to confirm");
        String confirmAccountNumber = scanner.nextLine();

        if (accountService.isValidConfirmation(accountNumber, confirmAccountNumber)) {
            Account account = accountRepository.getAccountByAccountNumber(accountNumber); // New line
            if (account != null && account.getCustomer_id() == customer.getId()) { // Check if the account belongs to the customer
                accountRepository.deleteAccount(accountNumber);
                System.out.println("The balance account was successfully deleted.");
            } else if (account == null) {
                System.out.println("The account you provided does not exist, make sure the information is correct and try again.");
            } else {
                System.out.println("You do not have permission to delete this account.");
            }
        } else {
            System.out.println("Wrong input, make sure the information is correct and try again.");
        }
    }


    private void showUserInfoMenu(Customer customer) {
        List<Account> accounts = accountRepository.getAccountsByCustomerID(customer.getId());
        System.out.println(ConsoleColors.CYAN);
        System.out.println("*******************************************************************");
        System.out.println("--------------------- === User information === ------------------- ");
        System.out.println("*******************************************************************");
        System.out.println(ConsoleColors.RESET);
        System.out.println("Name: " + customer.getName());
        System.out.println("SSN: " + customer.getSSN());
        System.out.println("Email: " + customer.getEmail());
        System.out.println("Phone number: " + customer.getPhone());
        System.out.println("-------------------------------------------------------------------");
        System.out.println("ACCOUNTS: ");
        for (Account account : accounts) {
            System.out.println(
                    "Account Number: " + account.getAccount_number() + " " +
                    "Account Name: " + account.getAccount_name() + " " +
                    "Balance: " + account.getBalance()
            );
        }

    }

    public void updateCustomerInfo(Customer customer) {
        System.out.println(ConsoleColors.CYAN);
        System.out.println("*******************************************************************");
        System.out.println("--------------- === Update customer information === -------------- ");
        System.out.println("*******************************************************************");
        System.out.println(ConsoleColors.RESET);
        System.out.println("Enter your new information in the prompt.");
        System.out.println("If you don't need to update a specific field you can just press ENTER.");

        boolean isUpdated = false;

        System.out.println("Enter a new name " + ConsoleColors.YELLOW + "(Press ENTER to skip)" + ConsoleColors.RESET);
        String newName = scanner.nextLine();
        if (!newName.isEmpty()) {
            customer.setName(newName);
            isUpdated = true;
        }

        System.out.println("Enter a new email " + ConsoleColors.YELLOW + "(Press ENTER to skip)" + ConsoleColors.RESET);
        String newEmail = scanner.nextLine();
        if (!newEmail.isEmpty() && customerService.isValidEmail(newEmail)) {
            customer.setEmail(newEmail);
            isUpdated = true;
        } else if (!newEmail.isEmpty()) {
            System.out.println("Invalid email format. Email was not updated.");
        }

        System.out.println("Enter a new phone number " + ConsoleColors.YELLOW + "(Press ENTER to skip)" + ConsoleColors.RESET);
        String newPhone = scanner.nextLine();
        if (!newPhone.isEmpty()) {
            customer.setPhone(newPhone);
            isUpdated = true;
        }

        System.out.println("Enter a new password " + ConsoleColors.YELLOW + "(Press ENTER to skip)" + ConsoleColors.RESET);
        String newPassword = scanner.nextLine();
        if (!newPassword.isEmpty()) {
            customer.setPassword(passwordService.hashPassword(newPassword));
            isUpdated = true;
        }

        // If any field has been updated, we update the customer in the database
        if(isUpdated) {
            boolean result = customerRepository.updateCustomer(customer);
            if(result) {
                System.out.println("Customer data successfully updated in the database.");
            } else {
                System.out.println("Failed to update customer data in the database.");
            }
        } else {
            System.out.println("No data to update.");
        }
    }

    public void transferMoneyMenu(Customer customer) {
        System.out.println(ConsoleColors.CYAN);
        System.out.println("*******************************************************************");
        System.out.println("--------------- === Update customer information === -------------- ");
        System.out.println("*******************************************************************");
        System.out.println(ConsoleColors.RESET);

        System.out.println("Sender account number: ");
        System.out.println("Receiver account number: ");
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