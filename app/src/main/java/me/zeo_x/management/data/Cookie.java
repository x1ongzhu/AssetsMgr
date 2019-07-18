package me.zeo_x.management.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;


import me.zeo_x.management.model.bean.UserBean;

/**
 * Created by xiong on 2016/1/3.
 */
public class Cookie {
    public static UserBean user;
    public static String username;
    public static SharedPreferences spf;

    public static String[] getEquArr(Context context) {
        if (spf == null) {
            spf = context.getApplicationContext().getSharedPreferences("cookie", Context.MODE_APPEND);
        }
        String[] arr = spf.getString("equArr", "").split("#");
        return arr;
    }

    public static void updateEquArr(Context context, String equid) {
        if (spf == null) {
            spf = context.getApplicationContext().getSharedPreferences("cookie", Context.MODE_APPEND);
        }
        SharedPreferences.Editor editor = spf.edit();
        String str = spf.getString("equArr", "");
        String[] arr = str.split("#");
        for (String s : arr) {
            if (TextUtils.equals(s, equid)) {
                return;
            }
        }
        String newStr = "";
        if (arr.length == 10) {
            for (int i = 0; i < 9; i++) {
                arr[i] = arr[i + 1];
            }
            arr[9] = equid;
            for (String s : arr) {
                newStr += s + "#";
            }
        } else {
            String[] newArr = new String[arr.length + 1];
            System.arraycopy(arr, 0, newArr, 0, arr.length);
            newArr[arr.length] = equid;
            for (String s : newArr) {
                newStr += s + "#";
            }
        }
        editor.putString("equArr", newStr);
        editor.apply();
    }

    public static String[] getContractArr(Context context) {
        if (spf == null) {
            spf = context.getApplicationContext().getSharedPreferences("cookie", Context.MODE_APPEND);
        }
        String[] arr = spf.getString("contractArr", "").split("#");
        return arr;
    }

    public static void updateContractArr(Context context, String contractid) {
        if (spf == null) {
            spf = context.getApplicationContext().getSharedPreferences("cookie", Context.MODE_APPEND);
        }
        SharedPreferences.Editor editor = spf.edit();
        String str = spf.getString("contractArr", "");
        String[] arr = str.split("#");
        for (String s : arr) {
            if (TextUtils.equals(s, contractid)) {
                return;
            }
        }
        String newStr = "";
        if (arr.length == 10) {
            for (int i = 0; i < 9; i++) {
                arr[i] = arr[i + 1];
            }
            arr[9] = contractid;
            for (String s : arr) {
                newStr += s + "#";
            }
        } else {
            String[] newArr = new String[arr.length + 1];
            System.arraycopy(arr, 0, newArr, 0, arr.length);
            newArr[arr.length] = contractid;
            for (String s : newArr) {
                newStr += s + "#";
            }
        }
        editor.putString("contractArr", newStr);
        editor.apply();
    }
}
