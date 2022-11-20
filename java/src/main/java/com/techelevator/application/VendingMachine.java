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

    // Manu menu for the vending machine with its 3 options
    public void run()
    {
        // This makes sure all use of money has 2 decimal points no matter what
        this.money = this.money.setScale(2, BigDecimal.ROUND_HALF_UP);
        readInputFile("catering1.csv");

        while(true)
        {
            UserOutput.displayHomeScreen();
            // Grabs the user input and makes sure it is the correct input
            String choice = UserInput.getHomeScreenOption();

            if(choice.equals("display"))
            {
                // display the vending machine slots
                printItems();
            }
            else if(choice.equals("purchase"))
            {
                // make a purchase
                runSubMenu(); // Submenu with adding money or selecting item
            }
            else if(choice.equals("exit"))
            {
                System.out.println("** CREATING SALES REPORT **");
                UserOutput.clearSalesFile(); // Clears the sales report in order to add a new one
                printSalesReport(); // Prints the new sales report
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
            String finalStr = String.format("%-3s: %-18s - Costs: %s", entry.getKey().getItemCode(),
                    entry.getKey().getName(), entry.getKey().getPrice());
            System.out.print(finalStr);
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

    // Runs the sub menu which has the options for adding money to the vending machine,
    // selecting an item from the vending machine or exiting the sub menu
    public void runSubMenu() {
        int itemsPurchased = 0;
        while(true)
        {
            UserOutput.displaySubMenuScreen();
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

    // Adds money to the vending machine only if its in values of
    // 1, 5, 10 or 20
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

    // Calculates how much change you are given back in the lowest denominations
    // and sets the money in the vending machine back to 0
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

    // Method that buys an item from the vending machine
    // Tells you if you dont have enough money or if you chose an invalid code
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
                            entry.getKey().addToRegularPurchase();
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
                            break;
                        }
                        else {
                            System.out.println("YOU DON'T HAVE ENOUGH MONEY");
                            isFound = true;
                            break;
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
                            entry.getKey().addToDiscountedPurchase();

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
                            break;
                        }
                        else {
                            System.out.println("YOU DON'T HAVE ENOUGH MONEY");
                            isFound = true;
                            break;
                        }
                    }
                }
                else {
                    System.out.println("THERE ARE NO MORE AVAILABLE");
                    isFound = true;
                    break;
                }
            }

        }
        if (!isFound) {
            System.out.println("THE CODE WAS NOT FOUND");
        }
        return itemsPurchased;
    }

    // Prints a sales report of all the items that have been purchased
    public void printSalesReport() {
        BigDecimal total = new BigDecimal("0");
        UserOutput.printToSalesFile("       Taste Elevator Sales Report:");
        String top = String.format("| %-18s | %-10s | %-10s |", "Item", "Full Price", "Discounted");
        UserOutput.printToSalesFile(top);
        UserOutput.printToSalesFile("|--------------------|------------|------------|");
        for (Map.Entry<Item, Integer> entry: map.entrySet()) {
            BigDecimal fullPrice = entry.getKey().getPrice();
            BigDecimal discountedPrice = entry.getKey().getPrice().subtract(new BigDecimal("1"));
            total = total.add(fullPrice.multiply(new BigDecimal(entry.getKey().getRegularPurchase())));
            total = total.add(discountedPrice.multiply(new BigDecimal(entry.getKey().getDiscountedPurchase())));
            String str = String.format("| %-18s | %5d      | %5d      |", entry.getKey().getName(),
                    entry.getKey().getRegularPurchase(), entry.getKey().getDiscountedPurchase());
            UserOutput.printToSalesFile(str);
        }
        UserOutput.printToSalesFile("\nTotal Sales: $" + total);
    }


}
