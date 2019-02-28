package com.qq.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class ServerThread1 extends Thread{
	private Socket socket = null;
	private BufferedReader reader = null;
	private BufferedWriter writer = null;

	public ServerThread1() {

	}
	
	public ServerThread1(Socket socket) {
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
		String info = null;
		try {
			while(true) {
				info = this.reader.readLine();
				System.out.println("服务器收到的消息是:" + info);
				if ("bye".equals(info)) {
					break;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
