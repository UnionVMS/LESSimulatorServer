package fish.focus.uvms.simulator.les;

import java.util.Calendar;
import java.util.Date;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import fish.focus.uvms.simulator.les.common.msgdecodingsupport.InmarsatBody;
import fish.focus.uvms.simulator.les.common.msgdecodingsupport.InmarsatHeader;
import fish.focus.uvms.simulator.les.common.msgencodingsupport.TTTLesEncoder;

public class TestTTLesMessageEncoder {

	private static final Logger LOGGER = LoggerFactory.getLogger(TestTTLesMessageEncoder.class);

	@Test
	public void examineWhatIsHappening() {

		int test = 17;
		byte aByte = (byte) test;

		int result1 = Integer.MIN_VALUE;;
		int result2 = Integer.MIN_VALUE;

		{ // dissekerad kod
			String tmp1 = String.format("%02X ", aByte);
			String tmp2 = tmp1.trim();

			int num = Integer.parseInt(tmp2, 16);

			String s = Integer.toBinaryString(num);
			String s2 = String.format("%8s", s);
			String s3 = s2.replace(' ', '0');
			int i = Integer.parseInt(s3.substring(0, 2), 2);

			LOGGER.info("'" + tmp1 + "'");
			LOGGER.info("'" + tmp2 + "'");
			LOGGER.info(num + "");
			LOGGER.info(s);
			LOGGER.info(s2);
			LOGGER.info(s3);
			result1 = i;
		}
		// original code in inmarsatBody
		{
			int num = Integer.parseInt(String.format("%02X ", aByte).trim(), 16);
			String s = Integer.toBinaryString(num);
			s = String.format("%8s", s).replace(' ', '0');
			int i = Integer.parseInt(s.substring(0, 2), 2);
			result2 = i;
		}

		LOGGER.info(result1 + "");
		LOGGER.info(result2 + "");

		Assert.assertTrue(result1 == result2);

	}

	@Test
	public void createHeader() {

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 1999);
		cal.set(Calendar.MONTH, Calendar.DECEMBER);
		cal.set(Calendar.DAY_OF_MONTH, 31);
		Date storedTime = cal.getTime();

		TTTLesEncoder enc = new TTTLesEncoder();

		int headerType = 4;
		int refNumber = 847842;
		int dataPresentation = 2;
		int satId = 42; // ?????
		int messageLength = 282;
		int dnid = 10745;
		int memberId = 4;

		byte[] header = enc.createHeader(headerType, refNumber, dataPresentation, satId, messageLength, storedTime,
				dnid, memberId);

		InmarsatHeader iHeader = new InmarsatHeader(header);

		int ht = Integer.parseInt(iHeader.getheaderType());
		Assert.assertTrue(ht == headerType);
		Assert.assertTrue(iHeader.getMsgRefNr() == refNumber);
		Assert.assertTrue(iHeader.getDataPresentation() == dataPresentation);
		// assert(iHeader.getSatId() == satId); // ???????
		Assert.assertTrue(iHeader.getMessageLength() == messageLength);
		Assert.assertTrue(iHeader.getDnid() == dnid);
		Assert.assertTrue(iHeader.getMemberId() == memberId);

		System.out.println(iHeader.toString());

	}

	@Test
	public void testHeader() {

		byte[] header = "T&T4c D Q©Yù)€Š    T&Tïc D Z©Yù)€Š    ".getBytes();
		InmarsatHeader inmarsatHeader = new InmarsatHeader(header);

		System.out.println(inmarsatHeader.toString());

	}

	@Test
	public void setDataReportFormat() {
		byte data = 3;
		int num = Integer.parseInt(String.format("%02X ", data).trim(), 16);
		String s = Integer.toBinaryString(num);
		s = String.format("%8s", s).replace(' ', '0');
		int i = Integer.parseInt(s.substring(0, 2), 2);

		String bin = Integer.toBinaryString(data);
		String bin2 = bin.substring(0, 2);
		byte theByte = Byte.parseByte(bin2, 2);

		System.out.println();

	}

	@Test
	public void testBody() {

		TTTLesEncoder enc = new TTTLesEncoder();

		int dataReportFormat = 3;
		Double latitude = 57.500700;
		Double longitude = 11.491699;
		int memCode = Integer.MIN_VALUE;
		int dayOfMonth = 7;
		int hour = 6;
		int minutes = 30;
		DateTime positionDate = DateTime.now();
		Double speed = 5.0;
		Double course = 6.0;

		byte[] body = enc.createBody(dataReportFormat, latitude, longitude, memCode, dayOfMonth, hour, minutes,
				positionDate, speed, course);

		InmarsatBody inmarsatBody = new InmarsatBody(body);

		System.out.println(inmarsatBody.toString());

	}

}
