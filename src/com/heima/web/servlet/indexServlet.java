package com.heima.web.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.heima.domain.Category;
import com.heima.domain.Product;
import com.heima.service.ProductService;

/**
 * Servlet implementation class IndexServlet
 */
@WebServlet("/index")
public class indexServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public indexServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		ProductService service = new ProductService();
		
		//准备热门商品  --List<product>
		List<Product> hotProductlist = service.findHotProductList();
		//准备最新商品
		List<Product> newProductlist = service.findNewProductList();
		//准备分类
		List<Category> categorylist = service.findAllCategory();
		
		
		//放入查找到的热门商品
		request.setAttribute("hotProductlist", hotProductlist);
		//放入查找到的最新商品
		request.setAttribute("newProductlist", newProductlist);
		//放到分类中
		//List<Category> categoryList = service.findAllCategory();
		
		//request.setAttribute("categorylist", categorylist);
		request.getRequestDispatcher("index.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
