package com.heima.web.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.heima.domain.PageBean;
import com.heima.domain.Product;
import com.heima.service.ProductService;

/**
 * Servlet implementation class ProductServletByCidServlet
 */
@WebServlet("/productListByCid")
public class ProductServletByCidServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProductServletByCidServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//���cid
		String cid = request.getParameter("cid");
		//��õ�ǰҳ��
		String currentPageStr = request.getParameter("currentPage");
		//һҳ��ʾ12����Ʒ
		int currentCount = 12;
		//�����������ʱ��û�д����� û��currentPageStr ��Ҫ�ж�һ���Ƿ�Ϊ��
		if (currentPageStr==null) {
			currentPageStr = "1";
		}
		
		int currentPage = Integer.parseInt(currentPageStr);

		ProductService service = new ProductService();
		
		PageBean pageBean = service.findProductListByCid(cid,currentPage,currentCount);
		
		request.setAttribute("pageBean", pageBean);
		
		request.getRequestDispatcher("/product_list.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
