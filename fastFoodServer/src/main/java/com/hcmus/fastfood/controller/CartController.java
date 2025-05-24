package com.hcmus.fastfood.controller;

import com.hcmus.fastfood.dto.AddToCartDTO;
import com.hcmus.fastfood.dto.CartFoodDTO;
import com.hcmus.fastfood.dto.CartUpdateQuantityDTO;
import com.hcmus.fastfood.model.Cart;
import com.hcmus.fastfood.model.FastFood;
import com.hcmus.fastfood.service.CartService;
import com.hcmus.fastfood.utils.ResponseEntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping
    public ResponseEntity<?> addToCart(@RequestBody AddToCartDTO dto) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName(); // or get userId if stored as principal

            Cart cart = cartService.addToCart(username, dto); // Pass username instead of userId
            return ResponseEntityUtils.created(cart);
        } catch (RuntimeException ex) {
            return ResponseEntityUtils.error(ex.getMessage(), null);
        } catch (Exception ex) {
            return ResponseEntityUtils.serverError("Server error", null);
        }
    }

    @GetMapping
    public ResponseEntity<?> getCartByUserToken() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName(); // Extracted from JWT access token

            List<FastFood> carts = cartService.findCartsByUserName(username);
            return ResponseEntityUtils.success(carts);
        } catch (RuntimeException ex) {
            return ResponseEntityUtils.error(ex.getMessage(), null);
        } catch (Exception ex) {
            return ResponseEntityUtils.serverError("Server error", null);
        }
    }

    @GetMapping("/user/cart-food")
    public ResponseEntity<?> getCartFoodByUserToken() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            List<CartFoodDTO> cartFoods = cartService.findCartFoodByUserName(username);
            return ResponseEntityUtils.success(cartFoods);
        } catch (RuntimeException ex) {
            return ResponseEntityUtils.error(ex.getMessage(), null);
        } catch (Exception ex) {
            return ResponseEntityUtils.serverError("Server error", null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCartFood(@PathVariable Integer id, @RequestBody CartUpdateQuantityDTO dto) {
        try {
            cartService.updateCartQuantity(dto);
            return ResponseEntityUtils.success("Cart updated successfully");
        } catch (RuntimeException e) {
            return ResponseEntityUtils.error("Food not found", null);
        } catch (Exception e) {
            return ResponseEntityUtils.serverError("Server error", null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCartFood(@PathVariable Integer id) {
        try {
            cartService.deleteCartById(id);
            return ResponseEntityUtils.success("Food deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntityUtils.error("Food not found", null);
        }
    }
}