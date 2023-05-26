package com.finalproject.jimmy.services;

import com.finalproject.jimmy.models.DBCSingleton;

import java.sql.*;

public class PopulateDatabaseService {
    private final String DB_NAME = "db_finalproject";
    private final String ACCOUNT_TABLE_NAME = "account";
    private final String CUSTOMER_TABLE_NAME = "customer";
    private final String TRANSACTION_TABLE_NAME = "transaction";
    private final String ACCOUNT_TABLE_CREATION_QUERY = "CREATE TABLE `account` ("
            + "`id` bigint unsigned NOT NULL AUTO_INCREMENT,"
            + "`account_name` varchar(16) NOT NULL DEFAULT 'unnamed account',"
            + "`created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,"
            + "`customer_id` bigint unsigned NOT NULL,"
            + "`balance` decimal(19,4) DEFAULT NULL,"
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
            + "`amount` decimal(19,4) NOT NULL DEFAULT '0.0000',"
            + "`created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,"
            + "`message` text,"
            + "UNIQUE KEY `id` (`id`),"
            + "KEY `transaction_account__fk` (`sender`),"
            + "KEY `transaction_account__fk2` (`receiver`),"
            + "CONSTRAINT `transaction_account__fk` FOREIGN KEY (`sender`) REFERENCES `account` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,"
            + "CONSTRAINT `transaction_account__fk2` FOREIGN KEY (`receiver`) REFERENCES `account` (`id`) ON DELETE CASCADE ON UPDATE CASCADE"
            + ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;";


    public void createDatabaseAndTables() {
        try (Connection connection = DBCSingleton.getConnection()) {
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
