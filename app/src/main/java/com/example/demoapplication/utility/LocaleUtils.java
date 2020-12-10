package com.example.demoapplication.utility;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import com.example.demoapplication.model.AppConfig;
import java.util.Locale;

public class LocaleUtils {

    private static LocaleUtils localeUtils;

    public static LocaleUtils getInstance() {
        if(localeUtils==null) {
            localeUtils=new LocaleUtils();
        }
        return localeUtils;
    }

    public String getEnglishLocale() {
        return "en";
    }

    public String getArabicLocale() {
        return "ar";
    }

    public Context setLocale(Context context,String language,String locale,boolean isLocaleChange){
        if(isLocaleChange) {
            setLocalePref(language, locale);
        }
        return loadLanguage(context,locale);
    }

    private Context loadLanguage(Context context,String localeLang) {
        Locale locale = new Locale(localeLang);
        Locale.setDefault(locale);
        Resources resources=context.getResources();
        Configuration config = resources.getConfiguration();
        config.setLayoutDirection(locale);
        config.setLocale(locale);
        context = context.createConfigurationContext(config);
        return context;
    }

    /**
     * Method to set Language Locale and Language in Preference
     * @param language Language
     * @param locale Language Locale
     */
    private void setLocalePref(String language,String locale){
        AppConfig appConfig=AppConfig.getInstance();
        appConfig.setLanguage(language);
        appConfig.setLocale(locale);
    }

}
