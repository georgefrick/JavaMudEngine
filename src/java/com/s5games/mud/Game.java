package com.s5games.mud;

import com.s5games.mud.beans.Actor;
import com.s5games.mud.beans.Area;
import com.s5games.mud.beans.Room;
import com.s5games.mud.beans.World;
import com.s5games.mud.command.Command;
import com.s5games.mud.command.Script;
import com.s5games.mud.event.Action;
import com.s5games.mud.event.TickAction;
import com.s5games.mud.event.Trigger;
import com.s5games.mud.services.WorldService;
import com.s5games.mud.services.impl.WorldServiceImpl;

import org.apache.commons.validator.GenericValidator;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * 
 * @author George Frick (george.frick@gmail.com)
 *
 */
public class Game {

	private static final int VNUM_ALL = -1;
	private static final int VNUM_DEFAULT = 1;
	Collection<World> worlds;
	SortedSet<Command> commands;
	SortedSet<Script> scripts;
    Collection<TickAction> actions;
    private static Game _instance;
	Collection<Actor> players; // logged in players.
	World defaultWorld;
	static Logger logger = Logger.getLogger("mudlog");
    private long timer;
    private long tracker;
    private long tcount;
    public final static long SECOND = 1000000000;
    public final static long TICK = SECOND / 10; // 10 ticks per second.
    public final static long FIGHT_ROUND = TICK * 5;

    WorldService worldService;

    /**
	 * Private Game constructor, creates a game and initializes all of the main arrays. 
	 * Loads the worlds, scripts, and commands.
	 *
	 */
	private Game() {		

        // Get our world service, this is the portal to all of the data.
        worldService = new WorldServiceImpl();
        worldService.load();
        
        players = new ArrayList<Actor>();
        actions = new ArrayList<TickAction>();
        commands = new TreeSet<Command>();
        scripts = new TreeSet<Script>();

        worlds = worldService.getAllWorlds();
        commands.addAll(worldService.getAllCommands());
        scripts.addAll(worldService.getAllScripts());
        
        logger.error("Loaded " + worlds.size() + " worlds.");
        logger.error("Loaded " + commands.size() + " commands.");
        logger.error("Loaded " + scripts.size() + " scripts");

        defaultWorld = getWorldByVnum(VNUM_DEFAULT);

		if( defaultWorld == null) { 
			logger.error("Could not startup. A world with vnum " + VNUM_DEFAULT + " must be present.");
			Main.getInstance().shutDown("Boot Failure, no default world.");
		} else {
            logger.error("Found default world.");
        }
//		
//		Trigger trig = new Trigger();
//		trig.setAction("look");
//		trig.setVnum(1);
//		trig.setDescription("When a character tests");
//		trig.setType(Action.ActionType.ATTEMPT);
//		ScriptDao scriptDao = new ScriptDao();
//		try {
//		trig.setScript(scriptDao.get(10));
//		room2.addTrigger(trig);
//		} catch (Exception ex) {
//			logger.info("failed to add trigger");
//		}
        timer = System.nanoTime();
        tracker = 0;
        tcount = 0;
    }
	
	/**
	 * Lookup a command by its vnum.
     * The game uses the cached commands, we don't want to look up each time...they will be
     * called alot.
	 * @param vnum of command to lookup
	 * @return Command, null if not found.
	 */
	public Command getCommandByVnum(long vnum) {
		for( Command command : commands) {
			if( command.getVnum() == vnum) {
				return command;
			}
		}
		logger.error("Failed to lookup command "  + vnum);
		return null;
	}

	/**
	 * Lookup a script by its vnum.
	 * @param vnum of script to lookup
	 * @return Script, null if not found.
	 */
	public Script getScriptByVnum(long vnum) {
		for( Script script : scripts) {
			if( script.getVnum() ==  vnum) {
				return script;
			}
		}
        logger.error("Failed to lookup script " + vnum);
		return null;
	}

	/**
	 * Lookup a world by vnum.
	 * @param vnum of World to lookup
	 * @return World, null if not found
	 */
	public World getWorldByVnum(long vnum) {
		for( World world : worlds) {
			if( world.getVnum() ==  vnum) {
				return world;
			}
		}
		logger.error("Failed to lookup world " + vnum);
        return null;
	}
	
	/**
	 * Lookup an area by vnum
	 * @param vnum of Area to lookup
	 * @return Area, null if not found
	 */
	public Area getAreaByVnum(long vnum) {
		for( World world : worlds) { 
			for( Area area : world.getAreas()) {
				if( area.getVnum() == vnum) 
					return area;
			}
		}
		logger.error("Failed to lookup area: " + vnum);
		return null;
	}


	/**
	 * Loads a single or all scripts. Firsts drops the old script(s)
	 * @param vnum of script to reload, or Game.VNUM_ALL for all.
	 */
	public void loadScripts(int vnum) {

		// Load all
		if( vnum == Game.VNUM_ALL ) {
            logger.info("Refreshing all Scripts.");
            // reload
    	} else {
            Script script = getScriptByVnum(vnum);
            if( script != null) {
                logger.info("Refreshing script " + script.getVnum() + ".");                
                logger.info("Refreshing script " + script.getScript());
                // refresh
                logger.info("Refreshed script " + script.getVnum() + ".");
                logger.info("Refreshed script " + script.getScript());
            } else {
                logger.error("Invalid Script, could not reload; " + vnum);
            }
        }
	}
	
	public void loadCommands(int vnum) {
        if( vnum == Game.VNUM_ALL ) {
            logger.info("Refreshing all Commands.");
            // refresh
		} else {
            Command command = getCommandByVnum(vnum);
            if( command != null) {
                logger.info("Refreshing command " + command.getVnum() + ".");
                logger.info("Refreshing command " + command.getScript().getScript());
                // refresh
                logger.info("Refreshed command " + command.getVnum() + ".");
                logger.info("Refreshed command " + command.getScript().getScript());
            } else {
                logger.error("Invalid Command, could not reload; " + vnum);
            }
        }
	}

	public void loadWorlds(int vnum) {

		// Load all
		if( vnum == Game.VNUM_ALL ) {
			logger.info("Dropped " + worlds.size() + " Worlds.");
			worlds.clear();
            worlds = worldService.getAllWorlds();
            logger.info("Loaded " + worlds.size() + " Worlds from database.");
		} else {
			worlds.remove(this.getWorldByVnum(vnum));
			try {
// 			worlds.add(worldDao.get(vnum));
			} catch (Exception e) {
				logger.error("Could not load world from database in loadWorlds(" + vnum + ")",e);
			}
		}

		for( World world : worlds) {
			logger.info("Loaded World: " + world.getName() + " [" + world.getVnum() + "]");
			for( Area area : world.getAreas()) {
				logger.info("-> Loaded Area: " + area.getName() + " [" + area.getVnum() + "]");
			}
		}
	}

	public void reload(Actor ch, String argument) {
		String[] args = argument.split(" "); // split on space.

		if( args.length == 0 || args[0].equalsIgnoreCase("all")) {
			loadScripts(Game.VNUM_ALL);
			loadCommands(Game.VNUM_ALL);
			loadWorlds(Game.VNUM_ALL);
			ch.writeln("Reloaded all Worlds, Commands, and Scripts.");
			return;
		}

		if( args.length != 2) {
			logger.info("Invalid call to reload: " + argument);
			ch.writeln("Invalid call to reload.");
			return;
		}

		boolean all = false;
		if( args[1].equalsIgnoreCase("all"))
			all = true;

		if( args[0].startsWith("scr")) {
			if( all)
				loadScripts(Game.VNUM_ALL);
			else
				loadScripts(Integer.parseInt(args[1]));
		} else
        if ( args[0].startsWith("wor")) {
			if( all)
				loadWorlds(Game.VNUM_ALL);
			else
				loadWorlds(Integer.parseInt(args[1]));
		}
        else if( args[0].startsWith("com")) {
			if( all)
				loadCommands(Game.VNUM_ALL);
			else
				loadCommands(Integer.parseInt(args[1]));
		}
		ch.writeln("Reloaded.");
	}
	
	public static Game getInstance() {
		if( _instance == null) {
			_instance = new Game();
		}
		
		return _instance;
	}

	// Insert a character (back?) into the game.
	public void addCharacter(Actor ch) {
		players.add(ch);
		logger.info(ch.getName() + " added to Game");
		if( ch.getLocation() == null) {
            Room room = getAreaByVnum(1).getRoom(0, 0, 0);
            ch.setLocation(room);
            room.addActor(ch);
        }
	}
	
	/**
	 * Tick the game.
	 *
	 */
	public void tick() {
        long time;
        for( Actor player : players) {
			if( player.hasCommand() ) {
				String command = player.getNextCommand();
				interpret(player,command);
			}
		}
        time = System.nanoTime();
        tracker += (time = timer);
        timer = time;
        if( tracker > TICK ) {
            tracker -= TICK;
            tcount++;
            // 1 second
            if( tcount == 10) {
                updateActions();
            }
            tcount = 0;
        }

    }

    private void updateActions() {
        for( TickAction action : actions) {
            if( ! action.done()) {
                if( action.tick() ) {
                    Main.getInstance().runAction(action);                
                }
            } else {
                actions.remove(action);
            }
        }
    }
    /**
	 * Commands enter here!
	 * @param ch - Entity acting out the command
	 * @param command - the command, unparsed.
	 */
	public void interpret(Actor ch, String command) {
		if( GenericValidator.isBlankOrNull(command)) {
			return;
		}
		int spaceIndex = command.trim().indexOf(' ');
		String base;
		String args;
		if( spaceIndex == -1) {
			base = command;
			args = "";
		}
		else {
			base = command.substring(0,spaceIndex);
			args = command.substring(spaceIndex+1);
		}
		
		System.out.println("Command base string: " + base);
		System.out.println("Command args string: " + args);
		
		/*
		 * 1. Check if we already have the command.
		 * 2. Send a general action out to Entities.
		 * 3. Failover.
		 */
		for( Command cmd : commands) {
			if( cmd.getName().startsWith(base) || cmd.getName().equalsIgnoreCase(base)) {
				// Found.
				Action attempt = new Action(ch,cmd.getAction(),Action.ActionType.ATTEMPT,args);
				doAction(attempt);
				if(! attempt.isSuccess() ) {
					return;
				}
			    Action pre = new Action(ch,base,Action.ActionType.BEFORE,args);
			    doAction(pre);
				cmd.execute(ch, args);
			    Action post = new Action(ch,base,Action.ActionType.AFTER,args);
			    doAction(post);
			    return;
			}
		}
		// command not found, create a generic action for the room/etc.
		Action action = new Action(ch,base,Action.ActionType.GENERIC,args);
		doAction(action);	
		if(! action.isSuccess() ) {
			ch.writeln("Command not found.");
		}
	}
	
	/**
	 * Executes an action for an Entity.
	 * @param action
	 * @return
	 */
	private void doAction(Action action) {
		/*
		 * Depending on type of actor in the action, we determine which set of Entities to send the action to; who of course
		 * must be listening with a Trigger.
		 */
		
		// A player is committing the action, handle it.
		if( action.getActor() instanceof Actor) {
			doActorAction((Actor)action.getActor(), action);
			return;
		}
				
		return;
	}
	
	private void doActorAction(Actor actor, Action action) {
		Room room = actor.getLocation();
		boolean found = false;
		
		// Check worn equipment

		// Check equipment in inventory
		
		// Check the room for Triggers.
		for( Trigger trigger : room.getTriggers()) {
			if(trigger.getType() == action.getType() && trigger.getAction().equalsIgnoreCase(action.getAction())) {
				found = true;
				Main.getInstance().executeTrigger(room, actor, trigger, action);
			}
			if(! action.isSuccess() )
				return;
		}

		// Then check NPC's
		
		// Then check objects

		// We didn't find anything, so generic actions fail
		if( !found && action.getType() == Action.ActionType.GENERIC) {
			action.fail();
		}
		
		return;
	}

	public void moveChar(Actor ch, Room old, Room to) {
		old.removeActor(ch);
		to.addActor(ch);
		ch.setLocation(to);
	}
	
	/*
	 * current command scripts:
	 	 
	 // movement
	 a = ch.getLocation();
	 b = command.getName();
	 if( a.getExit(b) == null ) {
	 ch.writeln("You cannot move that direction");
	 } else {
	 x = a.getNewX(b);
	 y = a.getNewY(b);
	 z = a.getNewZ(b);
	 game.moveChar(ch,a,a.getParent().getRoom(x,y,z));
	 game.interpret(ch,"look");
	 }
	 
	 // reload
	 game.loadCommands(); 
	 ch.writeln("Reloaded commands and scripts.");
	 
	 // look (wayyyyyy incomplete)
	 loc = ch.getLocation();
	 ch.writeln(loc.getName());
	 ch.writeln(loc.getDescription());
     ch.write("Exits: [ ");
	 if( loc.getExit("north") != null) { ch.write("north "); } 
	 if( loc.getExit("south") != null) { ch.write("south "); } 
	 if( loc.getExit("east") != null) { ch.write("east "); } 
	 if( loc.getExit("west") != null) { ch.write("west "); } 
	 if( loc.getExit("up") != null) { ch.write("up "); } 
	 if( loc.getExit("down") != null) { ch.write("down "); } 
	 ch.writeln("]");
	  
	 
	 // Test trigger
	 ch.writeln("As is from nowhere, you hear - What the hell are you looking at?");
	 
	 // This is a demo script.
	 if( ch.hasTag("beatquest521")); return; }
	 
	 if( ch.getLocation().getName() == "room1") {
	     ch.setTag("beeninroom1");
	 } 
	 if ( ... room2 ) { ch.setTag("beeninroom2"); }
	 
	 if( ch.hasTag("beeninroom1") and ch.hasTag("beenrinroom2")...) {
	     ch.getWorld().
	 }
	 ch.putProperty("reputationMidgaard",5);
	 rep = ch.getProperty("reputationMidgaard");
	 // award xp.
	 // ch.setTag("beatquest521");
	  * 
	 */

    public SortedSet<Command> getCommands() {
        return commands;
    }

    public void setCommands(SortedSet<Command> commands) {
        this.commands = commands;
    }

    public void addTickAction(TickAction action) {
        actions.add(action);
    }

    public WorldService getWorldService() {
        return worldService;
    }

    public Collection<Actor> getPlayers() {
        return players;
    }
}
