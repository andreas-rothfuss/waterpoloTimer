package de.wasserball.wabaclock.settings;

import android.content.SharedPreferences;

public class BooleanSetting {

    public final String key;
    public final String title;
    public final boolean defaultVal;
    public boolean value;

    public BooleanSetting(String key, boolean defaultVal, String title){
        this.key = key;
        this.defaultVal = defaultVal;
        value = defaultVal;
        this.title = title;
    }

    void readFromSettings(SharedPreferences settings) {
        value = settings.getBoolean(key, defaultVal);
    }

    public void applyValue(SharedPreferences settings, boolean value) {
        this.value = value;
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(key, value);
        // Apply the edits!
        editor.apply();
    }
}
