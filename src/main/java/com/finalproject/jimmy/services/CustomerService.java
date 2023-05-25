package com.finalproject.jimmy.services;

import com.finalproject.jimmy.repositories.CustomerRepository;

public class CustomerService {
    private final CustomerRepository customerRepository;
    private final PasswordService passwordService;

    public CustomerService(CustomerRepository customerRepository, PasswordService passwordService) {
        this.customerRepository = customerRepository;
        this.passwordService = passwordService;
    }
}
