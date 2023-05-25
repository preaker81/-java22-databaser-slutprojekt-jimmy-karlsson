package com.finalproject.jimmy.models;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class transaction {
protected int id;
protected int sender;
protected int reciever;
protected BigDecimal amount;
protected Timestamp created;
protected String message;

    public transaction(int id, int sender, int reciever, BigDecimal amount, Timestamp created, String message) {
        this.id = id;
        this.sender = sender;
        this.reciever = reciever;
        this.amount = amount;
        this.created = created;
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSender() {
        return sender;
    }

    public void setSender(int sender) {
        this.sender = sender;
    }

    public int getReciever() {
        return reciever;
    }

    public void setReciever(int reciever) {
        this.reciever = reciever;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
