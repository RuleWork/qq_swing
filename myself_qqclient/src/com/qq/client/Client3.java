package com.qq.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import com.qq.bean.Qquser;
import com.qq.pub.CommonUse;

public class Client3 {
	public static void main(String[] args) {
		System.out.println("������һ���ͻ��ˣ�");
		Socket socket = null;
		ObjectInputStream in = null;
		ObjectOutputStream out = null;
		
		BufferedReader sysRead = new BufferedReader(new InputStreamReader(System.in));
		
		try {
			socket = new Socket("127.0.0.1", 10088);
			//You have to fork out New ��
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
			
			String info = null;
			info = sysRead.readLine();
			String[] infos = info.split("/");
			
			Qquser qquser = new Qquser();
			qquser.setAccount(infos[0]);
			qquser.setPassword(infos[1]);
			
			out.writeObject(qquser);
			out.flush();
			//�ȴ�����������
			String back = in.readObject().toString();
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
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
