package fish.focus.uvms.simulator.les.telnetclient;

import java.io.IOException;

public final class Les {

	private final String host;
	private final int port;
	private final String username;
	private final String password;

	public Les(String host, int port, String username, String password) {
		this.host = host;
		this.port = port;
		this.username = username;
		this.password = password;
	}


	public void test() {
		try (TelnetSession c = new TelnetSession(host, port)) {
			c.waitFor("name:");
			c.println(username);
			c.waitFor("word");
			c.println(password);
			String response = c.response();
			if (!response.equals("> ")) {
				// logon failed
				System.out.println(response);
				return;
			}
			System.out.println(response);

			c.println("hi sture  andersson");
			System.out.println(c.response());

			c.println("quit");
			c.waitFor("bye");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static void main(String[] args) {

		Les les = new Les("localhost", 8585, "donald", "duck");

		les.test();

	}

}