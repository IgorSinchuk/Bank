package com.igorsinchuk.bank.dao;

import com.igorsinchuk.bank.enums.Currency;
import com.igorsinchuk.bank.model.Account;
import com.igorsinchuk.bank.model.User;

import java.util.List;

public interface ServiceDao {

    List<User> getAllUsers();
    List<Account> getAccountsOfUser(User user);
    void refill(Double amountFrom, Currency currencyFrom, Account toAccount);
    void transfer(Double amount, Account fromAccount, Account toAccount);
    void convert(Double amountConvertFrom, Currency convertFrom, Account fromAccount, Account toAccount);
    Double getAllMoneyInUAH(User user);

}
