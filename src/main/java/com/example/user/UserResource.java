package com.example.user;

import com.example.cart.CartNotFoundException;
import com.example.cart.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
public class UserResource {

	@Autowired
	private UserRepository cartRepository;

	@GetMapping("/users")
	public List<User> retrieveAllusers() {
		return cartRepository.findAll();
	}

	@GetMapping("/users/{id}")
	public User retrievecart(@PathVariable long id) {
		Optional<User> cart = cartRepository.findById(id);

		if (!cart.isPresent())
			throw new CartNotFoundException("id-" + id);

		return cart.get();
	}

	@DeleteMapping("/users/{id}")
	public void deletecart(@PathVariable long id) {
		cartRepository.deleteById(id);
	}

	@PostMapping("/users")
	public ResponseEntity<Object> createcart(@RequestBody User cart) {
		User savedcart = cartRepository.save(cart);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(savedcart.getId()).toUri();

		return ResponseEntity.created(location).build();

	}
	
	@PutMapping("/users/{id}")
	public ResponseEntity<Object> updatecart(@RequestBody User cart, @PathVariable long id) {

		Optional<User> cartOptional = cartRepository.findById(id);

		if (!cartOptional.isPresent())
			return ResponseEntity.notFound().build();

		cart.setId(id);
		
		cartRepository.save(cart);

		return ResponseEntity.noContent().build();
	}

}
