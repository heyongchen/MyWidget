package com.widget.howard.bobwidget;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.facebook.drawee.backends.pipeline.Fresco;

public class MainApplication extends Application {

    private static MainApplication application;

    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);//在Application 初始化时，进行初始化Fresco
        application = this;
    }

    /**
     * 将String类型信息存入sp中
     *
     * @param key
     * @param value
     */
    public static void saveStringInfo(String key, String value) {
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(application.getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    /**
     * 从sp获取String类型信息
     *
     * @param key
     * @return
     */
    public static String loadStringInfo(String key) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences
                (application
                        .getApplicationContext());
        String value = preferences.getString(key, "");
        return value;
    }
}
