package org.JavaPractice;

import java.util.UUID;

public class User {
    private String uuid;
    private String name;
    private String email;
    private String password;
    private double balance;

    public User(String uuid, String name, String email, String password, double balance) {
        this.uuid = uuid;
        this.name = name;
        this.email = email;
        this.password = password;
        this.balance = balance;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}