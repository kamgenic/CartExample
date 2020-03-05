package com.example.cart;

import com.example.user.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class CartResource {

	private final Jedis jedis;

	public CartResource() {
		jedis = new Jedis("52.91.75.7", 10001);
		jedis.auth("mypass");
	}

	@GetMapping("/carts")
	public List<Cart> retrieveAllcarts() {
		return jedis.keys("cart:*")
				.stream()
				.map(cart_key -> retrieveCart(getId(cart_key).toString()))
				.collect(Collectors.toList());
	}

	@GetMapping("/carts/{id}")
	public Cart retrieveCart(@PathVariable String id) {
		Map<String, String> items = jedis.hgetAll("items:" + id);

		Map<String, String> cart = jedis.hgetAll("cart:" + id);
		String user_key = cart.get("user");
		long user_id  = Long.valueOf(user_key.split(":")[1]);

		User user = new User(user_id, jedis.get(user_key));

		if (items.isEmpty())
			throw new CartNotFoundException("id-" + id);

		return new Cart(Long.valueOf(id), user, items);
	}

	@DeleteMapping("/carts/{id}")
	public void deletecart(@PathVariable String id) {
		jedis.del("cart:" + id);
	}

	@PostMapping("/carts")
	public ResponseEntity<Object> createCart(@RequestBody Cart cart) {
		Long counter = jedis.incr("cart-counter");
		cart.setId(counter);

		jedis.hmset("items:"+counter, cart.getItems());

		Map<String, String> cartMap  = new HashMap<>();
		cartMap.put("user", "user:" + cart.getUser().getId());
		cartMap.put("items", "items:"+counter);

		jedis.hmset("cart:"+counter, cartMap);
		return ResponseEntity.ok(cart);
	}

	@PutMapping("/carts/{id}")
	public ResponseEntity<Object> updateCart(@PathVariable String id, @RequestBody Cart new_cart) {
		jedis.del("items:" + id);
		jedis.hmset("items:" + id, new_cart.getItems());

		return ResponseEntity.ok(new_cart);
	}

	private String getKey(Cart id) {
		return "cart:" + id.getId();
	}

	private Long getId(String k) {
		return Long.valueOf(k.split(":")[1]);
	}

	private void addUserToList(Map items, User user) {
		items.put("user", "user:" + user.getId());
	}
}
