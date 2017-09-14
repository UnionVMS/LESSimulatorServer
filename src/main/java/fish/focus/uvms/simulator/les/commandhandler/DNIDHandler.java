package fish.focus.uvms.simulator.les.commandhandler;

import java.util.List;

import fish.focus.uvms.simulator.les.server.Response;

public class DNIDHandler {

	private List<String> arguments;

	public DNIDHandler(List<String> arguments) {
		this.arguments = arguments;

	}
	
	public boolean verify(){
		return true;
	}
	
	public Response execute(){
		return new Response("DNID >");
	}


}
