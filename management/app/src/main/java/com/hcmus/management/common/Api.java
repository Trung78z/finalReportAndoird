package com.hcmus.management.common;

public class Api {
       public static String baseUrl = "https://api.food.hcmuss.site/api";
//     public static String baseUrl = "http://10.0.2.2:8080/api";
    public static String apiLogin = baseUrl + "/auth/login";
    public static String apiRegister = baseUrl + "/auth/register";
    public static String apiRefreshToken = baseUrl + "/auth/refresh";
    public static String createFood = baseUrl + "/food";
    public static String getFoodList = baseUrl + "/food";
    public static String getCart = baseUrl + "/cart/user/cart-food";
    public static String createCart = baseUrl + "/cart";
}
