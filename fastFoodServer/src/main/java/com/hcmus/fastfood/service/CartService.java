package com.hcmus.fastfood.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hcmus.fastfood.dto.AddToCartDTO;
import com.hcmus.fastfood.model.Cart;
import com.hcmus.fastfood.model.FastFood;
import com.hcmus.fastfood.model.User;
import com.hcmus.fastfood.repositories.FastFoodRepository;
import com.hcmus.fastfood.repositories.UserRepo;
import com.hcmus.fastfood.repository.CartRepository;

import java.util.List;

@Service
public class CartService {
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepo userRepository;

    @Autowired
    private FastFoodRepository fastFoodRepository;

    public Cart addToCart(String username, AddToCartDTO dto) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        FastFood food = fastFoodRepository.findById(dto.getFoodId())
                .orElseThrow(() -> new RuntimeException("Food not found"));

        Cart cart = new Cart();
        cart.setUser(user);
        cart.setFood(food);
        cart.setQuantity(dto.getQuantity());
        cart.setActive(true);
        return cartRepository.save(cart);
    }

    public List<Cart> findCartsByUserName(String userName) {
        return cartRepository.findByUserNameWithFood(userName);
    }
}
