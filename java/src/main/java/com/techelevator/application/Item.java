package com.techelevator.application;

import java.math.BigDecimal;

public class Item {
    private String itemCode;
    private String name;
    private String type;
    private BigDecimal price;
    private int discountedPurchase = 0;
    private int regularPurchase = 0;

    public void addToRegularPurchase() {
        this.regularPurchase++;
    }

    public void addToDiscountedPurchase() {
        this.discountedPurchase++;
    }

    public int getDiscountedPurchase() {
        return discountedPurchase;
    }

    public int getRegularPurchase() {
        return regularPurchase;
    }

    public Item(String itemCode, String name, String type, BigDecimal price) {
        this.itemCode = itemCode;
        this.name = name;
        this.type = type;
        this.price = price;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

}
