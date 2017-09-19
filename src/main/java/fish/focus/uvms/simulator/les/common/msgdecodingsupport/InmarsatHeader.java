package fish.focus.uvms.simulator.les.common.msgdecodingsupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class InmarsatHeader implements InmarsatMessage{
    final static Logger LOG = LoggerFactory.getLogger(InmarsatHeader.class);
    final static int apiHeaderLength = 22;
    private String headerType;
    private int headerLength;
    private int msgRefNr;
    private int dataPresentation;
    private int satId = -1; //Ocean region
    private int lesId = -1;
    private int msgLength;
    private Date storedTime;
    private int dnid;
    private int membId;
    private byte [] header;
    
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss");
    
    public InmarsatHeader(byte[] message){
        setHeader(message);
    }
    
    private void setHeader(byte[] message) {
        if(message!=null && message.length >apiHeaderLength){
            this.header = Arrays.copyOfRange(message, 0, apiHeaderLength);
        }
    }
    public String getheaderType() {
        headerType = String.format("%02X ", header[4]).trim();
        return headerType;
    }

    public int getHeaderLength() {
        String hex = String.format("%02X ", header[5]);
        return Integer.parseInt(hex.trim(), 16);
    }
    public int getMsgRefNr() {
        String hex = String.format("%02X ", header[9]).trim();
        hex += String.format("%02X ", header[8]).trim();
        hex += String.format("%02X ", header[7]).trim();
        hex += String.format("%02X ", header[6]).trim();
        return Integer.parseInt(hex, 16);
    }
     public int getDataPresentation() {
        String hex = String.format("%02X ", header[10]).trim();
        return Integer.parseInt(hex, 16);
    }
    public int getSatId() {
        int num = Integer.parseInt(String.format("%02X ", header[11]).trim(), 16);
        String s = Integer.toBinaryString(num);
        this.satId = Integer.parseInt(s.substring(0, 2), 2);
        this.lesId = Integer.parseInt(s.substring(2), 2);
        return satId;
    }
    public int getLesId(){
        if(lesId<0)
          getSatId();
        return lesId;
    }
    public int getMessageLength() {
        String hex = String.format("%02X ", header[13]).trim();
        hex += String.format("%02X ", header[12]).trim();
        return Integer.parseInt(hex, 16);
    }
    //Stored at LES
    public Date getStoredTime() {
        String hex = String.format("%02X ", header[17]).trim();
        hex += String.format("%02X ", header[16]).trim();
        hex += String.format("%02X ", header[15]).trim();
        hex += String.format("%02X ", header[14]).trim();
        Long l = Long.parseLong(hex, 16);
        storedTime = new Date(l * 1000);
        return storedTime;
    }
    
    public String getFormattedDate(Date d){
        return sdf.format(d);
    }
    
    public int getDnid() {
        String hex = String.format("%02X ", header[19]).trim();
        hex += String.format("%02X ", header[18]).trim();
        return Integer.parseInt(hex, 16);
    }

    public int getMemberId() {
        String hex = String.format("%02X ", header[20]).trim();
        return Integer.parseInt(hex, 16);
    }
    
    @Override
    public boolean validate() throws Exception {
        byte dot=1;
        byte t = 84;
        byte and = 38;
        byte end = 2;
        boolean ret = false;
        if(header==null || header.length!=apiHeaderLength){
            LOG.debug("header validation failed is either null or to short");
            return ret;
        }
        else if(header[0]!=dot||header[1]!=t||header[2]!=and||header[3]!=t||header[21]!=end){
            LOG.debug("header validation failed format check");
            return ret;
        }
        return true;
    }
}