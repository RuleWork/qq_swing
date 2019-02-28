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
		//找好友 
		IQqUserDao dao = new QqUserDaoImpl();
		String sql = "select * from qquser "
				+ "where account in "
				+ "(select friendAccount from friend where userAccount = '"+ qquser.getAccount() +"') and state = '1'";
		List<Qquser> list = dao.findBySql(sql);
		
		//发送下线报文，将下线消息，下线用户的账号发回去
		String UDPMessage = CommonUse.OFFLINE + CommonUse.UDP_PACKET_SYMBOL
					+ qquser.getAccount() + CommonUse.UDP_PACKET_SYMBOL;
		//遍历你的好友列表，通知下线
		for (Qquser temp : list) {
			//包装类用起来
			UDPSocket udpSocket = new UDPSocket(temp.getIp(),Integer.parseInt(temp.getPort()));
			udpSocket.send(UDPMessage);
		}
	}
	
	//通知上线
	private void online(Qquser qquser) {
		IQqUserDao dao = new QqUserDaoImpl();
		String sql = "select * from qquser "
				+ "where account in "
				+ "(select friendAccount from friend where userAccount = '"+ qquser.getAccount() +"') and state = '1'";
		List<Qquser> list = dao.findBySql(sql);
		//上线,account这个人,它的ip和port
		String UDPMessage = CommonUse.ONLINE + CommonUse.UDP_PACKET_SYMBOL
					+ qquser.getAccount() + CommonUse.UDP_PACKET_SYMBOL
					+ qquser.getIp() + CommonUse.UDP_PACKET_SYMBOL
					+ qquser.getPort() + CommonUse.UDP_PACKET_SYMBOL;
		//遍历你的好友列表
		for (Qquser temp : list) {
			//包装类用起来
			UDPSocket udpSocket = new UDPSocket(temp.getIp(),Integer.parseInt(temp.getPort()));
			udpSocket.send(UDPMessage);
		}
	}
	
	//查出聊天记录
	private String findRecordMessage(Qquser sender,Qquser receiver) {
		IQqUserDao dao = new QqUserDaoImpl();
		String sqlFind = "select message from friend "
				+ "where friendAccount = '"+ receiver.getAccount() +"' and userAccount = '"+ sender.getAccount() +"'";
		Friend friend = dao.findBySql_String(sqlFind);
		String message = friend.getMessage();
		return message;
	}
	
	//更新聊天记录(将聊天记录塞进数据库中)
	private void updateRecordMessage(Qquser sender,Qquser receiver,String message) {
		IQqUserDao dao = new QqUserDaoImpl();
		String sqlUpdate = "update friend set message = '"+ message +"'"
				+ "where userAccount = '"+ sender.getAccount() +"' and friendAccount = '"+ receiver.getAccount() +"'";
		int num = dao.update(sqlUpdate);
		if (num > 0) {
			System.out.println("更新聊天记录成功");
		} else {
			System.out.println("更新聊天记录失败");
		}
	}
	
	//查找全部好友的全部信息
	private List<Qquser> findFriends(Qquser qquser){
		List<Qquser> list = null;
		IQqUserDao dao = new QqUserDaoImpl();
		String sql = "select * from qquser "
				+ "where account in "
				+ "(select friendAccount from friend where userAccount = '"+ qquser.getAccount() +"') ";
		list = dao.findBySql(sql);
		return list;
	}
	
	//改库并得到完整用户
	private Qquser modifyDB(Qquser qquser) {
		qquser.setState("1");
		IQqUserDao dao = new QqUserDaoImpl();
		dao.update(qquser);//默认修改成功
		Qquser fullUser = new QqUserDaoImpl().findById(qquser.getAccount());
		return fullUser;
	}
	
	//用户注册
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
		System.out.println("有一个客户端上线了！");
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
					//客户端已将Ip和port发送了过来,此lqquser已包含ip和port
					Qquser lqquser = (Qquser)rMessage.getBody(CommonUse.QQ_USER);
					//验证
					if (checkUser(lqquser.getAccount(), lqquser.getPassword())) {
						//改库并得到完整用户信息
						this.fullUser = this.modifyDB(lqquser);
						//通知上线
						this.online(this.fullUser);
						
						sMessage.setHead(CommonUse.SUCCESSFUL);
						sMessage.setBody(CommonUse.QQ_USER, this.fullUser);
					} else {
						sMessage.setHead(CommonUse.FAILURE);
					}
					//把报文再发回去
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
					
					//存进数据库
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
			//记得改库
			this.fullUser.setState("0");
			new QqUserDaoImpl().update(this.fullUser);
			this.offline(this.fullUser);
		}
	}
}
