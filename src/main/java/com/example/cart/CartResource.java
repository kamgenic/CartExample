package com.example.cart;

import com.example.user.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;

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
		Map<String, String> items = jedis.hgetAll("cart:" + id);
		String userid = items.remove("user");
		User user = new User(getId(userid), jedis.get(userid));

		if (items.isEmpty())
			throw new CartNotFoundException("id-" + id);

		Cart cart = new Cart(Long.valueOf(id), user, items);

		return cart;
	}

	@DeleteMapping("/carts/{id}")
	public void deletecart(@PathVariable String id) {
		jedis.del("cart:" + id);
	}

	@PostMapping("/carts")
	public ResponseEntity<Object> createCart(@RequestBody Cart cart) {
		Long counter = jedis.incr("cart-counter");
		cart.setId(counter);

		addUserToList(cart.getItems(), cart.getUser());

		jedis.hmset(getKey(cart), cart.getItems());
		return ResponseEntity.ok(cart);
	}

	@PutMapping("/carts/{id}")
	public ResponseEntity<Object> updateCart(@PathVariable String id, @RequestBody Cart new_cart) {
		jedis.del(getKey(new_cart));
		jedis.hmset(getKey(new_cart), new_cart.getItems());
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
