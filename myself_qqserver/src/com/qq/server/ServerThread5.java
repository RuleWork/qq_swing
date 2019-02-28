package com.qq.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

import com.qq.bean.Qquser;
import com.qq.dao.IQqUserDao;
import com.qq.dao.QqUserDaoImpl; 
import com.qq.pub.CommonUse;
import com.qq.pub.TCPMessage;

public class ServerThread5 extends Thread{
	private Socket socket = null;
	private ObjectInputStream in = null;
	private ObjectOutputStream out = null;
	
	//用户注册
	private boolean register(Qquser qquser) {
		boolean flag = false;
		IQqUserDao dao = new QqUserDaoImpl();
		if (dao.save(qquser)) {
			flag = true;
		}
		return flag;
	}
	
	public ServerThread5() {

	}
	
	public ServerThread5(Socket socket) {
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
					Qquser qquser = (Qquser)rMessage.getBody(CommonUse.QQ_USER);
					if (this.register(qquser)) {
						sMessage.setHead(CommonUse.SUCCESSFUL);
					} else {
						sMessage.setHead(CommonUse.FAILURE);
					}
					out.writeObject(sMessage);
					out.flush(); 
				}
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
