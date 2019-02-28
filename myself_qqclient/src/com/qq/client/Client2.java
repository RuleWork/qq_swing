package com.qq.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import com.qq.pub.CommonUse;

public class Client2 {
	public static void main(String[] args) {
		System.out.println("启动了一个客户端！");
		Socket socket = null;
		BufferedWriter writer = null;
		BufferedReader reader = null;
		
		BufferedReader sysRead = null;
		sysRead = new BufferedReader(new InputStreamReader(System.in));
		
		try {
			socket = new Socket("127.0.0.1", 10088);
			writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			String info = null;
			
			info = sysRead.readLine();
			writer.write(info + "\n");
			writer.flush();
			//等待服务器处理
			String back = reader.readLine();
			if (CommonUse.SUCCESSFUL.equals(back)) {
				System.out.println("登录成功！");
			} else {
				System.out.println("登录失败！");
			}
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
