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
		//查询到的数据放到categorieList中
		List<Category> categorieList = service.findAllcategory();
		
		Gson gson = new Gson();

		String json = gson.toJson(categorieList);
		
		response.setContentType("text/html;charset=UTF-8");
		
		response.getWriter().write(json);
	}
	
	public void updatecategoryList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		//因为更新category要cid和cname 所以 要把cid和cname传到下一个页面去
		String cid = request.getParameter("cid");
		//查询canme
		AdminService service = new AdminService();
		
		Category category = service.findcameByCid(cid);

		request.setAttribute("category", category);
		//转发到编辑页面
		//response.sendRedirect(request.getContextPath()+"/admin/category/edit.jsp");
		request.getRequestDispatcher("/admin/category/edit.jsp").forward(request, response);
	}
	
	public void updateCategoryName(HttpServletRequest request, HttpServletResponse response) throws IOException{
		//获取cid
		String cid = request.getParameter("cid");
		
		String cname = request.getParameter("cname");
		
		AdminService service = new AdminService();
		//执行sql语句更新数据库
		service.updateCategoryName(cid,cname);
	}
	
	public void addcategory(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		
		AdminService service = new AdminService();
		
		List<Category> categoryList = service.findAllcategory();
		if(categoryList!=null){
			//获取插入位置
			int pos = 0;
			pos = categoryList.size()+1;
			
			//获取分类名
			String cname = request.getParameter("cname");
			if (cname!=null && pos!=0) { //参数获取成功才执行添加分类
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
	 * 跳转到跟新商品信息页面
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
