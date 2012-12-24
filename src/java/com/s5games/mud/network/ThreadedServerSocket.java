package com.s5games.mud.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * A server socket running within a thread. New connections are accepted and inserted into a connection pool for retrieval.
 * @author George Frick
 */
public class ThreadedServerSocket extends Thread {

	ServerSocket sSocket;
	ArrayList<Socket> connections;
	boolean keepAlive = false;

	/**
	 * Create a ThreadedServerSocket for the given port.
	 * @param port The port to listen on.
	 */
	public ThreadedServerSocket(int port) {
		try {
			sSocket = new ServerSocket(port);
			connections = new ArrayList<Socket>();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Thread run method, loops while the thread is alive.
	 * Accepts a new connection, and adds it to th Socket pool.
	 */
	public void run() {
		keepAlive = true;
		while (keepAlive) {
			try {
				Socket s;
				s = sSocket.accept();
				connections.add(s);
				// TODO: Log instead of system.out
				System.out.println("Accepted New Connection.");
				yield();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * ShutDown method to inform the thread to die.
	 */
	public void shutDown() {
		keepAlive = false;
	}

	/**
	 * Polling method to get status of connection pool
	 * @return True if there are open Sockets waiting in the pool.
	 */
	public boolean poll() {
		return !(connections.isEmpty());
	}

	/**
	 * Removes an open Socket from the pool, returning it.
	 * @return A Socket from the Socket pool.
	 */
	public Socket getNextConnection() {
		return connections.remove(0);
	}
}
