package com.s5games.mud.state;

import java.util.ArrayList;
import java.util.Iterator;

import com.s5games.mud.Main;
import com.s5games.mud.network.Connection;
/**
 * 
 * @author George Frick (george.frick@gmail.com)
 *
 */
public class ChatState implements ConnectionState {

	private ArrayList<Connection> people;

	private static ChatState _chatState;

	private String prompt;

	private ChatState() {
		people = new ArrayList<Connection>();
		prompt = "> ";
	}

	public static ChatState getInstance() {
		if (_chatState == null) {
			_chatState = new ChatState();
		}

		return _chatState;
	}

	public void enterState(Connection conn) {
		echoToAll(conn.getAccount().getUserName() + " has entered the chat!\n\r");
		people.add(conn);
	}

	public ConnectionState getNextState(Connection conn, String input) {

		if (input == null )
			return this;
		
	    if( input.trim().length() == 0) {
			conn.writeLine(prompt);
			return this;
		}

		if (input.trim().equalsIgnoreCase("shutdown")) {
			echoToAll("Shutting down...");
			Main.getInstance().shutDown("Shutdown by user!");
			return this;
		}
		
		if (input.trim().equalsIgnoreCase("quit")) {
			echoToAll(conn.getAccount().getUserName() + " has quit.");
			return null;
		}
		
		if (input.trim().length() > 0) {
			echoToAllBut(conn, input.trim());
		}
		//conn.writeLine(prompt);
		conn.forceFlush();
		return this;
	}

	public void leaveState(Connection conn) {
		people.remove(conn);
		echoToAll(conn.getAccount().getUserName() + " has left the chat!\n\r");
	}

	private void echoToAll(String s) {
		Iterator<Connection> it = people.iterator();
		while (it.hasNext()) {
			it.next().writeLine("\n\r" + s);
		}
	}

	private void echoToAllBut(Connection conn, String s) {
		Iterator<Connection> it = people.iterator();
		Connection curr;
		while (it.hasNext()) {
			curr = it.next();
			if (curr == conn)
				continue;
			curr.writeLine("\n\r" + conn.getAccount().getUserName() + ": " + s + "\n\r");
		}
	}

	public void bustAPrompt(Connection conn) {
		conn.writeLine(prompt);
	}
}
