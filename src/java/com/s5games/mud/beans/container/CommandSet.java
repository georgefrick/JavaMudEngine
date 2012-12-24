package com.s5games.mud.beans.container;


import java.util.Set;
import java.util.TreeSet;

import javax.xml.bind.annotation.XmlRootElement;

import com.s5games.mud.command.Command;
/**
 * 
 * @author George Frick (george.frick@gmail.com)
 *
 */
@XmlRootElement
public class CommandSet {

	private Set<Command> commands;
	
	public CommandSet() {
		commands = new TreeSet<Command>();
	}

	public Set<Command> getCommands() {
		return commands;
	}

	public void setCommands(Set<Command> commands) {
		this.commands = commands;
	}
	
}
