package com.hcmus.fastfood.controller;

import com.hcmus.fastfood.dto.AddToCartDTO;
import com.hcmus.fastfood.model.Cart;
import com.hcmus.fastfood.service.CartService;
import com.hcmus.fastfood.utils.ResponseEntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/")
    public ResponseEntity<?> addToCart(@RequestBody AddToCartDTO dto) {
        try {
            Cart cart = cartService.addToCart(dto);
            return ResponseEntityUtils.created(cart);
        } catch (RuntimeException ex) {
            return ResponseEntityUtils.error(ex.getMessage(), null);
        } catch (Exception ex) {
            return ResponseEntityUtils.serverError("Server error", null);
        }
    }
}