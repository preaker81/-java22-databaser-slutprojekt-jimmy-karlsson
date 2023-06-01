package com.finalproject.jimmy.view;


import com.finalproject.jimmy.models.Account;
import com.finalproject.jimmy.models.Customer;

import com.finalproject.jimmy.models.Transaction;
import com.finalproject.jimmy.repositories.AccountRepository;
import com.finalproject.jimmy.repositories.CustomerRepository;

import com.finalproject.jimmy.services.*;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class ConsoleInterface {

    private final CustomerRepository customerRepository;
    private final AccountRepository accountRepository;
    private final PopulateDatabaseService populateDatabaseService;
    private final PasswordService passwordService;
    private final CustomerService customerService;
    private final AccountService accountService;
    private final TransactionService transactionService;
    private final Scanner scanner;

    public ConsoleInterface(
            CustomerRepository customerRepository,
            AccountRepository accountRepository,
            PopulateDatabaseService populateDatabaseService,
            PasswordService passwordService,
            CustomerService customerService,
            AccountService accountService,
            TransactionService transactionService,
            Scanner scanner) {
        this.customerRepository = customerRepository;
        this.accountRepository = accountRepository;
        this.populateDatabaseService = populateDatabaseService;
        this.passwordService = passwordService;
        this.customerService = customerService;
        this.accountService = accountService;
        this.transactionService = transactionService;
        this.scanner = scanner;
    }

    public void setupDatabase() {
        // These calls will create tables and template users if not exist.
        populateDatabaseService.createDatabaseAndTables();
        populateDatabaseService.createTemplateCustomers();
    }

    public void startMenu() {

        int choice;

        do {
            printMenuHeader("Start");
            System.out.println("1. Log in.");
            System.out.println("2. Create new Customer account.");
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
                    case 0:
                        System.out.println("Exiting...");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } else {
                // Consume invalid input
                scanner.nextLine(); // Consume newline character
                System.out.println("Invalid choice. Please try again.");
                choice = -1;
            }
        } while (choice != 0);
    }

    private void createCustomerAccountMenu() {
        printMenuHeader("Create new customer account");

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
            String accountName = "Primary Account";
            int initialBalance = 2000;
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
        printMenuHeader("Login"); // Prints the header for the menu
        System.out.println(ConsoleColors.YELLOW + "You have two template customer logins you can use.");
        System.out.println("Customer 1: Anders Andersson, SSN = 20230101-0101, password = 1111.");
        System.out.println("Customer 2: Berit Bengtsson, SSN = 20230202-0202, password = 2222.");
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
                System.out.println(ConsoleColors.RED + "Login failed. Please try again or enter 'exit' to quit." + ConsoleColors.RESET);
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
            printMenuHeader("Main menu"); // Prints the header for the menu
            System.out.println("1. Account options.");
            System.out.println("2. User information.");
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
            printMenuHeader("Account options"); // Prints the header for the menu
            printCustomerAccounts(customer.getId()); // Prints the customers accounts.

            System.out.println("1. Create a new balance account.");
            System.out.println("2. Delete a existing balance account.");
            System.out.println("3. Transfer money between accounts.");
            System.out.println("4. Show all Customer transactions made.");
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
                case 3:
                    transferMoneyMenu(customer);
                    break;
                case 4:
                    showAllCustomerTransaction(customer);
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
        printMenuHeader("Create new account"); // Prints the header for the menu
        printCustomerAccounts(customer.getId()); // Prints the customers accounts.

        System.out.println("Enter the account details...");
        System.out.println("");
        System.out.println("Account name (leave empty for default name): ");
        String accountName = scanner.nextLine();
        accountName = accountName.trim();

        // Assign default account name if none is provided
        if (accountName.isEmpty()) {
            accountName = "Default Account";
        }

        System.out.println("How much money should it have initially: ");
        int initialBalance = scanner.nextInt();

        Account account = new Account(0, accountName, Timestamp.from(Instant.now()), customer.getId(), initialBalance, accountService.generateAccountNumber());

        // Insert the account into the database
        if (accountRepository.createAccount(account)) {
            System.out.println("Account '" + accountName + "' created successfully.");
        } else {
            System.out.println("Failed to create account '" + accountName + "'.");
        }
    }

    private void deleteBalanceAccountMenu(Customer customer) {
        printMenuHeader("Delete account"); // Prints the header for the menu
        printCustomerAccounts(customer.getId()); // Prints the customers accounts.

        System.out.println("!WARNING! This action is irreversible !WARNING!");
        System.out.println("");
        System.out.println("Enter the account number [#########]");
        String accountNumber = scanner.nextLine();
        System.out.println("Enter 'DELETE [#########]' to confirm");
        String confirmAccountNumber = scanner.nextLine();

        if (accountService.isValidConfirmation(accountNumber, confirmAccountNumber)) {
            Account account = accountRepository.getAccountByAccountNumber(accountNumber); // New line
            if (account != null && account.getCustomer_id() == customer.getId()) { // Check if the account belongs to the customer
                accountRepository.deleteAccountByAccountNumber(accountNumber);
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

    public void transferMoneyMenu(Customer customer) {

        printMenuHeader("Transfer funds"); // Prints the header for the menu
        printCustomerAccounts(customer.getId()); // Prints the customers accounts.

        System.out.println("Sender account number: ");
        String sender = scanner.nextLine();
        System.out.println("Receiver account number: ");
        String receiver = scanner.nextLine();
        System.out.println("Amount transfered");
        int amount = scanner.nextInt();
        scanner.nextLine(); // consume remaining newline

        System.out.println("Transaction message: ");
        String message = scanner.nextLine();

        boolean result = transactionService.performTransaction(sender, receiver, amount, message);

        if (result) {
            System.out.println("Transaction completed successfully.");
        } else {
            System.out.println("Transaction failed. Please check the details and try again.");
        }
    }

    private void showAllCustomerTransaction(Customer customer) {
        printMenuHeader("All transactions");

        System.out.println(ConsoleColors.YELLOW + "Please provide a start and an end date for the transactions you want to see");
        System.out.println(ConsoleColors.RESET);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        System.out.println("Start date 'YYYY-MM-DD'");
        String startDateStr = scanner.next();
        while (transactionService.isValidDateFormat(startDateStr)) {
            System.out.println("Invalid date format. Please use 'YYYY-MM-DD'");
            startDateStr = scanner.next();
        }

        System.out.println("End date 'YYYY-MM-DD'");
        String endDateStr = scanner.next();
        while (transactionService.isValidDateFormat(endDateStr)) {
            System.out.println("Invalid date format. Please use 'YYYY-MM-DD'");
            endDateStr = scanner.next();
        }

        try {
            LocalDate startDate = LocalDate.parse(startDateStr);
            LocalDate endDate = LocalDate.parse(endDateStr);

            List<Transaction> transactions = transactionService.processTransactions(customer.getId(), startDate, endDate);

            // print the transactions
            for (Transaction transaction : transactions) {
                System.out.println("Transaction ID: " + transaction.getId() + ", Sender: " + transaction.getSender() + ", Receiver: " + transaction.getReceiver() + ", Amount: " + transaction.getAmount() + ", Date: " + transaction.getCreated() + ", Message: " + transaction.getMessage());
            }

        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format. Please use 'YYYY-MM-DD'");
        }
    }

    private void showUserInfoMenu(Customer customer) {
        printMenuHeader("User information"); // Prints the header for the menu
        System.out.println("Name: " + customer.getName());
        System.out.println("SSN: " + customer.getSSN());
        System.out.println("Email: " + customer.getEmail());
        System.out.println("Phone number: " + customer.getPhone());
        System.out.println("-------------------------------------------------------------------");
        printCustomerAccounts(customer.getId()); // Prints the customers accounts.
        System.out.println("-------------------------------------------------------------------");
        int choice;

        do {
            System.out.println("1. Update Customer information.");
            System.out.println("2. Delete Customer Account (This).");
            System.out.println("0. Go back");

            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline character

            switch (choice) {
                case 1:
                    updateCustomerInfo(customer);
                    break;
                case 2:
                    deleteCustomerAccount(customer);
                    break;
                case 0:
                    System.out.println("Going back to the previous menu...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 0);
    }

    public void updateCustomerInfo(Customer customer) {
        printMenuHeader("Update user information"); // Prints the header for the menu
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
        if (isUpdated) {
            boolean result = customerRepository.updateCustomer(customer);
            if (result) {
                System.out.println("Customer data successfully updated in the database.");
            } else {
                System.out.println("Failed to update customer data in the database.");
            }
        } else {
            System.out.println("No data to update.");
        }
    }

    public void deleteCustomerAccount(Customer customer) {
        printMenuHeader("Delete user account"); // Prints the header for the menu
        System.out.println("!WARNING! This action is irreversible !WARNING!");
        System.out.println("Enter SSN: ");
        String SSN = scanner.nextLine();
        System.out.println("Enter " + ConsoleColors.YELLOW + "'DELETE CUSTOMER ACCOUNT'" + ConsoleColors.RESET + " to verify.");
        String verification = scanner.nextLine();
        if (customer.getSSN().equals(SSN) && "DELETE CUSTOMER ACCOUNT".equals(verification)) {
            boolean isDeleted = customerRepository.deleteCustomer(SSN);

            if (isDeleted) {
                System.out.println("Customer account successfully deleted.");
            } else {
                System.out.println("Failed to delete customer account. Please check if the provided SSN is correct.");
            }

            startMenu();
        }
    }

    public void printMenuHeader(String header) {
        System.out.println(ConsoleColors.CYAN);
        System.out.println(" --------------- ==== " + header + " ==== --------------- ");
        System.out.println(ConsoleColors.RESET);
    }

    private void printCustomerAccounts(int customerID) {
        List<Account> accounts = accountRepository.getAccountsByCustomerID(customerID);

        System.out.println(ConsoleColors.YELLOW + "User accounts:" + ConsoleColors.RESET);
        for (Account account : accounts) {
            System.out.println(
                    "Account Number: " + account.getAccount_number() + " " +
                            "Account Name: " + account.getAccount_name() + " " +
                            "Balance: " + account.getBalance()
            );
        }
        System.out.println("");
    }

}