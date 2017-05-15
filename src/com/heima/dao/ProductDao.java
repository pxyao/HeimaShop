package com.heima.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import com.heima.domain.Category;
import com.heima.domain.Order;
import com.heima.domain.OrderItem;
import com.heima.domain.Product;
import com.heima.utils.DataSourceUtils;

public class ProductDao {

	/*
	 * 获取热门商品
	 */
	public List<Product> findHotProductList() throws SQLException {

		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "select * from product where is_hot=? limit ?,?";
		return runner.query(sql, new BeanListHandler<Product>(Product.class), 1, 0, 9);

	}

	/*
	 * 获取最新商品
	 */
	public List<Product> findNewProductList() throws SQLException {

		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "select * from product order by pdate desc limit ?,?";
		return runner.query(sql, new BeanListHandler<Product>(Product.class), 0, 9);
	}

	public List<Category> findAllCategory() throws SQLException {
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "select * from category";
		return runner.query(sql, new BeanListHandler<Category>(Category.class));
	}

	public int getCount(String cid) throws SQLException {
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "select count(*) from product where cid=?";
		long query = (long) runner.query(sql, new ScalarHandler(), cid);
		return (int) query;
	}

	public List<Product> findProuctByPage(String cid, int index, int currentCount) throws SQLException {
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "select * from product where cid=? limit ?,?";
		List<Product> query = runner.query(sql, new BeanListHandler<Product>(Product.class), cid, index, currentCount);
		return query;
	}

	public Product findProductByPid(String pid) throws SQLException {
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "select * from product where pid=?";
		return runner.query(sql, new BeanHandler<Product>(Product.class), pid);
	}

	// 向order表中插入数据
	public void addOrders(Order order) throws SQLException {
		QueryRunner runner = new QueryRunner();
		String sql = "insert into orders values(?,?,?,?,?,?,?,?)";
		Connection conn = DataSourceUtils.getConnection();
		runner.update(conn, sql, order.getOid(), order.getOrdertime(), order.getTotal(), order.getState(),
				order.getAddress(), order.getName(), order.getTelephone(), order.getUser().getUid());
	}

	// 向orderitem表中插入数据
	public void addOrderitem(Order order) throws SQLException {
		QueryRunner runner = new QueryRunner();
		String sql = "insert into orderitem values(?,?,?,?,?)";
		Connection conn = DataSourceUtils.getConnection();
		List<OrderItem> orderItems = order.getOrderItems();
		for (OrderItem item : orderItems) {
			runner.update(conn, sql, item.getItemid(), item.getCount(), item.getSubtotal(), item.getProduct().getPid(),
					item.getOrder().getOid());
		}
	}

	public void updateOrderAddr(Order order) throws SQLException {
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "update orders set address=?,name=?,telephone=? where oid=?";
		runner.update(sql, order.getAddress(), order.getName(), order.getTelephone(), order.getOid());
	}

	public List<Order> findAllOrders(String uid) throws SQLException {
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "select * from orders where uid = ?";
		return runner.query(sql, new BeanListHandler<Order>(Order.class), uid);
	}

	public List<Map<String, Object>> findallOrderItemsByOid(String oid) throws SQLException {
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "select * from orderitem i,product p where i.pid = p.pid and i.oid = ?";
		List<Map<String, Object>> mapList = runner.query(sql, new MapListHandler(), oid);
		return mapList;
	}

	public void AddProduct(Product product) throws SQLException {
		QueryRunner runner = new QueryRunner();
		String sql = "insert into product values(?,?,?,?,?,?,?,?,?,?)";
		Connection conn = DataSourceUtils.getConnection();
		runner.update(conn, sql, product.getPid(), product.getPname(), product.getMarket_price(),
				product.getShop_price(), product.getPimage(), product.getPdate(), product.getIs_hot(),
				product.getPdesc(),product.getPflag(),product.getCategory().getCid());
	}
}
