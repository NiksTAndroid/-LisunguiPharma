package com.lisungui.pharma.utility;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefManager {

    public static String getUserID(Context context) {
        SharedPreferences pref = context.getSharedPreferences("Pharmacy", 0);
        return pref.getString("USER_ID", "");

    }

    public static void setUserID(Context context, String userid) {
        SharedPreferences pref = context.getSharedPreferences("Pharmacy", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("USER_ID", userid);
        editor.apply();

    }

    public static String getUserProfilePic(Context context) {
        SharedPreferences pref = context.getSharedPreferences("Pharmacy", 0);
        return pref.getString("USER_Profile", "");

    }

    public static void setUserProfilePic(Context context, String profile) {
        SharedPreferences pref = context.getSharedPreferences("Pharmacy", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("USER_Profile", profile);
        editor.apply();
    }


    public static void setFCM(Context context, String regId) {
        SharedPreferences pref = context.getSharedPreferences("Pharmacy", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("regId", regId);
        editor.apply();
    }

    public static String getFCM(Context context) {
        SharedPreferences pref = context.getSharedPreferences("Pharmacy", 0);
        return pref.getString("regId", "0");

    }

    public static String getUserName(Context context) {
        SharedPreferences pref = context.getSharedPreferences("Pharmacy", 0);
        return pref.getString("USER", "");

    }

    public static String getUserType(Context context) {
        SharedPreferences pref = context.getSharedPreferences("Pharmacy", 0);
        return pref.getString("USERTYPEID", "0");
    }

    public static void setUserType(Context context, String value) {
        SharedPreferences pref = context.getSharedPreferences("Pharmacy", 0);
        SharedPreferences.Editor edit = pref.edit();
        edit.putString("USERTYPEID", value);
        edit.apply();
    }

    public static void getUserLogedIn(Context context, boolean value) {
        SharedPreferences pref = context.getSharedPreferences("Pharmacy", 0);
        SharedPreferences.Editor edit = pref.edit();
        edit.putBoolean("LoggedIn", value);
        edit.apply();
    }

    public static boolean setUserLogedIn(Context context) {
        SharedPreferences pref = context.getSharedPreferences("Pharmacy", 0);
      return   pref.getBoolean("LoggedIn", false);

    }


    public static String getOrderType(Context context) {
        SharedPreferences pref = context.getSharedPreferences("Pharmacy", 0);
        return pref.getString("ORDER_Type", "");

    }

    public static void setOrderType(Context context, String orderType) {
        SharedPreferences pref = context.getSharedPreferences("Pharmacy", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("ORDER_Type", orderType);
        editor.apply();

    }

}
