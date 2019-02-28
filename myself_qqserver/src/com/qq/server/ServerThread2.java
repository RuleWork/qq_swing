package com.qq.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import com.qq.pub.CommonUse;

public class ServerThread2 extends Thread{
	private Socket socket = null;
	private BufferedReader reader = null;
	private BufferedWriter writer = null;
	
	private boolean checkUser(String id,String pass) {
		boolean flag = false;
		if ("abc".equals(id) && "123".equals(pass)) {
			flag = true;
		}
		return flag;
	}
	public ServerThread2() {
		// TODO Auto-generated constructor stub
	}
	public ServerThread2(Socket socket) {
		System.out.println("有一个客户端上线了！");
		this.socket = socket;
		try {
			this.reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
			this.writer = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));
			
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		
	}
	@Override
	public void run() {
		try {
			String info = null;
			info = this.reader.readLine();
			String[] infos = info.split("/");
			String id = infos[0];
			String pass = infos[1];
			System.out.println("服务器收到的消息：" + info);
			if (this.checkUser(id, pass)) {
				this.writer.write(CommonUse.SUCCESSFUL + "\n");
			} else {
				this.writer.write(CommonUse.FAILURE + "\n");
			}
			this.writer.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
	}
}
