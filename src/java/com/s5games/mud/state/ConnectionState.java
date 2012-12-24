package com.s5games.mud.state;

import com.s5games.mud.network.Connection;
/**
 * 
 * @author George Frick (george.frick@gmail.com)
 *
 */
public interface ConnectionState {

	public ConnectionState getNextState(Connection conn, String input);
	public void leaveState(Connection conn);
	public void enterState(Connection conn);
	public void bustAPrompt(Connection conn);
}
