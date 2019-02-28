package com.qq.dao;

import java.util.List;

import com.qq.bean.Friend;
import com.qq.bean.Qquser;


public interface IQqUserDao {

	public boolean save(Qquser icqUser);

	public int delete(String qqNo);
	
	public int update(Qquser qquser);
	
	public int update(String SQL);

	public Qquser findById(String qqNo);
	public List findBySql(String sql);
	public Friend findBySql_String(String sql);
}

