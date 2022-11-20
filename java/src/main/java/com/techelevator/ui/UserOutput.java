package com.techelevator.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.Map;


/**
 * Responsibilities: This class should handle formatting and displaying ALL
 * messages to the user
 * 
 * Dependencies: None
 */
public class UserOutput
{
    static File file = new File("Audit.txt");
    static File salesFile = new File("SalesReport.txt");

    // Clears the sales file so we can create a new list.
    public static void clearSalesFile() {
        try (PrintWriter writer = new PrintWriter(salesFile)) {
            writer.print("");
        }
        catch (FileNotFoundException e) {
            System.err.println("SalesFile.txt was not found");
        }
    }

    // Option to print to the audit file when something is done.
    public static void printToAuditFile(String something) {
        try (PrintWriter fileWriter = new PrintWriter(new FileOutputStream(file, true))) {
            fileWriter.println(something);
        }
        catch (FileNotFoundException e) {
            System.err.println("Audit.txt file was not found.");
        }
    }

    // Prints to the sales file when exiting vending machine
    public static void printToSalesFile(String something) {
        try (PrintWriter writer = new PrintWriter(new FileOutputStream(salesFile, true))) {
            writer.println(something);
        }
        catch (FileNotFoundException e) {
            System.err.println("SalesFile.txt file was not found.");
        }
    }

    // Never used this but I won't delete it.
    public static void displayMessage(String message)
    {
        System.out.println();
        System.out.println(message);
        System.out.println();
    }

    public static void displayHomeScreen()
    {
        System.out.println();
        System.out.println("***************************************************");
        System.out.println("                      Home");
        System.out.println("***************************************************");
        System.out.println();
    }

    public static void displaySubMenuScreen()
    {
        System.out.println();
        System.out.println("***************************************************");
        System.out.println("                   Sub Menu");
        System.out.println("***************************************************");
        System.out.println();
    }

}
