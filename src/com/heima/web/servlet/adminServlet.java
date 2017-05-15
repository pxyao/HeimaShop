package com.heima.web.servlet;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.google.gson.Gson;
import com.heima.domain.Category;
import com.heima.domain.Order;
import com.heima.domain.OrderItem;
import com.heima.domain.Product;
import com.heima.service.AdminService;

/**
 * Servlet implementation class adminServlet
 */
@WebServlet("/adminservlet")
public class adminServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;
       
	public void findcategoryList(HttpServletRequest request, HttpServletResponse response) throws IOException{
		AdminService service = new AdminService();
		//��ѯ�������ݷŵ�categorieList��
		List<Category> categorieList = service.findAllcategory();
		
		Gson gson = new Gson();

		String json = gson.toJson(categorieList);
		
		response.setContentType("text/html;charset=UTF-8");
		
		response.getWriter().write(json);
	}
	
	public void updatecategoryList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		//��Ϊ����categoryҪcid��cname ���� Ҫ��cid��cname������һ��ҳ��ȥ
		String cid = request.getParameter("cid");
		//��ѯcanme
		AdminService service = new AdminService();
		
		Category category = service.findcameByCid(cid);

		request.setAttribute("category", category);
		//ת�����༭ҳ��
		//response.sendRedirect(request.getContextPath()+"/admin/category/edit.jsp");
		request.getRequestDispatcher("/admin/category/edit.jsp").forward(request, response);
	}
	
	public void updateCategoryName(HttpServletRequest request, HttpServletResponse response) throws IOException{
		//��ȡcid
		String cid = request.getParameter("cid");
		
		String cname = request.getParameter("cname");
		
		AdminService service = new AdminService();
		//ִ��sql���������ݿ�
		service.updateCategoryName(cid,cname);
	}
	
	public void addcategory(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		
		AdminService service = new AdminService();
		
		List<Category> categoryList = service.findAllcategory();
		if(categoryList!=null){
			//��ȡ����λ��
			int pos = 0;
			pos = categoryList.size()+1;
			
			//��ȡ������
			String cname = request.getParameter("cname");
			if (cname!=null && pos!=0) { //������ȡ�ɹ���ִ����ӷ���
				service.addcategory(pos,cname);
			}
		}
		request.getRequestDispatcher("admin/category/add.jsp").forward(request, response);
	} 
	
	public void findAllProduct(HttpServletRequest request, HttpServletResponse response) throws IOException{
		
		AdminService service = new AdminService();
		
		List<Product> productList = service.findAllProduct();
		
		Gson gson = new Gson();
		
		String json = gson.toJson(productList);
		
		response.setContentType("text/html;charset=UTF-8");
		
		response.getWriter().write(json);
	}
	
	/**
	 * ��ת��������Ʒ��Ϣҳ��
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void updateProductInfo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		
		String pid = request.getParameter("pid");
		
		request.setAttribute("pid", pid);
		
		AdminService service = new AdminService();
		
		Product  product = service.findProductByPid(pid);
		
		request.setAttribute("product", product);
		
		request.getRequestDispatcher("admin/product/edit.jsp").forward(request, response);
	}
	public void findAllOrders(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		AdminService service = new AdminService();
		
		List<Order> orderList = service.findAllOrders();
		
		request.setAttribute("orderList", orderList);
		
		request.getRequestDispatcher("admin/order/list.jsp").forward(request, response);
	}
	
	public void findOrderInfoByOid(HttpServletRequest request, HttpServletResponse response) throws IOException{
		AdminService service = new AdminService();
		
		String oid = request.getParameter("oid");
		
		List<Map<String, Object>> orderInfoList = service.findOrderInfoByOid(oid);
		
		Gson gson = new Gson();
		
		String json = gson.toJson(orderInfoList);
		
		response.setContentType("text/css;charset=UTF-8");
		
		response.getWriter().write(json);
	}
	
}
