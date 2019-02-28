package com.qq.listener;

import com.qq.pub.UDPSocket;
//receiveÊÇ×èÈû·½·¨
public class UDPThread extends Thread{
	private UDPSocket udpSocket = null;
	
	private UDPListener udpListener = null;
	
	public UDPThread() {

	}
	public UDPThread(UDPSocket udpSocket) {
		this.udpSocket = udpSocket;
	}
	
	public void addUdpListener(UDPListener udpListener) {
		this.udpListener = udpListener;
	}
	
	
	
	@Override
	public void run() {
		while(true) {
			String udpInfo = this.udpSocket.receive();
			this.udpListener.udpExcu(udpInfo);
		}
	}
}
