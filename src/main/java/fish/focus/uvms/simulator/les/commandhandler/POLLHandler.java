package fish.focus.uvms.simulator.les.commandhandler;

import java.util.List;

import fish.focus.uvms.simulator.les.server.Response;

public class POLLHandler {

	private List<String> arguments;

	public POLLHandler(List<String> arguments) {
		this.arguments = arguments;
	}
	
	public boolean verify(){
		
		return true;
	}
	
	public Response execute(){
		return new Response(arguments.get(0) + " POLL");
	}

}
