package com.s5games.mud.services;

import com.s5games.mud.beans.Account;

/**
 * User: george Frick
 * Date: Feb 10, 2008
 * Time: 5:50:09 PM
 */
public interface AccountService {

    public final static String SERVICE_NAME = "accountService";

    Account findAccount(String accountName);
}
