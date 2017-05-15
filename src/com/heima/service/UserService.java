package com.heima.service;

import java.sql.SQLException;

import com.heima.dao.UserDao;
import com.heima.domain.User;

public class UserService {

	public boolean regist(User user) {
		
		UserDao userDao=new UserDao();
		int row = 0;
		try {
			row=userDao.regist(user);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return row>0?true:false;
	}
	
	public User login(String username,String password){
		UserDao userDao = new UserDao();
		User user = new User();
		try {
			user = userDao.login(username, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return user;
	}
	
	//激活
	public void active(String activeCode){
		UserDao userDao=new UserDao();
		try {
			userDao.active(activeCode);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	//校验用户名是否存在
	public boolean checkUsername(String username) {
		UserDao dao=new UserDao();
		long isExist=0L;
		try {
			isExist=dao.checkUsername(username);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return isExist>0?true:false;
	}
}
