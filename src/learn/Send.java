package learn;


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Scanner;

public class Send {

	public static void main(String[] args) throws IOException {
		DatagramSocket ds1 = ds1 = new DatagramSocket();//码头  随机端口
		DatagramPacket dp1;
		Scanner sc = new Scanner(System.in);
		while(true) {
			String str = sc.nextLine(); 
			if(str.equals("exit"))
				break;
		dp1 = new DatagramPacket(str.getBytes(), str.getBytes().length,InetAddress.getByName("127.0.0.1"),8888);
		ds1.send(dp1);
		}
		
		ds1.close();
		sc.close();
		
	}

}

