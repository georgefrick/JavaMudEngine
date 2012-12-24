package com.s5games.mud.state;

import com.s5games.mud.network.Connection;

/**
 * User: george frick
 * Date: Feb 21, 2008
 * Time: 11:06:21 PM
 */
public class NewAccountState implements ConnectionState {

    private String accountName;
    private String password1;

    public NewAccountState(String name) {
        accountName = name;
        password1 = null;
    }
    
    public ConnectionState getNextState(Connection conn, String input) {
        if (input == null)
			return this;

		if (input.trim().length() == 0) {
			conn.forcePrompt();
			return this;
		}

        if( password1 == null) {
            password1 = input.trim();
            conn.forcePrompt();
            return this;
        } else {
            if( password1.equals(input.trim())) {

                conn.writeLine("You first character is the same name as your account. You can delete this char later.\n\r");
                
            } else {
                conn.writeLine("Passwords don't match.\n\r");
                password1 = null;
                conn.forcePrompt();
                return this;
            }
        }
        return this;
    }

    public void leaveState(Connection conn) {
//To change body of implemented methods use File | Settings | File Templates.
    }

    public void enterState(Connection conn) {
        conn.writeLine("You are creating a new account, " + accountName + "\n\r");
        //bustAPrompt(conn);
    }

    public void bustAPrompt(Connection conn) {
        if( password1 == null) {
            conn.writeLine("Enter a password for new the new account: ");
        } else {
            conn.writeLine("Please enter the password again to confirm: ");
        }
    }
}
