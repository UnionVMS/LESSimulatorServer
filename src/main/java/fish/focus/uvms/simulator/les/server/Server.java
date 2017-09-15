package fish.focus.uvms.simulator.les.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
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

	// TODO make this correct later
	private boolean autenticate(String user, String pwd) {
		if ((user == null) || (user.length() < 1))
			return false;
		if ((pwd == null) || (pwd.length() < 1))
			return false;

		String allowed_uid = Main.getSettings().getProperty("server.uid");
		String allowed_pwd = Main.getSettings().getProperty("server.pwd");
		if ((allowed_uid == null) || (allowed_uid.length() < 1))
			return false;
		if ((allowed_uid == null) || (allowed_uid.length() < 1))
			return false;
		return user.trim().equals(allowed_uid) && pwd.trim().equals(allowed_pwd);
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

		BufferedReader in = null;
		OutputStream out = null;
		while (loop) {
			try {
				Socket clientSocket = socket.accept();

				// before authenticated

				in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), "UTF-8"));
				out = clientSocket.getOutputStream();
				// logon before job starts
				write(out, "name:");
				String user = readLine(in);
				write(out, "word:");
				String pwd = readLine(in);

				// authenticate

				if (autenticate(user, pwd)) {
					write(out, ">");
					Thread jobThread = new Thread(new Client(clientSocket, this));
					jobThread.start();
				} else {
					LOGGER.info("User is not authenticated >");
					write(out, "User is not authenticated");
				}
			} catch (IOException e) {
				LOGGER.error(e.toString(), e);
			} finally {
			}
		}
	}

	public void write(OutputStream out, String msg) throws UnsupportedEncodingException, IOException{
		out.write(msg.getBytes("UTF-8"));		
	}
	
	public String readLine(BufferedReader in) throws IOException {

		String line = null;
			while ((line = in.readLine()) != null) {
				if (line.length() > 0) {
					return line;
				}
			}
		return "";
	}

	public void start() {
		serverThread = new Thread(this);
		serverThread.start();
	}

	public void stop() {
		loop = false;
	}

}