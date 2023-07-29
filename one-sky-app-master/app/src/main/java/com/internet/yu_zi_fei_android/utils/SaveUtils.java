package com.internet.yu_zi_fei_android.utils;

/**
 * Created by 59102 on 2020/8/25.
 */
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;

public class SaveUtils {

    private static final String PREFERENCES_FILE = "sp_settings";

    public static boolean readSharedSetting(Context context, String settingSPName, boolean defaultValue) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        return sharedPref.getBoolean(settingSPName, defaultValue);
    }

    public static void saveSharedSetting(Context context, String settingSPName, boolean settingValue) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(settingSPName, settingValue);
        editor.apply();
    }

    public static Drawable tintMyDrawable(Drawable drawable, int color) {
        drawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(drawable, color);
        DrawableCompat.setTintMode(drawable, PorterDuff.Mode.SRC_IN);
        return drawable;
    }

}