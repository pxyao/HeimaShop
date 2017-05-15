package com.heima.web.servlet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.apache.tomcat.util.net.SSLSupport.CipherData;

import com.heima.domain.Category;
import com.heima.domain.Product;
import com.heima.service.AdminService;
import com.heima.service.ProductService;

/**
 * Servlet implementation class AdminUpdateServlet
 */
@WebServlet("/AdminUpdate")
public class AdminUpdateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public AdminUpdateServlet() {
		super();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Product product = new Product();
		try {
			String temp_path = this.getServletContext().getRealPath("temp");
			DiskFileItemFactory factory = new DiskFileItemFactory(1024 * 1024, new File(temp_path));

			ServletFileUpload upload = new ServletFileUpload(factory);

			upload.setHeaderEncoding("UTF-8");
			List<FileItem> fileItems = upload.parseRequest(request);
			
			if (fileItems!=null) {
				Map<String, Object> map = new HashMap<String,Object>();
				for(FileItem fileItem : fileItems){
					if(fileItem.isFormField()){//如果是普通文件项
						String fieldName = fileItem.getFieldName();
						String fieldValue = fileItem.getString("UTF-8");
						map.put(fieldName, fieldValue);
					}else {
						//文件上传项
						InputStream in = fileItem.getInputStream();
						String file_path = this.getServletContext().getRealPath("upload");
						String filename = fileItem.getName();
						OutputStream out = new FileOutputStream(file_path+"/"+filename);
						IOUtils.copy(in, out);
						in.close();
						out.close();
						map.put("pimage", "upload/"+filename);
						fileItem.delete();
					}
				}
				//封装数据到product对象中
				BeanUtils.populate(product, map);
				
				//添加时间
				product.setPdate(new Date(System.currentTimeMillis()));
				//分类
				Category category = new Category();
				category.setCid((String)map.get("cid"));
				product.setCategory(category);
				//热门
				String pflag = (String) map.get("is_hot");
				product.setPflag(Integer.parseInt(pflag));

				AdminService service = new AdminService();
				
				String pid = (String) map.get("pid");
				
				service.updateProductInfo(pid, product);
			}
			
		} catch (FileUploadException | IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
