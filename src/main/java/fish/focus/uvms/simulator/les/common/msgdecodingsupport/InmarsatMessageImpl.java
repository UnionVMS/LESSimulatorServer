package fish.focus.uvms.simulator.les.common.msgdecodingsupport;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InmarsatMessageImpl implements InmarsatMessage {

	final static Logger LOG = LoggerFactory.getLogger(InmarsatMessageImpl.class);
	private byte[] message = null;
	private InmarsatHeader header = null;
	private InmarsatBody body = null;

	public InmarsatMessageImpl(byte[] bArr) {
		setMessage(bArr);
	}

	private void setMessage(byte[] bArr) {
		this.message = bArr;
		header = new InmarsatHeader(message);
		try {

			if (header.validate()) {
				int start = header.getHeaderLength();
				int end = header.getMessageLength();

				body = new InmarsatBody(Arrays.copyOfRange(message, start, start + end));

				// For debug
				/*
                byte[] report = Arrays.copyOfRange(message, 0, start+end);
                for (int i = 0; i < report.length; i++) {
                    int num = Integer.parseInt(String.format("%02X ", report[i]).trim(), 16);
                    String tmp = Integer.toBinaryString(num);
                    LOG.debug(i + "\t" + report[i] + "\t" + String.format("%02X ", report[i])+"\t" + String.format("%8s", tmp).replace(' ', '0'));
                }
                printReport();
				 */
			}

		} catch (Exception e) {
			LOG.debug("setMessage header probably failed", e);
		}
	}

	public InmarsatHeader getHeader() {
		return this.header;
	}

	public InmarsatBody getBody() {
		return this.body;
	}

	public void printReport() {
		LOG.debug("ApiHeader: " + getHeader().getheaderType());
		LOG.debug("Header length: " + getHeader().getHeaderLength());
		LOG.debug("Message Length: " + getHeader().getMessageLength());
		LOG.debug("MessageRef: " + getHeader().getMsgRefNr());
		LOG.debug("SatId: " + getHeader().getSatId());
		LOG.debug("Stored time: " + getHeader().getStoredTime());
		LOG.debug("Dnid: " + getHeader().getDnid());
		LOG.debug("MemberId: " + getHeader().getMemberId());
		LOG.debug("ReportDateFormat: " + getBody().getDataReportFormat());
		LOG.debug("Latidude: " + getBody().getLatitude());
		LOG.debug("Longitude: " + getBody().getLongitude());
		LOG.debug("Memcode: " + getBody().getMemCode());
		LOG.debug("DayOfMonth: " + getBody().getDayOfMonth());
		LOG.debug("Hour: " + getBody().getHour());
		LOG.debug("Minutes: " + getBody().getMinutes());
		LOG.debug("Speed: " + getBody().getSpeed());
		LOG.debug("Course: " + getBody().getCourse());
	}

	@Override
	public boolean validate() throws Exception {
		return header.validate();
	}

}