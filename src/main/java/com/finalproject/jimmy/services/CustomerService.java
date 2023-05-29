package com.finalproject.jimmy.services;

import com.finalproject.jimmy.models.Customer;
import com.finalproject.jimmy.repositories.CustomerRepository;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CustomerService {
    private final CustomerRepository customerRepository;
    private final PasswordService passwordService;

    public CustomerService(CustomerRepository customerRepository, PasswordService passwordService) {
        this.customerRepository = customerRepository;
        this.passwordService = passwordService;
    }

    public Customer authenticateUser(String SSN, String password) {
        Customer customer = customerRepository.getCustomer(SSN);

        if (customer != null) {
            boolean passwordMatch = passwordService.verifyPassword(password, customer.getPassword());

            if (passwordMatch) {
                return customer;
            }
        }

        return null;
    }

//    SSN format validation
    public boolean isValidSSN(String ssn) {
        // Validate SSN format: YYYYMMDD-XXXX
        return ssn.matches("\\d{8}-\\d{4}");
    }

    public boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }


}
