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
		//���Ҫɾ����item �� pid
		String pid = request.getParameter("pid");
		//ɾ�����ﳵ�еĹ�����  �����е�item
		HttpSession session = request.getSession();
		Cart cart = (Cart)session.getAttribute("cart");
		if (cart!=null) {
			Map<String, Cartitem> cartitems = cart.getCartitems();
			
			//���¼���total
			cart.setTotal(cart.getTotal()-cartitems.get(pid).getSubtotal());
			
			cartitems.remove(pid);
		}
		

		//��ת��cart.jsp
		response.sendRedirect(request.getContextPath()+"/cart.jsp");
	}

	public void addproductToCart(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		//������ڴ洢cart��session
		HttpSession session = request.getSession();
		
		ProductService service = new ProductService();
		
		//���pid
		String pid = request.getParameter("pid");
		//���buyNum;
		int buyNum = Integer.parseInt(request.getParameter("buyNum"));
		//���product����
		Product product = service.findProductByPid(pid);
		//����С��
		double subtotal = buyNum * product.getShop_price();
		//��װcartitem ������
		Cartitem cartitem = new Cartitem();
		cartitem.setBuy_num(buyNum);
		cartitem.setProduct(product);
		cartitem.setSubtotal(subtotal);
		
		//��ù��ﳵ
		Cart cart = (Cart) session.getAttribute("cart");
		
		if (cart==null) {
			cart = new Cart();
		}
		
		//�ж�֮ǰ�Ƿ��Ѿ������˹�����  �ж�key�Ƿ����
		//������ﳵ�Ѿ����ڸ���Ʒ-------�����ڵ�������ԭ�е�����������Ӳ���
		Map<String, Cartitem> cartitems = cart.getCartitems();
		if (cartitems.containsKey(pid)) {
			//������  ȡ��ԭ�е���Ʒ��������������
			int newBuyNum = cartitems.get(pid).getBuy_num()+buyNum;
			cartitem.setBuy_num(newBuyNum);
			//С��ҲҪ���¼���
			cartitem.setSubtotal(newBuyNum*cartitem.getProduct().getShop_price());
		}else{
			//��������������ŵ����� --key��pid
			cart.getCartitems().put(product.getPid(), cartitem);
		}
		
		//���û�и���Ʒ 
		//��װcart��total
		double total = cart.getTotal() + subtotal;
		cart.setTotal(total);
		
		//�����ٴ˷ŵ�session 
		session.setAttribute("cart", cart);
		
		//ת�� ���ת�� �û�ˢ��һ������� �൱���ַ�����һ�θ�servlet �ܼ�
		//�ֻ��һ�� ����������  �ض���ȽϺ�
		//request.getRequestDispatcher("/cart.jsp").forward(request, response);
		response.sendRedirect(request.getContextPath()+"/cart.jsp");
	}
	
	public void index(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ProductService service = new ProductService();

		// ׼��������Ʒ --List<product>
		List<Product> hotProductlist = service.findHotProductList();
		// ׼��������Ʒ
		List<Product> newProductlist = service.findNewProductList();
		// ׼������
		List<Category> categorylist = service.findAllCategory();

		// ������ҵ���������Ʒ
		request.setAttribute("hotProductlist", hotProductlist);
		// ������ҵ���������Ʒ
		request.setAttribute("newProductlist", newProductlist);
		// �ŵ�������
		// List<Category> categoryList = service.findAllCategory();

		// request.setAttribute("categorylist", categorylist);
		request.getRequestDispatcher("index.jsp").forward(request, response);
	}

	public void productInfo(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// ��ȡpid
		String pid = request.getParameter("pid");
		// ��ȡcid
		String cid = request.getParameter("cid");
		// ��ȡ��ǰҳ
		String currentPage = request.getParameter("currentpage");
		ProductService productService = new ProductService();
		// ����pid��ѯ��Ʒ��ϸ��Ϣ
		Product product = productService.findProductByPid(pid);
		request.setAttribute("product", product);

		request.setAttribute("cid", cid);

		request.setAttribute("currentPage", currentPage);

		// ��ÿͻ���Я����cookie ���������pids
		String pids = pid;

		Cookie[] cookies = request.getCookies();

		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if ("pids".equals(cookie.getName())) {
					pids = cookie.getValue();
					// 1-3-2 ���η��ʵ���Ʒpid��8 8-1-3-2
					// 1-3-2���η��ʵ���Ʒpid��3 3-1-2
					// 1-3-2���η��ʵ���Ʒpid��2 2-3-1
					// pids���һ������
					String[] split = pids.split("-"); // 3,1,2
					List<String> aslist = Arrays.asList(split); // [3,1,2]
					LinkedList<String> list = new LinkedList<>(aslist);
					// �ж�����������Ƿ���ڵ�ǰ��pid
					if (list.contains(pid)) {
						// ��ǰ�����鿴��Ʒ��pid
						// ���pid�� list�� ֱ��ɾ����ŵ���һ��
						list.remove(pid);
						list.addFirst(pid);
					} else {
						// ��������ǰ�鿴��Ʒ���pid ֱ�ӽ�pid�ŵ�ͷ��
						list.addFirst(pid);
					}

					// �� 3 1 2ת�� 3-1-2�ַ���
					StringBuffer sb = new StringBuffer();
					for (int i = 0; i < list.size() && i < 7; i++) {
						sb.append(list.get(i));
						sb.append("-"); // 3-1-2
					}
					// ȥ��3-1-2 ���
					pids = sb.substring(0, sb.length() - 1);
				}
			}
		}
		Cookie cookie_pids = new Cookie("pids", pids);
		response.addCookie(cookie_pids);

		// ת����product_info.jsp
		request.getRequestDispatcher("/product_info.jsp").forward(request, response);
	}

	public void categoryList(HttpServletRequest request, HttpServletResponse response) throws IOException {
		ProductService service = new ProductService();
		// ��redis
		// //�ȴӻ������Ǹ���ѯcategoryList �������ֱ��ʹ�� û���ٴ����ݿ��в�ѯ��������
		// //1. ���jedis ����redis ���ݿ�
		// Jedis jedis = JedisPoolUtils.getJedis();
		// String categoryListJson = jedis.get("categoryListJson");
		// //2. �ж�categoryListJson�Ƿ�Ϊ��
		// if (categoryListJson==null) {
		// System.out.println("redis���ݿ���û�� categoryListJson ��ѯ���ݿ�");
		// List<Category> categoryList = service.findAllCategory();
		// Gson gson = new Gson();
		// categoryListJson = gson.toJson(categoryList);
		// jedis.set("categoryListJson", categoryListJson);
		// }
		// ����redis

		List<Category> categoryList = service.findAllCategory();

		Gson gson = new Gson();

		String json = gson.toJson(categoryList);

		response.setContentType("text/html;charset=UTF-8");

		response.getWriter().write(json);
	}

	public void productList(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// ���cid
		String cid = request.getParameter("cid");
		// ��õ�ǰҳ��
		String currentPageStr = request.getParameter("currentPage");
		// һҳ��ʾ12����Ʒ
		int currentCount = 12;
		// �����������ʱ��û�д����� û��currentPageStr ��Ҫ�ж�һ���Ƿ�Ϊ��
		if (currentPageStr == null) {
			currentPageStr = "1";
		}

		int currentPage = Integer.parseInt(currentPageStr);

		ProductService service = new ProductService();

		PageBean pageBean = service.findProductListByCid(cid, currentPage, currentCount);

		request.setAttribute("cid", cid);

		request.setAttribute("pageBean", pageBean);

		// ��ȡcookies
		Cookie[] cookies = request.getCookies();

		List<Product> historyProductList = new ArrayList<Product>();

		// ȡ��ÿһ��cookie�д洢��id �ֱ���в�ѯ ���ص�product������ӵ�historyproductList��
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
		// �����������¼���õ� ����
		request.setAttribute("historyProductList", historyProductList);

		request.getRequestDispatcher("/product_list.jsp").forward(request, response);
	}
}
