package fish.focus.uvms.simulator.les.telnetclient;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

public class CommandHelper {

	public void write(String value, PrintStream out) throws IOException {
		out.println(value);
		out.flush();
	}

	public String readUntil(String pattern, InputStream in, FileOutputStream stream, String url, String port)
			throws TelnetException, IOException {
		String[] faultPatterns =
				{"????????", "[Connection to 41424344 aborted: error status 0]", "Illegal address parameter."};
		StringBuffer stringbuffer = new StringBuffer();
		byte[] bytebuffer = new byte[1024];
		int bytesRead = 0;
		do {
			bytesRead = in.read(bytebuffer);
			if (bytesRead > 0) {
				String s = new String(bytebuffer, 0, bytesRead);
				stringbuffer.append(s);
				if (stream != null) {
					stream.write(bytebuffer, 0, bytesRead);
					stream.flush();
				}
				if (stringbuffer.toString().trim().endsWith(pattern)) {
					return stringbuffer.toString();
				} else {
					for (String faultPattern : faultPatterns) {
						if (stringbuffer.toString().trim().contains(faultPattern)) {
							throw new TelnetException("Error while reading from Inmarsat-C LES Telnet @ " + url + ":"
									+ port + ": " + stringbuffer.toString());
						}
					}
				}
			}
		} while (bytesRead >= 0);
		throw new TelnetException(
				"Unknown response from Inmarsat-C LES Telnet @ " + url + ":" + port + ": " + stringbuffer.toString());
	}

}
