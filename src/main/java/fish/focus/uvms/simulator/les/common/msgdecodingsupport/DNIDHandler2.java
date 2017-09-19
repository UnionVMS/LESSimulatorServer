package fish.focus.uvms.simulator.les.common.msgdecodingsupport;

import fish.focus.uvms.simulator.les.server.Main;
import fish.focus.uvms.simulator.les.server.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Vector;


public class DNIDHandler2 {

	public static final String END = "\r\n";
	private static final Logger LOG = LoggerFactory.getLogger(DNIDHandler2.class);

	private final byte[] BYTE_PATTERN = { 1, 84, 38, 84 };
	private int PATTERN_LENGTH;

	private String dnid;
	private String area;
	String dnidRoot = null;
	String arguments;

	public DNIDHandler2(String arguments) {
		this.arguments = arguments;
	}

	private boolean parseArguments() {

		String parts[] = arguments.split(" ");
		if (parts.length < 2) {
			return false;
		}
		if (parts.length >= 1) {
			dnid = parts[0];
		}
		if (parts.length >= 2) {
			area = parts[1];
		} else {
			area = "1";

		}
		return true;
	}

	public boolean verify() {

		if (!parseArguments()) {
			LOG.error("arguments not ok . Check your call");
			return false;
		}

		try {
			dnidRoot = Main.getSettings().getProperty("dnidroot");
		} catch (IOException e) {
			LOG.error(e.toString(), e);
			return false;
		}
		File folder = new File(dnidRoot);
		if (!folder.exists()) {
			LOG.error("DNID root does not exist. Nothing to download.");
			return false;
		}

		return true;
	}

	public Response execute() {

		String pattern = new String(BYTE_PATTERN);

		String returnValues = "";

		File folder = new File(dnidRoot);

		File[] dnidFiles = folder.listFiles();

		// for every file in the catalog

		 InmarsatMessageImpl[]  fileMessages = null;

		for (File file : dnidFiles) {

			try {
				byte[] bArr = Files.readAllBytes(file.toPath());
				String fileStr = new String(bArr);
				if (fileStr.indexOf(pattern) >= 0) {
					fileMessages = byteToInmMessge(bArr, fileMessages);
				}

				System.out.println();
			} catch (IOException e) {
			}

		}

		return new Response(returnValues);

	}

	private InmarsatMessageImpl[] byteToInmMessge(byte[] fileArray, InmarsatMessageImpl[] dirMessages) {
		byte[] fileBytes = insertId(fileArray);
		// byte[]fileBytes = fileArray;
		InmarsatMessageImpl[] messages = null;
		Vector v = new Vector();
		PATTERN_LENGTH = BYTE_PATTERN.length;
		// Parse file content for messages
		if (fileBytes != null && fileBytes.length > PATTERN_LENGTH) {
			for (int i = 0; i < (fileBytes.length - PATTERN_LENGTH); i++) {
				// Find message
				if (fileBytes[i] == BYTE_PATTERN[0] && fileBytes[i + 1] == BYTE_PATTERN[1]
						&& fileBytes[i + 2] == BYTE_PATTERN[2] && fileBytes[i + 3] == BYTE_PATTERN[3]) {

					// For debug
					/*
					 * byte[] report = Arrays.copyOfRange(fileBytes, i, i+42);
					 * for (int J = 0; J < report.length; J++) { int num =
					 * Integer.parseInt(String.format("%02X ",
					 * report[J]).trim(), 16); String tmp =
					 * Integer.toBinaryString(num); LOG.debug(J + "\t" +
					 * report[J] + "\t" + String.format("%02X ", report[J])+"\t"
					 * + String.format("%8s", tmp).replace(' ', '0')); }
					 */
					InmarsatMessageImpl iMes;
					iMes = new InmarsatMessageImpl(Arrays.copyOfRange(fileBytes, i, fileBytes.length));

					try {
						if (iMes.validate()) {
							v.add(iMes);
						} else {
							LOG.debug("Message rejected");
						}
					} catch (Exception e) {
						LOG.debug("InmarsatMessages failed Validation", e);
					}

				}
			}
		} else {
			LOG.info("FileByte is null or fileSize to small");
		}

		if (dirMessages != null) {
			v.addAll(Arrays.asList(dirMessages));
		}
		messages = new InmarsatMessageImpl[v.size()];
		v.copyInto(messages);

		return messages;
	}

	public byte[] insertId(byte[] contents) {
		ByteArrayOutputStream output_ = new ByteArrayOutputStream();
		int found = -1;

		for (int i = 0; i < (contents.length - BYTE_PATTERN.length); i++) {
			// Find message
			if (contents[i] == BYTE_PATTERN[0] && contents[i + 1] == BYTE_PATTERN[1]
					&& contents[i + 2] == BYTE_PATTERN[2] && contents[i + 3] == BYTE_PATTERN[3]) {
				found = i;
			}
			if (found >= 0 && found + 20 == i && contents[i] == (byte) 2) {
				output_.write((byte) 255);
				found = -1;
			}
			output_.write(contents[i]);
		}
		return output_.toByteArray();
	}

}
