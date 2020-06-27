package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

//配套Server1
public class Client1 extends JFrame {
	private TextArea viewArea;
	private TextArea sendArea;
	private TextField tip;
	private JLabel lip;
	private JLabel lport;
	private JButton send;
	private JButton log;
	private JButton clear;
	private JButton shake;
	private JButton login;
	private BufferedWriter bw;
	private DatagramSocket socket;
	private boolean flag = false;
	private int port = 9999 + (int)(Math.random()*100);
	private final int serverPort = 9999;
	private String serverIp = "39.98.155.200";
//	private String serverIp = "127.0.0.1";
	private TextField tport;
	private boolean isOnline = false;
//	private long statTime;
//	private long endTime;
	
	/**
	 * 未知聊天室 v1.2
	 * 作者:Marig_Weizhi
	 * 基于UDP协议并经由服务器中转的信息转发软件
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Client1 frame = new Client1();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * @throws SocketException 
	 */
	public Client1() {
		try {
			socket = new DatagramSocket(port);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
			System.out.println("socket创建错误");
			JOptionPane.showMessageDialog(null, "请尝试重启客户端", "端口异常", JOptionPane.ERROR_MESSAGE);
		}
		init();
		southPanel();
		centerPanel();
		event();
		
	}
	
	protected void logFile() throws IOException {
		bw.flush();
		FileInputStream fis = new FileInputStream("config.txt");
		//内存缓冲区
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int len;
		byte[] arr = new byte[8192];
		while((len = fis.read(arr)) != -1) {
			baos.write(arr,0,len);
		}
		String str = baos.toString();
		viewArea.setText(str);
		fis.close();
		
	}

	private String getCurrentTime() {
		Date d = new Date(System.currentTimeMillis());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
		
		return sdf.format(d);
	}
	
	private void send(byte[] b,String ip) throws Exception {
		try {
			DatagramPacket packet;
			String address = tip.getText() + ":" + tport.getText();
			String massage = new String(b,0,b.length,"utf-8") + ":::" + address;
			
			packet = new DatagramPacket(massage.getBytes(), massage.getBytes().length,InetAddress.getByName(serverIp),serverPort);
			socket.send(packet);
			//防止丢包
			try {
				System.out.println("第一次发送"+  massage);
                Thread.sleep(100);
             } catch (InterruptedException e1) {
             }
//			System.out.println("第二次发送");
//			packet = new DatagramPacket(address.getBytes(), address.getBytes().length,InetAddress.getByName(serverIp),serverPort);
//			socket.send(packet);

			}catch(SocketException e) {
				System.out.println(e.getMessage());
				System.out.println("请检查ip是否输入错误");
				JOptionPane.showMessageDialog(null, "请检查ip地址格式是否正确", "ip地址错误", JOptionPane.ERROR_MESSAGE);
				throw new Exception();
			}
	}
	private void send() throws IOException {
		if(!isOnline) {
			JOptionPane.showMessageDialog(null, "请登录后使用"); 
			return;
		}
		String massage = sendArea.getText().trim();
		String ip = tip.getText();
		ip = ip.trim().length() == 0 ? "255.255.255.255" : ip;
		if(massage.equals("")) {
			JOptionPane.showMessageDialog(null, "请说点什么吧"); 
			return;
		}
		//更改编码
		
		try{
		send(massage.getBytes("utf-8"),ip);
		}catch(Exception e) {
			return;
		}
		if(ip.equals("255.255.255.255")) {
			ip = "所有人";
		}else if(ip.equals("127.0.0.1"))
			ip = "本地";
			
		String str = getCurrentTime() + "你对 " + ip + " 说:\r\n" + massage  + "\r\n";
		bw.write(str);
		bw.flush();//聊天记录实时更新
		viewArea.append(str);
		sendArea.setText("");
		
	}

	private void centerPanel() {
		Panel center = new Panel();
		viewArea  = new TextArea("");
		sendArea = new TextArea("");
		viewArea.setEditable(false);
		viewArea.setBackground(Color.WHITE);
		sendArea.setFont(new Font("xxx", Font.PLAIN, 16));
		
		center.setLayout(new BorderLayout());
		center.add(viewArea,BorderLayout.CENTER);
		center.add(sendArea,BorderLayout.SOUTH);
//		sendArea.setSize(50, 50);
		getContentPane().add(center,BorderLayout.CENTER);
		
	}

	private void southPanel() {
		Panel south = new Panel();
		lip = new JLabel("ip:");
		lip.setFont(new Font("xx", Font.BOLD, 16));
		lport = new JLabel("端口：");
		lport.setFont(new Font("xx", Font.BOLD, 16));
		tip = new TextField("127.0.0.1",10);
		tport = new TextField(port + "" ,3);
		login = new JButton("登录") ;
		send = new JButton("发送") ;
		log = new JButton("记录");
		clear = new JButton("清屏");
		shake = new JButton("震动");
		south.add(login);
		south.add(lip);
		south.add(tip);
		south.add(lport);
		south.add(tport);
		south.add(send);
		south.add(log);
		south.add(clear);
		south.add(shake);
		getContentPane().add(south,BorderLayout.SOUTH);
		
	}
	
	//接收线程
	class Receive extends Thread{
		public void run() {
			try {
//				receiveSocket = new DatagramSocket(9999);
				while(!flag) {
					DatagramPacket packet = new DatagramPacket(new byte[8192], 8192);
				//获取内容
					System.out.println("等待接收信息");
				socket.receive(packet);
				byte[] arr = packet.getData();
				int len = packet.getLength();
//				System.out.println("接收到信息" +  new String(arr,0,arr.length,"utf-8"));
				//更改编码	
//				String massage = new String(arr,0,len,"utf-8").trim();
				String[] sarr = new String(arr,0,len,"utf-8").trim().split(":::");
				String massage = sarr[0];
				String addressF = sarr[1];
//				System.out.println(massage);
				try {
                    Thread.sleep(100);
                 } catch (InterruptedException e1) {
                }
				
				//登录成功指令1
				if(massage.equals("1")) {
					isOnline = true;
					JOptionPane.showMessageDialog(null, "登录成功");
					//登录信息
//					socket.receive(packet);
//					arr = packet.getData();
//					len = packet.getLength();
//					massage = new String(arr,0,len,"utf-8");
					String ipF = packet.getAddress().getHostAddress();
					String str = getCurrentTime() + " \r\n" + addressF + "\r\n";
					bw.write(str);
					viewArea.append(str);
					continue;
				}
				
				//接收窗口震动指令
				if(massage.equals("2")) {
//					socket.receive(packet);
//					String addressF = new String(packet.getData(),0,packet.getData().length,"utf-8");
					String ipF = addressF.split(":")[0];
					int portF = Integer.parseInt(addressF.split(":")[1]);
					shake();
					viewArea.append(ipF + ":" + portF + " 向你发起了窗口震动\r\n");
					continue;
				}
				
				//获取发送者的地址
//				socket.receive(packet);
//				String addressF = new String(packet.getData(),0,packet.getData().length,"utf-8").trim();
				System.out.println("获取到发送者信息" + addressF);
				
				String[] temp = addressF.trim().split(":");
				String ipF = temp[0].trim();
//				System.out.println(temp[1].getClass());
				int portF = Integer.parseInt(temp[1]);
				
				String str ;
				//管理员
				if(ipF.equals("39.98.155.200")) {
					str = getCurrentTime() + "　" + "\r\n" + massage + "\r\n";
				}
				str = getCurrentTime() + "　" + ipF + ":" + portF + "对你说:\r\n" + massage + "\r\n";
				bw.write(str);
				viewArea.append(str);
				}
				
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

	//界面初始化
	private void init() {
//		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocation(500,50);
		this.setVisible(true);
		this.setSize(550,600);
		new Receive().start(); 
		setTitle("未知聊天室");
		try {
			//if(!new File("config.txt").exists())
			//true 表示在尾部追加内容
//			sendSocket = new DatagramSocket();
			bw = new BufferedWriter(new FileWriter("config.txt",true));
			//else
				//bw = new BufferedWriter(new "config.txt");
		} catch (Exception e) {
			
		}
		
	}
	
	//事件处理
	public void event() {
		
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				//关闭窗口前，关闭Scoket释放端口
//				sendSocket.close();
//				receiveSocket.close();
				flag = true;
				try {
					bw.close();
					//socket.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				System.exit(0);
			}
			
		});
		
		sendArea.addKeyListener(new KeyAdapter() {
			 public void keyPressed(KeyEvent e) {
				 if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					 try {
						send();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				 }
			 }
		});
		
		login.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(isOnline) {
					JOptionPane.showMessageDialog(null, "你已经登录了"); 
					return;
				}
				try {
					send("1".getBytes(),serverIp);
					int millis = 1000;
					Thread.sleep(millis);
					if(!isOnline)
					JOptionPane.showMessageDialog(null, "请联系管理员或稍后再试", "登录失败", JOptionPane.ERROR_MESSAGE);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		
		send.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					send();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		log.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					logFile();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		
		clear.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				viewArea.setText("");
			}
		});
		
		shake.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					send("2".getBytes(),tip.getText());
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		
		
	}

	protected void login() {
		// TODO Auto-generated method stub
		
	}

	protected void shake() {
	int x = this.getLocation().x;
	int y = this.getLocation().y;
	
	for(int i = 0;i < 5;i++) {
		try {
			this.setLocation(x + 10, y + 10);
			Thread.sleep(20);
			this.setLocation(x + 10 , y - 10);
			Thread.sleep(20);
			this.setLocation(x - 10 , y + 10);
			Thread.sleep(20);
			this.setLocation(x - 10 , y - 10);
			Thread.sleep(20);
			this.setLocation(x, y);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
		
	}

}


