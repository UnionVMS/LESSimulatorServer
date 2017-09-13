package com.havochvatten.se.simpletelnetserver;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Server implements Runnable {

	private static final Logger LOGGER = LoggerFactory.getLogger(Server.class);

	private int port;
	private InetAddress bind = null;
	private ServerSocket socket = null;
	private int timeout = 0;
	private int backlog = 0;

	private static Thread serverThread = null;
	private static boolean loop = true;

	public int getBacklog() {
		return backlog;
	}

	public void setBacklog(int backlog) {
		this.backlog = backlog;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	private HashMap<String, Command> commands = new HashMap<String, Command>();

	public Server(int port, InetAddress bind) {
		this.bind = bind;
		this.port = port;
	}

	public Server(int port) {
		this.port = port;
	}

	public void registerCommand(String command, Command handler) {
		commands.put(command.toLowerCase(), handler);
	}

	public void registerCommand(Command handler) {
		commands.put(handler.getName().toLowerCase(), handler);
	}

	public HashMap<String, Command> getCommands() {
		return commands;
	}

	private boolean autenticate(String user, String pwd) {

		return true;

	}

	// @Override
	public void run() {

		try {
			socket = new ServerSocket(port, backlog, bind);
			socket.setSoTimeout(getTimeout());
		} catch (IOException e) {
			LOGGER.equals("Could not get a server socket. Server will not start");
			LOGGER.error(e.toString(), e);
			stop();
		}

		while (loop) {
			try {
				Socket clientSocket = socket.accept();

				// logon before job starts
				byte nameBuffer[] = new byte[1024];
				byte pwdBuffer[] = new byte[1024];
				clientSocket.getOutputStream().write("name:".getBytes("UTF-8"));
				clientSocket.getInputStream().read(nameBuffer);
				clientSocket.getOutputStream().write("word:".getBytes("UTF-8"));
				clientSocket.getInputStream().read(pwdBuffer);

				String user = new String(nameBuffer, "UTF-8");
				String pwd = new String(pwdBuffer, "UTF-8");

				if (autenticate(user, pwd)) {
					clientSocket.getOutputStream().write("> ".getBytes("UTF-8"));
					Thread jobThread = new Thread(new Job(clientSocket, this));
					jobThread.start();
				}else{
					LOGGER.info("User is not authenticated > ");
					clientSocket.getOutputStream().write("User is not authenticated".getBytes("UTF-8"));
				}
			} catch (IOException e) {
				LOGGER.error(e.toString(), e);
			}
		}
	}

	public void start() {
		serverThread = new Thread(this);
		serverThread.start();
	}

	public void stop() {
		loop = false;
	}

}