package com.example.user;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="USER")
public class User {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

//    @OneToMany(mappedBy="user")
//    private Set<Cart> cart;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

//    public Set<Cart> getCart() {
//        return cart;
//    }
//
//    public void setCart(Set<Cart> cart) {
//        this.cart = cart;
//    }
}