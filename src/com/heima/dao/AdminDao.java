package com.heima.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;

import com.heima.domain.Category;
import com.heima.domain.Order;
import com.heima.domain.Product;
import com.heima.utils.DataSourceUtils;

public class AdminDao {
	public List<Category> findAllcategory() throws SQLException{
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "SELECT * FROM category";
		return runner.query(sql, new BeanListHandler<Category>(Category.class));
	}

	public Category findcameByCid(String cid) throws SQLException {
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "SELECT * FROM category where cid = ?";
		return runner.query(sql, new BeanHandler<Category>(Category.class),cid);
	}

	public void updateCategoryName(String cid,String cname) throws SQLException {
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "UPDATE category SET cname=? WHERE cid=?";
		runner.update(sql,cname,cid);
	}

	public void addcategory(int pos,String cname) throws SQLException {
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "INSERT INTO category (cid,cname)VALUES(?,?)";
		runner.update(sql,pos,cname);
	}

	public List<Product> findAllProduct() throws SQLException {
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "SELECT * FROM product";
		return runner.query(sql, new BeanListHandler<Product>(Product.class));
	}

	public void updateProductInfo(String pid,Product product) throws SQLException {
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql =  "UPDATE product values(?,?,?,?,?,?,?,?,?,?) where pid=?";
		runner.update(sql, product.getPid(), product.getPname(), product.getMarket_price(),
				product.getShop_price(), product.getPimage(), product.getPdate(), product.getIs_hot(),
				product.getPdesc(),product.getPflag(),product.getCategory().getCid(),pid);
	}

	public Product findProductByPid(String pid) throws SQLException {
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "SELECT * FROM product where pid = ?";
		return runner.query(sql, new BeanHandler<Product>(Product.class),pid);
	}

	public List<Order> findAllOrders() throws SQLException {
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "SELECT * FROM orders";
		return runner.query(sql, new BeanListHandler<Order>(Order.class));
	}

	public List<Map<String, Object>> findOrderInfoByOid(String oid) throws SQLException {
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "SELECT p.pimage,p.pname,p.shop_price,i.count,i.subtotal "+
					"FROM orderitem i,product p "+
					"WHERE i.pid=p.pid AND oid=?";
		return runner.query(sql, new MapListHandler(),oid);
		
	}
}
