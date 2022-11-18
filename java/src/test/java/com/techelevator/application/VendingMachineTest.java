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
        BigDecimal expected = new BigDecimal("10");

        vendingMachine.addMoney("10");
        BigDecimal actual = vendingMachine.getMoney();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void addMoney_should_add_nothing_to_vending_machine_for_input_7() {
        BigDecimal expected = new BigDecimal("0");

        vendingMachine.addMoney("7");
        BigDecimal actual = vendingMachine.getMoney();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void addMoney_should_add_nothing_to_vending_machine_for_input_Hello() {
        BigDecimal expected = new BigDecimal("0");

        vendingMachine.addMoney("Hello");
        BigDecimal actual = vendingMachine.getMoney();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void getItemFromMachine_should_(){
        vendingMachine.readInputFile("catering1.csv");
        vendingMachine.getItemFromMachine();
    }



}
