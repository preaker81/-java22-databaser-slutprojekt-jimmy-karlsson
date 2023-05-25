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
        loadDatabaseProperties();
        createConnection();
    }

    private void loadDatabaseProperties() {
        Properties props = new Properties();

        try (InputStream input = getClass().getClassLoader().getResourceAsStream("database.properties")) {
            if (input == null) {
                logger.error("Unable to find database.properties");
                return;
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

        } catch (Exception e) {
            logger.error("Error loading database properties", e);
        }
    }

    private void createConnection() {
        try {
            connection = dataSource.getConnection();
        } catch (SQLException e) {
            logger.error("Error creating connection", e);
        }
    }

    public static Connection getConnection() {
        try {
            synchronized (DBCSingleton.class) {
                if (db == null) {
                    db = new DBCSingleton();
                } else if (db.connection == null || db.connection.isClosed()) {
                    db.createConnection();
                }
            }
        } catch (SQLException e) {
            logger.error("Error checking or creating connection", e);
        }

        return db.connection;
    }

}
