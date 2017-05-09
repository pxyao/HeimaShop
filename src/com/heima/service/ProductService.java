package com.heima.service;

import java.sql.SQLException;
import java.util.List;

import com.heima.dao.ProductDao;
import com.heima.domain.Category;
import com.heima.domain.PageBean;
import com.heima.domain.Product;

public class ProductService {

	/*
	 * 获得热门商品
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
	 * 获得最新商品
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
	 * 获得所有分类
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
		
		
		//封装一个PageBean 返回web层
		PageBean<Product> pageBean = new PageBean<Product>();

		//1.封装当前页
		pageBean.setCurrentPage(currentPage);
		//2.封装每页显示的条数
		pageBean.setCurrentCcount(currentCount);
		//3.封装总条数
		int totalCount = 0;
		try {
			totalCount = dao.getCount(cid);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		pageBean.setTotalCount(totalCount);
		//4.封装总页数
		int totalPage = (int) Math.ceil(1.0*totalCount/currentCount);
		pageBean.setTotalPage(totalPage);
		//5.当前页显示的数据
		//select * from product where cid = ? limit = ?,?
		//当前页与启示索引index的关系
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
