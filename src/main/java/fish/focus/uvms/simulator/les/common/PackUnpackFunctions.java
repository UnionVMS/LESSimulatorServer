package fish.focus.uvms.simulator.les.common;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Date;

public class PackUnpackFunctions {

	// ---------------------------------------
	// pack
	// ---------------------------------------

	public byte[] int2ByteArray(int myInteger) {
		return ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(myInteger).array();
	}

	public byte[] date2ByteArray_INHEADER(Date aDate) {
		byte[] arr = new byte[4];
		Long l = aDate.getTime() / 1000;
		byte[] wrk = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN).putLong(l).array();

		arr[0] = wrk[0];
		arr[1] = wrk[1];
		arr[2] = wrk[2];
		arr[3] = wrk[3];
		return arr;
	}

	public byte[] double2ByteArray(double myDouble) {
		return ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN).putDouble(myDouble).array();
	}

	private byte[] packDataReportingFormatAndLatitude(int dataReportFormat, Double latitude) {

		/*
		 * String s = Integer.toBinaryString(num); String s2 = String.format("%8s", s); String s3 = s2.replace(' ',
		 * '0'); int i = Integer.parseInt(s3.substring(0, 2), 2);
		 * 
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
		
		return null;


	}



	// ---------------------------------------
	// unpack
	// ---------------------------------------

	public int byteInHex2Int(byte theByte) {
		return Integer.parseInt(String.format("%02X ", theByte).trim(), 16);
	}

	public String int2Binary(int theInteger) {
		String binaryString = Integer.toBinaryString(theInteger);
		binaryString = String.format("%8s", binaryString).replace(' ', '0');
		return binaryString;
	}

	public int binaryStringPart2Int(String binaryString, int startPos, int endPos) {
		return Integer.parseInt(binaryString.substring(startPos, endPos), 2);
	}

	public int binaryStringPart2Int(String binaryString, int startPos) {
		return Integer.parseInt(binaryString.substring(startPos), 2);
	}



}
