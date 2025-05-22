package com.hcmus.fastfood.controller;

import com.hcmus.fastfood.dto.FastFoodSaveDTO;
import com.hcmus.fastfood.model.FastFood;
import com.hcmus.fastfood.service.FastFoodService;
import com.hcmus.fastfood.utils.ResponseEntityUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/food")
public class FastFoodController {

    @Autowired
    private FastFoodService fastFoodService;

    @PostMapping
    public FastFood saveFastFood(@RequestBody FastFoodSaveDTO dto) {
        return fastFoodService.saveFastFood(dto);
    }

    @GetMapping
    public List<FastFood> getAllFastFood() {
        return fastFoodService.getAllFastFood();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFastFood(@PathVariable String id) {
        try {
            fastFoodService.deleteFastFood(id);
            return ResponseEntityUtils.success("Food deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntityUtils.error("Food not found", null);
        }

    }
}