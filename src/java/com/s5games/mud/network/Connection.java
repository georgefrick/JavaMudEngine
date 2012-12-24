package com.s5games.mud.network;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import com.s5games.mud.io.InputHandler;
import com.s5games.mud.io.OutputHandler;
import com.s5games.mud.io.StreamUtil;
import com.s5games.mud.beans.Account;
import com.s5games.mud.state.ConnectionState;
import com.s5games.mud.state.LoginState;
/**
 * 
 * @author George Frick (george.frick@gmail.com)
 *
 */
public class Connection implements InputHandler, OutputHandler{

	private Socket socket;

	private BufferedReader bufferedReader;

	private BufferedWriter bufferedWriter;

	private ConnectionState _state;

	private Account account;
	private int passwordTries;

	private boolean flushed;
	private boolean forcePrompt;
    private boolean shouldClose; // We are done, close the connection.
    private String createAccount;

    private StringBuffer buffer;
	private String inline;

	private final static int INITIAL_BUFFER_SIZE = 250;

	public static final int MAX_PASS_TRIES = 3;

	public Connection(Socket sock) {
		socket = sock;
		flushed = true;
		forcePrompt = false;
        createAccount = null;
        buffer = new StringBuffer(INITIAL_BUFFER_SIZE);
		try {
			bufferedReader = new BufferedReader(new InputStreamReader(socket
					.getInputStream(),"ISO-8859-1"));
			bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket
					.getOutputStream()));
			account = null;			
			_state = LoginState.getInstance();
			_state.enterState(this);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String readLine() {
		try {
			if (bufferedReader.ready()) {
				inline = bufferedReader.readLine();
				if( inline != null ) {
					// Don't want any fuddy duddy telnet escape sequences.
				    inline = StreamUtil.stripEscape(inline.trim());
				}
				return inline;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public boolean isEmpty() {
		
		// Allows us to force a prompt to be displayed, without also displaying another prompt...
		if( forcePrompt) {
			forcePrompt = false;
			return false;
		}
		
		return flushed;
	}

	public void forcePrompt() {
		forcePrompt = true;
	}
	
	public void forceFlush() {
		flushed = false;
	}
	
	public void flush() {

		try {
			bufferedWriter.write(buffer.toString());
			buffer = new StringBuffer(INITIAL_BUFFER_SIZE);
			bufferedWriter.flush();
			flushed = true;
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	// TODO: code to take advantage of the buffering
	public void writeLine(String line) {
		// bufferedWriter.write(line);
		buffer.append(line);
		flushed = false;
	}

	public ConnectionState getConnectionState() {
		return _state;
	}

	public void setConnectionState(ConnectionState nextState) {
		_state = nextState;
	}

	public void close() {
		try {
			flush();
			bufferedWriter.close();
			bufferedReader.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public String getInputLine() {
		return readLine();
	}

	public void writeOutputLine(String s) {
		writeLine(s);
	}

	public void setPasswordTries(int passwordTries) {
		this.passwordTries = passwordTries;
	}

	public int getPasswordTries() {
		return passwordTries;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public Account getAccount() {
		return account;
	}

    public boolean isShouldClose() {
        return shouldClose;
    }

    public void setShouldClose(boolean shouldClose) {
        this.shouldClose = shouldClose;
    }

    public boolean isCreateAccount() {
        return (createAccount != null);
    }

    public void setCreateAccount(String createAccount) {
        this.createAccount = createAccount;
    }

    public String getAccountName() {
        return createAccount;
    }
}
