package com.igorsinchuk.bank;

import com.igorsinchuk.bank.controller.Manager;
import com.igorsinchuk.bank.dao.hibernate.HibernateServiceDao;
import com.igorsinchuk.bank.enums.Currency;
import com.igorsinchuk.bank.model.User;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        HibernateServiceDao hsd = new HibernateServiceDao(Manager.getEntityManager());

        hsd.putTestDataIntoTables();
        List<User> users = hsd.getAllUsers();

        for(User user: users) {
            hsd.refill(10000d, Currency.UAH, user.getAccounts().get(0));
        }

        hsd.convert(5000d, Currency.UAH, users.get(0).getAccount(Currency.UAH), users.get(0).getAccount(Currency.USD));
        hsd.transfer(10d, users.get(0).getAccount(Currency.USD), users.get(1).getAccount(Currency.USD));
        hsd.convert(5000d, Currency.UAH, users.get(0).getAccount(Currency.UAH), users.get(0).getAccount(Currency.EUR));

        hsd.getAllMoneyInUAH(users.get(0));
        hsd.getAllMoneyInUAH(users.get(1));
        hsd.getAllMoneyInUAH(users.get(2));

        Manager.closeEntityManager();
    }
}
