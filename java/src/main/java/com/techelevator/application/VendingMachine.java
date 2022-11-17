package com.techelevator.application;

import com.techelevator.ui.UserInput;
import com.techelevator.ui.UserOutput;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.*;
import java.util.logging.FileHandler;

public class VendingMachine 
{
    private Map<Item,Integer> map = new HashMap<>(); // Map of items and their value is the quantity
    private final int MAX_QUANTITY = 6; // Maximum quantity per item
    private BigDecimal money = new BigDecimal("0");

    public BigDecimal getMoney() {
        return this.money;
    }

    public void run()
    {
        readInputFile();

        while(true)
        {
            UserOutput.displayHomeScreen();
            String choice = UserInput.getHomeScreenOption();

            if(choice.equals("display"))
            {
                // display the vending machine slots
                printItems();
            }
            else if(choice.equals("purchase"))
            {
                // make a purchase
                runSubMenu();
            }
            else if(choice.equals("exit"))
            {
                System.out.println("** TERMINATING PROGRAM **");
                break;
            }
        }
    }

    // Reads the input file to load our vending machine.
    public void readInputFile() {
        File file = new File("catering1.csv");
        try (Scanner fileReader = new Scanner(file)) {
            while (fileReader.hasNextLine()) {
                String line = fileReader.nextLine();
                String array[] = line.split(",");
                String code = array[0];
                String name = array[1];
                BigDecimal price = new BigDecimal(array[2]);
                String type = array[3];
                Item item = new Item(code, name, type, price);
                map.put(item, this.MAX_QUANTITY);
            }
        }
        catch (FileNotFoundException e) {
            System.err.println("File was not found.");
        }
    }

    // Prints all the items in the vending machine.
    public void printItems() {
        // Loops through the map and prints needed information.
        for (Map.Entry<Item,Integer> entry : map.entrySet()) {
            System.out.print(entry.getKey().getItemCode() + ": " + entry.getKey().getName()
                    + " costs " + entry.getKey().getPrice());
            // If quantity is 0, print no longer available, otherwise
            // print the remaining number of items.
            if (entry.getValue() == 0) {
                System.out.println(" NO LONGER AVAILABLE");
            }
            else {
                System.out.println(" Quantity: " + entry.getValue());
            }
        }
    }

    public void runSubMenu() {
        while(true)
        {
            UserOutput.displayHomeScreen();
            String choice = UserInput.getSubMenuOption(this);

            if(choice.equals("money"))
            {
                // feeds money to machine
                String money = UserInput.getUserMoney();
                if (money.equals(""))
                {
                    System.out.println("Invalid money amount");
                }
                else {
                    this.money = this.money.add(new BigDecimal(money));
                }
            }
            else if(choice.equals("select"))
            {
                // select item to purchase
            }
            else if(choice.equals("finish"))
            {
                System.out.println("* Back to Main Menu *");
                break;
            }
        }
    }




}
