package fish.focus.uvms.simulator.les.telnetclient;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Reader;

final class TelnetConversation {
	private static final String crlf = "\r\n";
	private final Reader reader;
	private final PrintStream out;
	private final InputStream is;

	TelnetConversation(InputStream is, OutputStream os) {
		this.is = is;
		this.reader = new InputStreamReader(is);
		this.out = new PrintStream(os);
	}

	void println(String s) {
		out.print(s);
		out.print(crlf);
		out.flush();
	}

	void waitFor(String value) {
		StringBuilder sb = new StringBuilder();
		char ch;
		try {
			while ((ch = (char) reader.read()) != -1) {
				sb.append(ch);
				if (sb.toString().endsWith(value))
					return;
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public String response() {

		try {
			byte[] buff = new byte[1024];
			int ret_read = 0;

			do {
				ret_read = is.read(buff);
				if (ret_read > 0) {
					return new String(buff, 0, ret_read);
				}
			} while (ret_read >= 0);
		} catch (IOException e) {
			System.err.println("Exception while reading socket:" + e.getMessage());
		}

		return "";

	}

}