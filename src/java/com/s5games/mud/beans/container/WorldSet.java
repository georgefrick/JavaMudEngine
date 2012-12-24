package com.s5games.mud.beans.container;

import java.util.Set;
import java.util.TreeSet;

import javax.xml.bind.annotation.XmlRootElement;

import com.s5games.mud.beans.World;
/**
 * 
 * @author George Frick (george.frick@gmail.com)
 *
 */
@XmlRootElement
public class WorldSet {

	private Set<World> worlds;
	
	public WorldSet() {
		worlds = new TreeSet<World>();
	}

	public Set<World> getWorlds() {
		return worlds;
	}

	public void setWorlds(Set<World> worlds) {
		this.worlds = worlds;
	}
	
	
}
