package com.igorsinchuk.bank.model;


import com.igorsinchuk.bank.enums.Currency;
import com.igorsinchuk.bank.enums.TransactionType;

import javax.persistence.*;
import java.sql.Date;

@Entity(name="transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    @ManyToOne
    @JoinColumn(name="from_account_id")
    private Account fromAccount;

    @ManyToOne
    @JoinColumn(name="to_account_id")
    private Account toAccount;

    @Column(name="transaction_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @Column(name="currency_from", nullable = false)
    @Enumerated(EnumType.STRING)
    private Currency currencyFrom;

    @Column(name="currency_to", nullable = false)
    @Enumerated(EnumType.STRING)
    private Currency currencyTo;

    @OneToOne
    @JoinColumn(name="exchange_rate_id")
    private ExchangeRate exchangeRate;

    @Column (name="ammount_from", nullable = false)
    private Double ammountFrom;

    @Column (name="ammount_to", nullable = false)
    private Double ammountTo;

    @Column (nullable = false)
    private Date date = new Date(System.currentTimeMillis());

    public Transaction() {
    }

    public Account getFromAccount() {
        return fromAccount;
    }

    public void setFromAccount(Account fromAccount) {
        this.fromAccount = fromAccount;
    }

    public Account getToAccount() {
        return toAccount;
    }

    public void setToAccount(Account toAccount) {
        this.toAccount = toAccount;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Currency getCurrencyFrom() {
        return currencyFrom;
    }

    public void setCurrencyFrom(Currency currencyFrom) {
        this.currencyFrom = currencyFrom;
    }

    public Currency getCurrencyTo() {
        return currencyTo;
    }

    public void setCurrencyTo(Currency currencyTo) {
        this.currencyTo = currencyTo;
    }

    public ExchangeRate getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(ExchangeRate exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public Double getAmmountFrom() {
        return ammountFrom;
    }

    public void setAmmountFrom(Double ammountFrom) {
        this.ammountFrom = ammountFrom;
    }

    public Double getAmmountTo() {
        return ammountTo;
    }

    public void setAmmountTo(Double ammountTo) {
        this.ammountTo = ammountTo;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
