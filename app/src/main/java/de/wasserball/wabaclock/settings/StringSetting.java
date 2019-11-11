package de.wasserball.wabaclock.settings;

import android.content.SharedPreferences;

public class StringSetting {

    public final String key;
    public final String title;
    public final String defaultVal;
    public String value;

    public StringSetting(String key, String defaultVal, String title){
        this.key = key;
        this.defaultVal = defaultVal;
        value = defaultVal;
        this.title = title;
    }

    void readFromSettings(SharedPreferences settings) {
        value = settings.getString(key, defaultVal);
    }

    public void applyValue(SharedPreferences settings, String value) {
        this.value = value;
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        // Apply the edits!
        editor.apply();
    }
}
