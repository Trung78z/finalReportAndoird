package com.hcmus.management.common;

public class Api {
    private static String baseUrl = "http://10.0.2.2:8080";
    public static String apiLogin = baseUrl + "/api/auth/login";
    public static String apiRefreshToken = baseUrl + "/api/auth/refresh";
}
