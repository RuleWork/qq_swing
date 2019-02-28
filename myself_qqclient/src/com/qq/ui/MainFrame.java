package com.qq.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionListener;

import com.qq.bean.Qquser;
import com.qq.component.ClientImgCell;
import com.qq.listener.UDPListener;
import com.qq.listener.UDPThread;
import com.qq.pub.CommonUse;
import com.qq.pub.TCPMessage;
import com.qq.pub.TCPSocket;
import com.qq.pub.UDPSocket;

public class MainFrame extends JFrame implements UDPListener{
	private TCPSocket tcpSocket = null;
	private UDPSocket udpSocket = null;
	
	private Qquser fullUser = null;
	private List<Qquser> friends = null;
	
	private JLabel lableUserName = null;
	private JList friendList = null;
	
	private DefaultListModel listModel = null;
	private JScrollPane scrollPane = null;
	
	private void refreshFriendList() {
		 List<Qquser> afterList = new ArrayList<Qquser>();
		
		 //找在线的
		 for (Qquser qquser : this.friends) {
			if ("1".equals(qquser.getState())) {
				afterList.add(qquser);
			}
		 }
		 //找不在线的
		 for (Qquser qquser : this.friends) {
			if ("0".equals(qquser.getState())){
				afterList.add(qquser);
			}
		 }
		 //做头像
		 for (Qquser qquser : afterList) {
			if ("1".equals(qquser.getState())) {
				qquser.setPlace1("./onimg/" + qquser.getPic() + ".png");
			}
			if ("0".equals(qquser.getState())) {
				qquser.setPlace1("./outimg/" + qquser.getPic() + ".png");
			}
		}
		 //排过序的
		 this.friends = afterList;
		 //设置列表样式
		 this.listModel = new DefaultListModel();
		 //将好友信息循环加入
		 for (Qquser qquser : this.friends) {
			 this.listModel.addElement(qquser);
		 }
		 this.friendList.setModel(this.listModel);
	}
	
	//这里还设置了鼠标事件
	private void createFriendList() {
		 TCPMessage sMessage = new TCPMessage();
		 sMessage.setHead(CommonUse.FIND);
		 sMessage.setBody(CommonUse.QQ_USER, this.fullUser);
		 
		 TCPMessage rMessage = tcpSocket.submit(sMessage);
		 
		 List<Qquser> beforeList = (List<Qquser>)rMessage.getBody(CommonUse.FRIENDS_INFO);
		 List<Qquser> afterList = new ArrayList<Qquser>();
		 //找在线的
		 for (Qquser qquser : beforeList) {
			if ("1".equals(qquser.getState())) {
				afterList.add(qquser);
			}
		 }
		 //找不在线的
		 for (Qquser qquser : beforeList) {
			if ("0".equals(qquser.getState())){
				afterList.add(qquser);
			}
		 }
		 //做头像
		 for (Qquser qquser : afterList) {
			if ("1".equals(qquser.getState())) {
				qquser.setPlace1("./onimg/" + qquser.getPic() + ".png");
			}
			if ("0".equals(qquser.getState())) {
				qquser.setPlace1("./outimg/" + qquser.getPic() + ".png");
			}
		 }
		 //排过序的
		 this.friends = afterList;
		 //设置列表样式
		 this.listModel = new DefaultListModel();
		 //将好友信息循环加入
		 for (Qquser qquser : friends) {
			 this.listModel.addElement(qquser);
		 }
		 //将列表样式加入list
		 this.friendList = new JList(this.listModel);
		 //添加鼠标事件,用匿名类来搞定，传入fullUser和receiver
		 this.friendList.addMouseListener(new MouseAdapter() {
			 @Override
			 public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
					Qquser receiver = (Qquser)friendList.getSelectedValue();
					SendFrame sendFrame = new SendFrame(fullUser, receiver);
					sendFrame.setVisible(true);
				}
			 }
		 });
		 //设置格子渲染器
		 this.friendList.setCellRenderer(new ClientImgCell());
	}
	
	private void init() {
		JPanel bodyPanel = (JPanel)this.getContentPane();
		bodyPanel.setLayout(new BorderLayout());
		
		this.lableUserName = new JLabel(this.fullUser.getName());
		bodyPanel.add(this.lableUserName,BorderLayout.NORTH);
		
		this.createFriendList();
		bodyPanel.add(this.friendList,BorderLayout.CENTER);
		
		//事件源
		UDPThread udpThread = new UDPThread(this.udpSocket);
		udpThread.addUdpListener(this);
		udpThread.start();
		
		this.setTitle(this.fullUser.getAccount() + "的QQ界面");
		this.setBounds(100,20,200,600);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public MainFrame() {}
	public MainFrame(Qquser qquser,TCPSocket tcpSocket,UDPSocket udpSocket) {
		this.fullUser = qquser;
		this.tcpSocket = tcpSocket;
		this.udpSocket = udpSocket;
		this.init();
	}
	//这里执行的都是udp的报文
	@Override
	public void udpExcu(String udpInfo) {
		//拆报文
		String infos[] = udpInfo.split(CommonUse.UDP_PACKET_SYMBOL);
		//将报文头取出来
		String head = infos[0];
		//进行逻辑判断
		if (CommonUse.ONLINE.equals(head)) {
			String account = infos[1];
			String ip = infos[2];
			String port = infos[3];
			
			for (Qquser temp : this.friends) {
				if (account.equals(temp.getAccount())) {
					temp.setState("1"); 
					temp.setIp(ip);
					temp.setPort(port);
				} 
			}
			this.refreshFriendList();
		} else if (CommonUse.OFFLINE.equals(head)) {
			String account = infos[1];
			for (Qquser temp : this.friends) {
				if (account.equals(temp.getAccount())) {
					temp.setState("0"); //不用设置ip,port上线会冲掉
					break;
				} 
			}
			this.refreshFriendList();
		} else if (CommonUse.MESSAGE.equals(head)) {
			//给你发消息的那个人(自己永远是sender，别人永远是receiver)
			Qquser receiver = null;
			
			String account = infos[1];
			String info = infos[2];
			
			
			for (Qquser temp : this.friends) {
				if (account.equals(temp.getAccount())) {
					receiver = temp;
					break;
				} 
			}
			ReceiveFrame receiveFrame = new ReceiveFrame(this.fullUser,receiver,info);
			receiveFrame.setVisible(true);
		}
	}
	
	
	
}
