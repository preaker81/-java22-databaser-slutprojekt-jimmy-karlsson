package com.finalproject.jimmy.view;


import com.finalproject.jimmy.services.PopulateDatabaseService;

public class ConsoleInterface {

    private final PopulateDatabaseService populateDatabaseService;

    public ConsoleInterface(PopulateDatabaseService populateDatabaseService) {
        this.populateDatabaseService = populateDatabaseService;
    }

    public void startMenu(){
        populateDatabaseService.createDatabaseAndTables();
    }

}
