package com.heima.domain;

import java.util.HashMap;
import java.util.Map;

public class Cart {
	private Map<String, Cartitem> cartitems = new HashMap<String,Cartitem>();
	
	//商品的总价
	private double total;

	//商品的单价
	private double subtatal;

	public Map<String, Cartitem> getCartitems() {
		return cartitems;
	}

	public void setCartitems(Map<String, Cartitem> cartitems) {
		this.cartitems = cartitems;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public double getSubtatal() {
		return subtatal;
	}

	public void setSubtatal(double subtatal) {
		this.subtatal = subtatal;
	}
}
