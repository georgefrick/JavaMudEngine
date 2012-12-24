package com.s5games.mud.command;

import javax.xml.bind.annotation.XmlRootElement;
/**
 * 
 * @author George Frick (george.frick@gmail.com)
 *
 */
@XmlRootElement
public class Script implements Comparable<Script> {

	private long vnum;
	private String description;
	private String script;
	
	public Script() {
		vnum = -1;
	}
	
	public String getDescription() {
		return description;
	}

	public String getScript() {
		return script;
	}

	public long getVnum() {
		return vnum;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setScript(String script) {
		this.script = script;
	}

	public void setVnum(long vnum) {
		this.vnum = vnum;
	}

	/**
	 * Sort scripts by vnum.
	 */
	public int compareTo(Script other) {
		Long long1 = new Long(this.getVnum());
		Long long2 = new Long(other.getVnum());
		return long1.compareTo(long2);
	}

	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("Vnum: " );
		buf.append(getVnum());
		buf.append(":Description: " );
		buf.append(getDescription());
		return buf.toString();
	}
}
