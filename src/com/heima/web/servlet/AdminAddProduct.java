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

import com.heima.domain.Category;
import com.heima.domain.Product;
import com.heima.service.ProductService;
import com.heima.utils.CommonsUtils;

/**
 * Servlet implementation class AdminAddProduct
 */
@WebServlet("/AdminAddProduct")
public class AdminAddProduct extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// �ռ������ݷ�װһ��productʵ�� ���ϴ�ͼƬ�浽������
		Product product = new Product();
		//�ռ����ݵ�����
		Map<String, Object> map = new HashMap<String,Object>();
		
		try {
			String temp_path = this.getServletContext().getRealPath("temp");
			// ���������ļ�����
			DiskFileItemFactory factory = new DiskFileItemFactory(1024 * 1024, new File(temp_path));
			// �����ļ��ϴ�������
			ServletFileUpload upload = new ServletFileUpload(factory);
			// ���ñ���
			upload.setHeaderEncoding("UTF-8");
			boolean isMultipartContent = upload.isMultipartContent(request);
			// �ж��Ƿ���Multipart��
			if (isMultipartContent) {
				List<FileItem> fileItems = upload.parseRequest(request);
				for(FileItem fileItem : fileItems){
					if (fileItem.isFormField()) {
						String fieldName = fileItem.getFieldName();
						String fieldValue = fileItem.getString("UTF-8");
						map.put(fieldName, fieldValue);
						
					}else{
						//�ļ��ϴ���
						//����ļ�·��
						String file_path = this.getServletContext().getRealPath("upload");
						String filename = fileItem.getName();
						InputStream in = fileItem.getInputStream();
						OutputStream out = new FileOutputStream(file_path+"/"+filename);
						IOUtils.copy(in, out);
						out.close();
						in.close();
						fileItem.delete();
						
						// private String pimage;
						map.put("pimage", "upload/"+filename);
					}
				}
				BeanUtils.populate(product, map);
				//�Ƿ����ݷ�װ��ȫ
				/*
				 * private String pid;
				 * private Date pdate;
				 * private Category category;
				 * private pflag
				 * */
				//����pid
				product.setPid(CommonsUtils.getUuid());
				//���ʱ��
				product.setPdate(new Date(System.currentTimeMillis()));
				//����
				Category category = new Category();
				category.setCid((String)map.get("cid"));
				product.setCategory(category);
				//����
				String pflag = (String) map.get("is_hot");
				product.setPflag(Integer.parseInt(pflag));
				ProductService service = new ProductService();
				service.AddProduct(product);
			}

		} catch (FileUploadException | IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
