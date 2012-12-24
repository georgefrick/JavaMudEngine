package com.s5games.mud.event;

import com.s5games.mud.beans.Entity;
/**
 * 
 * @author George Frick (george.frick@gmail.com)
 *
 */
public class Action {

	public enum ActionType { ATTEMPT, BEFORE, AFTER, TIMED, GENERIC }
	
	private Entity actor;
	private String action;
	private ActionType type;
	private String argument;
	boolean success;
	
	public Action(Entity actor, String action, ActionType type, String argument) {
		this.actor = actor;
		this.action = action;
		this.type = type;
		this.argument = argument;
		success = true;
	}
	
	public Action() {
		actor = new Entity();
		action = "none";
		type = ActionType.ATTEMPT;
		argument = "argument";
		success = true;
	}

	public void fail() {
		success = false;
	}
	
	public String getAction() {
		return action;
	}

	public Entity getActor() {
		return actor;
	}

	public boolean isSuccess() {
		return success;
	}

	public ActionType getType() {
		return type;
	}

	public String getArgument() {
		return argument;
	} 
	
}
