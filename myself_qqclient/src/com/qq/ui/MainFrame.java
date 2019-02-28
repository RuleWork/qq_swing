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
		
		 //�����ߵ�
		 for (Qquser qquser : this.friends) {
			if ("1".equals(qquser.getState())) {
				afterList.add(qquser);
			}
		 }
		 //�Ҳ����ߵ�
		 for (Qquser qquser : this.friends) {
			if ("0".equals(qquser.getState())){
				afterList.add(qquser);
			}
		 }
		 //��ͷ��
		 for (Qquser qquser : afterList) {
			if ("1".equals(qquser.getState())) {
				qquser.setPlace1("./onimg/" + qquser.getPic() + ".png");
			}
			if ("0".equals(qquser.getState())) {
				qquser.setPlace1("./outimg/" + qquser.getPic() + ".png");
			}
		}
		 //�Ź����
		 this.friends = afterList;
		 //�����б���ʽ
		 this.listModel = new DefaultListModel();
		 //��������Ϣѭ������
		 for (Qquser qquser : this.friends) {
			 this.listModel.addElement(qquser);
		 }
		 this.friendList.setModel(this.listModel);
	}
	
	//���ﻹ����������¼�
	private void createFriendList() {
		 TCPMessage sMessage = new TCPMessage();
		 sMessage.setHead(CommonUse.FIND);
		 sMessage.setBody(CommonUse.QQ_USER, this.fullUser);
		 
		 TCPMessage rMessage = tcpSocket.submit(sMessage);
		 
		 List<Qquser> beforeList = (List<Qquser>)rMessage.getBody(CommonUse.FRIENDS_INFO);
		 List<Qquser> afterList = new ArrayList<Qquser>();
		 //�����ߵ�
		 for (Qquser qquser : beforeList) {
			if ("1".equals(qquser.getState())) {
				afterList.add(qquser);
			}
		 }
		 //�Ҳ����ߵ�
		 for (Qquser qquser : beforeList) {
			if ("0".equals(qquser.getState())){
				afterList.add(qquser);
			}
		 }
		 //��ͷ��
		 for (Qquser qquser : afterList) {
			if ("1".equals(qquser.getState())) {
				qquser.setPlace1("./onimg/" + qquser.getPic() + ".png");
			}
			if ("0".equals(qquser.getState())) {
				qquser.setPlace1("./outimg/" + qquser.getPic() + ".png");
			}
		 }
		 //�Ź����
		 this.friends = afterList;
		 //�����б���ʽ
		 this.listModel = new DefaultListModel();
		 //��������Ϣѭ������
		 for (Qquser qquser : friends) {
			 this.listModel.addElement(qquser);
		 }
		 //���б���ʽ����list
		 this.friendList = new JList(this.listModel);
		 //�������¼�,�����������㶨������fullUser��receiver
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
		 //���ø�����Ⱦ��
		 this.friendList.setCellRenderer(new ClientImgCell());
	}
	
	private void init() {
		JPanel bodyPanel = (JPanel)this.getContentPane();
		bodyPanel.setLayout(new BorderLayout());
		
		this.lableUserName = new JLabel(this.fullUser.getName());
		bodyPanel.add(this.lableUserName,BorderLayout.NORTH);
		
		this.createFriendList();
		bodyPanel.add(this.friendList,BorderLayout.CENTER);
		
		//�¼�Դ
		UDPThread udpThread = new UDPThread(this.udpSocket);
		udpThread.addUdpListener(this);
		udpThread.start();
		
		this.setTitle(this.fullUser.getAccount() + "��QQ����");
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
	//����ִ�еĶ���udp�ı���
	@Override
	public void udpExcu(String udpInfo) {
		//����
		String infos[] = udpInfo.split(CommonUse.UDP_PACKET_SYMBOL);
		//������ͷȡ����
		String head = infos[0];
		//�����߼��ж�
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
					temp.setState("0"); //��������ip,port���߻���
					break;
				} 
			}
			this.refreshFriendList();
		} else if (CommonUse.MESSAGE.equals(head)) {
			//���㷢��Ϣ���Ǹ���(�Լ���Զ��sender��������Զ��receiver)
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
