package fish.focus.uvms.simulator.les;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Main {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Server server = new Server(8585);

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
			public Response handle(List<String>  arguments) {
				return new Response("bye", false);
			}

		});

		server.registerCommand(new Command("AYT") {

			@Override
			public Response handle(List<String>  arguments) {
				return new Response("AYT on server ");
			}

		});

		server.start();

	}

}
