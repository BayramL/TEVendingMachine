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

    public static void printToAuditFile(String something) {
        try (PrintWriter fileWriter = new PrintWriter(new FileOutputStream(file, true))) {
            fileWriter.println(something);
        }
        catch (FileNotFoundException e) {
            System.err.println("Audit.txt file was not found.");
        }
    }

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

}
