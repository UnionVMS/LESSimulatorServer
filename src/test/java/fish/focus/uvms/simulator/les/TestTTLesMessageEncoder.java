package fish.focus.uvms.simulator.les;

import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

import fish.focus.uvms.simulator.les.common.msgdecodingsupport.InmarsatHeader;
import fish.focus.uvms.simulator.les.common.msgencodingsupport.TTTLesEncoder;

public class TestTTLesMessageEncoder {

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
		int satelliteId = 34; // ?????
		int messageLength = 282;
		int dnid = 10745;
		int memberId = 4;

		byte[] header = enc.createHeader(headerType, refNumber, dataPresentation, satelliteId, messageLength,
				storedTime, dnid, memberId);

		InmarsatHeader iHeader = new InmarsatHeader(header);
		
		int ht = Integer.parseInt(iHeader.getheaderType());
		assert(ht == headerType);
		assert(iHeader.getMsgRefNr() == refNumber);
		assert(iHeader.getDataPresentation() == dataPresentation);
		//assert(iHeader.getSatId() == satelliteId);                                   // ???????
		assert(iHeader.getMessageLength() == messageLength);
		assert(iHeader.getDnid() == dnid);
		assert(iHeader.getMemberId() == memberId);
		
		
		
		
		
		System.out.println(iHeader.toString());

	}

	@Test
	public void testHeader() {

		byte[] header = "T&T4c D Q©Yù)€Š    T&Tïc D Z©Yù)€Š    ".getBytes();
		InmarsatHeader inmarsatHeader = new InmarsatHeader(header);

		System.out.println(inmarsatHeader.toString());

	}

}
