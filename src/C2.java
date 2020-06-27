

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Scanner;

public class C2 {
	public static DatagramSocket socket;
	public static int inPort = 9989;
	public static int outPort = 9999;
	
	public static void main(String[] args) throws InterruptedException, SocketException {
		socket = new DatagramSocket(inPort);
		new Receive2().start();
		Thread.sleep(1000);
		new Send2().start();
		System.out.println("广播" +  inPort);
	}

}

class Receive2 extends Thread {
	public void run() {
		try {
			//C2.socket = new DatagramSocket(9999);
			DatagramPacket dp1 = new DatagramPacket(new byte[1024], 1024);
			
			while (true) {
				C2.socket.receive(dp1);
				byte[] arr = dp1.getData();
				int len = dp1.getLength();
				String ip = dp1.getAddress().getHostAddress();
				int port = dp1.getPort();
				System.out.println(ip + ":" + port + "一号客户端对你说:" + new String(arr, 0, len));
			} 
		} catch (Exception e) {
			System.out.println("C2 Receive异常" + e.getMessage());
		}
	}
}

class Send2 extends Thread{
	public void run() {
		try {
			//DatagramSocket ds1 = new DatagramSocket(9999);//码头 
			
			Scanner sc = new Scanner(System.in);
			while (true) {
				String str = sc.nextLine();
				if (str.equals("exit")) {
					break;
				}
				DatagramPacket dp1 = new DatagramPacket(str.getBytes(), str.getBytes().length, InetAddress.getByName("39.98.155.200"),
						C2.outPort);
				System.out.println("发送成功");
				C2.socket.send(dp1);
			}
			C2.socket.close();
			sc.close();
		} catch (Exception e) {
			System.out.println("C2 Send异常");
		}
	}
}
