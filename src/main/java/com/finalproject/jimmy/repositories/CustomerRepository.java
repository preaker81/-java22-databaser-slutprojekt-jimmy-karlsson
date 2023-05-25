package com.finalproject.jimmy.repositories;

import com.finalproject.jimmy.models.Customer;
import com.finalproject.jimmy.models.DBCSingleton;

import java.sql.*;

public class CustomerRepository {
    public void createCustomer(String name, String SSN, String email, String phone, String password){
        String query = "INSERT INTO customer (name, SSN, email, phone, password) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DBCSingleton.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, name);
            preparedStatement.setString(2, SSN);
            preparedStatement.setString(3, email);
            preparedStatement.setString(4, phone);
            preparedStatement.setString(5, password);


            int result = preparedStatement.executeUpdate();
            System.out.println("Result: " + result);

        } catch (SQLException e) {
            System.out.println("failed!");
            e.printStackTrace();
        }
    }

    public Customer getCustomer(String SSN){
        String query = "SELECT * FROM customer WHERE SSN=?";

        try (Connection connection = DBCSingleton.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, SSN);

            try (ResultSet result = preparedStatement.executeQuery()) {

                if (result.next()) {
                    int respId = result.getInt("id");
                    String respName = result.getString("name");
                    String respSSN = result.getString("SSN");
                    String respEmail = result.getString("email");
                    String respPhone = result.getString("phone");
                    String respPassword = result.getString("password");

                    return new Customer(respId, respName, respSSN, respEmail, respPhone, respPassword);
                }
            }
        } catch (SQLException e) {
            System.out.println("An error occurred!");
            e.printStackTrace();
        }
        return null;
    }

    public boolean deleteCustomer(String SSN){
        String query = "DELETE FROM customer WHERE SSN = ?";

        try (Connection connection = DBCSingleton.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, SSN);

            int rowsAffected = preparedStatement.executeUpdate();

            System.out.println(rowsAffected);
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("An error occurred: " + e.getMessage());
            return false;
        }
    }
}
