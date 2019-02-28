package com.qq.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
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

public class RegisterFrame3 extends JFrame implements ActionListener{
	private Qquser qquser = null;
	private TCPSocket tcpSocket = null; 
	
	private JPanel bodyPanel = null;
	private JPanel centerPanel = null;
	private JPanel bottomPanel = null;
	
	private JLabel accountLabel = null;
	private JLabel nameLabel = null;
	private JLabel passwordLabel = null;
	private JLabel repasswordLabel = null;
	private JLabel imgLabel = null;
	
	private JTextField accountTextField = null;
	private JTextField nameTextField = null;
	private JPasswordField passwordField = null;
	private JPasswordField repasswordField = null;
	private JComboBox imgComboBox = null;
	
	private JButton registerButton = null;
	private JButton resetButton = null;
	
	private void loadHead() {
		File[] file = new File("./selectimg").listFiles();
		for (File temp : file) {
			ImageIcon imageIcon = new ImageIcon(temp.getAbsolutePath());
			this.imgComboBox.addItem(imageIcon);
		}
	}
	
	private void createCenterPanel() {
		this.centerPanel = new ImgPanel("./register.jpg");
		
		this.accountLabel = new JLabel("账 号：",JLabel.RIGHT);
		this.accountLabel.setPreferredSize(new Dimension(80, 24));//所有的矩形都有这个方法
		this.nameLabel = new JLabel("用户名：",JLabel.RIGHT);
		this.nameLabel.setPreferredSize(new Dimension(80, 24));
		this.passwordLabel = new JLabel("密 码：",JLabel.RIGHT);
		this.passwordLabel.setPreferredSize(new Dimension(80, 24));
		this.repasswordLabel = new JLabel("确认密码：",JLabel.RIGHT);
		this.repasswordLabel.setPreferredSize(new Dimension(80, 24));
		this.imgLabel = new JLabel("选择头像：",JLabel.RIGHT);
		this.imgLabel.setPreferredSize(new Dimension(80, 24));
		
		this.accountTextField = new JTextField();
		this.accountTextField.setPreferredSize(new Dimension(160, 24));
		this.nameTextField = new JTextField();
		this.nameTextField.setPreferredSize(new Dimension(160, 24));
		this.passwordField = new JPasswordField();
		this.passwordField.setPreferredSize(new Dimension(160, 24));
		this.repasswordField= new JPasswordField();
		this.repasswordField.setPreferredSize(new Dimension(160, 24));
		this.imgComboBox = new JComboBox();
		this.loadHead(); 
		
		Box box0 = Box.createVerticalBox();
		Box box1 = Box.createHorizontalBox();
		Box box2 = Box.createHorizontalBox();
		Box box3 = Box.createHorizontalBox();
		Box box4 = Box.createHorizontalBox();
		Box box5 = Box.createHorizontalBox();
		
		box1.add(this.accountLabel);
		box1.add(this.accountTextField);
		
		box2.add(this.nameLabel);
		box2.add(this.nameTextField);
		
		box3.add(this.passwordLabel);
		box3.add(this.passwordField);
		
		box4.add(this.repasswordLabel);
		box4.add(this.repasswordField);
		
		box5.add(this.imgLabel);
		box5.add(this.imgComboBox);
		
		box0.add(Box.createVerticalStrut(50));
		box0.add(box1);
		box0.add(Box.createVerticalStrut(10));
		box0.add(box2);
		box0.add(Box.createVerticalStrut(10));
		box0.add(box3);
		box0.add(Box.createVerticalStrut(10));
		box0.add(box4);
		box0.add(Box.createVerticalStrut(10));
		box0.add(box5);
		
		this.centerPanel.add(box0);
		this.bodyPanel.add(this.centerPanel, BorderLayout.CENTER);
	}
	
	private void createBottomPanel() {
		this.bottomPanel = new JPanel();

		this.registerButton = new JButton("注册");
		this.resetButton = new JButton("重置");

		this.registerButton.addActionListener(this);
		this.resetButton.addActionListener(this);
		
		this.bottomPanel.add(this.registerButton);
		this.bottomPanel.add(this.resetButton);
		
		this.bodyPanel.add(this.bottomPanel, BorderLayout.SOUTH);
	}
	
	private void init() {
		this.bodyPanel = (JPanel)this.getContentPane();
		this.bodyPanel.setLayout(new BorderLayout());
		
		this.createCenterPanel();
		this.createBottomPanel();
		
		this.setTitle("QQ注册");
		this.setSize(512, 340);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	
	public RegisterFrame3() {
		this.tcpSocket = new TCPSocket("127.0.0.1",10088);
		this.init();
	}
	
	public static void main(String[] args) {
		RegisterFrame3 frame3 = new RegisterFrame3();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.registerButton) {
			//取内容
			String accout = this.accountTextField.getText();
			String name = this.nameTextField.getText();
			String password = new String(this.passwordField.getPassword()); 
			String repassword = new String(this.repasswordField.getPassword());
			//判断内容不能为空
			if (accout.equals("") || name.equals("") || password.equals("") || repassword.equals("")) {
				JOptionPane.showMessageDialog(this, "内容不能为空！");
			}
			//判断二次输入密码是否一致
			if (!(password.equals(repassword))) {
				JOptionPane.showMessageDialog(this, "确认密码与原密码不一致！");
			}
			//将图片信息当中的数字取出来(字符串处理)
			String imgString = this.imgComboBox.getSelectedItem().toString();
			int start = imgString.lastIndexOf("\\");//记住\最后一次出现的位置
			int end = imgString.lastIndexOf(".");//记住.最后一次出现的位置
			String img = imgString.substring(start + 1, end);//左边闭区间，右边开区间
			
			//保存用户的注册信息
			this.qquser = new Qquser();
			this.qquser.setAccount(accout);
			this.qquser.setName(name);
			this.qquser.setPassword(password);
			this.qquser.setState("0");
			this.qquser.setIp("0");
			this.qquser.setPort("0");
			this.qquser.setPic(img);
			
			//打包报文
			TCPMessage sMessage = new TCPMessage();
			sMessage.setHead(CommonUse.REGISTER);
			sMessage.setBody(CommonUse.QQ_USER, this.qquser);
			//发送数据并得到处理后的数据
			TCPMessage rMessage = this.tcpSocket.submit(sMessage);
			String back = rMessage.getHead();
			
			//判断服务器返回的处理结果
			if (CommonUse.SUCCESSFUL.equals(back)) {
				JOptionPane.showMessageDialog(this, "注册成功");
				this.dispose();
			} else {
				JOptionPane.showMessageDialog(this, "注册失败");
			}
		} else if (e.getSource() == this.resetButton) {
			this.accountTextField.setText("");
			this.nameTextField.setText("");
			this.passwordField.setText("");
			this.repasswordField.setText("");
			this.imgComboBox.setSelectedIndex(1);
		}
	}
}
