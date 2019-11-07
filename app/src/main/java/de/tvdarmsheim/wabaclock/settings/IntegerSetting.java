package de.tvdarmsheim.wabaclock.settings;

import android.content.SharedPreferences;

public class IntegerSetting {

    public final String key;
    public final int max;
    public final int min;
    public final String title;
    public final int defaultVal;
    public int value;

    public IntegerSetting(String key, int min, int max, int defaultVal, String title){
        this.key = key;
        this.min = min;
        this.max = max;
        this.defaultVal = defaultVal;
        value = defaultVal;
        this.title = title;
    }

    void readFromSettings(SharedPreferences settings) {
        value = settings.getInt(key, defaultVal);
    }

    public void applyValue(SharedPreferences settings, Integer value) {
        this.value = value;
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(key, value);
        // Apply the edits!
        editor.apply();
    }
}
