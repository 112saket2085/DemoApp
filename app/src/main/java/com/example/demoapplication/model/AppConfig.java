package com.example.demoapplication.model;

import android.content.SharedPreferences;
import com.example.demoapplication.app.App;
import com.example.demoapplication.utility.LocaleUtils;

public class AppConfig {

    private static final String USER_LOGGED_IN = "user_logged_in";
    private static final String LOCALE = "locale";
    private static final String LANGUAGE = "language";
    private static AppConfig instance;
    private final SharedPreferences preferences = App.getInstance().getSharedPreferences();
    private final SharedPreferences.Editor editor = preferences.edit();

    public static AppConfig getInstance() {
        if(instance==null) {
            instance = new AppConfig();
        }
        return instance;
    }

    public boolean isLoggedIn() {
        return preferences.getBoolean(USER_LOGGED_IN,false);
    }

    public void setLoggedIn(boolean isLoggedIn) {
        editor.putBoolean(USER_LOGGED_IN,isLoggedIn);
        commit();
    }

    public String getLocale() {
        return preferences.getString(LOCALE, LocaleUtils.getInstance().getEnglishLocale());
    }

    public void setLocale(String locale) {
        editor.putString(LOCALE, locale);
        commit();
    }

    public String getLanguage() {
        return preferences.getString(LANGUAGE, "English");
    }

    public void setLanguage(String language) {
        editor.putString(LANGUAGE, language);
        commit();
    }

    private void commit() {
        editor.commit();
    }

}
