package com.s5games.mud.beans;

/**
 * 
 * @author George Frick (george.frick@gmail.com)
 *
 */
public class Exit extends Entity {

	private boolean door;
	private boolean closed;
	private int direction;

	public boolean isClosed() {
		return closed;
	}
	public boolean isDoor() {
		return door;
	}
	public void setClosed(boolean closed) {
		this.closed = closed;
	}
	public void setDoor(boolean hasDoor) {
		this.door = hasDoor;
	}
	public int getDirection() {
		return direction;
	}
	public void setDirection(int direction) {
		this.direction = direction;
	}
	
}
