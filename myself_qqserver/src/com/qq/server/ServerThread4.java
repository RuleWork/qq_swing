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

public class ServerThread4 extends Thread{
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

	private boolean checkUser(String id,String pass) {
		boolean flag = false;
		if ("abc".equals(id) && "123".equals(pass)) {
			flag = true;
		}
		return flag;
	}
	
	public ServerThread4() {

	}
	
	public ServerThread4(Socket socket) {
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
		Qquser qquser = null;
		try {
			while(true) {
				qquser = (Qquser)this.in.readObject();
				if (this.register(qquser)) {
					out.writeObject(CommonUse.SUCCESSFUL);
				} else {
					out.writeObject(CommonUse.FAILURE);
				}
				out.flush();
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
