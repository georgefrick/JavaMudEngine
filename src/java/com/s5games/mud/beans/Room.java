package com.s5games.mud.beans;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Each room is located at
 * currX - current X location
 * currY - current Y location
 * currZ - current Z location
 * world - current world.
 * 
 * A room's vnum is simply a unique identifier (Database, etc)
 * What really distinguishes a room is its unique (x,y,z,w).
 * 
 * Each world has it's own coordinates, as you don't move between worlds via the ordinal movement commands.
 * 
 * Regarding Exits.
 * Each room has three exits, North, East, and Down. If you call getRoom(Direction.WEST) you are really looking
 * for the east exit of the room at (currX-1,currY,currZ)
 * @author George Frick
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class Room extends Entity {

	public enum Direction { NORTH, EAST, UP, SOUTH, WEST, DOWN }
	public final static int MAX_EXIT = 3;
	public Area parent; // A Room has to belong to an Area
	@XmlAttribute
	public int x;
	@XmlAttribute
	public int y;
	@XmlAttribute
	public int z;
	public Collection<Actor> people;
	public Collection<MudObject> contents;	
	public Map<Integer,Exit> exits;
	@XmlAttribute
	public boolean indoor; 

	public boolean isIndoor() {
		return indoor;
	}

	public void setIndoor(boolean indoor) {
		this.indoor = indoor;
	}

	public Room() {
		super();
		//exits = new Exit[MAX_EXIT];
		exits = new HashMap<Integer,Exit>();
		people = new ArrayList<Actor>();
		contents = new ArrayList<MudObject>();
	}

	public Map<Integer,Exit> getExits() {
		return exits;
	}
	
	public void setExits(Map<Integer,Exit> exits) {
		this.exits = exits;
	}
	
	public Area getParent() {
		return parent;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getZ() {
		return z;
	}

	public void setParent(Area parent) {
		this.parent = parent;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void setZ(int z) {
		this.z = z;
	}
	
	public Exit getExit(String dir) {
		return getExit(Direction.valueOf(dir.toUpperCase()));
	}
	
	public Exit getExit(Direction dir) {
		if( dir == Direction.SOUTH) {
			Room temp = parent.getRoom(getX(),getY()-1,getZ());
			if( temp != null) {
				return temp.getExit(Direction.NORTH);
			}
			return null;
		}
		if( dir == Direction.WEST) {
			Room temp = parent.getRoom(getX()-1,getY(),getZ());
			if( temp != null) {
				return temp.getExit(Direction.EAST);
			}
			return null;
		}
		if( dir == Direction.DOWN) {
			Room temp = parent.getRoom(getX(),getY(),getZ()-1);
			if( temp != null) {
				return temp.getExit(Direction.UP);
			}
			return null;
		}
		return exits.get(dir.ordinal());
	}

	public void setExit(Direction dir) {
		if( dir == Direction.SOUTH) {
			Room temp = parent.getRoom(getX(),getY()-1,getZ());
			if( temp != null) {
				temp.setExit(Direction.NORTH);
			}
			return;
		}
		if( dir == Direction.WEST) {
			Room temp = parent.getRoom(getX()-1,getY(),getZ());
			if( temp != null) {
				temp.setExit(Direction.EAST);
			}
			return;
		}
		if( dir == Direction.DOWN) {
			Room temp = parent.getRoom(getX(),getY(),getZ()-1);
			if( temp != null) {
				temp.setExit(Direction.UP);
			}
			return;
		}
		exits.put(dir.ordinal(), new Exit());
	}

	public void addActor(Actor actor) {
		people.add(actor);
	}
	
	public void removeActor(Actor actor) {
		people.remove(actor);
	}
	
	public void addMudObject(MudObject object) {
		contents.add(object);
	}
	
	public void removeMudObject(MudObject object) {
		contents.add(object);
	}
	 
	public long getNewX(String dir) {
		if( Direction.valueOf(dir.toUpperCase()) == Direction.EAST) {
			return getX()+1;
		} else if( Direction.valueOf(dir.toUpperCase()) == Direction.WEST) {
			return getX()-1;
		}
		return getX();
	}

	public long getNewY(String dir) {
		if( Direction.valueOf(dir.toUpperCase()) == Direction.NORTH) {
			return getY()+1;
		} else if( Direction.valueOf(dir.toUpperCase()) == Direction.SOUTH) {
			return getY()-1;
		}
		return getY();
	}
	
	public long getNewZ(String dir) {
		if( Direction.valueOf(dir.toUpperCase()) == Direction.UP) {
			return getZ()+1;
		} else if( Direction.valueOf(dir.toUpperCase()) == Direction.DOWN) {
			return getZ()-1;
		}
		return getZ();
	}

}
