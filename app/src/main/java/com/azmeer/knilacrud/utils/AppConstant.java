package com.azmeer.knilacrud.utils;

import android.Manifest;
import android.content.SharedPreferences;

public class AppConstant {
    public static final boolean IsToastShow = true;
    public static final String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    public static final int REQUEST_EXTERNAL_STORAGE = 1;
    public static String ADMIN_USER_NAME ="admin@gmail.com";
    public static String ADMIN_PASSWORD ="admin1234";
    public static String ROLE ="role";
    public static String USERID ="userid";
    public static String BASEURL ="https://reqres.in/api/";

}
