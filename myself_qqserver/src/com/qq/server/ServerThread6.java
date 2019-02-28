package com.qq.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.List;

import com.qq.bean.Friend;
import com.qq.bean.Qquser;
import com.qq.dao.IQqUserDao;
import com.qq.dao.QqUserDaoImpl; 
import com.qq.pub.CommonUse;
import com.qq.pub.TCPMessage;
import com.qq.pub.UDPSocket;

public class ServerThread6 extends Thread{
	private Qquser fullUser = null;
	
	private Socket socket = null;
	private ObjectInputStream in = null;
	private ObjectOutputStream out = null;
	
	private void offline(Qquser qquser) {
		//�Һ��� 
		IQqUserDao dao = new QqUserDaoImpl();
		String sql = "select * from qquser "
				+ "where account in "
				+ "(select friendAccount from friend where userAccount = '"+ qquser.getAccount() +"') and state = '1'";
		List<Qquser> list = dao.findBySql(sql);
		
		//�������߱��ģ���������Ϣ�������û����˺ŷ���ȥ
		String UDPMessage = CommonUse.OFFLINE + CommonUse.UDP_PACKET_SYMBOL
					+ qquser.getAccount() + CommonUse.UDP_PACKET_SYMBOL;
		//������ĺ����б�֪ͨ����
		for (Qquser temp : list) {
			//��װ��������
			UDPSocket udpSocket = new UDPSocket(temp.getIp(),Integer.parseInt(temp.getPort()));
			udpSocket.send(UDPMessage);
		}
	}
	
	//֪ͨ����
	private void online(Qquser qquser) {
		IQqUserDao dao = new QqUserDaoImpl();
		String sql = "select * from qquser "
				+ "where account in "
				+ "(select friendAccount from friend where userAccount = '"+ qquser.getAccount() +"') and state = '1'";
		List<Qquser> list = dao.findBySql(sql);
		//����,account�����,����ip��port
		String UDPMessage = CommonUse.ONLINE + CommonUse.UDP_PACKET_SYMBOL
					+ qquser.getAccount() + CommonUse.UDP_PACKET_SYMBOL
					+ qquser.getIp() + CommonUse.UDP_PACKET_SYMBOL
					+ qquser.getPort() + CommonUse.UDP_PACKET_SYMBOL;
		//������ĺ����б�
		for (Qquser temp : list) {
			//��װ��������
			UDPSocket udpSocket = new UDPSocket(temp.getIp(),Integer.parseInt(temp.getPort()));
			udpSocket.send(UDPMessage);
		}
	}
	
	//��������¼
	private String findRecordMessage(Qquser sender,Qquser receiver) {
		IQqUserDao dao = new QqUserDaoImpl();
		String sqlFind = "select message from friend "
				+ "where friendAccount = '"+ receiver.getAccount() +"' and userAccount = '"+ sender.getAccount() +"'";
		Friend friend = dao.findBySql_String(sqlFind);
		String message = friend.getMessage();
		return message;
	}
	
	//���������¼(�������¼�������ݿ���)
	private void updateRecordMessage(Qquser sender,Qquser receiver,String message) {
		IQqUserDao dao = new QqUserDaoImpl();
		String sqlUpdate = "update friend set message = '"+ message +"'"
				+ "where userAccount = '"+ sender.getAccount() +"' and friendAccount = '"+ receiver.getAccount() +"'";
		int num = dao.update(sqlUpdate);
		if (num > 0) {
			System.out.println("���������¼�ɹ�");
		} else {
			System.out.println("���������¼ʧ��");
		}
	}
	
	//����ȫ�����ѵ�ȫ����Ϣ
	private List<Qquser> findFriends(Qquser qquser){
		List<Qquser> list = null;
		IQqUserDao dao = new QqUserDaoImpl();
		String sql = "select * from qquser "
				+ "where account in "
				+ "(select friendAccount from friend where userAccount = '"+ qquser.getAccount() +"') ";
		list = dao.findBySql(sql);
		return list;
	}
	
	//�ĿⲢ�õ������û�
	private Qquser modifyDB(Qquser qquser) {
		qquser.setState("1");
		IQqUserDao dao = new QqUserDaoImpl();
		dao.update(qquser);//Ĭ���޸ĳɹ�
		Qquser fullUser = new QqUserDaoImpl().findById(qquser.getAccount());
		return fullUser;
	}
	
	//�û�ע��
	private boolean register(Qquser qquser) {
		boolean flag = false;
		IQqUserDao dao = new QqUserDaoImpl();
		if (dao.save(qquser)) {
			flag = true;
		}
		return flag;
	}
	
	private boolean checkUser(String id,String pass) {
		boolean flag = false;
		IQqUserDao dao = new QqUserDaoImpl();
		Qquser temp = dao.findById(id);
		if (temp != null && temp.getPassword().equals(pass)) {
			flag = true;
		}
		return flag;
	}
	public ServerThread6() {

	}
	
	public ServerThread6(Socket socket) {
		System.out.println("��һ���ͻ��������ˣ�");
		this.socket = socket;
		try {
			this.in = new ObjectInputStream(this.socket.getInputStream());
			this.out = new ObjectOutputStream(this.socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		TCPMessage rMessage = null;
		String head = null;
		
		try {
			while(true) {
				rMessage = (TCPMessage)this.in.readObject();
				head = rMessage.getHead();
				
				if (CommonUse.REGISTER.equals(head)) {
					TCPMessage sMessage = new TCPMessage();
					Qquser rqquser = (Qquser)rMessage.getBody(CommonUse.QQ_USER);
					if (this.register(rqquser)) {
						sMessage.setHead(CommonUse.SUCCESSFUL);
					} else {
						sMessage.setHead(CommonUse.FAILURE);
					}
					out.writeObject(sMessage);
					out.flush(); 
				} else if (CommonUse.LOGIN.equals(head)) {
					TCPMessage sMessage = new TCPMessage();
					//�ͻ����ѽ�Ip��port�����˹���,��lqquser�Ѱ���ip��port
					Qquser lqquser = (Qquser)rMessage.getBody(CommonUse.QQ_USER);
					//��֤
					if (checkUser(lqquser.getAccount(), lqquser.getPassword())) {
						//�ĿⲢ�õ������û���Ϣ
						this.fullUser = this.modifyDB(lqquser);
						//֪ͨ����
						this.online(this.fullUser);
						
						sMessage.setHead(CommonUse.SUCCESSFUL);
						sMessage.setBody(CommonUse.QQ_USER, this.fullUser);
					} else {
						sMessage.setHead(CommonUse.FAILURE);
					}
					//�ѱ����ٷ���ȥ
					out.writeObject(sMessage);
					out.flush();
				} else if (CommonUse.FIND.equals(head)) {
					TCPMessage sMessage = new TCPMessage();
					
					Qquser fqquser = (Qquser)rMessage.getBody(CommonUse.QQ_USER);
					sMessage.setBody(CommonUse.FRIENDS_INFO, this.findFriends(fqquser));
					
					out.writeObject(sMessage);
					out.flush();
				} else if (CommonUse.Record.equals(head)) {
					TCPMessage sMessage = new TCPMessage();
					
					Object[] chater = (Object[])rMessage.getBody(CommonUse.Object_SRM);
					Qquser sender = (Qquser)chater[0];
					Qquser receiver = (Qquser)chater[1];
					String message = (String)chater[2];
					
					//������ݿ�
					this.updateRecordMessage(sender, receiver, message);
					
					out.writeObject(sMessage);
					out.flush();
				} else if(CommonUse.Chated.equals(head)) {
					TCPMessage sMessage = new TCPMessage();
					
					Qquser[] chater = (Qquser[])rMessage.getBody(CommonUse.Object_SRM);
					
					Qquser sender = chater[0];
					Qquser receiver = chater[1];
					
					sMessage.setBody(CommonUse.FRIENDS_INFO, this.findRecordMessage(sender, receiver));
					
					out.writeObject(sMessage);
					out.flush();
				}
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			//�ǵøĿ�
			this.fullUser.setState("0");
			new QqUserDaoImpl().update(this.fullUser);
			this.offline(this.fullUser);
		}
	}
}
