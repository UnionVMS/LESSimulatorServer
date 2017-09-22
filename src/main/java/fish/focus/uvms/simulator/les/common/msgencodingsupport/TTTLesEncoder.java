package fish.focus.uvms.simulator.les.common.msgencodingsupport;

import java.util.Date;
import org.joda.time.DateTime;
import fish.focus.uvms.simulator.les.common.PackUnpackFunctions;

public class TTTLesEncoder {
	
	private PackUnpackFunctions functions = new PackUnpackFunctions();


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

		byte[] headerType_BA = functions.int2ByteArray(headerType);
		h[4] = headerType_BA[0];

		h[5] = headerLength;

		byte[] refNumber_BA = functions.int2ByteArray(refNumber);
		h[6] = refNumber_BA[0];
		h[7] = refNumber_BA[1];
		h[8] = refNumber_BA[2];
		h[9] = refNumber_BA[3];

		byte[] dataPresentation_BA = functions.int2ByteArray(dataPresentation);
		h[10] = dataPresentation_BA[0];

		//h[11] = int2ByteArray(satId)[0];
		h[11] = 42;

		byte[] messageLength_BA = functions.int2ByteArray(messageLength);
		h[12] = messageLength_BA[0];
		h[13] = messageLength_BA[1];

		byte[] storedTime_BA = functions.date2ByteArray_INHEADER(storedTime);
		h[14] = storedTime_BA[0];
		h[15] = storedTime_BA[1];
		h[16] = storedTime_BA[2];
		h[17] = storedTime_BA[3];

		byte[] dnid_BA = functions.int2ByteArray(dnid);
		h[18] = dnid_BA[0];
		h[19] = dnid_BA[1];

		byte[] memberId_BA = functions.int2ByteArray(memberId);

		h[20] = memberId_BA[0];

		return h;

	}

	public byte[] createBody(int dataReportFormat, Double latitude, Double longitude, int memCode, int dayOfMonth,
			int hour, int minutes, DateTime positionDate, Double speed, Double course) {

		byte[] b = new byte[255];

		byte[] xxxxx = functions.packDataReportingFormatAndLatitude(dataReportFormat, latitude);

		b[0] = functions.int2ByteArray(dataReportFormat)[0];

		byte[] latitude_BA = functions.double2ByteArray(latitude);

		byte[] longitude_BA = functions.double2ByteArray(longitude);

		byte[] memCode_BA = functions.int2ByteArray(memCode);

		byte[] dayOfMonth_BA = functions.int2ByteArray(dayOfMonth);

		byte[] hour_BA = functions.int2ByteArray(hour);

		byte[] minutes_BA = functions.int2ByteArray(minutes);

		byte[] speed_BA = functions.double2ByteArray(speed);

		byte[] course_BA = functions.double2ByteArray(course);

		return b;
	}

}
