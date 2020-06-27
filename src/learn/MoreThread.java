package learn;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Scanner;

public class MoreThread {

	public static void main(String[] args) throws InterruptedException {
		new Receive1().start();

		new Send1().start();

	}

}

class Receive1 extends Thread {
	public void run() {
		try {
			DatagramSocket ds1 = new DatagramSocket(9999);
			DatagramPacket dp1 = new DatagramPacket(new byte[1024], 1024);
			
			while (true) {
				ds1.receive(dp1);
				byte[] arr = dp1.getData();
				int len = dp1.getLength();
				String ip = dp1.getAddress().getHostAddress();
				int port = dp1.getPort();
				System.out.println(ip + ":" + port + ":" + new String(arr, 0, len));
			} 
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}

class Send1 extends Thread{
	public void run() {
		try {
			DatagramSocket ds1 = new DatagramSocket();//码头  随机端口
			
			Scanner sc = new Scanner(System.in);
			while (true) {
				String str = sc.nextLine();
				if (str.equals("exit")) {
					break;
				}
				DatagramPacket dp1 = new DatagramPacket(str.getBytes(), str.getBytes().length, InetAddress.getByName("127.0.0.1"),
						9999);
				ds1.send(dp1);
			}
			ds1.close();
			sc.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}


