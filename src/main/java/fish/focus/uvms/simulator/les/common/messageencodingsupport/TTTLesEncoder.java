package fish.focus.uvms.simulator.les.common.messageencodingsupport;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Date;

public class TTTLesEncoder {

	private byte[] int2ByteArray(int myInteger) {
		return ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(myInteger).array();
	}

	private byte[] date2BA(Date aDate) {
		byte[] arr = new byte[4];
		Long l = aDate.getTime() / 1000;
		byte[] wrk = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN).putLong(l).array();

		arr[0] = wrk[0];
		arr[1] = wrk[1];
		arr[2] = wrk[2];
		arr[3] = wrk[3];
		return arr;
	}

	public byte[] createHeader(int headerType, int refNumber, int dataPresentation, int satelliteId, int messageLength,
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

		int satIdHex = Integer.valueOf(String.valueOf(satelliteId), 16);
		byte[] satelliteId_BA = int2ByteArray(satIdHex);
		h[11] = satelliteId_BA[0];

		byte[] messageLength_BA = int2ByteArray(messageLength);
		h[12] = messageLength_BA[0];
		h[13] = messageLength_BA[1];

		byte[] storedTime_BA = date2BA(storedTime);
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

}
