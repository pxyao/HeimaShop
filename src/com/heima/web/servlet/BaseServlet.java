package com.heima.web.servlet;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class BaseServlet
 */
// @WebServlet("/BaseServlet") 不用直接访问的
public class BaseServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		try {
			String methodName = request.getParameter("method");

			// 获得当前被访问的对象的字节码对象
			Class clazz = this.getClass();// ProductServlet.class--
											// UserServlet.class

			// 获得当前字节码对象中的指定的方法
			Method method = clazz.getMethod(methodName, HttpServletRequest.class, HttpServletResponse.class);

			method.invoke(this, request, response);

		} catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
		// 执行相应的功能方法
		catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}

	}

}
