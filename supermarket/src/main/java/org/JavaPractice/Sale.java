package org.JavaPractice;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.Date;

public class Sale {
    private String saleID;
    private String userID;
    private String productID;
    private int quantitySold;
    private double priceAtSale;
    private double costAtSale;
    private LocalDateTime saleDate;

    public Sale(String userID, String productID, int quantitySold, double priceAtSale, double costAtSale, LocalDateTime saleDate) {
        this.saleID = UUID.randomUUID().toString();
        this.userID = userID;
        this.productID = productID;
        this.quantitySold = quantitySold;
        this.priceAtSale = priceAtSale;
        this.costAtSale = costAtSale;
        this.saleDate = saleDate;
    }

    public Sale(String saleID, String userID, String productID, int quantitySold, double priceAtSale, double costAtSale, LocalDateTime saleDate) {
        this.saleID = saleID;
        this.userID = userID;
        this.productID = productID;
        this.quantitySold = quantitySold;
        this.priceAtSale = priceAtSale;
        this.costAtSale = costAtSale;
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

    public double getCostAtSale() {
        return costAtSale;
    }

    public void setCostAtSale(double costAtSale) {
        this.costAtSale = costAtSale;
    }

    public LocalDateTime getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(LocalDateTime saleDate) {
        this.saleDate = saleDate;
    }
}

