package com.finalproject.jimmy.services;

import com.finalproject.jimmy.models.DBCSingleton;

import java.sql.*;

public class PopulateDatabaseService {
    private final AccountService accountService;
    private final PasswordService passwordService;
    private final String DB_NAME = "db_finalproject";
    private final String ACCOUNT_TABLE_NAME = "account";
    private final String CUSTOMER_TABLE_NAME = "customer";
    private final String TRANSACTION_TABLE_NAME = "transaction";
    private final String ACCOUNT_TABLE_CREATION_QUERY = "CREATE TABLE `account` ("
            + "`id` bigint unsigned NOT NULL AUTO_INCREMENT,"
            + "`account_name` varchar(16) NOT NULL DEFAULT 'unnamed account',"
            + "`created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,"
            + "`customer_id` bigint unsigned NOT NULL,"
            + "`balance` int DEFAULT NULL,"
            + "`account_number` varchar(9) DEFAULT NULL,"
            + "UNIQUE KEY `id` (`id`),"
            + "KEY `account_customer__fk` (`customer_id`),"
            + "CONSTRAINT `account_customer__fk` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`id`) ON DELETE CASCADE ON UPDATE CASCADE"
            + ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;";


    private final String CUSTOMER_TABLE_CREATION_QUERY = "CREATE TABLE `customer` ("
            + "`id` bigint unsigned NOT NULL AUTO_INCREMENT,"
            + "`name` varchar(255) NOT NULL,"
            + "`SSN` varchar(13) NOT NULL,"
            + "`email` varchar(255) NOT NULL,"
            + "`phone` varchar(15) NOT NULL,"
            + "`password` varchar(96) NOT NULL,"
            + "UNIQUE KEY `id` (`id`)"
            + ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;";

    private final String TRANSACTION_TABLE_CREATION_QUERY = "CREATE TABLE `transaction` ("
            + "`id` bigint unsigned NOT NULL AUTO_INCREMENT,"
            + "`sender` bigint unsigned NOT NULL DEFAULT '0',"
            + "`receiver` bigint unsigned NOT NULL DEFAULT '0',"
            + "`amount` int NOT NULL DEFAULT '0',"
            + "`created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,"
            + "`message` text,"
            + "UNIQUE KEY `id` (`id`),"
            + "KEY `transaction_account__fk` (`sender`),"
            + "KEY `transaction_account__fk2` (`receiver`),"
            + "CONSTRAINT `transaction_account__fk` FOREIGN KEY (`sender`) REFERENCES `account` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,"
            + "CONSTRAINT `transaction_account__fk2` FOREIGN KEY (`receiver`) REFERENCES `account` (`id`) ON DELETE CASCADE ON UPDATE CASCADE"
            + ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;";



    public PopulateDatabaseService(AccountService accountService, PasswordService passwordService) {
        this.accountService = accountService;
        this.passwordService = passwordService;
    }

    public void createDatabaseAndTables() {
        Connection connection = null;
        try {
            connection = DBCSingleton.getConnection();
            if (connection == null) {
                System.out.println("Database connection is null");
                return;
            }
            if (!databaseExists(connection, DB_NAME)) {
                createDatabase(connection, DB_NAME);
            }
            if (!tableExists(connection, CUSTOMER_TABLE_NAME)) {
                createTable(connection, CUSTOMER_TABLE_NAME, CUSTOMER_TABLE_CREATION_QUERY);
            }
            if (!tableExists(connection, ACCOUNT_TABLE_NAME)) {
                createTable(connection, ACCOUNT_TABLE_NAME, ACCOUNT_TABLE_CREATION_QUERY);
            }
            if (!tableExists(connection, TRANSACTION_TABLE_NAME)) {
                createTable(connection, TRANSACTION_TABLE_NAME, TRANSACTION_TABLE_CREATION_QUERY);
            }
        } catch (SQLException e) {
            System.out.println("Unable to create database or tables: " + e.getMessage());
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private long createCustomer(Connection connection, String name, String SSN, String email, String phone, String password) throws SQLException {
        String query = "INSERT INTO customer (name, SSN, email, phone, password) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, name);
            statement.setString(2, SSN);
            statement.setString(3, email);
            statement.setString(4, phone);
            String hashedPassword = passwordService.hashPassword(password);
            statement.setString(5, hashedPassword);
            statement.executeUpdate();
            System.out.println("Customer " + name + " created");

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getLong(1);
                } else {
                    throw new SQLException("Creating customer failed, no ID obtained.");
                }
            }
        }
    }

    private void createAccount(Connection connection, long customerId, String accountName, double balance, String accountNumber) throws SQLException {
        String query = "INSERT INTO account (account_name, customer_id, balance, account_number) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, accountName);
            statement.setLong(2, customerId);
            statement.setDouble(3, balance);
            statement.setString(4, accountNumber);
            statement.executeUpdate();
            System.out.println("Account " + accountName + " for customer ID " + customerId + " created");
        }
    }

    private boolean customerExists(Connection connection, String SSN) throws SQLException {
        String query = "SELECT * FROM customer WHERE SSN = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, SSN);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        }
    }


    public void createTemplateCustomers() {
        Connection connection = null;
        try {
            connection = DBCSingleton.getConnection();
            if (connection == null) {
                System.out.println("Database connection is null");
                return;
            }

            if (!customerExists(connection, "20230101-0101")) {
                long customerId1 = createCustomer(connection, "Anders Andersson", "20230101-0101", "anders.andersson@mail.com", "0702987654", "1111");
                createAccount(connection, customerId1, "Primary Account", 5000, accountService.generateAccountNumber());
            }

            if (!customerExists(connection, "20230202-0202")) {
                long customerId2 = createCustomer(connection, "Berit Bengtsson", "20230202-0202", "berit.bengtsson@mail.com", "0702876543", "2222");
                createAccount(connection, customerId2, "Primary Account", 5000, accountService.generateAccountNumber());
            }

        } catch (SQLException e) {
            System.out.println("Unable to create template customers: " + e.getMessage());
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private boolean databaseExists(Connection connection, String dbName) throws SQLException {
        ResultSet resultSet = connection.getMetaData().getCatalogs();
        while (resultSet.next()) {
            String databaseName = resultSet.getString(1);
            if(databaseName.equals(dbName)){
                System.out.println("Database exists");
                return true;
            }
        }
        resultSet.close();
        return false;
    }

    private void createDatabase(Connection connection, String dbName) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE DATABASE " + dbName);
            System.out.println("Database created");
        }
    }

    private boolean tableExists(Connection connection, String tableName) throws SQLException {
        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet resultSet = metaData.getTables(null, null, tableName, null);
        boolean exists = resultSet.next();
        if (exists) {
            System.out.println("Table " + tableName + " exists");
        }
        resultSet.close();
        return exists;
    }

    private void createTable(Connection connection, String tableName, String tableCreationQuery) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(tableCreationQuery);
            System.out.println("Table " + tableName + " created");
        }
    }
}
