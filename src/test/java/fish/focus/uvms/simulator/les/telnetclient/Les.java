package fish.focus.uvms.simulator.les.telnetclient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import org.junit.BeforeClass;
import org.junit.Test;

import eu.europa.ec.fisheries.schema.exchange.plugin.types.v1.PollType;
import fish.focus.uvms.simulator.les.TelnetException;

public final class Les {

	private static String host;
	private static int port;
	private static String username;
	private static String password;

	@BeforeClass
	public static void beforeClass() {

		host = "localhost";
		port = 8585;
		username = "donald";
		password = "duck";
		new File("downloads/").mkdirs();

	}

	@Test
	public void testPoll() throws TelnetException {
		try (TelnetSession c = new TelnetSession(host, port)) {
			c.waitFor("name:");
			c.println(username);
			c.waitFor("word");
			c.println(password);
			String response = c.response();
			if (!response.equals(">")) {
				// logon failed
				System.out.println(response);
				return;
			}

			CommandPoll commandPoll = new CommandPoll();

			PollType poll = new PollType();

			InputStream in = c.getInputStream();
			PrintStream out = new PrintStream(c.getOutputStream());

			long s = System.currentTimeMillis();
			String fileName = "downloads/" + s + "_POLL.fil";
			File resultFile = new File(fileName);
			FileOutputStream stream = new FileOutputStream(resultFile);

			String rs = commandPoll.sendPollCommand(poll, out, in, stream, host, String.valueOf(port));

			if (stream != null) {
				stream.flush();
				stream.close();
			}

			System.out.println(response);

			c.println("quit");
			c.waitFor("bye");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Test
	public void testDNID() throws TelnetException {
		try (TelnetSession c = new TelnetSession(host, port)) {
			c.waitFor("name:");
			c.println(username);
			c.waitFor("word");
			c.println(password);
			String response = c.response();
			if (!response.equals(">")) {
				System.out.println(response);
			}

			CommandDownLoad commandDownload = new CommandDownLoad();

			String dnid = "1234";

			InputStream in = c.getInputStream();
			PrintStream out = new PrintStream(c.getOutputStream());
			long s = System.currentTimeMillis();
			File resultFile = new File("downloads/" + s + "_" + dnid + "_DNID.fil");
			FileOutputStream stream = new FileOutputStream(resultFile);

			String reponse = commandDownload.sendDownloadCommand(out, in, stream, dnid, host, String.valueOf(port));

			System.out.println(response);
			if (stream != null) {
				stream.flush();
				stream.close();
			}

			c.println("quit");
			c.waitFor("bye");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}