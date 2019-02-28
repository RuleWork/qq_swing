package com.qq.pub;

public class Test {
	public static void main(String[] args) {
		String str = "ÄãºÃ°¡" + CommonUse.UDP_PACKET_SYMBOL;
		String info[] =  str.split(CommonUse.UDP_PACKET_SYMBOL);
		System.out.println(info[0]);
	}
}
