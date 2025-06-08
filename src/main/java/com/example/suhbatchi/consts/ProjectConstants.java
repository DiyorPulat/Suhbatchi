package com.example.suhbatchi.consts;

public interface ProjectConstants {
    public static final String AUTH = "/auth";
    public static final String LOGIN = "/login";
    public static final String GET_PHONE = "/get-number";
    public static final String SAVE_NAMES = "/save-names";
    public static final String SAVE_PHONE = "/save-password";

    public static final String OTP_MESSAGE_1 = "suhbatchi.uz sayti orqali ro'yxatdan o'tish uchun tasdiqlash kodingiz: ";
    public static final String OTP_MESSAGE_2 = "Suhbatchi.uz platformasida parolingizni tiklash uchun kod: ";
    public static final String GET_TOKEN_FROM_OTP = "/auth/login";
    public static final String SEND_OTP_URL = "/message/sms/send";
    public static final String EMAIL_ESKIZ = "kholikulovelyor@gmail.com";
    public static final String PASSWORD_ESKIZ = "lWMS8DpghTyKoxHalY8Rvi8OocKFLxYx4pWBSL9f";

    public static final String DEFAULT_FROM = "4546";

    public static final String TYPE_OF_FULL = "full";

    public static final String TAX_INFO_URL = "/remote-access-api/company/info/{INN}";


    public static final String PRICING_PLAN = "/pricing";
    public static final String CREATE_PRICING_PLAN = "/create";
    public static final String UPDATE_PRICING_PLAN = "/update";
    public static final String DELETE_PRICING_PLAN = "/delete";
    public static final String GET_PRICING_PLAN = "/get";
    public static final String GET_ALL_PRICING_PLAN = "/getAll";


    public static final String USER_CONTROL = "/user-control";
    public static final String GET_USER_STATUS = "/get";
    public static final String PERFORM_USER_STATUS = "/perform";
    public static final String GET_ALL_USER = "/getall";
    public static final String USER_ACTIVATE = "/activate";
}
