package com.heima.web.servlet;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.sql.Date;
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

import org.apache.commons.beanutils.BeanUtils;

import com.google.gson.Gson;
import com.heima.domain.Cart;
import com.heima.domain.Cartitem;
import com.heima.domain.Category;
import com.heima.domain.Order;
import com.heima.domain.OrderItem;
import com.heima.domain.PageBean;
import com.heima.domain.Product;
import com.heima.domain.User;
import com.heima.service.ProductService;
import com.heima.utils.CommonsUtils;

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
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// String methodName = request.getParameter("method");
		// if ("index".equals(methodName)) {
		// index(request, response);
		// } else if ("productList".equals(methodName)) {
		// productList(request, response);
		// } else if ("categoryList".equals(methodName)) {
		// categoryList(request, response);
		// } else if ("productInfo".equals(methodName)) {
		// productInfo(request, response);
		// }

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	public void myOrders(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
		
		//校验用户是否登录
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");

		ProductService service = new ProductService();
		//查询该用户的所有订单信息
		//单表查询 orders表
		//集合中的每一个orders对象的数据是不完整的  缺少List<OrderItem> orderItems数据
		List<Order> orderList = service.findAllOrders(user.getUid());
		if (orderList!=null) {
			for(Order order : orderList){
				//获得每一个订单的oid
				String oid = order.getOid();
				//查询该订单的所有订单项
				List<Map<String, Object>> orderMapList = service.findallOrderItemsByOid(oid);
				//将maplist转化成orderitem
				for(Map<String, Object> map : orderMapList){
					//从map中取出count subtotal 封装到orderitem中
					OrderItem orderItem = new OrderItem();
					//orderItem.setCount(Integer.parseInt(map.get("count").toString()));
					try {
						
						BeanUtils.populate(orderItem, map);
						//从map中取出pimage pname shop_price 封装到product
						Product product = new Product();
						BeanUtils.populate(product, map);
						
						//将product 封装到item中
						orderItem.setProduct(product);
						
						//将orderitem 封装到order中的orderitemlist
						order.getOrderItems().add(orderItem);
						
					} catch (IllegalAccessException | InvocationTargetException e) {
						e.printStackTrace();
					}
					
				}
			
			}
		}
		
		
		//orderList已经封装完整了
		//存到域中 
		request.setAttribute("orderList", orderList);
		
		request.getRequestDispatcher("/order_list.jsp").forward(request, response);
		
	}
	
	/**
	 * 确认订单
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	public void confirmOrder(HttpServletRequest request, HttpServletResponse response) throws IOException{
		//1.获取表单的数据
		Map<String, String[]> properties = request.getParameterMap();
		Order order = new Order();
		try {
			BeanUtils.populate(order, properties);
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
		
		ProductService service = new ProductService();
		
		service.updateOrderAddr(order);
		
		
		//2.在线支付
		//...
		
		request.getSession().removeAttribute("cart");
		request.getSession().removeAttribute("order");
		response.sendRedirect(request.getContextPath()+"/");
	}

	/**
	 * 提交订单
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException 
	 */
	public void submitOrder(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

		HttpSession session = request.getSession();
		
		User user = (User) session.getAttribute("user");
		if (user==null) {
			request.getRequestDispatcher("/login.jsp").forward(request, response);
			return;
		}
		// 封装一个order对象 传递给service
		Order order = new Order();

		// 要封装的数据

		// 1.String oid 该订单的订单号
		String oid = CommonsUtils.getUuid();
		order.setOid(oid);

		// 2.Date ordertime 下单时间
		order.setOrdertime(new Date(System.currentTimeMillis()));
		
		// 3.private double total 订单总金额
		// 获得购物车
		Cart cart = (Cart) session.getAttribute("cart");
		double total = cart.getTotal();
		order.setTotal(total);

		// 4.private int state 支付状态
		order.setState(0);

		// 5.private String address 收货地址
		order.setAddress(null);

		// 6.private String name 收货人
		order.setName(null);
		// 7.private String telephone 收货电话
		order.setTelephone(null);
		// 8.private User user; 该订单属于哪个用户
		order.setUser(user);
		// 9.该订单有多少订单项 List<OrderItem>
		// 获得购物车的购物项的集合map
		Map<String, Cartitem> cartitems = cart.getCartitems();
		for (Map.Entry<String, Cartitem> entry : cartitems.entrySet()) {
			// 取出每一个购物项
			Cartitem Cartitem = entry.getValue();
			// 创建每一个订单项
			OrderItem Orderitem = new OrderItem();
			// 1.private String itemid 订单项id
			Orderitem.setItemid(CommonsUtils.getUuid());
			// 2.private int count 订单项商品购买数量
			Orderitem.setCount(Cartitem.getBuy_num());
			// 3.private double subtotal 订单项的小计
			Orderitem.setSubtotal(Cartitem.getSubtotal());
			// 4.private Product pid 订单项内部的商品
			Orderitem.setProduct(Cartitem.getProduct());
			// 5.private Order oid 该订单项属于哪个订单
			Orderitem.setOrder(order);
			// 该订单项 添加到订单项的集合中
			order.getOrderItems().add(Orderitem);
		}
		// order 订单对象封装完毕
		// 传递数据到service层
		ProductService service = new ProductService();
		service.submitOrder(order);

		session.setAttribute("order", order);

		// 页面跳转
		response.sendRedirect(request.getContextPath() + "/order_info.jsp");

	}

	public void ClearCart(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// 获得Session
		HttpSession session = request.getSession();
		// 把cart从session中移除
		session.removeAttribute("cart");
		// 重定向
		response.sendRedirect(request.getContextPath() + "/cart.jsp");
	}

	public void delProductFromCart(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// 获得要删除的item 的 pid
		String pid = request.getParameter("pid");
		// 删除购物车中的购物项 集合中的item
		HttpSession session = request.getSession();
		Cart cart = (Cart) session.getAttribute("cart");
		if (cart != null) {
			Map<String, Cartitem> cartitems = cart.getCartitems();

			// 重新计算total
			cart.setTotal(cart.getTotal() - cartitems.get(pid).getSubtotal());

			cartitems.remove(pid);
		}

		// 跳转到cart.jsp
		response.sendRedirect(request.getContextPath() + "/cart.jsp");
	}

	public void addproductToCart(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 获得用于存储cart的session
		HttpSession session = request.getSession();

		ProductService service = new ProductService();
		// 获得pid
		String pid = request.getParameter("pid");
		// 获得buyNum;
		int buyNum = Integer.parseInt(request.getParameter("buyNum"));
		// 获得product对象
		Product product = service.findProductByPid(pid);
		// 计算小计
		double subtotal = buyNum * product.getShop_price();
		// 封装cartitem 数据项
		Cartitem cartitem = new Cartitem();
		cartitem.setBuy_num(buyNum);
		cartitem.setProduct(product);
		cartitem.setSubtotal(subtotal);

		// 获得购物车
		Cart cart = (Cart) session.getAttribute("cart");

		if (cart == null) {
			cart = new Cart();
		}

		// 判断之前是否已经包含此购物项 判断key是否存在
		// 如果购物车已经存在该商品-------将现在的数量与原有的数量进行相加操作
		Map<String, Cartitem> cartitems = cart.getCartitems();
		if (cartitems.containsKey(pid)) {
			// 包含则 取出原有的商品的数量并加起来
			int newBuyNum = cartitems.get(pid).getBuy_num() + buyNum;
			cartitem.setBuy_num(newBuyNum);
			// 小计也要重新计算
			cartitem.setSubtotal(newBuyNum * cartitem.getProduct().getShop_price());
		} else {
			// 不包含将购物项放到车中 --key是pid
			cart.getCartitems().put(product.getPid(), cartitem);
		}

		// 如果没有该商品
		// 封装cart的total
		double total = cart.getTotal() + subtotal;
		cart.setTotal(total);

		// 将车再此放到session
		session.setAttribute("cart", cart);

		// 转发 如果转发 用户刷新一次浏览器 相当于又访问了一次该servlet 总计
		// 又会加一次 所以这里用 重定向比较好
		// request.getRequestDispatcher("/cart.jsp").forward(request, response);
		response.sendRedirect(request.getContextPath() + "/cart.jsp");
	}

	public void index(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
