package fish.focus.uvms.simulator.les;

import java.util.Calendar;
import java.util.Date;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import fish.focus.uvms.simulator.les.common.PackUnpackFunctions;
import fish.focus.uvms.simulator.les.common.msgdecodingsupport.InmarsatBody;
import fish.focus.uvms.simulator.les.common.msgdecodingsupport.InmarsatHeader;
import fish.focus.uvms.simulator.les.common.msgencodingsupport.TTTLesEncoder;

public class TestTTLesMessageEncoder {

	private PackUnpackFunctions fnc = new PackUnpackFunctions();

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
	public void packLongitude() {

		float longitude = 11.543884f;
		System.out.println(longitude);
		LOGGER.info("original longitude                 : " + longitude);

		String longitudeAsHex = fnc.float2HexString(longitude);
		LOGGER.info("original longitude as hex          : " + longitudeAsHex);
		Long i = Long.parseLong(longitudeAsHex, 16);
		Float f = Float.intBitsToFloat(i.intValue());
		LOGGER.info("original longitude converted back  : " + f);

		String part1 = longitudeAsHex.substring(0, 2);
		String part2 = longitudeAsHex.substring(2, 4);
		String part3 = longitudeAsHex.substring(4, 6);
		String part4 = longitudeAsHex.substring(6, 8);

		/*
		 * NO byte b1 = (byte) (part1.getBytes()[0] + part1.getBytes()[1]); byte b2 = (byte) (part2.getBytes()[0] +
		 * part2.getBytes()[1]); byte b3 = (byte) (part3.getBytes()[0] + part3.getBytes()[1]); byte b4 = (byte)
		 * (part4.getBytes()[0] + part4.getBytes()[1]);
		 * 
		 */

		/* this gives the same output as input BUT not correct longitude */
		byte b1 = part1.getBytes()[0];
		byte b2 = part2.getBytes()[0];
		byte b3 = part3.getBytes()[0];
		byte b4 = part4.getBytes()[0];

		byte[] body = new byte[4];
		body[0] = b1;
		body[1] = b2;
		body[2] = b3;
		body[3] = b4;

		char hemisphere = 'E';
		int num = Integer.parseInt(String.format("%02X ", body[0]).trim(), 16);

		LOGGER.info("body[0]                            : " + body[0]);
		LOGGER.info("unpacked first digit               : " + num);

		String tmp = Integer.toBinaryString(num);
		String s = String.format("%8s", tmp).replace(' ', '0');

		num = Integer.parseInt(String.format("%02X ", body[1]).trim(), 16);

		LOGGER.info("body[1]                            : " + body[1]);
		LOGGER.info("unpacked second digit              : " + num);

		tmp = Integer.toBinaryString(num);
		s += String.format("%8s", tmp).replace(' ', '0');

		num = Integer.parseInt(String.format("%02X ", body[2]).trim(), 16);

		LOGGER.info("body[2]                            : " + body[2]);
		LOGGER.info("unpacked third  digit              : " + num);

		tmp = Integer.toBinaryString(num);

		s += String.format("%8s", tmp).replace(' ', '0');

		num = Integer.parseInt(String.format("%02X ", body[3]).trim(), 16);

		LOGGER.info("body[3]                            : " + body[3]);
		LOGGER.info("unpacked fourth digit              : " + num);

		tmp = Integer.toBinaryString(num);
		s += String.format("%8s", tmp).replace(' ', '0');

		int ii = Integer.parseInt(s.substring(5, 6), 2);
		if (ii > 0) {
			hemisphere = 'W';
		}

		int lonDeg = Integer.parseInt(s.substring(6, 14), 2);
		int lonMin = Integer.parseInt(s.substring(14, 20), 2);
		int lonFrac = Integer.parseInt(s.substring(20, 25), 2) * 4;

		double lonMinFrac = (double) lonMin + ((double) lonFrac / 100);
		Double d = (double) lonDeg + (lonMinFrac / 60);
		if (ii > 0) {
			d *= -1.0;
		}

		LOGGER.info("unpacked all                       : " + d);
	}



	@Test
	public void packOcean() {

		// 1 - 63
		// 100 - 163
		// 200 - 263
		// 300 - 363

		int satId = 305;
		String all = Integer.toBinaryString(satId);

		String ocean = all.substring(0, 2);
		String id = all.substring(2);

		System.out.println(ocean);
		System.out.println(id);
	}


	public byte[] createHeader_helper(Date storedTime, int headerType, int refNumber, int dataPresentation, int satId, int messageLength, int dnid, int memberId){
		TTTLesEncoder enc = new TTTLesEncoder();

		byte[] header = enc.createHeader(headerType, refNumber, dataPresentation, satId, messageLength, storedTime,
				dnid, memberId);
		return header;
	}
	

	@Test
	public void createHeader() {
		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 1999);
		cal.set(Calendar.MONTH, Calendar.DECEMBER);
		cal.set(Calendar.DAY_OF_MONTH, 31);
		Date storedTime = cal.getTime();
		int headerType = 4;
		int refNumber = 847842;
		int dataPresentation = 2;
		int satId = 100; // ?????
		int messageLength = 282;
		int dnid = 10745;
		int memberId = 4;

		byte[] header = createHeader_helper(storedTime,headerType,refNumber,dataPresentation,satId,messageLength,dnid, memberId);

		InmarsatHeader iHeader = new InmarsatHeader(header);

		int ht = Integer.parseInt(iHeader.getheaderType());
		Assert.assertTrue(ht == headerType);
		Assert.assertTrue(iHeader.getMsgRefNr() == refNumber);
		Assert.assertTrue(iHeader.getDataPresentation() == dataPresentation);
		// assert(iHeader.getSatId() == satId); // ???????
		Assert.assertTrue(iHeader.getMessageLength() == messageLength);
		Assert.assertTrue(iHeader.getDnid() == dnid);
		Assert.assertTrue(iHeader.getMemberId() == memberId);

		LOGGER.info(iHeader.toString());

	}
	
	
	@Test
	public void createAndVerifyHeader() {
		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 2025);
		cal.set(Calendar.MONTH, Calendar.FEBRUARY);
		cal.set(Calendar.DAY_OF_MONTH, 20);
		Date storedTime = cal.getTime();
		int headerType = 2;
		int refNumber = 42;
		int dataPresentation = 1;
		int satId = 100; // ?????
		int messageLength = 22;
		int dnid = 145;
		int memberId = 2;

		byte[] header = createHeader_helper(storedTime,headerType,refNumber,dataPresentation,satId,messageLength,dnid, memberId);
		InmarsatHeader iHeader = new InmarsatHeader(header);


		try {
			Assert.assertTrue(iHeader.validate());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		LOGGER.info(iHeader.toString());
	}

	

	@Test
	public void testHeader() {

		byte[] header = "T&T4c D Q©Yù)".getBytes();
		InmarsatHeader inmarsatHeader = new InmarsatHeader(header);

		LOGGER.info(inmarsatHeader.toString());
	}

	@Test
	public void testBody() {

		byte[] body = "€Š    T&Tïc D Z©Yù)€Š    ".getBytes();
		InmarsatBody inmarsatBody = new InmarsatBody(body);

		LOGGER.info(inmarsatBody.toString());

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


	}

	@Test
	public void createBody() {

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
