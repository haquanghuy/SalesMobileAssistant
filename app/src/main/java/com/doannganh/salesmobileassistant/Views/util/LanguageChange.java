package com.doannganh.salesmobileassistant.Views.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;

import java.util.Locale;

public class LanguageChange {
    Activity context;
    private final String PREFS_NAME = "preferencesLanguage";
    private final String PREF_LANG = "Language";

    public LanguageChange(Activity context){
        this.context = context;
    }

    public void loadLocale() {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME,
                Activity.MODE_PRIVATE);
        String language = prefs.getString(PREF_LANG, "");
        changeLang(language);
    }

    public void changeLang(String lang) {
        Locale locale = new Locale(lang);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        context.getBaseContext().getResources().updateConfiguration(configuration,
                context.getBaseContext().getResources().getDisplayMetrics());

    }

    public String getLanguagePresent(){
        /*
        Locale.getDefault().getLanguage()       ---> en
        Locale.getDefault().getISO3Language()   ---> eng
        Locale.getDefault().getCountry()        ---> US
        Locale.getDefault().getISO3Country()    ---> USA
        Locale.getDefault().getDisplayCountry() ---> United States
        Locale.getDefault().getDisplayName()    ---> English (United States)
        Locale.getDefault().toString()          ---> en_US
        Locale.getDefault().getDisplayLanguage()---> English
         */
        //return Locale.getDefault().getLanguage(); en
        return Locale.getDefault().getDisplayLanguage();
    }

    public void saveLocale(String lang) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME,
                Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PREF_LANG, lang);
        editor.commit();
    }
}
