package fish.focus.uvms.simulator.les.telnetclient;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

import fish.focus.uvms.simulator.les.TelnetException;

public class CommandDecode {

	private CommandHelper commandHelper = new CommandHelper();

	public String decode(PrintStream out, InputStream in, FileOutputStream stream, String dnid, String url,
			String port) throws TelnetException, IOException {
		String prompt = ">";
		String cmd = "DECODE " + dnid + " 1";
		commandHelper.write(cmd, out);
		return commandHelper.readUntil(prompt, in, stream, url, port);
	}

}
