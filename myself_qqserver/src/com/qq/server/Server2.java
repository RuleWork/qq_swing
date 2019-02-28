 package com.qq.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server2 {
	public static void main(String[] args) {
		System.out.println("������������");
		try {
			ServerSocket serverSocket = new ServerSocket(10088);
			//The number can only be bound once, but listening can be repeated many times
			/*
			 * Circular interception, as long as there is a request,
			 * they open a channel for them to communicate.
			 */
			while(true) {
				Socket socket = serverSocket.accept();
				ServerThread2 thread2 = new ServerThread2(socket);
				thread2.start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
