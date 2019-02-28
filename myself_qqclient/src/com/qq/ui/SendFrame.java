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
import com.qq.pub.TCPMessage;
import com.qq.pub.TCPSocket;
import com.qq.pub.UDPSocket;

public class SendFrame extends JFrame implements ActionListener{
	private Qquser sender = null;
	private Qquser receiver = null;
	
	private JPanel centerPanel = null;
	private JPanel bottomPanel = null;
	
	private JLabel receiverLabel = null;
	private JLabel sendercontentLabel = null;
	
	private JTextField receiverField = null;
	private JTextArea sendArea = null;
	private JScrollPane sendScrollPane = null;
	
	
	private JButton sendButton = null;
	private JButton recordButton = null;
	
	private void createCenterPanel() {
		this.receiverLabel = new JLabel("接受者：");
		this.receiverField = new JTextField();
		this.receiverField.setPreferredSize(new Dimension(400, 25));
		this.receiverField.setEditable(false);
		this.receiverField.setText(this.receiver.getName());
		
		this.sendercontentLabel = new JLabel("发送内容：");
		this.sendArea = new JTextArea();
		this.sendScrollPane = new JScrollPane(this.sendArea);
		//切记要给滚动框设置大小，而不是textField
		this.sendScrollPane.setPreferredSize(new Dimension(400, 130));
		this.sendScrollPane.setViewportView(this.sendArea);
		
		Box box = Box.createVerticalBox();
		
		Box box1 = Box.createHorizontalBox();
		Box box2 = Box.createHorizontalBox();
		
		box1.add(this.receiverLabel);
		box1.add(this.receiverField);
		
		box2.add(this.sendercontentLabel);
		box2.add(this.sendScrollPane);
		
		box.add(Box.createVerticalStrut(10));
		box.add(box1);
		box.add(Box.createVerticalStrut(10));
		box.add(box2);
		
		this.centerPanel.add(box);
	}
	
	private void createBottomPanel() {
		this.sendButton = new JButton("发送");
		this.recordButton = new JButton("聊天记录");
		
		this.sendButton.addActionListener(this);
		this.recordButton.addActionListener(this);

		this.bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER,50,10));
		
		this.bottomPanel.add(this.sendButton);
		this.bottomPanel.add(this.recordButton);
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
		
		this.setTitle(this.sender.getName() + "的发送框");
		this.setSize(500, 300);
		this.setLocationRelativeTo(null);
	}
	
	public SendFrame() {

	}
	
	public SendFrame(Qquser sender,Qquser receiver) {
		this.sender = sender;
		this.receiver = receiver;
		this.init();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.sendButton) {
			UDPSocket udpSocket = new UDPSocket(this.receiver.getIp(),Integer.parseInt(this.receiver.getPort()));
			String udpInfo = CommonUse.MESSAGE + CommonUse.UDP_PACKET_SYMBOL
					+ this.sender.getAccount() + CommonUse.UDP_PACKET_SYMBOL
					+ this.sendArea.getText() + CommonUse.UDP_PACKET_SYMBOL;
			udpSocket.send(udpInfo);

			String message = this.sendArea.getText() + CommonUse.UDP_PACKET_SYMBOL;
			TCPSocket tcpSocket = new TCPSocket("127.0.0.1",10088);
			
			Object[] chater = new Object[] {this.sender,this.receiver,message};
			
			TCPMessage sMessage = new TCPMessage();
			sMessage.setHead(CommonUse.Record);
			sMessage.setBody(CommonUse.Object_SRM,chater);
			
			//发送报文并得到服务器处理后的rmessage
			tcpSocket.submit(sMessage);
			
			this.dispose();
		} else if (e.getSource() == this.recordButton) {
			
			
			TCPSocket tcpSocket = new TCPSocket("127.0.0.1",10088);
			
			Qquser[] chater = new Qquser[] {this.sender,this.receiver};
			
			TCPMessage sMessage = new TCPMessage();
			sMessage.setHead(CommonUse.Chated);
			sMessage.setBody(CommonUse.Object_SRM,chater);
			
			//取出rmessage的头用于逻辑判断
			TCPMessage rMessage = tcpSocket.submit(sMessage);

			String message_search = (String)rMessage.getBody(CommonUse.FRIENDS_INFO);
			String infos[] = message_search.split(CommonUse.UDP_PACKET_SYMBOL);
			
			RecordFrame recordFrame = new RecordFrame(this.receiver);
			recordFrame.getTextArea().setText(infos[0]);
			recordFrame.setVisible(true);
		}
	}
}
