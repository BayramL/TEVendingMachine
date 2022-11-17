package com.techelevator.ui;

import com.techelevator.application.VendingMachine;

import java.math.BigDecimal;
import java.util.Scanner;

/**
 * Responsibilities: This class should handle receiving ALL input from the User
 * 
 * Dependencies: None
 */
public class UserInput
{
    private static Scanner scanner = new Scanner(System.in);

    public static String getUserMoney() {
        System.out.println("Enter in whole dollar amounts ($1, $5, $10, $20): ");
        String selectedOption = scanner.nextLine();

        if (selectedOption.equals("1")) {
            return selectedOption;
        }
        else if (selectedOption.equals("5")) {
            return selectedOption;
        }
        else if (selectedOption.equals("10")) {
            return selectedOption;
        }
        else if (selectedOption.equals("20")) {
            return selectedOption;
        }
        else {
            return "";
        }

    }

    public static String getHomeScreenOption()
    {
        System.out.println("What would you like to do?");
        System.out.println();

        System.out.println("D) Display Vending Machine Items");
        System.out.println("P) Purchase");
        System.out.println("E) Exit");

        System.out.println();
        System.out.print("Please select an option: ");

        String selectedOption = scanner.nextLine();
        String option = selectedOption.trim().toUpperCase();

        if (option.equals("D"))
        {
            return "display";
        }
        else if (option.equals("P"))
        {
            return "purchase";
        }
        else if (option.equals("E"))
        {
            return "exit";
        }
        else
        {
            return "";
        }

    }

    public static String getSubMenuOption(VendingMachine vendingMachine)
    {
        System.out.println("What would you like to do?");
        System.out.println();

        System.out.println("M) Feed Money");
        System.out.println("P) Select Item");
        System.out.println("F) Finish Transaction");
        System.out.println();

        System.out.println("Total money: $" + vendingMachine.getMoney());

        System.out.println();
        System.out.print("Please select an option: ");

        String selectedOption = scanner.nextLine();
        String option = selectedOption.trim().toUpperCase();

        if (option.equals("M"))
        {
            return "money";
        }
        else if (option.equals("P"))
        {
            return "select";
        }
        else if (option.equals("F"))
        {
            return "finish";
        }
        else
        {
            return "";
        }

    }

    
}
