package fish.focus.uvms.simulator.les.commandhandler;

import fish.focus.uvms.simulator.les.server.Response;

public class POLLHandler {

	private String arguments;

	public POLLHandler(String arguments) {
		this.arguments = arguments;
	}

	public boolean verify() {

		return true;
	}

	public Response execute() {
		return new Response(arguments + " POLL");
	}

}
