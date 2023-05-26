package com.finalproject.jimmy;

import com.finalproject.jimmy.services.PopulateDatabaseService;
import com.finalproject.jimmy.view.ConsoleInterface;

public class Main {

    public static void main(String[] args) {

//        Repositories


//        Services
        PopulateDatabaseService populateDatabaseService = new PopulateDatabaseService();

//        Console Interface
        ConsoleInterface consoleInterface = new ConsoleInterface(populateDatabaseService);

        consoleInterface.startMenu();
    }
}