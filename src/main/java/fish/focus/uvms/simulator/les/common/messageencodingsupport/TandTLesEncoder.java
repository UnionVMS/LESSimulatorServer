package fish.focus.uvms.simulator.les.common.messageencodingsupport;

public class TandTLesEncoder {

	public byte[] createHeaderTest(byte headerType, String refNumberStr, byte dataPresentation, byte satelliteId,
			String messageLengthStr, String storedTimeStr, String dnidStr, byte memberId) {

		byte[] h = new byte[22];

		byte headerLength = 22;
		byte[] refNumber = refNumberStr.getBytes();
		byte[] messageLength = messageLengthStr.getBytes();
		byte[] storedTime = storedTimeStr.getBytes();
		byte[] dnid = dnidStr.getBytes();

		byte DOT = 1;
		byte T = 84;
		byte AND = 38;
		byte END = 2;

		h[0] = DOT;
		h[1] = T;
		h[2] = AND;
		h[3] = T;
		h[21] = END;

		h[4] = headerType;
		h[5] = headerLength;

		h[6] = refNumber[0];
		h[7] = refNumber[1];
		h[8] = refNumber[2];
		h[9] = refNumber[3];

		h[10] = dataPresentation;

		h[11] = satelliteId;

		h[12] = messageLength[0];
		h[13] = messageLength[1];

		h[14] = storedTime[0];
		h[15] = storedTime[1];
		h[16] = storedTime[2];
		h[17] = storedTime[3];

		h[18] = dnid[0];
		h[19] = dnid[1];

		h[20] = memberId;

		return h;

	}

}
