package com.htec.shelfserver.security;

public class SecurityConstants {

    public static final long EXPIRATION_TIME = 864000000; // 10 days
    public static final long PASSWORD_RESET_EXPIRATION_TIME = 3600000; // 1 hour
  
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String TOKEN_SECRET = "ao9h8vbm52kxfd9d6lw0yq";
    public static final String CONFIRMATION_TOKEN_SECRET = "ao9h8vbm52kxfd9d6lw0yq";
    public static final String HEADER_STRING = "Authorization";
  
    public static final String SIGN_IN_URL = "/users/login";
    public static final String SIGN_UP_URL = "/users/register";
    public static final String SIGN_UP_CONFIRM_EMAIL_URL = "/users/register/confirmation";
    public static final String SIGN_UP_RESEND_TOKEN_URL = "/users/register/resend";

}
