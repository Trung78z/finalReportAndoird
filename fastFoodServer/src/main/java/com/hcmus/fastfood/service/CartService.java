package com.hcmus.fastfood.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hcmus.fastfood.dto.AddToCartDTO;
import com.hcmus.fastfood.dto.CartFoodDTO;
import com.hcmus.fastfood.model.Cart;
import com.hcmus.fastfood.model.FastFood;
import com.hcmus.fastfood.model.User;
import com.hcmus.fastfood.repositories.CartRepository;
import com.hcmus.fastfood.repositories.FastFoodRepository;
import com.hcmus.fastfood.repositories.UserRepo;

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

    public List<FastFood> findCartsByUserName(String userName) {
        return cartRepository.findFoodsByUserName(userName);
    }

    public List<Cart> findCartsWithFoodByUserName(String userName) {
        return cartRepository.findCartsWithFoodByUserName(userName).stream().filter(Cart::isActive).toList();
    }

    public List<CartFoodDTO> findCartFoodByUserName(String userName) {
        List<Cart> carts = cartRepository.findCartsWithFoodByUserName(userName);
        return carts.stream()
                .map(cart -> new CartFoodDTO(cart.getFood(), cart.getQuantity(), cart.isActive()))
                .toList();
    }

    public void deleteCartById(String id) {
        Cart cart = cartRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
        cartRepository.delete(cart);
    }
}
