package com.example.cart;



import com.example.user.User;

import java.util.Map;

public class Cart {
	private Long id;

	private User user;

	private Map<String, String> items;
	
	public Cart() {
		super();
	}

	public Cart(Long id, User user) {
		super();
		this.id = id;
		this.user = user;
		this.items = items;
	}

	public Cart(Long id, User user, Map<String, String> items) {
		super();
		this.id = id;
		this.user = user;
		this.items = items;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Map<String, String> getItems() {
		return items;
	}

	public void setItems(Map<String, String> items) {
		this.items = items;
	}
}
