package com.example.cart;

public class CartNotFoundException extends RuntimeException {

	public CartNotFoundException(String exception) {
		super(exception);
	}

}
