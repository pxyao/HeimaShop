package com.heima.web.servlet;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;

import com.heima.domain.User;
import com.heima.service.UserService;
import com.heima.utils.CommonsUtils;
import com.heima.utils.MailUtils;

/**
 * Servlet implementation class UserServlet
 */
@WebServlet("/User")
public class UserServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;

	public UserServlet() {
		super();
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		super.doPost(req, resp);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		super.doGet(req, resp);
	}
	
	public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException{
		HttpSession session = request.getSession();
		session.removeAttribute("user");
		
		response.sendRedirect(request.getContextPath()+"/login.jsp");
		
	}

	public void login(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		HttpSession session = request.getSession();
		//获取用户名和密码
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		
		UserService service = new UserService();
		User user = service.login(username, password);
		if (user!=null) {
			session.setAttribute("user", user);
			
			//判断是否勾选自动登录
			if ("true".equals(request.getParameter("autologin"))) {
				Cookie cookie_username = new Cookie("username", user.getUsername());
				cookie_username.setMaxAge(10*60);
				Cookie cookie_password = new Cookie("password", user.getPassword());
				cookie_username.setMaxAge(10*60);
				//添加cookie
				response.addCookie(cookie_username);
				response.addCookie(cookie_password);
			}
			//将用户信息保存到 session中
			session.setAttribute("user", user);
			//重定向到首页
			response.sendRedirect(request.getContextPath()+"/index.jsp");
		}else{
			//用户名或密码错误
			request.setAttribute("loginError", "用户名或密码错误");
			request.getRequestDispatcher("/login.jsp").forward(request, response);
		}
		
	}

	public void register(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// 获得表单数据
		Map<String, String[]> properties = request.getParameterMap();
		User user = new User();
		try {

			// 自己指定一个类型转换器 将String转换成Date
			ConvertUtils.register(new Converter() {
				@Override
				public Object convert(Class clazz, Object value) {
					// 将string转成date
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
					Date parse = null;
					try {
						parse = format.parse(value.toString());
					} catch (ParseException e) {
						e.printStackTrace();
					}
					return parse;
				}
			}, Date.class);

			BeanUtils.populate(user, properties);
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}

		// 封装数据

		// uid
		user.setUid(CommonsUtils.getUuid());
		// telephone
		user.setTelephone(null);
		// state
		user.setState(0);
		// code
		String activecode = CommonsUtils.getUuid();
		user.setCode(activecode);

		// 将user传递给serice层
		UserService service = new UserService();
		boolean isRegisterSuccesss = service.regist(user);

		// 是否注册成功
		if (isRegisterSuccesss) {
			// 发送注册邮件
			String emailmsg = "恭喜您注册成功,点击下面的连接激活<a href='http://localhost:8080/HeimaShop/" + "active?activecode="
					+ activecode + "'>" + "'http://localhost:8080/HeimaShop/active" + "?activecode=" + activecode + ""
					+ "</a>";
			try {
				MailUtils.sendMail(user.getEmail(), emailmsg);
			} catch (MessagingException e) {
				e.printStackTrace();
			}

			// 成功 跳转到成功页面
			response.sendRedirect(request.getContextPath() + "/registerSuccess.jsp");
		} else {
			// 失败 跳转到失败的提示页面
			response.sendRedirect(request.getContextPath() + "/registerFail.jsp");
		}
	}

	public void checkUsername(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// 获取用户名
		String username = request.getParameter("username");

		UserService service = new UserService();

		boolean isExist = service.checkUsername(username);

		String json = "{\"isExist\":" + isExist + "}";

		response.getWriter().write(json);
	}
}
