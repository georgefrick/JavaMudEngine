package com.s5games.mud.beans;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * A world represents a collection of Areas that are linked under a set of scripts defining the behavior of the
 * world within the engine. As the engine defines scripts, a world can define a script to override those scripts
 * and substitute it's own behavior.
 * 
 * A world has a size, X,Y,Z in three dimensions. In defining this, you are defining a hollow spherical structure. The Z defines 
 * how deep the world is and you can't go any lower than that (negative) but you can go higher. If you move east at the maximum 
 * X, you will goto 0. The same for Y.
 * 
 * If a room within a world is not defined and is on the surface (z = 0), then that room is provided by the world.
 * @author George Frick
 *
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class World extends Entity {
	
	@XmlElement
	List<Area> areas;
	Area defaultArea;
	
	
	public World() {
		areas = new ArrayList<Area>();
		defaultArea = new Area();
	}
	
	/**
	 * Insert an area into a world. Will not work if it is already in a world, or the world already contains it.
	 * @param are Area to add.
	 */
	public void addArea(Area are) {
		if( are.getParent() != null) {
			logger.error("Could not add area " + are.getName() + " to world " + getName() + ", already part of " + are.getParent().getName());
			return;
		}
		
		are.setParent(this);

		if( areas.contains(are) ) { 
			logger.error("Could not add area " + are.getName() + " to world " + getName() + ", already there.");
			return;
		}
		logger.info("Added area " + are.getName() + " to World " + getName());
		areas.add(are);
	}

	public List<Area> getAreas() {
		return areas;
	}

	public void setAreas(List<Area> areas) {
		this.areas = areas;
	}
	
	public String toString() {
		return getName() + ": " + getShortName();
	}
	
}
