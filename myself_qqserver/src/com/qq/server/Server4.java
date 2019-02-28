 package com.qq.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server4 {
	public static void main(String[] args) {
		System.out.println("·þÎñÆ÷Æô¶¯£¡");
		try {
			ServerSocket serverSocket = new ServerSocket(10088);
			//The number can only be bound once, but listening can be repeated many times
			/*
			 * Circular interception, as long as there is a request,
			 * they open a channel for them to communicate.
			 */
			while(true) {
				Socket socket = serverSocket.accept();
				ServerThread4 thread4 = new ServerThread4(socket);
				thread4.start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
