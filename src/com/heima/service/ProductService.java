package com.heima.service;

import java.sql.SQLException;
import java.util.List;

import com.heima.dao.ProductDao;
import com.heima.domain.Category;
import com.heima.domain.PageBean;
import com.heima.domain.Product;

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

	public PageBean findProductListByCid(String cid,int currentPage,int currentCount) {
		
		ProductDao dao = new ProductDao();
		
		
		//��װһ��PageBean ����web��
		PageBean<Product> pageBean = new PageBean<Product>();

		//1.��װ��ǰҳ
		pageBean.setCurrentPage(currentPage);
		//2.��װÿҳ��ʾ������
		pageBean.setCurrentCcount(currentCount);
		//3.��װ������
		int totalCount = 0;
		try {
			totalCount = dao.getCount(cid);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		pageBean.setTotalCount(totalCount);
		//4.��װ��ҳ��
		int totalPage = (int) Math.ceil(1.0*totalCount/currentCount);
		pageBean.setTotalPage(totalPage);
		//5.��ǰҳ��ʾ������
		//select * from product where cid = ? limit = ?,?
		//��ǰҳ����ʾ����index�Ĺ�ϵ
		int index = (currentPage-1)*currentCount;
		List<Product> list=null;
		try {
			list = dao.findProuctByPage(cid,index,currentCount);
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
}
