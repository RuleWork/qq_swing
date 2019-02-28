package com.qq.pub;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class TCPSocket {
	private Socket socket = null;
	private ObjectInputStream in = null;
	private ObjectOutputStream out = null;
	
	public String getIp() {
		return this.socket.getLocalAddress().getHostAddress();
	}
	
	public TCPSocket() {

	}
	public TCPSocket(String ip,int port) {
		try {
			this.socket = new Socket(ip, port);
			this.out = new ObjectOutputStream(this.socket.getOutputStream());
			this.in = new ObjectInputStream(this.socket.getInputStream());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public TCPMessage submit(TCPMessage sMessage) {
		TCPMessage rMessage = null;
		try {
			this.out.writeObject(sMessage);
			this.out.flush();
			//µÈ´ý´¦Àí 
			rMessage = (TCPMessage)this.in.readObject();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rMessage;
	}
}
