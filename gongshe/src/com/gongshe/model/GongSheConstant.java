package com.gongshe.model;

public interface GongSheConstant {
    public static final String BASE_URL = "http://192.168.1.7:8080";
    public static final String PATH_USER = "/user";
    public static final String PATH_REGISTER = "/register";
    public static final String PATH_LOGIN = "/login";
    public static final String PATH_GROUP = "/group";
    public static final String PATH_MY_GROUP = "/findCreatedGroups";
    public static final String PATH_BELONG_GROUP = "/findBelongToGroups";
    public static final String PATH_GROUP_CREATE = "/create";

    public static final String RESULT_AUTH_ERROR = "auth_error";
    public static final String RESULT_OK = "ok";
    public static final String RESULT_FAIL = "fail";
}
