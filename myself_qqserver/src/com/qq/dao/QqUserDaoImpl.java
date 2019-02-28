package com.qq.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.qq.bean.Friend;
import com.qq.bean.Qquser;
import com.qq.db.DBUtil;

public class QqUserDaoImpl implements IQqUserDao {

	public int delete(String qqNo) {
		// TODO Auto-generated method stub
		return 0;
	}

	public Qquser findById(String qqNo) {
		Qquser qquser = null;
		String sql = "select * from qquser where account = '" + qqNo + "'";
		DBUtil util = new DBUtil();
		ResultSet rs = util.query(sql);
		try {
			while(rs.next()) {
				qquser = new Qquser();
				qquser.setAccount(rs.getString(1));
				qquser.setName(rs.getString(2));
				qquser.setPassword(rs.getString(3));
				qquser.setState(rs.getString(4));
				qquser.setIp(rs.getString(5));
				qquser.setPort(rs.getString(6));
				qquser.setPic(rs.getString(7));
				qquser.setInfo(rs.getString(8));
				qquser.setPlace1(rs.getString(9));
				qquser.setPlace2(rs.getString(10));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return qquser;
	}

	public boolean save(Qquser qquser) {
		if(qquser == null){
			return false;
		}
		boolean flag = false;
		String account = qquser.getAccount();
		String name = qquser.getName();
		String password = qquser.getPassword();
		String state = qquser.getState();
		String ip = qquser.getIp();
		String port = qquser.getPort();
		String pic = qquser.getPic();
		String info = qquser.getInfo();
		String place1 = qquser.getPlace1();
		String place2 = qquser.getPlace2();
		String sql = "insert into QqUser(account,name,password,state,ip,port,pic,info,place1,place2) " + 
		 "values(" +
		 "'" + account + "', " +
		 "'" + name + "', " +
		 "'" + password + "', " +
		 "'" + state + "', " +
		 "'" + ip + "', " +
		 "'" + port + "', " +
		 "'" + pic + "', " +
		 "'" + info + "', " +
		 "'" + place1 + "', " +
		 "'" + place2 + "'  " +
		 ")";
		System.out.println(sql);
		DBUtil dbUtil = new DBUtil();
		int result = dbUtil.update(sql);
		dbUtil.close();
		if (result > 0) {
			flag = true;
		}
		return flag;
	}

	public int update(Qquser qquser) {
		String sql = "update qquser set state = '" + qquser.getState() + "', ip = '" + qquser.getIp() + "', port = '" + qquser.getPort() + "' where account = '" + qquser.getAccount() + "'";
		DBUtil dbUtil = new DBUtil();
		int num = dbUtil.update(sql);
		return num;
	}

	public int update(String sql) {
		DBUtil dbUtil = new DBUtil();
		int num =dbUtil.update(sql);
		return num;
	}
	
	public Friend findBySql_String(String sql) {
		DBUtil util = new DBUtil();
		ResultSet rs = util.query(sql);
		Friend friend = null;
		try {
			while(rs.next()) {
				String userAccount = rs.getString(1);
				String friendAccount = rs.getString(2);
				String message = rs.getString(3);
				friend = new Friend(userAccount,friendAccount,message);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return friend;
	}
	
	public List<Qquser> findBySql(String sql) {
		List<Qquser> list = new ArrayList<Qquser>();
		DBUtil util = new DBUtil();
		ResultSet rs = util.query(sql);
		try {
			while(rs.next()) {
				Qquser qquser = new Qquser();
				qquser.setAccount(rs.getString(1));
				qquser.setName(rs.getString(2));
				qquser.setPassword(rs.getString(3));
				qquser.setState(rs.getString(4));
				qquser.setIp(rs.getString(5));
				qquser.setPort(rs.getString(6));
				qquser.setPic(rs.getString(7));
				qquser.setInfo(rs.getString(8));
				qquser.setPlace1(rs.getString(9));
				qquser.setPlace2(rs.getString(10));
				list.add(qquser);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
//	public int add(IcqUser icqUser) {
//		if(icqUser == null){
//			return -1;
//		}
//		int result = -1;
//		String sql = "insert into icqUser(icqNo,nickName,mypassword,status,ip,port,info,pic,sex,place)   " + 
//					 "values(" +
//					 "'"+icqUser.getIcqNo()+"', "+
//					 "'"+icqUser.getNickName()+"', "+
//					 "'"+icqUser.getPassword()+"', "+
//					 "'"+icqUser.getStatus()+  "', "+
//					 "'"+icqUser.getIp()+      "', "+
//					 "'"+icqUser.getPort()+    "', "+
//					 "'"+icqUser.getInfo()+    "', "+
//					 "'"+icqUser.getPic()+     "', "+
//					 "'"+icqUser.getSex()+     "', "+
//					 "'"+icqUser.getPlace()+   "'  "+
//					 ")";
//		
//		System.out.println("insert sql="+sql);
//		
//		DBUtil dbUtil = new DBUtil();
//		result = dbUtil.update(sql);
//		dbUtil.close();
//		return result;
//	}
//
//	public int delete(String icqNo) {
//
//		if(icqNo == null || icqNo.equals("")){
//			return -1;
//		}
//		
//		int result = -1;
//		String sql = "delete from icqUser where icqNo='" + icqNo + "'";
//		DBUtil dbUtil = new DBUtil();
//		result = dbUtil.update(sql);
//		return result;
//
//	}
//
//	public IcqUser queryById(String icqNo) {
//		if(icqNo == null || icqNo.equals("")){
//			return null;
//		}
//
//		IcqUser icqUser = null;
//		String sql = "select * from icqUser where icqNo='" + icqNo + "'";
//		DBUtil dbUtil = new DBUtil();
//		
//		try {
//			ResultSet rs = dbUtil.query(sql);
//			if(rs.next()){
//				icqUser = new IcqUser();
//				icqUser.setIcqNo(rs.getString("icqNo"));
//				icqUser.setPassword(rs.getString("mypassword"));
//				icqUser.setNickName(rs.getString("nickName"));
//				icqUser.setStatus(rs.getString("status"));
//				icqUser.setIp(rs.getString("ip"));
//				icqUser.setPort(rs.getString("port"));
//				icqUser.setInfo(rs.getString("info"));
//				icqUser.setPic(rs.getString("pic"));
//				icqUser.setSex(rs.getString("sex"));
//				icqUser.setPlace(rs.getString("place"));
//			}
//		} catch (SQLException e) {
//			System.out.println("icqUser query SQLException:"+e.getMessage());
//		} 
//		return icqUser;
//
//	}
//
//	public int update(IcqUser icqUser) {
//		if(icqUser == null){
//			return -1;
//		} else if (icqUser.getIcqNo() == null || icqUser.getIcqNo().equals("")){
//			return -1;
//		}
//		String sql = "update icqUser set " +
//				"mypassword='" + icqUser.getPassword() + "', " +
//				"nickName='" + icqUser.getNickName() + "', " +
//				"stuts='" + icqUser.getStatus() + "', " +
//				"ip='" + icqUser.getIp() + "', " +
//				"port='" + icqUser.getPort() + "', " +
//				"info='" + icqUser.getInfo() + "', " +
//				"pic='" + icqUser.getPic() + "', " +
//				"sex='" + icqUser.getSex() + "', " +
//				"place='" + icqUser.getPlace() + "' " +
//				"where icqNo='" + icqUser.getIcqNo() + "'";
//		
//		System.out.println("update SQL:" + sql);
//		
//		DBUtil dbUtil = new DBUtil();
//		int result = -1;
//		result = dbUtil.update(sql);
//		return result;
//	}
//
//	public int update(String sql) {
//		if(sql == null || sql.equals("")){
//			return -1;
//		}
//		int result = -1;
//		DBUtil dbUtil = new DBUtil();
//		result = dbUtil.update(sql);
//		return result;
//	}

}

