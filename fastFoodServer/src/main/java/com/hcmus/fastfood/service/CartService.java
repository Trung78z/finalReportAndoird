package com.hcmus.fastfood.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hcmus.fastfood.dto.AddToCartDTO;
import com.hcmus.fastfood.model.Cart;
import com.hcmus.fastfood.repository.CartRepository;

@Service
public class CartService {
    @Autowired
    private CartRepository cartRepository;

    public Cart addToCart(AddToCartDTO dto) {
        Cart cart = new Cart();
        cart.setUserId(dto.getUserId());
        cart.setFoodId(dto.getFoodId());
        cart.setQuantity(dto.getQuantity());
        cart.setActive(true);
        return cartRepository.save(cart);
    }
}
