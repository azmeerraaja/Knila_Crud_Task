package com.azmeer.knilacrud.utils;

import static android.content.Context.DOWNLOAD_SERVICE;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.FileOutputStream;

public class Util {

    // Show Dynamic Toast
    public static void showToast(Context context, String msg) {
        if (AppConstant.IsToastShow) {
            Toast.makeText(context, "" + msg, Toast.LENGTH_SHORT).show();
        }
    }


}
