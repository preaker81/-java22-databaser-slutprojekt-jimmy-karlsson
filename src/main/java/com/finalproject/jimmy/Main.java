package com.finalproject.jimmy;

import com.finalproject.jimmy.repositories.CustomerRepository;
import com.finalproject.jimmy.services.PasswordService;
import com.finalproject.jimmy.services.PopulateDatabaseService;
import com.finalproject.jimmy.view.ConsoleInterface;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

//        Repositories


//        Services
        Scanner scanner = new Scanner(System.in);
        PasswordService passwordService = new PasswordService();
        PopulateDatabaseService populateDatabaseService = new PopulateDatabaseService(passwordService);

//        Console Interface
        ConsoleInterface consoleInterface = new ConsoleInterface(populateDatabaseService, scanner);


//        Starting the program
        consoleInterface.startMenu();
    }
}