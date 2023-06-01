package com.finalproject.jimmy.models;

import com.mysql.cj.jdbc.MysqlDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.io.InputStream;
import java.util.Properties;

public class DBCSingleton {
    private static volatile DBCSingleton db; // Singleton instance
    private Connection connection;
    private MysqlDataSource dataSource;
    private static final Logger logger = LogManager.getLogger(DBCSingleton.class);

    // Private constructor to prevent instantiation
    private DBCSingleton() {
        try {
            // Load database properties from a properties file
            loadDatabaseProperties();
            // Establish a connection
            createConnection();
        } catch (Exception e) {
            logger.error("Error during database initialization", e);
        }
    }

    private void loadDatabaseProperties() throws Exception {
        Properties props = new Properties();
        // Load the properties file
        InputStream input = getClass().getClassLoader().getResourceAsStream("database.properties");

        if (input == null) {
            throw new Exception("Unable to find database.properties");
        }

        // Load properties from the properties file
        props.load(input);

        // Get properties
        String url = props.getProperty("hostname");
        int port = Integer.parseInt(props.getProperty("port"));
        String database = props.getProperty("dbname");
        String username = props.getProperty("username");
        String password = props.getProperty("password");

        // Set database connection properties
        dataSource = new MysqlDataSource();
        dataSource.setUser(username);
        dataSource.setPassword(password);
        dataSource.setUrl("jdbc:mysql://" + url + ":" + port + "/" + database + "?serverTimezone=UTC");
        dataSource.setUseSSL(false);
    }

    private void createConnection() throws SQLException {
        // Only create a new connection if the current one doesn't exist or is closed
        if (connection != null && !connection.isClosed()) {
            return;
        }
        // Create a new connection
        connection = dataSource.getConnection();
    }

    public static Connection getConnection() {
        DBCSingleton result = db;
        try {
            if (result == null) {
                synchronized (DBCSingleton.class) {
                    result = db;
                    if (result == null) {
                        db = new DBCSingleton();
                        result = db;
                    }
                }
            } else if (result.connection == null || result.connection.isClosed()) {
                // If connection is closed, create a new one
                result.createConnection();
            }
        } catch (SQLException e) {
            logger.error("Error checking or creating connection", e);
        }

        return result.connection;
    }


    public static void close() {
        // Close the connection if it exists and is open
        if (db != null && db.connection != null) {
            try {
                db.connection.close();
            } catch (SQLException e) {
                logger.error("Error closing connection", e);
            }
        }
    }
}
