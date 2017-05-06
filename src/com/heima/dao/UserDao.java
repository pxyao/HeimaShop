package com.heima.dao;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import com.heima.domain.User;
import com.heima.utils.DataSourceUtils;

public class UserDao {
	/**
	 * 注册
	 * @param user
	 * @return
	 * @throws SQLException
	 */
	public int regist(User user) throws SQLException{
		QueryRunner runner=new QueryRunner(DataSourceUtils.getDataSource());
		String sql="insert into user values(?,?,?,?,?,?,?,?,?,?)";
		int update=runner.update(sql,user.getUid(),user.getUsername(),
				user.getPassword(),user.getName(),user.getEmail(),
				user.getTelephone(),user.getBirthday(),user.getSex(),
				user.getState(),user.getCode());
		return update;
	}
	/**
	 * 激活
	 * @param activecode
	 * @throws SQLException
	 */
	public void active(String activecode) throws SQLException{
		QueryRunner runner=new QueryRunner(DataSourceUtils.getDataSource());
		String sql="update user set state=? where code=?";
		runner.update(sql,1,activecode);
	}
	
	/**
	 * 用户名是否存在
	 * @return
	 * @throws SQLException 
	 */
	public long checkUsername(String username) throws SQLException {
		QueryRunner runner=new QueryRunner(DataSourceUtils.getDataSource());
		String sql="select count(*) from user where username = ?";
		long query = (long) runner.query(sql,new ScalarHandler(),username);
		return query;
	}
}
