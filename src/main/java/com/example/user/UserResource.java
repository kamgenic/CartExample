package com.example.user;

import com.example.cart.CartNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class UserResource {

	private final Jedis jedis = new Jedis("52.91.75.7", 10001);

	public UserResource() {
		jedis.auth("mypass");
	}

	@GetMapping("/users")
	public List<User> retrieveAllusers() {
		return jedis.keys("user:*")
				.stream()
				.map(k -> new User(Long.valueOf(k.split(":")[1]), jedis.get(k)))
				.collect(Collectors.toList());
	}

	@GetMapping("/users/{id}")
	public User retrieveUser(@PathVariable long id) {
		String name = jedis.get("user:" + id);
		if( name != null)
			return new User(id, name);
		else
			throw new CartNotFoundException("id-" + id);
	}

	@DeleteMapping("/users/{id}")
	public void deletecart(@PathVariable long id) {
		jedis.del("user:" + id);
	}

	@PostMapping("/users")
	public ResponseEntity<Object> createUser(@RequestBody User user) {
		Long counter = jedis.incr("user-counter");
		user.setId(counter);

		jedis.set("user:" + counter, user.getName());

		return ResponseEntity.ok(user);

	}
	
	@PutMapping("/users/{id}")
	public ResponseEntity<Object> updateUser(@RequestBody User new_user, @PathVariable long id) {

		String user = jedis.get("user:" + id);

		if (!user.isEmpty()) {
			jedis.set("user:"  + id, new_user.getName());
			return ResponseEntity.ok(new_user);
		} else {
			return ResponseEntity.noContent().build();
		}

	}

}
