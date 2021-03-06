package fish.focus.uvms.simulator.les.verysimpleclient;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.net.telnet.EchoOptionHandler;
import org.apache.commons.net.telnet.InvalidTelnetOptionException;
import org.apache.commons.net.telnet.SuppressGAOptionHandler;
import org.apache.commons.net.telnet.TelnetClient;
import org.apache.commons.net.telnet.TelnetNotificationHandler;
import org.apache.commons.net.telnet.TerminalTypeOptionHandler;

public class TelnetClientExample implements Runnable, TelnetNotificationHandler {
	static TelnetClient tc = null;

	/***
	 * Main for the TelnetClientExample.
	 * 
	 * @param args
	 *            input params
	 * @throws Exception
	 *             on error
	 ***/
	public static void main(String[] args) throws Exception {
		FileOutputStream fout = null;

		String remoteip = "localhost";

		int remoteport = 8585;

		try {
			fout = new FileOutputStream("spy.log", true);
		} catch (IOException e) {
			System.err.println("Exception while opening the spy file: " + e.getMessage());
		}

		tc = new TelnetClient();

		TerminalTypeOptionHandler ttopt = new TerminalTypeOptionHandler("VT100", false, false, true, false);
		EchoOptionHandler echoopt = new EchoOptionHandler(true, false, true, false);
		SuppressGAOptionHandler gaopt = new SuppressGAOptionHandler(true, true, true, true);

		try {
			tc.addOptionHandler(ttopt);
			tc.addOptionHandler(echoopt);
			tc.addOptionHandler(gaopt);
		} catch (InvalidTelnetOptionException e) {
			System.err.println("Error registering option handlers: " + e.getMessage());
			return;
		}

		while (true) {
			boolean end_loop = false;
			try {
				tc.connect(remoteip, remoteport);

				Thread reader = new Thread(new TelnetClientExample());
				tc.registerNotifHandler(new TelnetClientExample());

				reader.start();
				OutputStream outstr = tc.getOutputStream();
				outstr.flush();
				byte[] buff = new byte[1024];

				int ret_read = 0;

				do {
					try {
						ret_read = System.in.read(buff);
						if (ret_read > 0) {
							try {
								outstr.write(buff, 0, ret_read);
								outstr.flush();
							} catch (IOException e) {
								System.err.println("Exception while writing to server :" + e.getMessage());
								end_loop = true;
							}
						}
					} catch (IOException e) {
						System.err.println("Exception while reading keyboard:" + e.getMessage());
						end_loop = true;
					}
				} while ((ret_read > 0) && (end_loop == false));

				try {
					tc.disconnect();
				} catch (IOException e) {
					System.err.println("Exception while connecting:" + e.getMessage());
				}
			} catch (IOException e) {
				System.err.println("Exception while connecting:" + e.getMessage());
				System.exit(1);
			}
		}
	}

	/***
	 * Callback method called when TelnetClient receives an option negotiation
	 * command.
	 *
	 * @param negotiation_code
	 *            - type of negotiation command received (RECEIVED_DO,
	 *            RECEIVED_DONT, RECEIVED_WILL, RECEIVED_WONT, RECEIVED_COMMAND)
	 * @param option_code
	 *            - code of the option negotiated
	 ***/
	@Override
	public void receivedNegotiation(int negotiation_code, int option_code) {
		String command = null;
		switch (negotiation_code) {
			case TelnetNotificationHandler.RECEIVED_DO:
				command = "DO";
				break;
			case TelnetNotificationHandler.RECEIVED_DONT:
				command = "DONT";
				break;
			case TelnetNotificationHandler.RECEIVED_WILL:
				command = "WILL";
				break;
			case TelnetNotificationHandler.RECEIVED_WONT:
				command = "WONT";
				break;
			case TelnetNotificationHandler.RECEIVED_COMMAND:
				command = "COMMAND";
				break;
			default:
				command = Integer.toString(negotiation_code); // Should not happen
				break;
		}
		System.out.println("Received " + command + " for option code " + option_code);
	}

	/***
	 * Reader thread. Reads lines from the TelnetClient and echoes them on the
	 * screen.
	 ***/
	@Override
	public void run() {
		InputStream instr = tc.getInputStream();

		try {
			byte[] buff = new byte[1024];
			int ret_read = 0;

			do {
				ret_read = instr.read(buff);
				if (ret_read > 0) {
					System.out.print(new String(buff, 0, ret_read));
				}
			} while (ret_read >= 0);
		} catch (IOException e) {
			System.err.println("Exception while reading socket:" + e.getMessage());
		}

		try {
			tc.disconnect();
		} catch (IOException e) {
			System.err.println("Exception while closing telnet:" + e.getMessage());
		}
	}
}
