package com.heima.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.omg.PortableServer.AdapterActivatorOperations;

import com.heima.dao.ProductDao;
import com.heima.domain.Category;
import com.heima.domain.Order;
import com.heima.domain.OrderItem;
import com.heima.domain.PageBean;
import com.heima.domain.Product;
import com.heima.utils.DataSourceUtils;

public class ProductService {

	/*
	 * ���������Ʒ
	 */
	public List<Product> findHotProductList() {

		ProductDao dao = new ProductDao();
		List<Product> findHotProductList = null;
		try {
			findHotProductList = dao.findHotProductList();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return findHotProductList;
	}

	/*
	 * ���������Ʒ
	 */
	public List<Product> findNewProductList() {

		ProductDao dao = new ProductDao();
		List<Product> findNewProductList = null;
		try {
			findNewProductList = dao.findNewProductList();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return findNewProductList;
	}

	/*
	 * ������з���
	 */
	public List<Category> findAllCategory() {

		ProductDao dao = new ProductDao();
		List<Category> findAllCategoryList = null;

		try {
			findAllCategoryList = dao.findAllCategory();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return findAllCategoryList;
	}

	public PageBean findProductListByCid(String cid, int currentPage, int currentCount) {

		ProductDao dao = new ProductDao();

		// ��װһ��PageBean ����web��
		PageBean<Product> pageBean = new PageBean<Product>();

		// 1.��װ��ǰҳ
		pageBean.setCurrentPage(currentPage);
		// 2.��װÿҳ��ʾ������
		pageBean.setCurrentCcount(currentCount);
		// 3.��װ������
		int totalCount = 0;
		try {
			totalCount = dao.getCount(cid);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		pageBean.setTotalCount(totalCount);
		// 4.��װ��ҳ��
		int totalPage = (int) Math.ceil(1.0 * totalCount / currentCount);
		pageBean.setTotalPage(totalPage);
		// 5.��ǰҳ��ʾ������
		// select * from product where cid = ? limit = ?,?
		// ��ǰҳ����ʾ����index�Ĺ�ϵ
		int index = (currentPage - 1) * currentCount;
		List<Product> list = null;
		try {
			list = dao.findProuctByPage(cid, index, currentCount);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		pageBean.setList(list);
		return pageBean;
	}

	public Product findProductByPid(String pid) {
		ProductDao productDao = new ProductDao();
		Product product = null;
		try {
			product = productDao.findProductByPid(pid);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return product;
	}

	/**
	 * �ύ���� �������Ͷ���������ݴ洢�����ݿ���
	 * 
	 * @param order
	 */
	public void submitOrder(Order order) {
		ProductDao dao = new ProductDao();
		try {
			// 1����������
			DataSourceUtils.startTransaction();
			// 2������dao�洢order�����ݵķ���
			dao.addOrders(order);
			// 3������dao�洢orderitem�����ݵķ���
			dao.addOrderitem(order);

		} catch (SQLException e) {
			try {
				DataSourceUtils.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			try {
				DataSourceUtils.commitAndRelease();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void updateOrderAddr(Order order) {
		ProductDao dao = new ProductDao();
		try {
			dao.updateOrderAddr(order);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public List<Order> findAllOrders(String uid) {
		ProductDao dao = new ProductDao();
		List<Order> ordersList = null;
		try {
			ordersList = dao.findAllOrders(uid);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ordersList;
	}

	public List<Map<String, Object>> findallOrderItemsByOid(String oid) {
		ProductDao dao = new ProductDao();
		List<Map<String, Object>> mapList = null;
		try {
			mapList = dao.findallOrderItemsByOid(oid);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return mapList;
	}

	public void AddProduct(Product product) {
		ProductDao dao = new ProductDao();
		try {
			dao.AddProduct(product);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
}
