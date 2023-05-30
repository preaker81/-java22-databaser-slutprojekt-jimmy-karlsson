package com.finalproject.jimmy.models;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Account {
    protected int id;
    protected String account_name;
    protected Timestamp created;
    protected int customer_id;
    protected int balance;
    protected String account_number;

    public Account(int id, String account_name, Timestamp created, int customer_id, int balance, String account_number) {
        this.id = id;
        this.account_name = account_name;
        this.created = created;
        this.customer_id = customer_id;
        this.balance = balance;
        this.account_number = account_number;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAccount_name() {
        return account_name;
    }

    public void setAccount_name(String account_name) {
        this.account_name = account_name;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public int getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public String getAccount_number() {
        return account_number;
    }

    public void setAccount_number(String account_number) {
        this.account_number = account_number;
    }
}
