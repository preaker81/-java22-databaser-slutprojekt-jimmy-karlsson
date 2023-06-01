package com.finalproject.jimmy;

import com.finalproject.jimmy.repositories.AccountRepository;
import com.finalproject.jimmy.repositories.CustomerRepository;
import com.finalproject.jimmy.repositories.TransactionRepository;
import com.finalproject.jimmy.services.*;
import com.finalproject.jimmy.view.ConsoleInterface;
import com.finalproject.jimmy.view.Swosh;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Swosh swosh = new Swosh();
        swosh.startApp();

    }
}