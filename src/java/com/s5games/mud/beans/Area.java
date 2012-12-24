package com.s5games.mud.beans;

import java.util.ArrayList;

import java.util.Collection;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.s5games.mud.beans.Entity;
import com.s5games.mud.beans.Room;
import com.s5games.mud.beans.World;

/**
 * 
 * @author George Frick (george.frick@gmail.com)
 *
 */
@XmlRootElement(namespace = "com.s5games.mud.beans")
@XmlAccessorType(XmlAccessType.NONE)
public class Area extends Entity {
	
	@XmlElement
	private Collection<Room> rooms;
	
	private World parent; // Every area is part of a world.
	
	public Area() {
		rooms = new ArrayList<Room>();
	}
	
	
	public World getParent() {
		return parent;
	}


	public void setParent(World parent) {
		this.parent = parent;
	}


	/**
	 * Return a room at a given coordinate for this Area.
	 * @param x - current X
	 * @param y - current Y
	 * @param z - current Z
	 * @return valid Room(x,y,z) or null
	 */
	public Room getRoom(int x, int y, int z) {
		for( Room room : rooms ) {
			if( room.getX() != x)
				continue;
			if( room.getY() != y)
				continue;
			if( room.getZ() != z)
				continue;
			return room;
		}
		// TODO: Log room not found.
		return null;
	}

	public Room getRoom(long vnum) {
		for( Room room : rooms) {
			if( room.getVnum() == vnum)
				return room;
		}
		return null;
	}
	
	/**
	 * Inserts a room into an area, will not work if the room is already present or already in an area.
	 * @param room to insert.
	 */
	public void addRoom(Room room) {
		if( room.getParent() != null ) {
			logger.error("Could not add room " + room.getName() + " to area " + getName() + ", room is already in " + room.getParent().getName());
			return;
		}
		room.setParent(this);
		if( rooms.contains(room) ) {
			logger.error("Could not add room " + room.getName() + " to area " + getName() + ", already there.");
			return;
		}
		logger.info("Added Room " + room.getName() + " to Area " + getName()); 
		rooms.add(room);
	}


	public Collection<Room> getRooms() {
		return rooms;
	}


	public void setRooms(Collection<Room> rooms) {
		this.rooms = rooms;
	}
	
	public String toString() {
		return getName() + ": " + getShortName();
	}
}
