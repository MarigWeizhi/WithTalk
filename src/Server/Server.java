package Server;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Scanner;

/*
 * 服务端接收：-9登录指令  -8注册指令  -7下线指令  -1窗口震动  
 * 客户端接收：-9登录成功 -8登录失败 -7注册成功 -1窗口震动
 * */
public class Server  {
	public static String replyIp = "";
	public static int replyPort = 9998;
	public static int inPort = 9999;
	public static int outPort = 9999;
	public static DatagramSocket socket;
	
	public static void main(String[] args) throws InterruptedException, SocketException {
		socket = new DatagramSocket(inPort);
		System.out.println("广播" + inPort);
		
		new Forward().start();
		Thread.sleep(1000);
		new Talk().start();

	}
	
	static //转发
	class Forward extends Thread{
		public static int number = -1;
		public static User[] users = new User[100];
		public void run() {
			receive();
		}
		private void receive() {
			try {
				DatagramPacket dp1 = new DatagramPacket(new byte[8196], 8196);
				while (true) {
					Server.socket.receive(dp1);
					String tempIp = dp1.getAddress().getHostAddress();
					int tempPort = dp1.getPort();
					byte[] arr = dp1.getData();
					int len = dp1.getLength();
					
					// -9 为登录指令
					if(arr[0] == -9 && len == 1) {
						Server.socket.receive(dp1);
						byte[] temp = dp1.getData();
						int lenth = dp1.getLength();
						int id = Integer.parseInt(new String(temp,0,lenth,"utf-8"));
						temp = dp1.getData();
					    lenth = dp1.getLength();
						String pass =new String(temp,0,lenth,"utf-8");
						if(login(id,pass,tempIp,tempPort)) {
							//-9登录成功
							dp1 = new DatagramPacket(new byte[] {-9}, 1, InetAddress.getByName(tempIp),
									tempPort);
							Server.socket.send(dp1);
						}else {
							//-8登录失败
							dp1 = new DatagramPacket(new byte[] {-8}, 1, InetAddress.getByName(tempIp),
									tempPort);
							Server.socket.send(dp1);
						}
					}
					
					//-8注册指令
					if(arr[0] == -8 && len == 1) {
						Server.socket.receive(dp1);
						byte[] temp = dp1.getData();
						int lenth = dp1.getLength();
						String name = new String(temp,0,lenth,"utf-8");
						temp = dp1.getData();
					    lenth = dp1.getLength();
						String pass =new String(temp,0,lenth,"utf-8");
						//注册成功
						String newid = String.valueOf(register(name,pass,tempIp,tempPort));
						
						dp1 = new DatagramPacket(newid.getBytes(), newid.getBytes().length, InetAddress.getByName(tempIp),
								tempPort);
						Server.socket.send(dp1);
					}
					
					//-7 用户下线
					if(arr[0] == -7 && len == 1) {
						for (int i = 0; i < number +1 ; i++) {
							if(users[i].isOnline && users[0].ip == tempIp  && users[i].port == tempPort) {
								users[i].isOnline = false;
								break;
							}
						}
					}
					
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

		int register(String name ,String pass ,String ip,int port) {
			User user = new User(name, pass);
			users[number] = user;
			users[number].isOnline = true;
			return user.id;
		}
		
		boolean login(int id ,String pass ,String ip,int port) {
			try {
			if(users[id].passWord == pass) {
				users[id].ip = ip;
				users[id].port = port;
				users[id].isOnline = true;
				return true;
			}else 
				return false;
			}catch(Exception e) {
				return false;
			}
		}
		
	}

	
}


class Receive extends Thread {
	public void run() {
		try {
//			DatagramSocket ds1 = new DatagramSocket(9999);
//			System.out.println("广播9999");
			DatagramPacket dp1 = new DatagramPacket(new byte[8196], 8196);
			
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



class Talk extends Thread{
	public void run() {
		try {
			DatagramSocket talkSocket = new DatagramSocket(Server.replyPort);
			DatagramPacket receivePacket = new DatagramPacket(new byte[8196], 8196);
			while (true) {
				talkSocket.receive(receivePacket);
				byte[] arr = receivePacket.getData();
				String massage = new String(arr,0,receivePacket.getLength(),"utf-8");
				talkSocket.receive(receivePacket);
				arr = receivePacket.getData();
				int id = Integer.valueOf(new String(arr,0,receivePacket.getLength(),"utf-8"));
				
				String ip = Server.Forward.users[id] == null ? "127.0.0.1": Server.Forward.users[id].ip;
				int port = Server.Forward.users[id] == null ? 9998 : Server.Forward.users[id].port;
//				String ip = "112.3.27.8";
				System.out.println("回复ip：" + ip);
				DatagramPacket dp1 = new DatagramPacket(massage.getBytes(), massage.getBytes().length, InetAddress.getByName(ip),
						port);
				System.out.println(ip + ":" + Server.replyPort + "对" + Server.Forward.users[id].name + "二号客户端说:" + massage );
				talkSocket.send(dp1);
			}
		} catch (Exception e) {
			System.out.println("C1 Send异常");
		}
	}
}
