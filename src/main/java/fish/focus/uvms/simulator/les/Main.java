package fish.focus.uvms.simulator.les;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

	private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

	private static Properties settings = new Properties();

	public static Properties getSettings() {
		return settings;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		try {
			loadProperties();
		} catch (IOException e) {
			LOGGER.error("settings.properties not found. cannot start");
			System.exit(-1);
		}

		String portStr = settings.getProperty("server.port");

		int port = Integer.MIN_VALUE;
		try {
			port = Integer.parseInt(portStr);
		} catch (NumberFormatException nfe) {
			LOGGER.error("Invalid port.  Check settings.properties");
			System.exit(-1);
		}

		Server server = new Server(port);

		server.registerCommand(new Command("hi") {

			@Override
			public Response handle(List<String> arguments) {
				if (arguments.size() > 0)
					return new Response("hi " + arguments.toString() + " really nice to meet you!");
				else
					return new Response("this is the " + this.getName() + " command");
			}

		});

		server.registerCommand(new Command("quit") {

			@Override
			public Response handle(List<String> arguments) {
				return new Response("bye", false);
			}

		});

		server.registerCommand(new Command("AYT") {

			@Override
			public Response handle(List<String> arguments) {
				return new Response("AYT on server ");
			}

		});

		server.start();

	}

	private static void loadProperties() throws IOException {

		InputStream is = Main.class.getClassLoader().getResourceAsStream("settings.properties");
		if (is == null) {
			throw new IOException();
		}
		settings.load(is);
	}

}
