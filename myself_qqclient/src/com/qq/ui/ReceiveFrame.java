package com.qq.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.qq.bean.Qquser;
import com.qq.pub.CommonUse;
import com.qq.pub.UDPSocket;

public class ReceiveFrame extends JFrame implements ActionListener{
	private Qquser sender = null;
	private Qquser receiver = null;
	
	private String info = null;
	
	private JPanel centerPanel = null;
	private JPanel bottomPanel = null;
	
	private JLabel fromLabel = null;
	private JLabel contentLabel = null;
	
	private JTextField fromField = null;
	private JTextArea contentArea = null;
	private JScrollPane sendScrollPane = null;
	
	
	private JButton returnButton = null;
	private JButton closeButton = null;
	
	private void createCenterPanel() {
		this.fromLabel = new JLabel("来自于：");
		this.fromField = new JTextField();
		this.fromField.setPreferredSize(new Dimension(400, 25));
		this.fromField.setEditable(false);
		//永远自己都是sender,不是自己就是receiver
		this.fromField.setText(this.receiver.getName());
		
		this.contentLabel = new JLabel("接收内容：");
		this.contentArea = new JTextArea(this.info);
		this.sendScrollPane = new JScrollPane(this.contentArea);
		//切记要给滚动框设置大小，而不是textField
		this.sendScrollPane.setPreferredSize(new Dimension(400, 130));
		this.sendScrollPane.setViewportView(this.contentArea);
		
		Box box = Box.createVerticalBox();
		
		Box box1 = Box.createHorizontalBox();
		Box box2 = Box.createHorizontalBox();
		
		box1.add(this.fromLabel);
		box1.add(this.fromField);
		
		box2.add(this.contentLabel);
		box2.add(this.sendScrollPane);
		
		box.add(Box.createVerticalStrut(10));
		box.add(box1);
		box.add(Box.createVerticalStrut(10));
		box.add(box2);
		
		this.centerPanel.add(box);
	}
	
	private void createBottomPanel() {
		this.returnButton = new JButton("回复");
		this.closeButton = new JButton("关闭");
		
		this.returnButton.addActionListener(this);

		this.bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER,50,10));
		
		this.bottomPanel.add(this.returnButton);
		this.bottomPanel.add(this.closeButton);
	}
	
	private void init() {
		JPanel bodyPanel = (JPanel)this.getContentPane();
		bodyPanel.setLayout(new BorderLayout());
		
		this.centerPanel = new JPanel();
		this.bottomPanel = new JPanel();
		
		bodyPanel.add(this.centerPanel,BorderLayout.CENTER);
		bodyPanel.add(this.bottomPanel,BorderLayout.SOUTH);
		
		this.createCenterPanel();
		this.createBottomPanel();
		
		this.setTitle(this.sender.getName() + "的接收框");
		this.setSize(500, 300);
		this.setLocationRelativeTo(null);
	}
	
	public ReceiveFrame() {

	}
	
	public ReceiveFrame(Qquser sender,Qquser receiver,String info) {
		this.sender = sender;
		this.receiver = receiver;
		this.info = info;
		this.init();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.returnButton) {
			SendFrame sendFrame = new SendFrame(this.sender,this.receiver);
			sendFrame.setVisible(true);
			this.dispose();
		}
	}
}
