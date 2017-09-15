package fish.focus.uvms.simulator.les.server;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fish.focus.uvms.simulator.les.commandhandler.DNIDHandler;
import fish.focus.uvms.simulator.les.commandhandler.POLLHandler;
import java.util.logging.Level;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class Main {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    private static Properties settings = new Properties();

    public static Properties getSettings() {
        return settings;
    }

    private static void loadProperties() throws IOException {

        InputStream is = Main.class.getClassLoader().getResourceAsStream("settings.properties");
        if (is == null) {
            throw new IOException();
        }
        settings.load(is);
    }

    /**
     * @param args
     */
    public static void main(String[] args) {

        try {
            loadProperties();
        } catch (IOException e) {
            LOGGER.error("settings.properties not found. cannot start");
            System.exit(-1);
        }

        String portStr = settings.getProperty("server.port");
        int port = Integer.MIN_VALUE;
        try {
            port = Integer.parseInt(portStr);
        } catch (NumberFormatException nfe) {
            LOGGER.error("Invalid port.  Check settings.properties");
            System.exit(-1);
        }

        CommandLineParser parser = new DefaultParser();

        Options options = new Options();

        options.addOption(Option.builder("h")
                .longOpt("help")
                .desc("print this message")
                .required(false)
                .build());

        options.addOption(Option.builder("p")
                .required(false)
                .longOpt("port")
                .desc("the telnet port for the simulator")
                .hasArg()
                .numberOfArgs(1)
                .argName("PORT")
                .build());

        options.addOption(Option.builder("d")
                .required(false)
                .longOpt("dnid")
                .desc("a semicolon separted list of DNIDS ex '10095,10096'")
                .hasArg()
                .valueSeparator(',')
                .argName("DNID")
                .build());

        try {
            CommandLine line = parser.parse(options, args);

            if (line.hasOption("port")) {
                port = Integer.parseInt(line.getOptionValue("port"));
                LOGGER.info("Use of none default port: {}", line.getOptionValue("port"));
            } else {
                LOGGER.info("Use of default port: {}", port);

            }

            if (line.hasOption("dnid")) {
                //TODO use dnid values
                LOGGER.info("Use of none default DNID {}", line.getOptionValue("dnid"));
            }
            if (line.hasOption("help")) {
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("LesSimulator Server", options);
                return;
            }

        } catch (ParseException exp) {
            LOGGER.error("Unexpected exception:", exp);
            return;
        }

        Server server = new Server(port);

        server.registerCommand(new Command("POLL") {
            @Override
            public Response handle(List<String> arguments) {
                if (arguments.size() > 0) {
                    POLLHandler pollHandler = new POLLHandler(arguments);
                    if (pollHandler.verify()) {
                        return pollHandler.execute();
                    } else {
                        return new Response("POLL request not OK >");
                    }
                } else {
                    return new Response("No arguments passed in POLL command >");
                }
            }

        });

        server.registerCommand(new Command("DNID") {
            @Override
            public Response handle(List<String> arguments) {
                if (arguments.size() > 0) {
                    DNIDHandler dnidHandler = new DNIDHandler(arguments);
                    if (dnidHandler.verify()) {
                        return dnidHandler.execute();
                    } else {
                        return new Response("DNID request not OK >");
                    }
                } else {
                    return new Response("No arguments passed in DNID command >");
                }
            }

        });

        server.registerCommand(new Command("quit") {

            @Override
            public Response handle(List<String> arguments) {
                return new Response("bye", false);
            }

        });

        server.start();
        System.out.println("Server running");

    }

}
