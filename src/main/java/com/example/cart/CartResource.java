package com.example.cart;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import com.example.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
public class CartResource {

	@Autowired
	private CartRepository cartRepository;

	@GetMapping("/carts")
	public List<Cart> retrieveAllcarts() {
		return cartRepository.findAll();
	}

	@GetMapping("/carts/{id}")
	public Cart retrievecart(@PathVariable long id) {
		Optional<Cart> cart = cartRepository.findById(id);

		if (!cart.isPresent())
			throw new CartNotFoundException("id-" + id);

		return cart.get();
	}

	@DeleteMapping("/carts/{id}")
	public void deletecart(@PathVariable long id) {
		cartRepository.deleteById(id);
	}

	@PostMapping("/carts")
	public ResponseEntity<Object> createcart(@RequestBody Cart cart) {
		Cart savedcart = cartRepository.save(cart);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(savedcart.getId()).toUri();

		return ResponseEntity.created(location).build();

	}
	
	@PutMapping("/carts/{id}")
	public ResponseEntity<Object> updatecart(@RequestBody Cart cart, @PathVariable long id) {

		Optional<Cart> cartOptional = cartRepository.findById(id);

		if (!cartOptional.isPresent())
			return ResponseEntity.notFound().build();

		cart.setId(id);
		
		cartRepository.save(cart);

		return ResponseEntity.noContent().build();
	}

}
