package com.igorsinchuk.bank.model;

import com.igorsinchuk.bank.enums.Currency;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity(name="accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Currency currency;

    @Column(nullable = false)
    private Double amount;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @OneToMany(mappedBy = "fromAccount", cascade = CascadeType.ALL)
    List<Transaction> transactionsFrom = new ArrayList<>();

    @OneToMany(mappedBy = "toAccount", cascade = CascadeType.ALL)
    List<Transaction> transactionsTo = new ArrayList<>();

    public Account() {
    }

    public Account(Currency currency) {
        this.currency = currency;
        this.amount = 0.0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Transaction> getTransactionsFrom() {
        return Collections.unmodifiableList(transactionsFrom);
    }

    public void addTransactionsFrom(Transaction transactionFrom) {
        transactionFrom.setFromAccount(this);
        this.transactionsFrom.add(transactionFrom);
    }

    public List<Transaction> getTransactionsTo() {
        return Collections.unmodifiableList(transactionsTo);
    }

    public void addTransactionsTo(Transaction transactionTo) {
        transactionTo.setToAccount(this);
        this.transactionsTo.add(transactionTo);
    }

    public void putMoney(Double amount) {
        this.amount += amount;
    }

    public void withdrawMoney(Double amount) {
        this.amount -= amount;
    }
}
