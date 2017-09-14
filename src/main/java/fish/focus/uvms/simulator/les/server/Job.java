package fish.focus.uvms.simulator.les.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Job implements Runnable {

	private static final Logger LOGGER = LoggerFactory.getLogger(Job.class);

	private Socket socket = null;
	private Server server = null;

	public Server getServer() {
		return server;
	}

	public Socket getSocket() {
		return socket;
	}

	public Job(final Socket socket, final Server server) {
		this.socket = socket;
		this.server = server;
	}

	public void run() {

		PrintWriter out = null;
		BufferedReader in = null;
		String line = null;

		try {
			out = new PrintWriter(socket.getOutputStream(), true);
		} catch (IOException e) {
			LOGGER.error(e.toString(), e);
			return;
		}

		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
		} catch (IOException e) {
			LOGGER.error(e.toString(), e);
			return;
		}

		Response response = new Response();
		try {
			while ((line = in.readLine()) != null) {
				if (line == null || line.length() == 0) {
					response.set(Command.ERROR);
				} else {
					StringTokenizer st = new StringTokenizer(line);
					String command = st.nextToken();
					command = command.toLowerCase();
					if (this.getServer().getCommands().containsKey(command)) {
						int n = st.countTokens();
						List<String> arguments = new ArrayList<>();
						if (n >= 1) {
							while (st.hasMoreTokens()) {
								arguments.add(st.nextToken());
							}
						} 
						Command commandHandler = this.getServer().getCommands().get(command);
						response = commandHandler.handle(arguments);
					} else {
						response.set(Command.ERROR);
					}
				}
				out.print(response);
				out.print(Command.END);
				out.flush();
				if (response.keepalive() == false) {
					break;
				}
			}
		} catch (IOException e) {
			LOGGER.info(e.toString(), e);
		}

		try {
			out.close();
			socket.close();
		} catch (IOException e) {
			LOGGER.info(e.toString(), e);
		}

	}
}