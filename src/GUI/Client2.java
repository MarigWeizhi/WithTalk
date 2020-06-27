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
import java.net.UnknownHostException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

import com.sun.xml.internal.bind.v2.runtime.Name;


public class Client2 extends JFrame {

	private TextArea viewArea;
	private TextArea sendArea;
	private TextField tip;
	private JLabel lip;
	private JButton send;
	private JButton log;
	private JButton clear;
	private JButton shake;
	private BufferedWriter bw;
	private DatagramSocket socket;
	private JLabel lid;
	private JLabel lpass;
	private JLabel ltip;
	private JButton login;
	private JButton register;
	private TextField tid;
	private TextField tpass;
	private String serverIp = "39.98.155.200";
	private String self;
	private int serverPort = 9999;
	private int userPort = 9998;
	private boolean flag = false;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Client2 frame = new Client2();
					Client2 login = new Client2(1);
					frame.setVisible(true);
					login.setVisible(true);
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
	
	public Client2() {
		try {
			socket = new DatagramSocket(9999);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
			System.out.println("socket创建错误");
		}
		
		init();
		southPanel();
		centerPanel();
		event();
		
	}
	
	//b1
	public Client2(int a) {
		Panel Login = new Panel();
		JTextArea bc = new JTextArea(""+
		"              公告\n"
	  + "             \n"
	  + "客户端版本：v1.0\n"
	  + "             \n"
	  + "如有bug请联系作者"
	);
		
		this.setLocation(550,200);
		this.setSize(300,200);
		add(Login);
		Login.setLayout(null);
		setTitle("登录");
		ltip = new JLabel("注:若未注册则无法");
		JLabel ltip2 = new JLabel("接收其他人的消息");
		login = new JButton("登录");
		register = new JButton("注册");
		lid = new JLabel("账号:");
		lpass = new JLabel("密码:");
		tid = new TextField("");
		tpass = new TextField("");
		lid.setBounds(10, 10, 30, 20);
		tid.setBounds(50, 10, 80, 20);
		lpass.setBounds(10, 40, 30, 20);
		tpass.setBounds(50, 40, 80, 20);
		login.setBounds(10, 80, 60, 20);
		register.setBounds(80, 80, 60, 20);
		ltip.setBounds(10, 110, 120, 20);
		ltip2.setBounds(10, 130, 120, 20);
		bc.setBounds(150, 0, 150, 200);
		bc.setEditable(false);
		
		Login.add(lid);
		Login.add(tid);
		Login.add(lpass);
		Login.add(tpass);
		Login.add(login);
		Login.add(ltip);
		Login.add(register);
		Login.add(ltip2);
		Login.add(bc);
		
		login.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					login();
				} catch (IOException e1) {
					System.out.println("登录异常");
					e1.printStackTrace();
				}
				
			}
			
		});

	}
	
	
	private void login() throws UnknownHostException, IOException {
		String name = tid.getText();
		String pass = tpass.getText();
		self = name;
		byte[] arr = new byte[] {-9};
		System.out.println(arr.length);
		socket.send(new DatagramPacket(arr, 1 , InetAddress.getByName(serverIp),userPort));
		socket.send(new DatagramPacket(name.getBytes(),name.getBytes().length , InetAddress.getByName(serverIp),serverPort));
		socket.send(new DatagramPacket(pass.getBytes(),pass.getBytes().length , InetAddress.getByName(serverIp),serverPort));
		
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
			DatagramPacket packet = new DatagramPacket(b, b.length,InetAddress.getByName(ip),9999);
			socket.send(packet);
			}
			catch(SocketException e) {
				System.out.println(e.getMessage());
				System.out.println("请检查ip是否输入错误");
				JOptionPane.showMessageDialog(null, "请检查ip地址格式是否正确", "ip地址错误", JOptionPane.ERROR_MESSAGE);
				throw new Exception();
			}
	}
	private void send() throws IOException {
		
		String massage = sendArea.getText();
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
		
		
		String str = getCurrentTime() + "你对 " + (ip.equals("255.255.255.255")? "所有人" : ip) + " 说:\r\n" + massage  + "\r\n";
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
		lip = new JLabel("对方id：");
		lip.setFont(new Font("xx", Font.BOLD, 16));
		tip = new TextField("",6);
		send = new JButton("发送") ;
		log = new JButton("记录");
		clear = new JButton("清屏");
		shake = new JButton("震动");
		south.add(lip);
		south.add(tip);
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
				DatagramPacket packet = new DatagramPacket(new byte[8192], 8192);
				while(!flag) {
					socket.receive(packet);
				byte[] arr = packet.getData();
				int len = packet.getLength();
				//接收窗口震动指令
				if(arr[0] == -1 && len == 1) {
					shake();
					viewArea.append("对方向你发起了窗口震动\r\n");
					continue;
				}
				
				if(arr[0] == -2 && len == 1) {
					setTitle("未知聊天室:" + self);
					continue;
				}
				//更改编码	
				String message = new String(arr,0,len,"utf-8");
				
				String ip = packet.getAddress().getHostAddress();
				String str = getCurrentTime() + "　" + ip + " " + "对你说:\r\n" + message + "\r\n";
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
		this.setSize(400,600);
		new Receive().start(); 
		setTitle("未知聊天室(游客)");
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
	
	public void event() {
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				//关闭窗口前，关闭Scoket释放端口
				flag = true;
//				sendSocket.close();
//				receiveSocket.close();
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
					shake();
					send(new byte[] {-1},tip.getText());
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		
		
		
		
		
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


