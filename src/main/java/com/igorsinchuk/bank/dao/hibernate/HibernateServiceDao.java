package com.igorsinchuk.bank.dao.hibernate;

import com.igorsinchuk.bank.dao.ServiceDao;
import com.igorsinchuk.bank.enums.Currency;
import com.igorsinchuk.bank.enums.TransactionType;
import com.igorsinchuk.bank.model.Account;
import com.igorsinchuk.bank.model.ExchangeRate;
import com.igorsinchuk.bank.model.Transaction;
import com.igorsinchuk.bank.model.User;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class HibernateServiceDao implements ServiceDao{
    private static EntityManager em;

    public HibernateServiceDao(EntityManager em) {
        this.em = em;
    }

    public void putTestDataIntoTables() {
        User user1 = new User("Name of User-1");
        User user2 = new User("Name of User-2");
        User user3 = new User("Name of User-3");
        user1.addAccount(new Account(Currency.UAH));
        user1.addAccount(new Account(Currency.USD));
        user1.addAccount(new Account(Currency.EUR));
        user2.addAccount(new Account(Currency.UAH));
        user2.addAccount(new Account(Currency.USD));
        user3.addAccount(new Account(Currency.UAH));

        ExchangeRate exchangeRate = new ExchangeRate();
        exchangeRate.setEUR(29d);
        exchangeRate.setUSD(26d);
        exchangeRate.setUAH(1d);

        em.getTransaction().begin();
        try {
            em.persist(user1);
            em.persist(user2);
            em.persist(user3);
            em.persist(exchangeRate);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
            throw new RuntimeException("Exception in putTestDataIntoTables() method ");
        }
    }

    @Override
    public List<User> getAllUsers() {
        return em.createQuery("select u from users u", User.class).getResultList();
    }

    @Override
    public List<Account> getAccountsOfUser(User user) {
        Query query = em.createQuery("select a from accounts a where a.user_id=:user_id", Account.class);
        query.setParameter("user_id", user.getId());
        return query.getResultList();
    }

    @Override
    public void refill(Double amountFrom, Currency currencyFrom, Account toAccount) {
        Transaction transaction = new Transaction();
        transaction.setTransactionType(TransactionType.REFILL);
        transaction.setAmmountFrom(amountFrom);
        transaction.setCurrencyFrom(currencyFrom);
        transaction.setToAccount(toAccount);
        Currency currencyTo = toAccount.getCurrency();
        transaction.setCurrencyTo(currencyTo);
        if(currencyTo == currencyFrom) {
            transaction.setAmmountTo(amountFrom);
            toAccount.putMoney(amountFrom);
        } else {
            ExchangeRate exchangeRate = getTodayExchangeRate();
            transaction.setExchangeRate(exchangeRate);
            Double amountTo = amountFrom*exchangeRate.get(currencyFrom)/exchangeRate.get(currencyTo);
            transaction.setAmmountTo(amountTo);
            toAccount.putMoney(amountTo );
        }
        em.getTransaction().begin();
        try {
            em.persist(transaction);
            em.persist(toAccount);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
            throw new RuntimeException("Exception in refill() method");
        }
    }

    @Override
    public void transfer(Double amount, Account fromAccount, Account toAccount) {
        if(fromAccount.getCurrency() != toAccount.getCurrency()) {
            throw new UnsupportedOperationException();
        }

        Transaction transaction = new Transaction();
        transaction.setTransactionType(TransactionType.TRANSFER);
        transaction.setFromAccount(fromAccount);
        transaction.setToAccount(toAccount);
        transaction.setCurrencyFrom(fromAccount.getCurrency());
        transaction.setCurrencyTo(toAccount.getCurrency());
        transaction.setAmmountFrom(amount);
        transaction.setAmmountTo(amount);
        fromAccount.withdrawMoney(amount);
        toAccount.putMoney(amount);
        em.getTransaction().begin();
        try {
            em.persist(transaction);
            em.persist(fromAccount);
            em.persist(toAccount);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
            throw new RuntimeException("Exception in transfer() method");
        }
    }

    @Override
    public void convert(Double amountConvertFrom, Currency convertFrom, Account fromAccount, Account toAccount) {
        Currency convertTo = toAccount.getCurrency();
        if(fromAccount.getUser().getId() != fromAccount.getUser().getId()
                || fromAccount.getCurrency() != convertFrom || convertFrom == convertTo) {
            throw new IllegalArgumentException();
        }
        Transaction transaction = new Transaction();
        transaction.setTransactionType(TransactionType.CONVERS);
        transaction.setFromAccount(fromAccount);
        transaction.setToAccount(toAccount);
        transaction.setCurrencyFrom(convertFrom);
        transaction.setCurrencyTo(convertTo);
        ExchangeRate exchangeRate = getTodayExchangeRate();
        transaction.setExchangeRate(exchangeRate);
        Double amountConvertTo = amountConvertFrom*exchangeRate.get(convertFrom)/exchangeRate.get(convertTo);
        transaction.setAmmountFrom(amountConvertFrom);
        transaction.setAmmountTo(amountConvertTo);
        fromAccount.withdrawMoney(amountConvertFrom);
        toAccount.putMoney(amountConvertTo);
        em.getTransaction().begin();
        try {
            em.persist(transaction);
            em.persist(fromAccount);
            em.persist(toAccount);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
            throw new RuntimeException("Exception in convert() method");
        }
    }

    @Override
    public Double getAllMoneyInUAH(User user) {
        Double total = 0d;
        ExchangeRate exchangeRate = getTodayExchangeRate();
        List<Account> accounts = user.getAccounts();
        for(Account account : accounts) {
            Currency currency = account.getCurrency();
            total += account.getAmount() * exchangeRate.get(currency);
        }
        System.out.println("On all accounts of " + user.getName() + "'s there is " + total + " UAH");
        return total;
    }

    public ExchangeRate getTodayExchangeRate() {
        Query query = em.createQuery("select er from exchange_rates er where er.date=:date", ExchangeRate.class);
        query.setParameter("date", new java.util.Date(System.currentTimeMillis()));
        ExchangeRate exchangeRate = (ExchangeRate) query.getSingleResult();
        return exchangeRate;
    }

}
