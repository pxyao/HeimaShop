package com.heima.web.servlet;

import java.awt.Button;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.heima.domain.Cart;
import com.heima.domain.Cartitem;
import com.heima.domain.Category;
import com.heima.domain.PageBean;
import com.heima.domain.Product;
import com.heima.service.ProductService;

/**
 * Servlet implementation class productServlet
 */
@WebServlet("/product")
public class productServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public productServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
//		String methodName = request.getParameter("method");
//		if ("index".equals(methodName)) {
//			index(request, response);
//		} else if ("productList".equals(methodName)) {
//			productList(request, response);
//		} else if ("categoryList".equals(methodName)) {
//			categoryList(request, response);
//		} else if ("productInfo".equals(methodName)) {
//			productInfo(request, response);
//		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
	
	public void delProductFromCart(HttpServletRequest request, HttpServletResponse response) throws IOException{
		//获得要删除的item 的 pid
		String pid = request.getParameter("pid");
		//删除购物车中的购物项  集合中的item
		HttpSession session = request.getSession();
		Cart cart = (Cart)session.getAttribute("cart");
		if (cart!=null) {
			Map<String, Cartitem> cartitems = cart.getCartitems();
			
			//重新计算total
			cart.setTotal(cart.getTotal()-cartitems.get(pid).getSubtotal());
			
			cartitems.remove(pid);
		}
		

		//跳转到cart.jsp
		response.sendRedirect(request.getContextPath()+"/cart.jsp");
	}

	public void addproductToCart(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		//获得用于存储cart的session
		HttpSession session = request.getSession();
		
		ProductService service = new ProductService();
		
		//获得pid
		String pid = request.getParameter("pid");
		//获得buyNum;
		int buyNum = Integer.parseInt(request.getParameter("buyNum"));
		//获得product对象
		Product product = service.findProductByPid(pid);
		//计算小计
		double subtotal = buyNum * product.getShop_price();
		//封装cartitem 数据项
		Cartitem cartitem = new Cartitem();
		cartitem.setBuy_num(buyNum);
		cartitem.setProduct(product);
		cartitem.setSubtotal(subtotal);
		
		//获得购物车
		Cart cart = (Cart) session.getAttribute("cart");
		
		if (cart==null) {
			cart = new Cart();
		}
		
		//判断之前是否已经包含此购物项  判断key是否存在
		//如果购物车已经存在该商品-------将现在的数量与原有的数量进行相加操作
		Map<String, Cartitem> cartitems = cart.getCartitems();
		if (cartitems.containsKey(pid)) {
			//包含则  取出原有的商品的数量并加起来
			int newBuyNum = cartitems.get(pid).getBuy_num()+buyNum;
			cartitem.setBuy_num(newBuyNum);
			//小计也要重新计算
			cartitem.setSubtotal(newBuyNum*cartitem.getProduct().getShop_price());
		}else{
			//不包含将购物项放到车中 --key是pid
			cart.getCartitems().put(product.getPid(), cartitem);
		}
		
		//如果没有该商品 
		//封装cart的total
		double total = cart.getTotal() + subtotal;
		cart.setTotal(total);
		
		//将车再此放到session 
		session.setAttribute("cart", cart);
		
		//转发 如果转发 用户刷新一次浏览器 相当于又访问了一次该servlet 总计
		//又会加一次 所以这里用  重定向比较好
		//request.getRequestDispatcher("/cart.jsp").forward(request, response);
		response.sendRedirect(request.getContextPath()+"/cart.jsp");
	}
	
	public void index(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ProductService service = new ProductService();

		// 准备热门商品 --List<product>
		List<Product> hotProductlist = service.findHotProductList();
		// 准备最新商品
		List<Product> newProductlist = service.findNewProductList();
		// 准备分类
		List<Category> categorylist = service.findAllCategory();

		// 放入查找到的热门商品
		request.setAttribute("hotProductlist", hotProductlist);
		// 放入查找到的最新商品
		request.setAttribute("newProductlist", newProductlist);
		// 放到分类中
		// List<Category> categoryList = service.findAllCategory();

		// request.setAttribute("categorylist", categorylist);
		request.getRequestDispatcher("index.jsp").forward(request, response);
	}

	public void productInfo(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 获取pid
		String pid = request.getParameter("pid");
		// 获取cid
		String cid = request.getParameter("cid");
		// 获取当前页
		String currentPage = request.getParameter("currentpage");
		ProductService productService = new ProductService();
		// 根据pid查询商品详细信息
		Product product = productService.findProductByPid(pid);
		request.setAttribute("product", product);

		request.setAttribute("cid", cid);

		request.setAttribute("currentPage", currentPage);

		// 获得客户端携带的cookie 获得名字是pids
		String pids = pid;

		Cookie[] cookies = request.getCookies();

		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if ("pids".equals(cookie.getName())) {
					pids = cookie.getValue();
					// 1-3-2 本次访问的商品pid是8 8-1-3-2
					// 1-3-2本次访问的商品pid是3 3-1-2
					// 1-3-2本次访问的商品pid是2 2-3-1
					// pids拆成一个数组
					String[] split = pids.split("-"); // 3,1,2
					List<String> aslist = Arrays.asList(split); // [3,1,2]
					LinkedList<String> list = new LinkedList<>(aslist);
					// 判断这个集合中是否存在当前的pid
					if (list.contains(pid)) {
						// 当前包含查看商品的pid
						// 如果pid在 list中 直接删除后放到第一个
						list.remove(pid);
						list.addFirst(pid);
					} else {
						// 不包含当前查看商品里的pid 直接将pid放到头上
						list.addFirst(pid);
					}

					// 将 3 1 2转成 3-1-2字符串
					StringBuffer sb = new StringBuffer();
					for (int i = 0; i < list.size() && i < 7; i++) {
						sb.append(list.get(i));
						sb.append("-"); // 3-1-2
					}
					// 去掉3-1-2 后的
					pids = sb.substring(0, sb.length() - 1);
				}
			}
		}
		Cookie cookie_pids = new Cookie("pids", pids);
		response.addCookie(cookie_pids);

		// 转发到product_info.jsp
		request.getRequestDispatcher("/product_info.jsp").forward(request, response);
	}

	public void categoryList(HttpServletRequest request, HttpServletResponse response) throws IOException {
		ProductService service = new ProductService();
		// 用redis
		// //先从缓存找那个查询categoryList 如果有则直接使用 没有再从数据库中查询到缓存中
		// //1. 获得jedis 连接redis 数据库
		// Jedis jedis = JedisPoolUtils.getJedis();
		// String categoryListJson = jedis.get("categoryListJson");
		// //2. 判断categoryListJson是否为空
		// if (categoryListJson==null) {
		// System.out.println("redis数据库中没有 categoryListJson 查询数据库");
		// List<Category> categoryList = service.findAllCategory();
		// Gson gson = new Gson();
		// categoryListJson = gson.toJson(categoryList);
		// jedis.set("categoryListJson", categoryListJson);
		// }
		// 不用redis

		List<Category> categoryList = service.findAllCategory();

		Gson gson = new Gson();

		String json = gson.toJson(categoryList);

		response.setContentType("text/html;charset=UTF-8");

		response.getWriter().write(json);
	}

	public void productList(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 获得cid
		String cid = request.getParameter("cid");
		// 获得当前页数
		String currentPageStr = request.getParameter("currentPage");
		// 一页显示12个商品
		int currentCount = 12;
		// 点击导航栏的时候没有传参数 没有currentPageStr 故要判断一下是否为空
		if (currentPageStr == null) {
			currentPageStr = "1";
		}

		int currentPage = Integer.parseInt(currentPageStr);

		ProductService service = new ProductService();

		PageBean pageBean = service.findProductListByCid(cid, currentPage, currentCount);

		request.setAttribute("cid", cid);

		request.setAttribute("pageBean", pageBean);

		// 获取cookies
		Cookie[] cookies = request.getCookies();

		List<Product> historyProductList = new ArrayList<Product>();

		// 取出每一个cookie中存储的id 分别进行查询 返回的product对象添加到historyproductList中
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if ("pids".equals(cookie.getName())) {
					String pids = cookie.getValue();
					String[] split = pids.split("-");
					for (String pid : split) {
						Product product = service.findProductByPid(pid);
						historyProductList.add(product);
					}
				}
			}
		}
		// 将里是浏览记录放置到 域中
		request.setAttribute("historyProductList", historyProductList);

		request.getRequestDispatcher("/product_list.jsp").forward(request, response);
	}
}
