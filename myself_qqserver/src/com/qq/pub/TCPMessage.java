package com.qq.pub;
//报文对象,io里流动就是它

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class TCPMessage implements Serializable{
	private String head = null;
	private Map<String, Object> map = new HashMap<String, Object>();
	
	public String getHead() {
		return head;
	}
	public void setHead(String head) {
		this.head = head;
	}
	
	public Object getBody(String key) {
		return this.map.get(key);
	}
	public void setBody(String key,Object value) {
		this.map.put(key, value);
	}
}
