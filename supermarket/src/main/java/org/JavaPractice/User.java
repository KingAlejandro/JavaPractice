package org.JavaPractice;

public class User {
    private String uuid;
    private String name;
    private String email;
    private double balance;
    private String userType;

    public User(String uuid, String name, String email, double balance, String userType) {
        this.uuid = uuid;
        this.name = name;
        this.email = email;
        this.balance = balance;
        this.userType = userType;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}