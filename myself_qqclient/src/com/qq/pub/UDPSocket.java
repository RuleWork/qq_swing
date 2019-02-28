package com.qq.pub;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class UDPSocket {
	private DatagramSocket datagramSocket = null;
	private DatagramPacket datagramPacket = null;
	
	//用于收
	public UDPSocket() {
		try {
			byte[] pool = new byte[1024];
			this.datagramSocket = new DatagramSocket();//不写无论如何会给一个空闲的
			this.datagramPacket = new DatagramPacket(pool, 0, pool.length);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String receive() {
		String info = null;
		try {
			this.datagramSocket.receive(this.datagramPacket);
			info = new String(this.datagramPacket.getData());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return info;
	}
	
	//用于发
	public UDPSocket(String ip, int port) {
		try {
			byte[] pool = new byte[1024];
			this.datagramSocket = new DatagramSocket();//不写无论如何会给一个空闲的
			this.datagramPacket = new DatagramPacket(pool, 0, pool.length, InetAddress.getByName(ip),port);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void send(String info) {
		try {
			this.datagramPacket.setData(info.getBytes());
			this.datagramSocket.send(this.datagramPacket);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public int getPort() {
		return this.datagramSocket.getLocalPort();
	}
	
}
