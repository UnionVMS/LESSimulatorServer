package fish.focus.uvms.simulator.les.server;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fish.focus.uvms.simulator.les.commandhandler.DNIDHandler;
import fish.focus.uvms.simulator.les.commandhandler.POLLHandler;

public class Main {

	private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

	private static Properties settings = new Properties();

	public static Properties getSettings() {
		return settings;
	}

	private static void loadProperties() throws IOException {

		InputStream is = Main.class.getClassLoader().getResourceAsStream("settings.properties");
		if (is == null) {
			throw new IOException();
		}
		settings.load(is);
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

		server.registerCommand(new Command("POLL") {
			@Override
			public Response handle(List<String> arguments) {
				if (arguments.size() > 0) {
					POLLHandler pollHandler = new POLLHandler(arguments);
					if(pollHandler.verify()){
						return pollHandler.execute();
					}else{
						return new Response("POLL request not OK ", false);
					}
				} else
					return new Response("No arguments passed in POLL command >", false);
			}

		});

		server.registerCommand(new Command("DNID") {
			@Override
			public Response handle(List<String> arguments) {
				if (arguments.size() > 0) {
					DNIDHandler dnidHandler = new DNIDHandler(arguments);
					if(dnidHandler.verify()){
						return dnidHandler.execute();
					}else{
						return new Response("DNID request not OK ", false);
					}
				} else
					return new Response("No arguments passed in DNID command >", false);
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
				return new Response("AYT on server");
			}

		});

		server.start();

	}

}
