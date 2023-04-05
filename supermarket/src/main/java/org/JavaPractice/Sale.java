package org.JavaPractice;

import java.util.UUID;
import java.util.Date;

public class Sale {
    private String saleID;
    private String userID;
    private String productID;
    private int quantitySold;
    private double priceAtSale;
    private Date saleDate;

    public Sale(String userID, String productID, int quantitySold, double priceAtSale, Date saleDate) {
        this.saleID = UUID.randomUUID().toString();
        this.userID = userID;
        this.productID = productID;
        this.quantitySold = quantitySold;
        this.priceAtSale = priceAtSale;
        this.saleDate = saleDate;
    }

    public Sale(String saleID, String userID, String productID, int quantitySold, double priceAtSale, Date saleDate) {
        this.saleID = saleID;
        this.userID = userID;
        this.productID = productID;
        this.quantitySold = quantitySold;
        this.priceAtSale = priceAtSale;
        this.saleDate = saleDate;
    }

    public String getSaleID() {
        return saleID;
    }

    public void setSaleID(String saleID) {
        this.saleID = saleID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public int getQuantitySold() {
        return quantitySold;
    }

    public void setQuantitySold(int quantitySold) {
        this.quantitySold = quantitySold;
    }

    public double getPriceAtSale() {
        return priceAtSale;
    }

    public void setPriceAtSale(double priceAtSale) {
        this.priceAtSale = priceAtSale;
    }

    public Date getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(Date saleDate) {
        this.saleDate = saleDate;
    }
}

