package com.hcmus.fastfood.controller;

import com.hcmus.fastfood.dto.AddToCartDTO;
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
}