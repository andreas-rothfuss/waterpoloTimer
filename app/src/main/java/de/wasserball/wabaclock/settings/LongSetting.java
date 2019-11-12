package de.wasserball.wabaclock.settings;

import android.content.SharedPreferences;

public class LongSetting {

    public final String key;
    public final long max;
    public final long min;
    public final String title;
    public final long defaultVal;
    public long value;

    public LongSetting(String key, long min, long max, long defaultVal, String title){
        this.key = key;
        this.min = min;
        this.max = max;
        this.defaultVal = defaultVal;
        value = defaultVal;
        this.title = title;
    }

    void readFromSettings(SharedPreferences settings) {
        value = settings.getLong(key, defaultVal);
    }

    public void applyValue(SharedPreferences settings, Long value) {
        this.value = value;
        SharedPreferences.Editor editor = settings.edit();
        editor.putLong(key, value);
        // Apply the edits!
        editor.apply();
    }
}
