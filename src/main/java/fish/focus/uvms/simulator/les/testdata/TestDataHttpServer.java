package fish.focus.uvms.simulator.les.testdata;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import fish.focus.uvms.simulator.les.server.Main;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestDataHttpServer {

	private static final Logger LOG = LoggerFactory.getLogger(TestDataHttpServer.class);

	private static TestDataHttpServer instance = null;
	private static HttpServer server = null;
	private static final String CONTEXT_NAF = "/nafdata";

	public static synchronized TestDataHttpServer getInstance() {
		if (instance == null) {
			instance = new TestDataHttpServer();
		}
		return instance;
	}

	/**
	 * returns the url parameters in a map
	 *
	 * @param query
	 * @return map
	 */
	public static Map<String, String> queryToMap(String query) {
		Map<String, String> result = new HashMap<String, String>();
		for (String param : query.split("&")) {
			String pair[] = param.split("=");
			if (pair.length > 1) {
				result.put(pair[0], pair[1]);
			} else {
				result.put(pair[0], "");
			}
		}
		return result;
	}

	private TestDataHttpServer() {

		try {
			Properties settings = Main.getSettings();
			String port = settings.getProperty(Main.ARG_PORT_TESTDATA);

			server = HttpServer.create(new InetSocketAddress(Integer.parseInt(port)), 0);
			server.createContext(CONTEXT_NAF, new NafDataHandler());
			server.setExecutor(null); // creates a default executor
			server.start();
			LOG.info("TestdataServer started and listening on port:{} and context:{}", port, CONTEXT_NAF);

		} catch (IOException ex) {
			LOG.error("Couldn't start Test data server", ex);
		}
	}

	static class NafDataHandler implements HttpHandler {

		@Override
		public void handle(HttpExchange t) throws IOException {
			Map<String, String> parms = queryToMap(t.getRequestURI().getQuery());
			LOG.debug("NAF data:{}", parms.get("naf"));
			// TODO:Handle the nafdata

			byte[] response = ("Please send in test data in NAF format like this: http://localhost:"
					+ server.getAddress().getPort() + CONTEXT_NAF + "[/]?naf=<NAFDATA>").getBytes();
			t.sendResponseHeaders(200, response.length);

			OutputStream os = t.getResponseBody();
			os.write(response);
			os.close();
		}
	}
}
