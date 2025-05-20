package com.hcmus.fastfood.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class ResponseEntityUtils {

    public static <T> ResponseEntity<Map<String, Object>> success(T data) {
        Map<String, Object> body = new HashMap<>();
        body.put("success", true);
        body.put("msg", data);
        return ResponseEntity.ok(body);
    }

    public static <T> ResponseEntity<Map<String, Object>> created(T data) {
        Map<String, Object> body = new HashMap<>();
        body.put("success", true);
        body.put("msg", data);
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    public static <T> ResponseEntity<Map<String, Object>> error(String message, T data) {
        Map<String, Object> body = new HashMap<>();
        body.put("success", false);
        body.put("msg", message);
        return ResponseEntity.badRequest().body(body);
    }

    public static <T> ResponseEntity<Map<String, Object>> serverError(String message, T data) {
        Map<String, Object> body = new HashMap<>();
        body.put("success", false);
        body.put("msg", message);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}