package com.s5games.mud.beans.container;

import java.util.Set;
import java.util.TreeSet;

import javax.xml.bind.annotation.XmlRootElement;

import com.s5games.mud.command.Script;
/**
 * 
 * @author George Frick (george.frick@gmail.com)
 *
 */
@XmlRootElement
public class ScriptSet {

	Set<Script> scripts;
	
	public ScriptSet() {
		scripts = new TreeSet<Script>();
	}

	public Set<Script> getScripts() {
		return scripts;
	}

	public void setScripts(Set<Script> scripts) {
		this.scripts = scripts;
	}
	
}
