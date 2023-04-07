package org.JavaPractice;

import java.util.UUID;

public class Product {
    private String productID;
    private String name;
    private double price;
    private double cost;
    private double weight;
    private int quantity;

    public Product(String name, double price, double cost, double weight, int quantity) {
        this.productID = UUID.randomUUID().toString();
        this.name = name;
        this.price = price;
        this.cost = cost;
        this.weight = weight;
        this.quantity = quantity;
    }

    public Product(String productID, String name, double price, double cost, double weight, int quantity) {
        this.productID = productID;
        this.name = name;
        this.price = price;
        this.cost = cost;
        this.weight = weight;
        this.quantity = quantity;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}