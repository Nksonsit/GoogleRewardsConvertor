package com.jatin.rewards;

import android.content.Context;

public class PrefUtils {
    public static String USER_ID = "UserId";
    public static String USER_PROFILE_KEY = "USER_PROFILE_KEY";
    public static String LOGGED_IN = "isLogin";
    public static String FILTER_APPLIED = "filter_apply";
    private static String FCM_TOKEN = "fcm";
    private static String EMAIL = "email";
    private static String USER_NAME = "name";
    private static String ACCESS_TYPE = "access";
    private static String CONNECTOR_TYPE = "CONNECTOR";
    private static String SPEED_LEVEL_TYPE = "SPEED_LEVEL";
    private static String FACILITIES_TYPE = "FACILITIES";
    private static String LANGUAGE = "lang";

    public static void setEmailId(Context ctx, String value) {
        Prefs.with(ctx).save(EMAIL, value);
    }

    public static String getEmailID(Context ctx) {
        return Prefs.with(ctx).getString(EMAIL, "");
    }

    public static void setUserName(Context ctx, String value) {
        Prefs.with(ctx).save(USER_NAME, value);
    }

    public static String getUserName(Context ctx) {
        return Prefs.with(ctx).getString(USER_NAME, "");
    }

    public static void setLoggedIn(Context ctx, boolean value) {
        Prefs.with(ctx).save(LOGGED_IN, value);
    }

    public static boolean isUserLoggedIn(Context ctx) {
        return Prefs.with(ctx).getBoolean(LOGGED_IN, false);
    }


}
