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
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;

import com.heima.domain.User;
import com.heima.service.UserService;
import com.heima.utils.CommonsUtils;
import com.heima.utils.MailUtils;

/**
 * Servlet implementation class registerServlet
 */
@WebServlet("/register")
public class registerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public registerServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("UTF-8");
		
		
		//获得表单数据
		Map<String, String[]> properties = request.getParameterMap();
		User user=new User();
			try {
				
				//自己指定一个类型转换器 将String转换成Date
				ConvertUtils.register(new Converter() {
					
					@Override
					public Object convert(Class arg0, Object value) {
						SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
						Date parse=null;
						try {
							parse = format.parse(value.toString());
						} catch (ParseException e) {
							e.printStackTrace();
						}
						return null;
					}
				}, Date.class);
				
				
				BeanUtils.populate(user, properties);
			} catch (IllegalAccessException | InvocationTargetException e) {
				e.printStackTrace();
			}
	
		
		//封装数据
			
		//uid
		user.setUid(CommonsUtils.getUuid());
		//telephone
		user.setTelephone(null);
		//state
		user.setState(0);
		//code
		String activecode=CommonsUtils.getUuid();
		user.setCode(activecode);
		
		
		//将user传递给serice层
		UserService service = new UserService();
		boolean isRegisterSuccesss=service.regist(user);
		
		//是否注册成功
		if(isRegisterSuccesss){
			//发送注册邮件
			String emailmsg="恭喜您注册成功,点击下面的连接激活<a href='http://localhost:8080/HeimaShop/"
					+ "active?activecode="+activecode+"'>"+"'http://localhost:8080/HeimaShop/active"
							+ "?activecode="+activecode+""+"</a>";
			try {
				MailUtils.sendMail(user.getEmail(), emailmsg);
			} catch (MessagingException e) {
				e.printStackTrace();
			}
			
			
			
			
			//成功 跳转到成功页面
			response.sendRedirect(request.getContextPath()+"/registerSuccess.jsp");
		}else{
			//失败 跳转到失败的提示页面
			response.sendRedirect(request.getContextPath()+"/registerFail.jsp");
		}
	
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
