package fish.focus.uvms.simulator.les.server;

public class Response {
	private byte[] response = null;
	private boolean keepalive = true;

	public boolean keepalive() {
		return keepalive;
	}

	public void keepalive(boolean keepalive) {
		this.keepalive = keepalive;
	}

	public Response(byte[] string, boolean keepalive) {
		this.response = string;
		this.keepalive = keepalive;
	}

	public Response(byte[] string) {
		this.response = string;
	}

	public Response() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return new String(response);
	}

	public byte[] getBytes() {
		if (response != null) {
			return response;
		} else {
			return new byte[0];
		}
	}

	public void set(byte[] response) {
		this.response = response;
	}

}