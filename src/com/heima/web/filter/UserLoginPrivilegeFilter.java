package com.heima.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.heima.domain.User;

public class UserLoginPrivilegeFilter implements Filter{

	/**
	 * 校验用户是否登录
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		HttpServletRequest req = (HttpServletRequest)request;
		HttpServletResponse resp = (HttpServletResponse)response;
		
		HttpSession session = req.getSession();
		User user = (User) session.getAttribute("user");
		//校验Session是否存在
		if (user == null) {
			// 返回登录
			resp.sendRedirect(req.getContextPath() + "/login.jsp");
			return;
		}
		
		chain.doFilter(req, resp);
	}
	
	@Override
	public void destroy() {
		
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		
	}

}
