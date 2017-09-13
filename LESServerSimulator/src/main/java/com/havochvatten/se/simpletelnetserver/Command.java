package com.havochvatten.se.simpletelnetserver;

public abstract class Command {

	public static final String END = "\r\n";
	public static final String ERROR = "ERROR";

	protected String cmd = null;

	public Command(String cmd) {
		this.cmd = cmd;
	}

	public String getName() {
		return this.cmd;
	}

	public abstract Response handle(String[] arguments);

}