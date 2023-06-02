package com.finalproject.jimmy.services;

import com.finalproject.jimmy.view.ConsoleColors;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationService {

    private final Scanner scanner;

    public ValidationService(Scanner scanner) {
        this.scanner = scanner;
    }

    public int getValidAmount() {
        while(true) {
            System.out.println("Amount to transfer: ");
            try {
                String input = scanner.nextLine();
                long longInput = Long.parseLong(input);
                if(longInput > Integer.MAX_VALUE) {
                    System.out.println(ConsoleColors.RED + "Input value is larger than maximum allowed integer. Try again." + ConsoleColors.RESET);
                } else {
                    return (int) longInput;
                }
            } catch (NumberFormatException e) {
                System.out.println(ConsoleColors.YELLOW + "Invalid input. Please enter an integer value." + ConsoleColors.RESET);
            }
        }
    }

    public boolean isValidConfirmation(String accountNumber, String confirmAccountNumber) {
        String expectedConfirmation = "DELETE " + accountNumber;

        return confirmAccountNumber.equals(expectedConfirmation);
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

    public boolean isValidDateFormat(String dateStr) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(dateStr);
        } catch (ParseException e) {
            return true;
        }
        return false;
    }

}
