package com.htec.account.security;

public class SecurityConstants {

    public static final long JWT_EXPIRATION_TIME = 3600000; // 1 hour
    public static final long REFRESH_JWT_EXPIRATION_TIME = 864000000; // 10 days
    public static final long PASSWORD_RESET_EXPIRATION_TIME = 3600000; // 1 hour

    public static final String BEARER_TOKEN_PREFIX = "Bearer ";
    public static final String TOKEN_SECRET = "ao9h8vbm52kxfd9d6lw0yq";
    public static final String CONFIRMATION_TOKEN_SECRET = "mjfjhj347892ikdo545234sA_+(57%&*^%";
    public static final String AUTHORIZATION_HEADER_STRING = "Authorization";
    public static final String SIGN_IN_URL = "/login";
    public static final String SIGN_IN_MICROSOFT_URL = "/login/microsoft";
    public static final String PASSWORD_RESET_REQUEST_URL = "/users/password-reset-request";
    public static final String PASSWORD_RESET_URL = "/users/password-reset";
    public static final String SIGN_UP_URL = "/register";
    public static final String SIGN_UP_MICROSOFT_URL = "/register/microsoft";
    public static final String SIGN_UP_CONFIRM_EMAIL_URL = "/tokens/confirmation";
    public static final String SIGN_UP_RESEND_TOKEN_URL = "/tokens/resend";
}
