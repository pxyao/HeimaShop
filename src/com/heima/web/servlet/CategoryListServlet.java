package com.heima.web.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.heima.domain.Category;
import com.heima.service.ProductService;
import com.heima.utils.JedisPoolUtils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Servlet implementation class CategoryListServlet
 */
@WebServlet("/CategoryList")
public class CategoryListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CategoryListServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		ProductService service = new ProductService();
		
		//先从缓存找那个查询categoryList 如果有则直接使用 没有再从数据库中查询到缓存中
		//1. 获得jedis 连接redis 数据库
		Jedis jedis = JedisPoolUtils.getJedis();
		String categoryListJson = jedis.get("categoryListJson");
		//2. 判断categoryListJson是否为空
		if (categoryListJson==null) {
			System.out.println("redis数据库中没有 categoryListJson 查询数据库");
			List<Category> categoryList = service.findAllCategory();
			Gson gson = new Gson();
			categoryListJson = gson.toJson(categoryList);
			jedis.set("categoryListJson", categoryListJson);
		}
		
		response.setContentType("text/html;charset=UTF-8");
		response.getWriter().write(categoryListJson);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
