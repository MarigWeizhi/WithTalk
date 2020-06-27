package learn;


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class Receive {

	public static void main(String[] args) throws IOException {
		
		DatagramSocket ds1 = new DatagramSocket(8888);
		DatagramPacket dp1 = new DatagramPacket(new byte[1024], 1024);
		byte[] arr;
		while(true) {
		ds1.receive(dp1);
		arr = dp1.getData();
		int len = dp1.getLength();
		String ip = dp1.getAddress().getHostAddress();
		int port = dp1.getPort();
		System.out.println(ip +":" +  port + ":" + new String(arr,0,len));
		}

	}

}

