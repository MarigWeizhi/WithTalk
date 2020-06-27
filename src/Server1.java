

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;

//配套Client1
public class Server1 {
	public static String replyIp = "";
	public static int replyPort = 9999;
	public static final int inPort = 9999;
	public static DatagramSocket socket;
//	public static int outPort = 9999;
	public static boolean flag = false;
	
	public static void main(String[] args) throws InterruptedException, SocketException {
		socket = new DatagramSocket(inPort);
		System.out.println("广播" + inPort + " " + socket.getPort());
		new Receive1().start();
		Thread.sleep(1000);
		new Send1().start();

	}

}

class Receive1 extends Thread {
	public void run() {
		try {
//			DatagramSocket ds1 = new DatagramSocket(9999);
//			System.out.println("广播9999");
			
			while (true) {
				DatagramPacket dp1 = new DatagramPacket(new byte[8196], 8196);
//				DatagramPacket dp2 = new DatagramPacket(new byte[8196], 8196);
				//获取发送内容,以及发送者的信息
				Server1.socket.receive(dp1);
				byte[] arr = dp1.getData();
				int len = dp1.getLength();
				String[] sarr = new String(arr,0,arr.length,"GBK").trim().split(":::");
				String massage = sarr[0];
				String addressT = sarr[1];
				System.out.println(massage);
				
				String ipF = dp1.getAddress().getHostAddress().trim();
				int portF = dp1.getPort();
				String addressF = ipF + ":" + portF;
				
				try {
					System.out.println("第一次接收" + new String(dp1.getData(),0,dp1.getData().length,"GBK"));
                    Thread.sleep(100);
                 } catch (InterruptedException e1) {
                 }
//				System.out.println("接收到来自" + addressF + "的消息");
				//获取发送对象的地址
//				C1.socket.receive(dp2);
//				System.out.println("len=" +  len);
				
//				System.out.println("第二次接收" + new String(dp2.getData(),0,dp2.getData().length,"utf-8"));
//				String addressT = new String(dp2.getData(),0,dp2.getData().length,"utf-8").trim();
				
				String[] temp = addressT.trim().split(":");
				String ipT = temp[0];
				int portT = Integer.parseInt(temp[1]);
//				System.out.println("消息2" + addressT);
				System.out.println(new String(arr,0,arr.length,"GBK"));
				
//				System.out.println("ipF=" + ipF + " portF =" + portF);
//				System.out.println("ipT=" + ipT + " portT =" + portT);
				
				//登录请求
				if(massage.equals("1")) {
//					System.out.println("ipF=" + ipF + " portF =" + portF);
//					System.out.println("ipT=" + ipT + " portT =" + portT);
					System.out.println("用户:" + ipF + ":" + portF + "请求登录");
					//发送登录成功指令
					String str = "1:::管理员：你的ip为：" + ipF + " 端口为:" + portF + " 你现在可以与人交流了!";
					dp1 = new DatagramPacket(str.getBytes(),str.getBytes().length,InetAddress.getByName(ipF),portF);
					Server1.socket.send(dp1);
//					dp1 = new DatagramPacket(new byte[] {-1},1,InetAddress.getByName(ipF),portF);
					continue;
				}
				
				if(arr[0] == -2 && len == 1) {
					//发送震动指令
					dp1 = new DatagramPacket(new byte[] {-2},1,InetAddress.getByName(ipF),portF);
					Server1.socket.send(dp1);
					try {
	                    Thread.sleep(100);
	                 } catch (InterruptedException e1) {
	                 }
					//发送者地址信息
//					dp2 = new DatagramPacket(addressF.getBytes(),addressF.getBytes().length,InetAddress.getByName(ipT),portT);
//					C1.socket.send(dp2);
					System.out.println(addressF + " 窗口震动转发给 " + addressT);
					continue;
				}
				if(ipT.equals("127.0.0.1")) {
					ipT = ipF;
					addressT = ipF + ":" + portT;
					System.out.println("测试if1");
				}
				
//				System.out.println("ipF=" + ipF + " portF =" + portF);
//				System.out.println("ipT=" + ipT + " portT =" + portT);
				
				System.out.println(addressF + "对" + addressT + "说:" + new String(arr,0,arr.length,"GBK"));
				//信息转发(arr原封不动的转发)
				String massageT = massage + ":::" +addressF;
//				dp1 = new DatagramPacket(Sarr.getBytes(),Sarr.getBytes().length,InetAddress.getByName(ipT),portT);
				dp1 = new DatagramPacket(massageT.getBytes(),massageT.getBytes().length,InetAddress.getByName(ipT),portT);
				Server1.socket.send(dp1);
				System.out.println("转发信息: "+ massageT);
				//发送者地址转发
//				dp2 = new DatagramPacket(addressF.getBytes(),addressF.getBytes().length,InetAddress.getByName(ipT),portT);
//				C1.socket.send(dp2);
//				System.out.println("转发信息2"+ new String(dp2.getData(),0,dp2.getData().length));
				Server1.replyIp = ipF;
				Server1.replyPort = portF;
			} 
		} catch (Exception e) {
			System.out.println("C1 Receive异常" + e.getMessage());
		}
	}
}

class Send1 extends Thread{
	public void run() {
		try {
//			DatagramSocket ds1 = new DatagramSocket(9999);//码头 
			Scanner sc = new Scanner(System.in);
			while (true) {
//				System.out.println(C1.flag);
				Thread.sleep(1000);
				String str = sc.nextLine();
				if(Server1.flag) {
					System.out.println("执行flag");
					String massage = "管理员：" + str;
					String ip = Server1.replyIp.equals("") ? "127.0.0.1": Server1.replyIp;
					int port = Server1.replyIp.equals("") ? Server1.inPort : Server1.replyPort;
	//				String ip = "112.3.27.8";
					DatagramPacket dp1 = new DatagramPacket(massage.getBytes(), massage.getBytes().length, InetAddress.getByName(ip),
							port);
					System.out.println(ip + ":" + Server1.replyPort + "你对 " + ip + ":"+  port +" 说:" + str);
					Server1.socket.send(dp1);
					Server1.flag = false;
				}
			}
		} catch (Exception e) {
			System.out.println("C1 Send异常");
		}
	}
}
