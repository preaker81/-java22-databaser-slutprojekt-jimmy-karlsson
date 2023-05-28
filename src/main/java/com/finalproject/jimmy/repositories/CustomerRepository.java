package com.finalproject.jimmy.repositories;

import com.finalproject.jimmy.models.Customer;
import com.finalproject.jimmy.models.DBCSingleton;

import java.sql.*;

public class CustomerRepository {
    public int createCustomer(Customer customer) {
        try (Connection connection = DBCSingleton.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO customer (name, ssn, email, phone, password) VALUES (?, ?, ?, ?, ?)",
                     Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, customer.getName());
            statement.setString(2, customer.getSSN());
            statement.setString(3, customer.getEmail());
            statement.setString(4, customer.getPhone());
            statement.setString(5, customer.getPassword());

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Return -1 if the customer creation fails or the index cannot be retrieved.
    }



//    Add update method


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
