package com.finalproject.jimmy.models;

import com.mysql.cj.jdbc.MysqlDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.io.InputStream;
import java.util.Properties;

public class DBCSingleton {
    private static volatile DBCSingleton db;
    private Connection connection;
    private MysqlDataSource dataSource;

    private static final Logger logger = LogManager.getLogger(DBCSingleton.class);

    private DBCSingleton() {
        try {
            loadDatabaseProperties();
            createConnection();
        } catch (Exception e) {
            logger.error("Error initializing the database", e);
            throw new RuntimeException("Failed to initialize the database", e);
        }
    }

    private void loadDatabaseProperties() throws Exception {
        Properties props = new Properties();

        try (InputStream input = getClass().getClassLoader().getResourceAsStream("database.properties")) {
            if (input == null) {
                throw new IllegalArgumentException("Unable to find database.properties");
            }

            // load properties file
            props.load(input);

            String url = props.getProperty("hostname");
            int port = Integer.parseInt(props.getProperty("port"));
            String database = props.getProperty("dbname");
            String username = props.getProperty("username");
            String password = props.getProperty("password");

            dataSource = new MysqlDataSource();
            dataSource.setUser(username);
            dataSource.setPassword(password);
            dataSource.setUrl("jdbc:mysql://" + url + ":" + port + "/" + database + "?serverTimezone=UTC");
            dataSource.setUseSSL(false);
        }
    }

    private void createConnection() throws SQLException {
        connection = dataSource.getConnection();
    }

    public Connection getConnection() {
        return connection;
    }

    public static DBCSingleton getInstance() {
        DBCSingleton result = db;
        if (result == null) {
            synchronized (DBCSingleton.class) {
                result = db;
                if (result == null) {
                    db = result = new DBCSingleton();
                }
            }
        }
        return result;
    }
}
