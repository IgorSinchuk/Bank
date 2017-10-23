package com.igorsinchuk.bank.model;


import com.igorsinchuk.bank.enums.Currency;

import javax.persistence.*;
import java.sql.Date;

@Entity(name="exchange_rates")
public class ExchangeRate {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    //@Temporal(javax.persistence.TemporalType.DATE)
    @Column(nullable = false)
    private Date date = new Date(System.currentTimeMillis());;

    @Column(nullable = false)
    private Double USD;

    @Column(nullable = false)
    private Double EUR;

    @Column(nullable = false)
    private Double UAH;

    public ExchangeRate() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Double getUSD() {
        return USD;
    }

    public void setUSD(Double USD) {
        this.USD = USD;
    }

    public Double getEUR() {
        return EUR;
    }

    public void setEUR(Double EUR) {
        this.EUR = EUR;
    }

    public Double getUAH() {
        return UAH;
    }

    public void setUAH(Double UAH) {
        this.UAH = UAH;
    }

    public Double get(Currency currency) {
        switch (currency) {
            case EUR:
                return this.EUR;
            case UAH:
                return this.UAH;
            case USD:
                return this.USD;
            default:
                return null;
        }
    }
}
