package com.hcmus.management.common;

public class Api {
    private static String baseUrl = "https://api.food.hcmuss.site/api";
    public static String apiLogin = baseUrl + "/auth/login";
    public static String apiRefreshToken = baseUrl + "/auth/refresh";
    public static String createFood = baseUrl + "/food";
}
