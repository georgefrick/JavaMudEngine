package com.s5games.mud.services.impl;

import java.io.File;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

import com.s5games.mud.beans.Area;
import com.s5games.mud.beans.Room;
import com.s5games.mud.beans.World;
import com.s5games.mud.beans.Actor;
import com.s5games.mud.beans.container.CommandSet;
import com.s5games.mud.beans.container.ScriptSet;
import com.s5games.mud.beans.container.WorldSet;
import com.s5games.mud.services.WorldService;
import com.s5games.mud.command.Command;
import com.s5games.mud.command.Script;
import com.s5games.mud.io.XmlIO;
/**
 * 
 * @author George Frick (george.frick@gmail.com)
 *
 */
public class WorldServiceImpl implements WorldService {

	private Set<World> worlds;
	private Set<Command> commands;
	private Set<Script> scripts;
	private Set<Actor> actors;
	
	public WorldServiceImpl() {
		worlds = new TreeSet<World>();
		commands = new TreeSet<Command>();
		scripts = new TreeSet<Script>();
		actors = new TreeSet<Actor>();
	}
	
    public Collection<World> getAllWorlds() {
    	return worlds;
    }

    public Collection<Command> getAllCommands() {
    	return commands;
    }

    public Collection<Script> getAllScripts() {
        return scripts;
    }

    public Script findScript(Long vnum) {
        for( Script script : scripts ) {
        	if( script.getVnum() == vnum ) {
        		return script;
        	}
        }
        return null;
    }

    public Command findCommand(Long vnum) {
    	for( Command comm : commands ) {
    		if( comm.getVnum() == vnum ) {
    			return comm;
    		}
    	}
        return null;
    }

    public Actor findActor(Long vnum) {
    	for( Actor actor : actors ) {
    		if( actor.getVnum() == vnum ) {
    			return actor;
    		}
    	}
        return null;
    }
    

    public void load() {
    	WorldSet wset = XmlIO.readWorlds(new File("worlds.xml"));
    	worlds = wset.getWorlds();
    	ScriptSet set = XmlIO.readScripts(new File("scripts.xml"));
    	scripts = set.getScripts();    	
    	CommandSet cset = XmlIO.readCommands(new File("commands.xml"));
    	commands = cset.getCommands();
    	for( Command c : commands ) {
    		for( Script s : scripts ) {
    			if( s.getVnum() == c.getScriptVnum() ) {
    				c.setScript(s);
    			}
    		}
    	}
    	for( World world : worlds ) {
    		for( Area area : world.getAreas() ) {
    			for( Room room : area.getRooms() ) {
    				room.setParent(area);
    			}
    		}
    	}
    }

}
