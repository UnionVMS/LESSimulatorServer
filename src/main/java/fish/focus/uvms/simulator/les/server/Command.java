package fish.focus.uvms.simulator.les.server;

/**
 * base class for command handlers just implement handle
 *
 */
public abstract class Command {

	public static final String END = "\r\n";
	public static final String ERROR = "ERROR";
	public static final String UNKNOWN = "Unknown command";

	protected String cmd = null;

	public Command(String cmd) {
		this.cmd = cmd;
	}

	public String getName() {
		return this.cmd;
	}

	public abstract Response handle(String arguments);

}