import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Scanner;

public class Server {
	public static String replyIp = "";
	public static int replyPort = 9999;
	public static DatagramSocket socket;
	public static int inPort = 9999;
	public static int outPort = 9999;
	
	public static void main(String[] args) throws InterruptedException, SocketException {
		socket = new DatagramSocket(inPort);
		System.out.println("广播" + inPort);
		new Receive1().start();
		Thread.sleep(1000);
		new Send1().start();

	}
	
	//转发
	class Forward extends Thread{
		public void run() {
			
		}
	}

}

class User{
	public static int number =0;
	private String id = "";
	private int key = 0;
	private String passWord = "";
	
	public User(String id,String passWord) {
		this.id = id;
		this.passWord = passWord;
		key = ++number;
	}
	
}

class Receive extends Thread {
	public void run() {
		try {
//			DatagramSocket ds1 = new DatagramSocket(9999);
//			System.out.println("广播9999");
			DatagramPacket dp1 = new DatagramPacket(new byte[1024], 1024);
			
			while (true) {
				Server.socket.receive(dp1);
				byte[] arr = dp1.getData();
				int len = dp1.getLength();
				String ip = dp1.getAddress().getHostAddress();
				int port = dp1.getPort();
				System.out.println(ip + ":" + port + "客户端对你说:" + new String(arr, 0, len));
				Server.replyIp = ip;
				Server.replyPort = port;
				
			} 
		} catch (Exception e) {
			System.out.println("Receive异常" + e.getMessage());
		}
	}
}

class Send extends Thread{
	public void run() {
		try {
//			DatagramSocket ds1 = new DatagramSocket(9999);//码头 
			Scanner sc = new Scanner(System.in);
			while (true) {
				String str = sc.nextLine();
				if (str.equals("exit")) {
					break;
				}
				String ip = Server.replyIp.equals("") ? "127.0.0.1": Server.replyIp;
//				String ip = "112.3.27.8";
				System.out.println("回复ip：" + ip);
				DatagramPacket dp1 = new DatagramPacket(str.getBytes(), str.getBytes().length, InetAddress.getByName(ip),
						Server.replyPort);
				System.out.println(ip + ":" + Server.replyPort + "你对二号客户端说:" + str);
				Server.socket.send(dp1);
			}
			Server.socket.close();
			sc.close();
		} catch (Exception e) {
			System.out.println("C1 Send异常");
		}
	}
}
