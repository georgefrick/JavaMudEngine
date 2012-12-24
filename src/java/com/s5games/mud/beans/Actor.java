package com.s5games.mud.beans;

import java.util.LinkedList;
import java.util.Queue;

import com.s5games.mud.command.Commandable;
/**
 * 
 * @author George Frick (george.frick@gmail.com)
 *
 */
public class Actor extends Entity implements Commandable {

	Queue<String> commands;
	StringBuffer output;
	Room location;
    private boolean active;
    private Account account;

    public Actor() {
        this("-NONAME-");
    }

    public Room getLocation() {
		return location;
	}

	public void setLocation(Room room) {
		this.location = room;
	}

	public Actor(String name) {
		super();
		setName(name);
		commands = new LinkedList<String>();
		output = new StringBuffer(256);
		location = null;
        active = true;
    }

	public void addCommand(String command) {
		// TODO Auto-generated method stub
		commands.add(command);
	}

	public String getOutput() {
		String temp = output.toString();
		return temp;
	}

	public String getPrompt() {
		// TODO Auto-generated method stub
		return getName() + "..> ";
	}
	
	public boolean hasCommand() {
		return ( commands.size() > 0);
	}
	
	public String getNextCommand() {
		return commands.remove();
	}

    /**
     * We don't want these buffers to fill up and start taking memory.
     * @param s
     */
    public void write(String s) {
        if( isNPC() )
            return;
        output.append(s);
	}

	public void writeln(String s) {
        if( isNPC() )
            return;
        
        output.append(s);
		output.append("\n\r");
	}
	
	public void resetOutput() {
		output.setLength(0);
	}

    public boolean active() {
        return active;
    }

    public void quit() {
        active = false;
    }

    public boolean isNPC() {
        return account == null;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;



        return false;
    }

    public int hashCode() {
        return (location != null ? location.hashCode() : 0);
    }
}
