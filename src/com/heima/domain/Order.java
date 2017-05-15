package com.heima.domain;

import java.util.ArrayList;
import java.sql.Date;
import java.util.List;

public class Order {
	/*`oid` varchar(32) NOT NULL,
	  `ordertime` datetime DEFAULT NULL,
	  `total` double DEFAULT NULL,
	  `state` int(11) DEFAULT NULL,
	  `address` varchar(30) DEFAULT NULL,
	  `name` varchar(20) DEFAULT NULL,
	  `telephone` varchar(20) DEFAULT NULL,
	  `uid` varchar(32) DEFAULT NULL,*/
	private String oid; //�ö����Ķ�����
	private Date ordertime;//�µ�ʱ��
	private double total; //�����ܽ��
	private int state; //֧��״̬
	private String address;//�ջ���ַ
	private String name; //�ջ���
	private String telephone;//�ջ��绰
	private User user;//�ö��������ĸ��û�
	
	//��װ����ʱ  ��Ҫ��һ��order ���û��orderitems ��ô�޷���װorderitem�е�����
	List<OrderItem> orderItems = new ArrayList<OrderItem>();
	
	public String getOid() {
		return oid;
	}
	public void setOid(String oid) {
		this.oid = oid;
	}
	public Date getOrdertime() {
		return ordertime;
	}
	public void setOrdertime(Date ordertime) {
		this.ordertime = ordertime;
	}
	public double getTotal() {
		return total;
	}
	public void setTotal(double total) {
		this.total = total;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public List<OrderItem> getOrderItems() {
		return orderItems;
	}
	public void setOrderItems(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}
	
}
