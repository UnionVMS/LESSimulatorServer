package fish.focus.uvms.simulator.les.common.msgencodingsupport;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Date;
import org.joda.time.DateTime;

public class TTTLesEncoder {

	private byte[] int2ByteArray(int myInteger) {
		return ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(myInteger).array();
	}

	private byte[] date2ByteArray_INHEADER(Date aDate) {
		byte[] arr = new byte[4];
		Long l = aDate.getTime() / 1000;
		byte[] wrk = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN).putLong(l).array();

		arr[0] = wrk[0];
		arr[1] = wrk[1];
		arr[2] = wrk[2];
		arr[3] = wrk[3];
		return arr;
	}

	private byte[] double2ByteArray(double myDouble) {
		return ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN).putDouble(myDouble).array();
	}

	public byte[] createHeader(int headerType, int refNumber, int dataPresentation, int satId, int messageLength,
			Date storedTime, int dnid, int memberId) {

		byte[] h = new byte[23];

		byte headerLength = 22;

		byte DOT = 1;
		byte T = 84;
		byte AND = 38;
		byte END = 2;

		h[0] = DOT;
		h[1] = T;
		h[2] = AND;
		h[3] = T;
		h[21] = END;

		byte[] headerType_BA = int2ByteArray(headerType);
		h[4] = headerType_BA[0];

		h[5] = headerLength;

		byte[] refNumber_BA = int2ByteArray(refNumber);
		h[6] = refNumber_BA[0];
		h[7] = refNumber_BA[1];
		h[8] = refNumber_BA[2];
		h[9] = refNumber_BA[3];

		byte[] dataPresentation_BA = int2ByteArray(dataPresentation);
		h[10] = dataPresentation_BA[0];

		//h[11] = int2ByteArray(satId)[0];
		h[11] = 42;

		byte[] messageLength_BA = int2ByteArray(messageLength);
		h[12] = messageLength_BA[0];
		h[13] = messageLength_BA[1];

		byte[] storedTime_BA = date2ByteArray_INHEADER(storedTime);
		h[14] = storedTime_BA[0];
		h[15] = storedTime_BA[1];
		h[16] = storedTime_BA[2];
		h[17] = storedTime_BA[3];

		byte[] dnid_BA = int2ByteArray(dnid);
		h[18] = dnid_BA[0];
		h[19] = dnid_BA[1];

		byte[] memberId_BA = int2ByteArray(memberId);

		h[20] = memberId_BA[0];

		return h;

	}

	private void pack(int dataReportFormat, Double latitude) {

		/*
		 * 			String s = Integer.toBinaryString(num);
			String s2 = String.format("%8s", s);
			String s3 = s2.replace(' ', '0');
			int i = Integer.parseInt(s3.substring(0, 2), 2);
		
		 */

		String reportFormat = Integer.toBinaryString(dataReportFormat);
		byte[] latitude_BA = double2ByteArray(latitude);

		String byte1 = Integer.toBinaryString(latitude_BA[0]);
		String byte2 = Integer.toBinaryString(latitude_BA[1]);
		String byte3 = Integer.toBinaryString(latitude_BA[2]);
		String byte4 = Integer.toBinaryString(latitude_BA[3]);
		String byte5 = Integer.toBinaryString(latitude_BA[4]);
		String byte6 = Integer.toBinaryString(latitude_BA[5]);
		String byte7 = Integer.toBinaryString(latitude_BA[6]);
		String byte8 = Integer.toBinaryString(latitude_BA[7]);

		System.out.println();

	}

	public byte[] createBody(int dataReportFormat, Double latitude, Double longitude, int memCode, int dayOfMonth,
			int hour, int minutes, DateTime positionDate, Double speed, Double course) {

		byte[] b = new byte[255];

		pack(dataReportFormat, latitude);

		b[0] = int2ByteArray(dataReportFormat)[0];

		byte[] latitude_BA = double2ByteArray(latitude);

		byte[] longitude_BA = double2ByteArray(longitude);

		byte[] memCode_BA = int2ByteArray(memCode);

		byte[] dayOfMonth_BA = int2ByteArray(dayOfMonth);

		byte[] hour_BA = int2ByteArray(hour);

		byte[] minutes_BA = int2ByteArray(minutes);

		byte[] speed_BA = double2ByteArray(speed);

		byte[] course_BA = double2ByteArray(course);

		return b;
	}

}
