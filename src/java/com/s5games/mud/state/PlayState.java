package com.s5games.mud.state;

import java.util.HashMap;
import java.util.Map;

import com.s5games.mud.Game;
import com.s5games.mud.io.StreamUtil;
import com.s5games.mud.beans.Actor;
import com.s5games.mud.command.Commandable;
import com.s5games.mud.network.Connection;
/**
 * 
 * @author George Frick (george.frick@gmail.com)
 *
 */
public class PlayState implements ConnectionState {

    private Map<Connection, Commandable> players;
    private static PlayState _playState;

    private PlayState() {
        players = new HashMap<Connection, Commandable>();
    }

    public static PlayState getInstance() {
        if (_playState == null) {
            _playState = new PlayState();
        }

        return _playState;
    }

    public void enterState(Connection conn) {
        System.out.println("Entering PlayState");
        // Todo: load character and all that stuff.
        Actor actor = new Actor(conn.getAccount().getUserName());
        actor.setDescription(actor.getName());
        actor.setAccount(conn.getAccount());


        players.put(conn, actor);

        // Now add the player to the game.
        Game.getInstance().addCharacter(actor);
    }

    public ConnectionState getNextState(Connection conn, String input) {

        Commandable c = players.get(conn);

        if (!c.active()) {
            conn.setShouldClose(true);            
        } else {

            if (input == null) {
                handleOutput(c, conn);
                return this;
            }

            if (input.trim().length() == 0) {
                handleOutput(c, conn);
                conn.forcePrompt();
                //conn.writeLine(c.getPrompt());
                return this;
            }

            if (input.trim().length() > 0) {
                c.addCommand(input.trim());
            }
        }
        handleOutput(c, conn);
        return this;
    }

    private void handleOutput(Commandable ch, Connection conn) {
        if (ch.getOutput() != null && ch.getOutput().trim().length() > 0) {
            // todo: check if they have color turned on
            conn.writeOutputLine(StreamUtil.colorString(ch.getOutput()));
            ch.resetOutput();
            // conn.forceFlush();
        }
    }

    public void leaveState(Connection conn) {
        Actor actor = (Actor)players.get(conn);
        actor.getLocation().removeActor(actor);
        actor.setLocation(null);
        players.remove(conn); // this apparently is not working?
    }

    public void bustAPrompt(Connection conn) {
        try {
            Commandable c = players.get(conn);
            conn.writeLine(c.getPrompt());
        } catch (Exception ex) {
            System.out.println("Failure to bustAPrompt");
            conn.writeLine("BadPrompt: ");
        }
    }

}
