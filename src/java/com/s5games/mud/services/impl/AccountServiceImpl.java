package com.s5games.mud.services.impl;

import java.util.Set;
import java.util.TreeSet;

import com.s5games.mud.services.AccountService;
import com.s5games.mud.beans.Account;
/**
 * 
 * @author George Frick (george.frick@gmail.com)
 *
 */
public class AccountServiceImpl implements AccountService {

    private Set<Account> accounts;
    
    public AccountServiceImpl() {
    	accounts = new TreeSet<Account>();
    	Account a = new Account();
    	a.setId(1L);
    	a.setPassword("password");
    	a.setUserName("tenchi");
    	accounts.add(a);
    }
    
    public Account findAccount(String accountName) {
    	for( Account acct : accounts ) {
    		if( acct.getUserName().equalsIgnoreCase(accountName)) {
    			return acct;
    		}
    	}
    	return null;
    }
}
