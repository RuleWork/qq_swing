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
		System.out.println("������һ���ͻ��ˣ�");
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
			//�ȴ�����������
			String back = reader.readLine();
			if (CommonUse.SUCCESSFUL.equals(back)) {
				System.out.println("��¼�ɹ���");
			} else {
				System.out.println("��¼ʧ�ܣ�");
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
