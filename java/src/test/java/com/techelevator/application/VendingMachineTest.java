package com.techelevator.application;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

public class VendingMachineTest {
    private VendingMachine vendingMachine;

    @Before
    public void setup() {
        vendingMachine = new VendingMachine();
    }

    @Test
    public void addMoney_should_add_10_to_vending_machine_for_input_10() {
        BigDecimal expected = new BigDecimal("10.00");

        vendingMachine.addMoney("10");
        BigDecimal actual = vendingMachine.getMoney();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void addMoney_should_add_nothing_to_vending_machine_for_input_7() {
        BigDecimal expected = new BigDecimal("0.00");

        vendingMachine.addMoney("7");
        BigDecimal actual = vendingMachine.getMoney();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void addMoney_should_add_nothing_to_vending_machine_for_input_Hello() {
        BigDecimal expected = new BigDecimal("0.00");

        vendingMachine.addMoney("Hello");
        BigDecimal actual = vendingMachine.getMoney();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void getItemFromMachine_should_subtract_correct_money_when_purchasing_one_item(){
        vendingMachine.readInputFile("catering1.csv");
        vendingMachine.addMoney("10");
        vendingMachine.getItemFromMachine("A1", 0);

        BigDecimal expected = new BigDecimal("8.35");
        BigDecimal actual = vendingMachine.getMoney();

        Assert.assertEquals(expected, actual);

    }

    @Test
    public void getItemFromMachine_should_do_nothing_when_purchasing_one_item_with_no_money(){
        vendingMachine.readInputFile("catering1.csv");
        vendingMachine.getItemFromMachine("A1", 0);

        BigDecimal expected = new BigDecimal("0.00");
        BigDecimal actual = vendingMachine.getMoney();

        Assert.assertEquals(expected, actual);

    }

    @Test
    public void getItemFromMachine_should_do_nothing_when_input_wrong_code(){
        vendingMachine.readInputFile("catering1.csv");
        vendingMachine.addMoney("10");
        vendingMachine.getItemFromMachine("Z1", 0);

        BigDecimal expected = new BigDecimal("10.00");
        BigDecimal actual = vendingMachine.getMoney();

        Assert.assertEquals(expected, actual);

    }

    @Test
    public void getItemFromMachine_should_take_BOGODO_discount() {
        vendingMachine.readInputFile("catering1.csv");
        vendingMachine.addMoney("20");
        vendingMachine.getItemFromMachine("C2", 0); // should cost 3.55
        vendingMachine.getItemFromMachine("C2", 1); // should cost 3.55 - 1
        // Should be 20 - (3.55 + 2.55) =  20 - 6.10 = 13.90
        BigDecimal expected = new BigDecimal("13.90");
        BigDecimal actual = vendingMachine.getMoney();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void getItemFromMachine_should_take_BOGODO_discount_for_only_one_item_when_purchasing_3() {
        vendingMachine.readInputFile("catering1.csv");
        vendingMachine.addMoney("20");
        vendingMachine.getItemFromMachine("C2", 0); // should cost 3.55
        vendingMachine.getItemFromMachine("C2", 1); // should cost 3.55 - 1
        vendingMachine.getItemFromMachine("C2", 2); // should cost 3.55
        // Should be 20 - (3.55 + 2.55 + 3.55) =  20 - 9.65 = 10.35
        BigDecimal expected = new BigDecimal("10.35");
        BigDecimal actual = vendingMachine.getMoney();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void getItemFromMachine_should_not_take_money_when_purchasing_same_item_7th_time(){
        vendingMachine.readInputFile("catering1.csv");
        vendingMachine.addMoney("20");
        vendingMachine.getItemFromMachine("A3", 0);
        vendingMachine.getItemFromMachine("A3", 1);
        vendingMachine.getItemFromMachine("A3", 2);
        vendingMachine.getItemFromMachine("A3", 3);
        vendingMachine.getItemFromMachine("A3", 4);
        vendingMachine.getItemFromMachine("A3", 5);
        vendingMachine.getItemFromMachine("A3", 6); // This should not take money from user

        BigDecimal expected = new BigDecimal("9.50");
        BigDecimal actual = vendingMachine.getMoney();

        Assert.assertEquals(expected, actual);

    }

    @Test
    public void calculateChange_sets_money_to_zero_when_executed() {
        vendingMachine.addMoney("20");
        vendingMachine.calculateChange();

        BigDecimal expected = new BigDecimal("0.00");
        BigDecimal actual = vendingMachine.getMoney();

        Assert.assertEquals(expected, actual);
    }





}
