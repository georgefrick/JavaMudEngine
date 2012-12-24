package com.s5games.mud.event;

import com.s5games.mud.command.Script;

/**
 * A trigger is very similar to a command, but it executed by the trigger owner and not a character command,
 * so a trigger on a door is executed by the door. Triggers are executed by Entities. The specific script
 * must handle casting as needed to get a more specific object.
 * 
 * Will later probably want a trigger to have arguments so that we don't have to have the script check for them.
 * @author George Frick
 *
 */
public class Trigger {
	
	long vnum; //id
	Script script;
	String description; 
    String action;
    Action.ActionType type;
    
	public String getAction() {
		return action;
	}
	
	public String getDescription() {
		return description;
	}
	public Script getScript() {
		return script;
	}
	public long getVnum() {
		return vnum;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void setScript(Script script) {
		this.script = script;
	}
	public void setVnum(long vnum) {
		this.vnum = vnum;
	}
	public Action.ActionType getType() {
		return type;
	}
	public void setType(Action.ActionType type) {
		this.type = type;
	}
    
}
