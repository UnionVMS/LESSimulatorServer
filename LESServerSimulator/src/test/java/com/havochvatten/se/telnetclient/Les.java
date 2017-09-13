package com.havochvatten.se.telnetclient;

import java.io.IOException;
import java.io.InputStream;

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

	public void testORG() {
		try (TelnetSession c = new TelnetSession(host, port)) {
			c.waitFor("Please enter username: ");
			c.println(username);
			c.waitFor("Please enter password: ");
			c.println(password);
			c.waitFor("> ");
			c.println("?");
			c.waitFor("> ");
			c.println("quit");
			c.waitFor("Logging off...");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void test() {
		try (TelnetSession c = new TelnetSession(host, port)) {
			c.waitFor("name:");
			c.println(username);
			c.waitFor("word");
			c.println(password);
			String response = c.response();
			if (!response.equals("> ")) {
				System.out.println(response);
				return;
			}
			System.out.println(response);

			c.println("hi thomas");
			System.out.println(c.response());

			c.println("quit");
			c.waitFor("bye");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static void main(String[] args) {

		Les les = new Les("localhost", 8585, "DonaldDuck", "password155");

		les.test();

	}

}