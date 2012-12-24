package com.s5games.mud.state;

import com.s5games.mud.Main;
import com.s5games.mud.beans.Account;
import com.s5games.mud.network.Connection;
import com.s5games.mud.services.AccountService;
import com.s5games.mud.services.impl.AccountServiceImpl;

import org.apache.log4j.Logger;
/**
 * 
 * @author George Frick (george.frick@gmail.com)
 *
 */
public class LoginState implements ConnectionState {

	protected static Logger logger = Logger.getLogger("mudlog");
	
	private static String namePrompt = "name: ";

	private static String passPrompt = "password: \033[8m";

    private static String newPrompt = "User doesn't exist. Do You want to create? (Yes/No): ";

    private static LoginState _loginState;

    private AccountService accountService;

    private LoginState() {
    	accountService = new AccountServiceImpl();
    }

	public static LoginState getInstance() {
		if (_loginState == null) {
			_loginState = new LoginState();
		}

		return _loginState;
	}

	public void enterState(Connection conn) {		
		conn.writeLine("Welcome to the Cynthe Game Engine, please report all bugs. (george.frick@gmail.com)\n\r");
	}

	public ConnectionState getNextState(Connection conn, String input) {
		if (input == null)
			return this;
		
		if (input.trim().length() == 0) {
			conn.forcePrompt();
			return this;
		}
				
		Account account = conn.getAccount();

		// Username.
		if (account == null) {
            if( conn.isCreateAccount() ) {
                if(input.trim().toLowerCase().startsWith("y")){

                    ConnectionState temp = new NewAccountState(conn.getAccountName());
			        leaveState(conn);
			        temp.enterState(conn);
                    return temp;
                } else {
                    conn.setCreateAccount(null);
                    conn.writeLine(namePrompt);
                    conn.flush();
                    return this;
                }
            }
            try {
				// TODO: Check for user already logged in.
            	logger.error("Checking or user [" + input.trim() + "]");
				account = accountService.findAccount(input.trim());
				if (account == null) {
					conn.writeLine(newPrompt);
                    conn.flush();
                    conn.setCreateAccount(input.trim());
                    return this;
				}
				conn.setAccount(account);
                conn.writeLine(passPrompt);
				conn.flush();
				return this;
			} catch (Exception e) {
				conn.writeLine("User doesn't exist.\n\r");
				return this;
			}
		} else {
			// Password
            conn.writeLine("\033[0m");
            if (input.trim().compareTo(account.getPassword()) != 0) {
				conn.writeLine("wrong password.\n\r");
				conn.setPasswordTries(conn.getPasswordTries() + 1);
				if (conn.getPasswordTries() > Connection.MAX_PASS_TRIES) {
					return null;
				}
				return this;
			}
			if (Main.getInstance().isLogged(conn)) {
				conn.writeLine("User already logged in.\n\r");
				return null;
			}
			ConnectionState temp = PlayState.getInstance();
			leaveState(conn);
			temp.enterState(conn);
			return temp;
		}
		// TODO: More work here, this is the first login?
	}

	public void leaveState(Connection conn) {
		conn.writeLine("Welcome!\n\r");
	}

	public void bustAPrompt(Connection conn) {
		if (conn.getAccount() != null) {
            conn.writeLine(passPrompt);
		} else {
            if( conn.isCreateAccount())
                conn.writeLine(newPrompt);
            else
                conn.writeLine(namePrompt);
		}
	}
}
