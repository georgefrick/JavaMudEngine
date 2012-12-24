package com.s5games.mud.command;
/**
 * 
 * @author George Frick (george.frick@gmail.com)
 *
 */
public interface Commandable {

	public String getPrompt();
	public void addCommand(String command);
	public String getOutput();
	public void resetOutput();
    public boolean active();
	
}
