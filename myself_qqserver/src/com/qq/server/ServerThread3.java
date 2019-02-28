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
import com.qq.pub.CommonUse;

public class ServerThread3 extends Thread{
	private Socket socket = null;
	private ObjectInputStream in = null;
	private ObjectOutputStream out = null;

	private boolean checkUser(String id,String pass) {
		boolean flag = false;
		if ("abc".equals(id) && "123".equals(pass)) {
			flag = true;
		}
		return flag;
	}
	
	public ServerThread3() {}
	
	public ServerThread3(Socket socket) {
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
			qquser = (Qquser)this.in.readObject();
			if (checkUser(qquser.getAccount(), qquser.getPassword())) {
				this.out.writeObject(CommonUse.SUCCESSFUL);
			} else {
				this.out.writeObject(CommonUse.FAILURE);
			}
			this.out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
