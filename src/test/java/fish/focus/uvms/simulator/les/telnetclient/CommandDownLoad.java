package fish.focus.uvms.simulator.les.telnetclient;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

public class CommandDownLoad {

	private CommandHelper commandHelper = new CommandHelper();

	public String sendDownloadCommand(PrintStream out, InputStream in, FileOutputStream stream, String dnid, String url,
			String port) throws TelnetException, IOException {
		String prompt = ">";
		String cmd = "DNID " + dnid + " 1";
		commandHelper.write(cmd, out);
		return commandHelper.readUntil(prompt, in, stream, url, port);
	}

}
