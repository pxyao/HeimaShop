package com.heima.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.heima.dao.AdminDao;
import com.heima.domain.Category;
import com.heima.domain.Order;
import com.heima.domain.Product;
import com.heima.web.servlet.adminServlet;

public class AdminService {

	public List<Category> findAllcategory() {
		AdminDao dao = new AdminDao();
		List<Category> categorieList = null;
		try {
			categorieList = dao.findAllcategory();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return categorieList;
	}

	public Category findcameByCid(String cid) {
		AdminDao dao = new AdminDao();
		Category category = null;
		try {
			category = dao.findcameByCid(cid);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return category;
	}

	public void updateCategoryName(String cid,String cname) {
		AdminDao dao = new AdminDao();
		try {
			dao.updateCategoryName(cid,cname);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void addcategory(int pos,String cname) {
		AdminDao dao = new AdminDao();
		try {
			dao.addcategory(pos,cname);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public List<Product> findAllProduct() {
		AdminDao dao = new AdminDao();
		List<Product> productList = null;
		try {
			productList = dao.findAllProduct();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return productList;
	}

	public void updateProductInfo(String pid,Product product) {
		AdminDao dao = new AdminDao();
		try {
			dao.updateProductInfo(pid,product);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public Product findProductByPid(String pid) {
		AdminDao dao = new AdminDao();
		Product product = null;
		try {
			product = dao.findProductByPid(pid);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return product;
	}

	public List<Order> findAllOrders() {
		AdminDao dao = new AdminDao();
		List<Order> orderList = null;
		try {
			orderList = dao.findAllOrders();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return orderList;
	}

	public List<Map<String, Object>> findOrderInfoByOid(String oid) {
		AdminDao dao = new AdminDao();
		List<Map<String, Object>> orderInfoList = null;
		try {
			orderInfoList = dao.findOrderInfoByOid(oid);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return orderInfoList;
	}
}
