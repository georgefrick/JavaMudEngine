package com.s5games.mud;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

import com.s5games.mud.beans.Actor;
import com.s5games.mud.beans.Entity;
import com.s5games.mud.command.Command;
import com.s5games.mud.event.Action;
import com.s5games.mud.event.Trigger;
import com.s5games.mud.event.TickAction;
import com.s5games.mud.network.Connection;
import com.s5games.mud.network.ThreadedServerSocket;

/**
 * Represent the Server, contains the "Game" simulation and manages connections for it.
 * 
 * @author George Frick (george.frick@gmail.com)
 */
public class Main {

	private ThreadedServerSocket sSocket;
	public Collection<Connection> connections;
	private boolean running = false;
	private boolean shutdown = false;
	private static Main _main;
	public final static int PORT = 6663;
    private Context jScriptContext;
	private Scriptable jScriptScope;
	protected static Logger logger = Logger.getLogger(Main.class);
	
	/**
	 * Private constructor creates the server socket for the defined port
	 * constant.
	 */
	private Main() {
		
		// Create the rhino javascript scope.
		jScriptContext = Context.enter();
		jScriptScope = new org.mozilla.javascript.ImporterTopLevel(jScriptContext); // jScriptContext.initStandardObjects();
		ScriptableObject.putProperty(jScriptScope, "main", Context.javaToJS(this, jScriptScope));
		ScriptableObject.putProperty(jScriptScope, "game", Context.javaToJS(Game.getInstance(), jScriptScope));
		jScriptContext.evaluateString(jScriptScope,"importPackage(Packages.com.s5games.mud.game);", "<cmd>", 1, null);

		sSocket = new ThreadedServerSocket(Main.PORT);
		connections = new ArrayList<Connection>();
		sSocket.start();
	}

	/**
	 * Just executes a chunk of javascript within the jScriptScope
	 * @param script
	 */
	public void runScript(String script) {
		jScriptContext.evaluateString(jScriptScope,script,"<cmd>", 1, null);
	
	}

	/**
	 * Executes a trigger by the owner on the ch entity. The trigger can fail by calling fail
	 * on the passed in action.
	 * @param owner Entity the Trigger belonged to.
	 * @param ch Entity causing the Trigger to execute
	 * @param trigger The script associated with the trigger
	 * @param action The action that tripped the Trigger.
	 */
	public void executeTrigger(Entity owner, Actor ch, Trigger trigger, Action action) {
		jScriptScope.put("owner", jScriptScope, owner);
		jScriptScope.put("ch", jScriptScope,ch);
		jScriptScope.put("args", jScriptScope, action.getArgument());
		jScriptScope.put("command", jScriptScope, trigger);
		jScriptScope.put("action", jScriptScope, action);
		jScriptContext.evaluateString(jScriptScope, trigger.getScript().getScript(), "<cmd>", 1, null);		
	}
	
	/**
	 * Executes a command by an entity ch.
	 * @param ch Entity executing the command.
	 * @param command The command to execute
	 * @param args String based arguments provided by the Entity ch.
	 */
	public void executeCommand(Actor ch, Command command, String args) {	
		try {
	 	jScriptScope.put("ch", jScriptScope,ch);
 		jScriptScope.put("args", jScriptScope, args); 
		jScriptScope.put("command", jScriptScope, command);
		jScriptContext.evaluateString(jScriptScope, command.getScript().getScript(), "<cmd>", 1, null);
		} catch( Exception ex) {
			ch.write("There was a problem executing that command. It was reported to admin.\n\r");
			logger.error("Failed to execute command script " + command.getName(),ex);
		}
	}
	
	/**
	 * Return the singleton instance of the main game object.
	 * 
	 * @return A singleton instance of Main
	 */
	public static Main getInstance() {
		if (_main == null) {
			_main = new Main();
		}

		return _main;
	}

	/**
	 * ShutDown hook function, provide a reason for the shutdown.
	 * @param reason The reason for the shutdown.
	 */
	public void shutDown(String reason) {
		logger.info("Shutting Down: " + reason);
		shutDown();
	}

	/**
	 * The main game loop, while running, loop.
	 */
	public void mainLoop() {
		running = true;
		while (running) {
			if (shutdown) {
				shutDown();
			} else {
				singleLoop(); // run one game loop
			}
		}
        Iterator<Connection> it = connections.iterator();
		Connection curr;
		while (it.hasNext()) {
			curr = it.next();
			curr.close();
		}
		sSocket.shutDown();
		Context.exit(); // close Rhino
		System.exit(0);
    }

	private void singleLoop() {
		// 1. Check for new connections
		while (sSocket.poll()) {
			Socket s = sSocket.getNextConnection();
			logger.error("Connection received from " + s.getInetAddress().getHostName() + " : " + s.getPort());			
			Connection c = new Connection(s);		 
			connections.add(c);
		}

		Iterator<Connection> it;
		Connection curr;

		// 2. Check for dead connections.
		// TODO: Check for dead connections
		// TODO: Check for timed out connections.
//		it = connections.iterator();
//		while (it.hasNext()) {
//			curr = it.next();
//			
//		}

		// 3. Check for input.
		for( Connection conn : connections ) {
			String s = conn.readLine();
			conn.setConnectionState(conn.getConnectionState().getNextState(conn, s));
			if( conn.getConnectionState() == null ) {
				conn.close();
				connections.remove(conn);
			}
		}
		
//		it = connections.iterator();
//		while (it.hasNext()) {
//			curr = it.next();
//			String s = curr.readLine();
//			curr.setConnectionState(curr.getConnectionState().getNextState(
//					curr, s));
//			if(curr.getConnectionState() == null) {
//				curr.flush();
//				curr.close();
//				it.remove();
//			}
//			// give string to matching character...
//		}

		// 4. Tick! the game (unless we use Timer)
		Game.getInstance().tick();
        
        // 5. flush output.
		it = connections.iterator();
		while (it.hasNext()) {
			curr = it.next();
			if (!curr.isEmpty()) {
				curr.getConnectionState().bustAPrompt(curr);
				curr.flush();
			}
            // We are leaving the game.
            if( curr.isShouldClose() ) {
                curr.getConnectionState().leaveState(curr);
                curr.close();
                it.remove(); 
            }
        }
	}

	/**
	 * Shut down the server by handling all connections and setting the running
	 * flag. - disconnect/close all connections. - refuse any pending
	 * connections. - close server socket. - set running to false
	 */
	private void shutDown() {
		running = false;
	}

	public static void main(String[] args) {
		Main main = Main.getInstance();
		main.mainLoop();
	}

	/**
	 * Tests if a given connections Account is already logged in. Accounts in the process of logging in do
	 * not count, so it's first one in.
	 */
	public boolean isLogged(Connection conn) {
		Iterator<Connection> it = connections.iterator();
		Connection curr;
		while (it.hasNext()) {
			curr = it.next();
			// Don't count yourself...
			if( curr == conn) {
				continue;
			}
			// Don't count anyone else logging in.
			if( curr.getAccount() == null || curr.getAccount().getPassword() == null) {
				continue;
			}
			// check for user match
			if( curr.getAccount().getUserName().equalsIgnoreCase(conn.getAccount().getUserName()) ) {
				return true;
			}
		}
		return false;
	}

    public void runAction(TickAction action) {
        jScriptScope.put("ch", jScriptScope, action.getTarget());
        jScriptScope.put("group", jScriptScope, action.getGroup());
		jScriptContext.evaluateString(jScriptScope, action.getScript().getScript(), "<cmd>", 1, null);
    }
}
