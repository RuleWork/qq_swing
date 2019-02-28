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
		
		this.accountLabel = new JLabel("�� �ţ�",JLabel.RIGHT);
		this.accountLabel.setPreferredSize(new Dimension(80, 24));//���еľ��ζ����������
		this.nameLabel = new JLabel("�û�����",JLabel.RIGHT);
		this.nameLabel.setPreferredSize(new Dimension(80, 24));
		this.passwordLabel = new JLabel("�� �룺",JLabel.RIGHT);
		this.passwordLabel.setPreferredSize(new Dimension(80, 24));
		this.repasswordLabel = new JLabel("ȷ�����룺",JLabel.RIGHT);
		this.repasswordLabel.setPreferredSize(new Dimension(80, 24));
		this.imgLabel = new JLabel("ѡ��ͷ��",JLabel.RIGHT);
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

		this.registerButton = new JButton("ע��");
		this.resetButton = new JButton("����");

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
		
		this.setTitle("QQע��");
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
			//ȡ����
			String accout = this.accountTextField.getText();
			String name = this.nameTextField.getText();
			String password = new String(this.passwordField.getPassword()); 
			String repassword = new String(this.repasswordField.getPassword());
			//�ж����ݲ���Ϊ��
			if (accout.equals("") || name.equals("") || password.equals("") || repassword.equals("")) {
				JOptionPane.showMessageDialog(this, "���ݲ���Ϊ�գ�");
			}
			//�ж϶������������Ƿ�һ��
			if (!(password.equals(repassword))) {
				JOptionPane.showMessageDialog(this, "ȷ��������ԭ���벻һ�£�");
			}
			//��ͼƬ��Ϣ���е�����ȡ����(�ַ�������)
			String imgString = this.imgComboBox.getSelectedItem().toString();
			int start = imgString.lastIndexOf("\\");//��ס\���һ�γ��ֵ�λ��
			int end = imgString.lastIndexOf(".");//��ס.���һ�γ��ֵ�λ��
			String img = imgString.substring(start + 1, end);//��߱����䣬�ұ߿�����
			
			//�����û���ע����Ϣ
			this.qquser = new Qquser();
			this.qquser.setAccount(accout);
			this.qquser.setName(name);
			this.qquser.setPassword(password);
			this.qquser.setState("0");
			this.qquser.setIp("0");
			this.qquser.setPort("0");
			this.qquser.setPic(img);
			
			//�������
			TCPMessage sMessage = new TCPMessage();
			sMessage.setHead(CommonUse.REGISTER);
			sMessage.setBody(CommonUse.QQ_USER, this.qquser);
			//�������ݲ��õ�����������
			TCPMessage rMessage = this.tcpSocket.submit(sMessage);
			String back = rMessage.getHead();
			
			//�жϷ��������صĴ�����
			if (CommonUse.SUCCESSFUL.equals(back)) {
				JOptionPane.showMessageDialog(this, "ע��ɹ�");
				this.dispose();
			} else {
				JOptionPane.showMessageDialog(this, "ע��ʧ��");
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
