package de.wasserball.wabaclock.settings;

import android.content.SharedPreferences;
import android.graphics.Color;

public class ColorSetting {

    public final String key;
    public final String title;
    public final int defaultVal;
    public int value;

    public ColorSetting(String key, int defaultVal, String title){
        this.key = key;
        this.defaultVal = defaultVal;
        value = defaultVal;
        this.title = title;
    }

    void readFromSettings(SharedPreferences settings) {
        value = settings.getInt(key, defaultVal);
    }

    public void applyValue(SharedPreferences settings, int value) {
        this.value = value;
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(key, value);
        // Apply the edits!
        editor.apply();
    }
}
