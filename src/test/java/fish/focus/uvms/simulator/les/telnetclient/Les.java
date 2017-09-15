package fish.focus.uvms.simulator.les.telnetclient;

import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

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

	}

	@Test
	public void testPoll() {
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
			// String reponse = commandPoll.sendPollCommand(poll, out, in,
			// stream, url, port);

			System.out.println(response);

			c.println("quit");
			c.waitFor("bye");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Test
	public void testDNID() {
		try (TelnetSession c = new TelnetSession(host, port)) {
			c.waitFor("name:");
			c.println(username);
			c.waitFor("word");
			c.println(password);
			String response = c.response();
			if (!response.equals(">")) {
				// logon failed
				System.out.println(response);
			}

			CommandDownLoad commandDownload = new CommandDownLoad();
			// String reponse = commandDownload.sendPollCommand(poll, out, in,
			// stream, url, port);

			System.out.println(response);

			c.println("quit");
			c.waitFor("bye");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}