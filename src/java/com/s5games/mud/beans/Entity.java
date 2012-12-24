package com.s5games.mud.beans;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.s5games.mud.event.Trigger;
/**
 * 
 * @author George Frick (george.frick@gmail.com)
 *
 */
public class Entity {

	private Long id;
	private long vnum;
	private String name;
	private String shortName;
	private String description;
	Set<String> tags;
	Map<String,String> properties;
	protected static Logger logger = Logger.getLogger("mudlog");
	Collection<Trigger> triggers;
	
	public Entity() {
		id = -1l;
		vnum = -1;
		name = "unset";
		shortName = "unset";
		description = "unset";
		tags = new HashSet<String>();
		properties = new HashMap<String,String>();
		triggers = new ArrayList<Trigger>();
	}
	
	public void addTrigger(Trigger trigger) {
		triggers.add(trigger);
	}
	
	public Collection<Trigger> getTriggers() {
		return triggers;
	}
	
	public String getName() {
		return name;
	}

	public Map<String, String> getProperties() {
		return properties;
	}
	
	public long getVnum() {
		return vnum;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}
	public void setVnum(long vnum) {
		this.vnum = vnum;
	}
	
	public String getProperty(String property) {
		if( hasProperty(property)) {
			return (String)properties.get(property);
		} else
			throw new IllegalArgumentException("Entity does not have property: " + property);		
	}
	
	public void setProperty(String property, String value) {
		properties.put(property, value);
	}
	
	public boolean hasProperty(String property) {
		return properties.containsKey(property);
	}
	
	public String getDescription() {
		return description;
	}
	public String getShortName() {
		return shortName;
	}
	public Set<String> getTags() {
		return tags;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	public void setTags(Set<String> tags) {
		this.tags = tags;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
}
