package fish.focus.uvms.simulator.les.telnetclient;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

import eu.europa.ec.fisheries.schema.exchange.plugin.types.v1.PollType;
import eu.europa.ec.fisheries.schema.exchange.plugin.types.v1.PollTypeType;
import fish.focus.uvms.simulator.les.InmarsatPoll;
import fish.focus.uvms.simulator.les.InmarsatPoll.OceanRegion;
import fish.focus.uvms.simulator.les.TelnetException;

public class CommandPoll {

	private CommandHelper commandHelper = new CommandHelper();

	public String sendPollCommand(PollType poll, PrintStream out, InputStream in, FileOutputStream stream, String url,
			String port) throws IOException, TelnetException {
		for (OceanRegion oceanRegion : OceanRegion.values()) {
			String result = sendPollCommand(poll, out, in, stream, oceanRegion, url, port);
			if (result.contains("Reference number")) {
				return result;
			}
		}

		return null;
	}

	private String sendPollCommand(PollType poll, PrintStream out, InputStream in, FileOutputStream stream,
			OceanRegion oceanRegion, String url, String port) throws TelnetException, IOException {
		String prompt = ">";
		String cmd = buildCommand(poll, oceanRegion);
		String ret;
		commandHelper.write(cmd, out);
		ret = commandHelper.readUntil("Text:", in, stream, url, port);
		if (ret != null) {
			commandHelper.write(".S", out);
			ret += commandHelper.readUntil(prompt, in, stream, url, port);
		}
		return ret;
	}

	private String buildCommand(PollType poll, OceanRegion oceanRegion) {
		InmarsatPoll inmPoll = new InmarsatPoll();
		inmPoll.setPollType(poll);
		inmPoll.setOceanRegion(oceanRegion);
		if (poll.getPollTypeType() == PollTypeType.CONFIG)
			inmPoll.setAck(InmarsatPoll.AckType.TRUE);
		return inmPoll.asCommand();
	}

}
