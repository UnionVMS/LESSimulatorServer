package com.havochvatten.se.simpletelnetserver;

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
			public Response handle(String[] arguments) {
				if(arguments == null) return new Response("command hi : no arguments");
				if (arguments.length > 0)
					return new Response("hi " + arguments[0] + " really nice to meet you!");
				else
					return new Response("this is the " + this.getName() + " command");
			}

		});

		server.registerCommand(new Command("quit") {

			@Override
			public Response handle(String[] arguments) {
				return new Response("see you...", false /* halt */);
			}

		});

		server.registerCommand(new Command("AYT") {

			@Override
			public Response handle(String[] arguments) {
				if(arguments == null) return new Response("command AYT : no arguments");
				return new Response("AYT on server ");
			}

		});

		server.start();

	}

}
