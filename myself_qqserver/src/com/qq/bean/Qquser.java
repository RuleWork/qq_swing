package com.qq.bean;

import java.io.Serializable;

public class Qquser implements Serializable {
    private String account;
    private String name;
    private String password;
    private String state;
    private String ip;
    private String port;
    private String pic;
    private String info;
    private String place1;
    private String place2;
    
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	public String getPlace1() {
		return place1;
	}
	public void setPlace1(String place1) {
		this.place1 = place1;
	}
	public String getPlace2() {
		return place2;
	}
	public void setPlace2(String place2) {
		this.place2 = place2;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	@Override
	public String toString() {
		return "Qquser [account=" + account + ", name=" + name + ", password="
				+ password + ", state=" + state + ", ip=" + ip + ", port="
				+ port + ", pic=" + pic + ", info=" + info + ", place1="
				+ place1 + ", place2=" + place2 + "]";
	}
	
	
}
