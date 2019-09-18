package com.bt_121shoppe.motorbike.Language;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.preference.PreferenceManager;

import java.util.Locale;

public class LocaleHapler {
    private static final String SELCTED_LANGUAGE = "Locale.Helper.Selected.Language";
    public static Context onAttach(Context context){
        String lang = getPersistedData(context, Locale.getDefault().getLanguage());
        return setLocale(context,lang);
    }
    public static Context onAttach(Context context, String defaultLanguage){
        String lang = getPersistedData(context,defaultLanguage);
        return setLocale(context,lang);
    }

    public static Context setLocale(Context context, String lang) {
        persist(context,lang);
        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.N)
            return updateResoures(context,lang);

        return updateResourceslegacy(context,lang);
    }

    @TargetApi(Build.VERSION_CODES.N)
    private static Context updateResoures(Context context, String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);

        Configuration config = context.getResources().getConfiguration();
        config.setLocale(locale);
        config.setLayoutDirection(locale);
        return context.createConfigurationContext(config);
    }
//    public void language(Context context,String lang) {
//        Locale locale = new Locale(lang);
//        Locale.setDefault(locale);
//        Configuration confi = new  Configuration();
//        confi.locale = locale;
//        context.getResources().updateConfiguration(confi, context.getResources().getDisplayMetrics());
////        SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
////        editor.putString("My_Lang", lang);
////        editor.apply();
//    }
    @SuppressWarnings("deprecation")
    private static Context updateResourceslegacy(Context context, String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);

        Resources resources = context.getResources();
        Configuration config = resources.getConfiguration();
        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.JELLY_BEAN_MR1)
           config.setLayoutDirection(locale);
        resources.updateConfiguration(config,resources.getDisplayMetrics());
        return context;
    }

    private static void persist(Context context, String lang){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(SELCTED_LANGUAGE,lang);
        editor.apply();
    }

    private static String getPersistedData(Context context, String language) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(SELCTED_LANGUAGE,language);
    }
}
