package fish.focus.uvms.simulator.les.telnetclient;

public class TelnetException extends Exception {
	private static final long serialVersionUID = 1L;

	public TelnetException(String message) {
		super(message);
	}

	public TelnetException(Exception ex) {
		super(ex);
	}
}
