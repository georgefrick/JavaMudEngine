package com.s5games.mud.services;

import com.s5games.mud.beans.World;
import com.s5games.mud.beans.Actor;
import com.s5games.mud.command.Command;
import com.s5games.mud.command.Script;

import java.util.Collection;
/**
 * 
 * @author George Frick (george.frick@gmail.com)
 *
 */
public interface WorldService {

    public final static String SERVICE_NAME = "worldService";
    
    public Collection<World> getAllWorlds();

    public Collection<Command> getAllCommands();

    public Collection<Script> getAllScripts();

    public Script findScript(Long vnum);

    public Command findCommand(Long vnum);

    public Actor findActor(Long vnum);

    public void load();
}
