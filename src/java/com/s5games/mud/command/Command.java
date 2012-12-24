package com.s5games.mud.command;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.s5games.mud.Main;
import com.s5games.mud.beans.Actor;
/**
 * 
 * @author George Frick (george.frick@gmail.com)
 *
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class Command implements Comparable<Command> {

	@XmlAttribute
	Long vnum;
	@XmlAttribute
	String name;
	@XmlAttribute
	String usage;
	@XmlAttribute
	Long scriptVnum;
	Script script;
	@XmlAttribute
	String action;

	public String getAction() {
		return action;
	}

	public Long getVnum() {
		return vnum;
	}

	public String getName() {
		return name;
	}

	public Script getScript() {
		return script;
	}

	public String getUsage() {
		return usage;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public void setVnum(Long id) {
		this.vnum = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setScript(Script script) {
		this.script = script;
	}

	public void setUsage(String usage) {
		this.usage = usage;
	}

	public Long getScriptVnum() {
		return scriptVnum;
	}

	public void setScriptVnum(Long scriptVnum) {
		this.scriptVnum = scriptVnum;
	}

	public int compareTo(Command other) {
		return getName().compareTo(other.getName());
	}

	public void execute(Actor ch, String args) {
		Main.getInstance().executeCommand(ch, this, args);
	}

	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("Vnum: ");
		buf.append(getVnum());
		buf.append(":Name: ");
		buf.append(getName());
		buf.append(": with script: ");
		buf.append(getScript());
		return buf.toString();
	}
}
