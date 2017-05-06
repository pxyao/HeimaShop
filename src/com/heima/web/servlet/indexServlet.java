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
		
		//׼��������Ʒ  --List<product>
		List<Product> hotProductlist = service.findHotProductList();
		//׼��������Ʒ
		List<Product> newProductlist = service.findNewProductList();
		//׼������
		List<Category> categorylist = service.findAllCategory();
		
		
		//������ҵ���������Ʒ
		request.setAttribute("hotProductlist", hotProductlist);
		//������ҵ���������Ʒ
		request.setAttribute("newProductlist", newProductlist);
		//�ŵ�������
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
