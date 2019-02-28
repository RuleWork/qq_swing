package com.qq.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.qq.bean.Qquser;
import com.qq.component.ImgPanel;
import com.qq.pub.CommonUse;
import com.qq.pub.TCPMessage;
import com.qq.pub.TCPSocket;
import com.qq.pub.UDPSocket;

public class LoginFrame extends JFrame implements ActionListener{
	private Qquser fullUser = null;
	private	TCPSocket tcpSocket = null;
	private UDPSocket udpSocket = null;
	
	private JPanel centerPanel = null;
	private JPanel bottomPanel = null;
	
	private JLabel accountLabel = null;
	private JTextField accountField = null;
	private JLabel passwordLabel = null;
	private JPasswordField passowordField = null;
	
	private JButton loginButton = null;
	private JButton registerButton = null;
	
	private void init() {
		JPanel bodyPanel = (JPanel)this.getContentPane();
		bodyPanel.setLayout(new BorderLayout());
		
		this.centerPanel = new ImgPanel("./logon.jpg");
		
		this.bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 10));//水平间隔，垂直间隔
		 
		this.accountLabel = new JLabel("用户名：",JLabel.RIGHT);
		this.accountField = new JTextField(16);
		this.passwordLabel = new JLabel("密     码：",JLabel.RIGHT); 
		this.passowordField = new JPasswordField(16);
		
		Box box0 = Box.createVerticalBox();
		Box box1 = Box.createHorizontalBox();
		Box box2 = Box.createHorizontalBox();
		
		box1.add(this.accountLabel);
		box1.add(this.accountField);
	
		box2.add(this.passwordLabel);
		box2.add(this.passowordField);
		
		box0.add(Box.createVerticalStrut(90));
		box0.add(box1);
		box0.add(Box.createVerticalStrut(15));
		box0.add(box2);
		this.centerPanel.add(box0);
		
		this.loginButton = new JButton("登录");
		this.loginButton.addActionListener(this);
		this.registerButton = new JButton("注册");
		this.registerButton.addActionListener(this);
		
		this.bottomPanel.add(this.loginButton);
		this.bottomPanel.add(this.registerButton);
		
		bodyPanel.add(this.centerPanel,BorderLayout.CENTER);
		bodyPanel.add(this.bottomPanel,BorderLayout.SOUTH);
		
		this.setTitle("用户登录");
		this.setSize(414, 307);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE) ;
		this.setVisible(true);
	}
	
	public LoginFrame() {
		//收到与连接服务器的tcp
		this.tcpSocket = new TCPSocket("127.0.0.1",10088);
		//用于收的udp，将它交给main页面，收来自服务器发送的上线消息
		this.udpSocket = new UDPSocket();
		this.init();
	}
	
	public static void main(String[] args) {
		LoginFrame frame1 = new LoginFrame();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.loginButton) {
			String account = this.accountField.getText();
			String password = new String(this.passowordField.getPassword());
			
			String ip = this.tcpSocket.getIp();
			String port = String.valueOf(this.udpSocket.getPort());
			
			Qquser lqquser = new Qquser();
			
			lqquser.setAccount(account);
			lqquser.setPassword(password);
			lqquser.setIp(ip);
			//转换关系用起来
			lqquser.setPort(port);
			
			//打包报文
			TCPMessage sMessage = new TCPMessage();
			sMessage.setHead(CommonUse.LOGIN);
			sMessage.setBody(CommonUse.QQ_USER, lqquser);
			
			//发送报文并得到服务器处理后的rmessage
			TCPMessage rMessage = this.tcpSocket.submit(sMessage);
			//取出rmessage的头用于逻辑判断
			String back = rMessage.getHead();

			if (CommonUse.SUCCESSFUL.equals(back)) {
				JOptionPane.showMessageDialog(this, "登录成功！");
				this.fullUser = (Qquser)rMessage.getBody(CommonUse.QQ_USER);
				MainFrame mainFrame = new MainFrame(this.fullUser,this.tcpSocket,this.udpSocket);
				mainFrame.setVisible(true);
				this.dispose();
			} else {
				JOptionPane.showMessageDialog(this, "登录失败");
			}
		} else if (e.getSource() == this.registerButton) {
			RegisterFrame3 registerFrame3 = new RegisterFrame3();
			registerFrame3.setVisible(true);
		}
	}
	
}
