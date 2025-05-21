package com.hcmus.fastfood.controller;

import com.hcmus.fastfood.dto.FastFoodSaveDTO;
import com.hcmus.fastfood.model.FastFood;
import com.hcmus.fastfood.service.FastFoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/fastfood")
public class FastFoodController {

    @Autowired
    private FastFoodService fastFoodService;

    @PostMapping
    public FastFood saveFastFood(@RequestBody FastFoodSaveDTO dto) {
        return fastFoodService.saveFastFood(dto);
    }
}