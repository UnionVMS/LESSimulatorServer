package fish.focus.uvms.simulator.les;

import java.util.List;

public abstract class Command {

	public static final String END = "\r\n";
	public static final String ERROR = "ERROR";

	protected String cmd = null;

	public Command(String cmd) {
		this.cmd = cmd;
	}

	public String getName() {
		return this.cmd;
	}

	public abstract Response handle(List<String> arguments);

}