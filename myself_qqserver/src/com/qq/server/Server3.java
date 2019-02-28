 package com.qq.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server3 {
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
				ServerThread3 thread3 = new ServerThread3(socket);
				thread3.start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
