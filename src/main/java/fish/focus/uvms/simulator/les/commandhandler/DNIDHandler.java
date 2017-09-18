package fish.focus.uvms.simulator.les.commandhandler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fish.focus.uvms.simulator.les.server.Main;
import fish.focus.uvms.simulator.les.server.Response;

public class DNIDHandler {

	public static final String END = "\r\n";
	private static final Logger LOGGER = LoggerFactory.getLogger(DNIDHandler.class);

	private String dnid;
	private String area;
	String dnidRoot = null;
	List<String> arguments;
	String flattenedArgument;

	public DNIDHandler(List<String> arguments) {
		this.arguments = arguments;
	}

	private boolean parseArguments() {

		String arg = "";
		for (String tmp : arguments) {
			if (tmp.equalsIgnoreCase("dnid")) {
				continue;
			}
			arg += tmp.trim();
			arg += " ";
		}
		arg = arg.trim();

		String parts[] = arg.split(" ");
		if (parts.length < 1) {
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

		if (arguments == null || arguments.size() < 2) {
			LOGGER.error("arguments is null. Cannot do anything meaningsful");
			return false;
		}
		if (!parseArguments()) {
			LOGGER.error("arguments not ok . Check your call");
			return false;
		}

		try {
			dnidRoot = Main.getSettings().getProperty("dnidroot");
		} catch (IOException e) {
			LOGGER.error(e.toString(), e);
			return false;
		}
		File folder = new File(dnidRoot);
		if (!folder.exists()) {
			LOGGER.error("DNID root does not exist. Nothing to download.");
			return false;
		}

		return true;
	}

	public Response execute() {

		String returnValues = "";

		File folder = new File(dnidRoot);

		File[] dnidFiles = folder.listFiles();

		// for every file in the catalog

		for (File file : dnidFiles) {

			String fileName = file.getAbsolutePath();
			// put the file in a list
			List<String> theFile = new ArrayList<>();
			BufferedReader br = null;
			try {
				
				br = new BufferedReader(new InputStreamReader(
					    new FileInputStream(fileName), "UTF-8"));
				String currentLine;
				while ((currentLine = br.readLine()) != null) {
					theFile.add(currentLine);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					br.close();
				} catch (IOException e) {
				}
			}
			// and close the stream

			/*
			try {
				byte[] data = Files.readAllBytes(file.toPath());
				System.out.println();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			*/
			
			

			// at least 3 lines header data 1 . . . . n and end line
			if (theFile.size() < 3)
				continue;

			// line 1 = DNID and area
			String dnidInFile = "";
			String areaInFile = "";

			boolean firstLine = true;
			for (String lineInFile : theFile) {

				// first line take out the dnid and area from the file
				if (firstLine) {
					String line1 = lineInFile;
					String parts[] = line1.split(" ");
					if (parts.length <= 1) {
						continue;
					}
					if (parts.length > 1) {
						dnidInFile = parts[1];
					}
					if (parts.length > 2) {
						areaInFile = parts[2];
					} else {
						areaInFile = "1";
					}
					firstLine = false;
					continue;
				}
				// check if line with human crap readable stuff
				if (lineInFile.startsWith("Retrieving")) {
					continue;
				}
				// check if last line lastLine
				if (lineInFile.startsWith(">")) {
					break;
				}

				// TODO: enhance with area as well ???
				// if the file dnid is what we want take it
				if (dnid.toLowerCase().trim().equals(dnidInFile.toLowerCase().trim())) {
					returnValues += lineInFile;
				}
			}
		}
		return new Response(returnValues);

	}

}
