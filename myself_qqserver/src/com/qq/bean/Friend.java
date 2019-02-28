package com.qq.bean;

import java.io.Serializable;

//保存对象的信息，在需要的时候再读取这个对象，这就是对象序列化
//加入了对象序列化的协议
public class Friend implements Serializable {
    private String userAccount;
    private String friendAccount;
    private String message;
    
    public Friend() {}
	public Friend(String userAccount, String friendAccount, String message) {
		super();
		this.userAccount = userAccount;
		this.friendAccount = friendAccount;
		this.message = message;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getFriendAccount() {
		return friendAccount;
	}
	public void setFriendAccount(String friendAccount) {
		this.friendAccount = friendAccount;
	}
	public String getUserAccount() {
		return userAccount;
	}
	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}
}
