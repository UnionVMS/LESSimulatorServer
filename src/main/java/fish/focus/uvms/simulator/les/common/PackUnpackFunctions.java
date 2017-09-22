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

	public byte[] float2ByteArray(float myFloat) {
		return ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putFloat(myFloat).array();
	}

	public String double2HexString(double theDouble) {
		long doubleAsLong = Double.doubleToRawLongBits(theDouble);
		return Long.toHexString(doubleAsLong);
	}

	public String float2HexString(float theFloat) {
		long floatAsLong = Float.floatToRawIntBits(theFloat);
		return Long.toHexString(floatAsLong);
	}

	/*
	 * public String doubleHexString2binaryString(String doubleHexString) { long num = (Long.parseLong(doubleHexString,
	 * 16)); return Long.toBinaryString(num); }
	 * 
	 * public String floatHexString2binaryString(String floatHexString) { long num = (Long.parseLong(floatHexString,
	 * 16)); return Long.toBinaryString(num); }
	 */

	public float byteArray2Float(byte[] byteArray) {
		return ByteBuffer.wrap(byteArray).order(ByteOrder.LITTLE_ENDIAN).getFloat();
	}



	public byte[] packLongitude2Bytes(Float latitude) {


		return null;
	}


	public byte[] packDataReportingFormatAndLatitude2Bytes(int dataReportFormat, Float latitude) {


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
