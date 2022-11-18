package com.techelevator.application;

import com.techelevator.ui.UserInput;
import com.techelevator.ui.UserOutput;


import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


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
        this.money = this.money.setScale(2, BigDecimal.ROUND_HALF_UP);
        readInputFile("catering1.csv");

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
    public void readInputFile(String fileName) {
        File file = new File(fileName);
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
        int itemsPurchased = 0;
        while(true)
        {
            UserOutput.displayHomeScreen();
            String choice = UserInput.getSubMenuOption(this);

            if(choice.equals("money"))
            {
                // feeds money to machine
                String money = UserInput.getUserMoney();
                addMoney(money);
            }
            else if(choice.equals("select"))
            {
                // select item to purchase
                // If items purchased is odd, do the BOGODO deal.
                // Display all items for purchase.
                printItems();
                // Get the choice from the user
                String userChoice = UserInput.getCodeFromUser();
                // Changed this part to method for easy testing
                itemsPurchased = getItemFromMachine(userChoice, itemsPurchased);
                // Print to audit.txt file

            }
            else if(choice.equals("finish"))
            {
                calculateChange();
                break;
            }
        }
    }

    public void addMoney(String money) {
        this.money = this.money.setScale(2, BigDecimal.ROUND_HALF_UP);
        if (!money.equals("1") && !money.equals("5") && !money.equals("10") && !money.equals("20"))
        {
            System.out.println("Invalid money amount");
        }
        else {
            this.money = this.money.add(new BigDecimal(money));
            String auditOutput = "";
            String pattern = "d/M/u hh:mm:ss a";

            LocalDateTime nowTime = LocalDateTime.now();
            auditOutput += (nowTime.format(DateTimeFormatter.ofPattern(pattern)));

            //auditOutput += " MONEY FED:           ";
            //auditOutput += "$" + money + ".00" + " $" + this.getMoney();
            // This is how we did it before switching to string.format

            String formatted = String.format(" %-18s %7s.00 %7s", "MONEY FED", "$" + money, "$" + this.getMoney());
            UserOutput.printToAuditFile(auditOutput + formatted);
        }
    }

    public void calculateChange() {
        String moneyBeforeChange = this.money.toString();
        int dollars = 0, quarters = 0, dimes = 0, nickels = 0;
        System.out.println("* Back to Main Menu *");
        while (this.money.compareTo(new BigDecimal("0")) > 0) {
            if (this.money.compareTo(new BigDecimal("1")) >= 0) {
                dollars++;
                this.money = this.money.subtract(new BigDecimal("1"));
            }
            else if (this.money.compareTo(new BigDecimal("0.25")) >= 0) {
                quarters++;
                this.money = this.money.subtract(new BigDecimal("0.25"));
            }
            else if (this.money.compareTo(new BigDecimal("0.10")) >= 0) {
                dimes++;
                this.money = this.money.subtract(new BigDecimal("0.10"));
            }
            else if (this.money.compareTo(new BigDecimal("0.05")) >= 0) {
                nickels++;
                this.money = this.money.subtract(new BigDecimal("0.05"));
            }
        }
        String change = "";
        if (dollars > 0) {
            change += dollars + " Dollar(s) ";
        }
        if (quarters > 0) {
            change += quarters + " Quarter(s) ";
        }
        if (dimes > 0) {
            change += dimes + " Dime(s) ";
        }
        if (nickels > 0) {
            change += nickels + " Nickel(s) ";
        }
        System.out.println("Your change is: " + change);

        String auditOutput = "";
        String pattern = "d/M/u hh:mm:ss a";

        LocalDateTime nowTime = LocalDateTime.now();
        auditOutput += (nowTime.format(DateTimeFormatter.ofPattern(pattern)));

        //auditOutput += " CHANGE GIVEN:           ";
        //auditOutput += "$" + moneyBeforeChange + " $" + this.getMoney();
        // This is how we did it before switching to string.format

        String formatted = String.format(" %-21s %7s %7s", "CHANGE GIVEN:", "$" + moneyBeforeChange, "$" + this.getMoney());


        UserOutput.printToAuditFile(auditOutput + formatted);

    }

    public int getItemFromMachine(String userChoice, int itemsPurchased) {
        this.money = this.money.setScale(2, BigDecimal.ROUND_HALF_UP);
        // Loop through map.
        boolean isFound = false;
        for (Map.Entry<Item, Integer> entry : map.entrySet()) {
            if (userChoice.equalsIgnoreCase(entry.getKey().getItemCode())) {
                if (entry.getValue() > 0) {
                    if (itemsPurchased % 2 == 0) {
                        // NO BOGODO deal
                        BigDecimal price = entry.getKey().getPrice(); // Creating variables for more clarity
                        if (this.money.compareTo(price) >= 0) {
                            map.put(entry.getKey(), entry.getValue() - 1);
                            String moneyBeforeTransaction = this.money.toString();
                            this.money = this.money.subtract(entry.getKey().getPrice());
                            isFound = true;
                            itemsPurchased++;

                            String auditOutput = "";
                            String pattern = "d/M/u hh:mm:ss a";
                            LocalDateTime nowTime = LocalDateTime.now();
                            auditOutput += (nowTime.format(DateTimeFormatter.ofPattern(pattern)));
                            String formatted = String.format(" %-18s %s %7s %7s", entry.getKey().getName()
                                    , entry.getKey().getItemCode(), "$" + moneyBeforeTransaction, "$" + this.money);
                            UserOutput.printToAuditFile(auditOutput + formatted);


                            if (entry.getKey().getType().equals("Gum")) {
                                System.out.println("Chewy, Chewy, Lots O Bubbles!");
                            }
                            else if (entry.getKey().getType().equals("Drink")) {
                                System.out.println("Drinky, Drinky, Slurp Slurp!");
                            }
                            else if (entry.getKey().getType().equals("Candy")) {
                                System.out.println("Sugar, Sugar, so Sweet!");
                            }
                            else if (entry.getKey().getType().equals("Munchy")) {
                                System.out.println("Munchy, Munchy, so Good!");
                            }
                        }
                        else {
                            System.out.println("YOU DON'T HAVE ENOUGH MONEY");
                        }
                    }
                    else {
                        // YES BOGODO deal
                        BigDecimal discount = new BigDecimal("1"); // Creating variables for more clarity
                        BigDecimal price = entry.getKey().getPrice(); // Creating variables for more clarity
                        if (this.money.compareTo(price.subtract(discount)) >= 0) {
                            map.put(entry.getKey(), entry.getValue() - 1);
                            String moneyBeforeTransaction = this.money.toString();
                            this.money = this.money.subtract(entry.getKey().getPrice().subtract(discount));

                            String auditOutput = "";
                            String pattern = "d/M/u hh:mm:ss a";
                            LocalDateTime nowTime = LocalDateTime.now();
                            auditOutput += (nowTime.format(DateTimeFormatter.ofPattern(pattern)));
                            String formatted = String.format(" %-18s %s %7s %7s", entry.getKey().getName()
                                    , entry.getKey().getItemCode(), "$" + moneyBeforeTransaction, "$" + this.money);
                            UserOutput.printToAuditFile(auditOutput + formatted);

                            isFound = true;
                            itemsPurchased++;

                            if (entry.getKey().getType().equals("Gum")) {
                                System.out.println("Chewy, Chewy, Lots O Bubbles!");
                            }
                            else if (entry.getKey().getType().equals("Drink")) {
                                System.out.println("Drinky, Drinky, Slurp Slurp!");
                            }
                            else if (entry.getKey().getType().equals("Candy")) {
                                System.out.println("Sugar, Sugar, so Sweet!");
                            }
                            else if (entry.getKey().getType().equals("Munchy")) {
                                System.out.println("Munchy, Munchy, so Good!");
                            }
                        }
                        else {
                            System.out.println("YOU DON'T HAVE ENOUGH MONEY");
                        }
                    }
                }
                else {
                    System.out.println("THERE ARE NO MORE AVAILABLE");
                }
            }

        }
        if (!isFound) {
            System.out.println("THE CODE WAS NOT FOUND");
        }
        return itemsPurchased;
    }


}
