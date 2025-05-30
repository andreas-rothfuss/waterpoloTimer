package de.wasserball.wabaclock.settings;

public interface DialogListener {
    void applyValue(StringSetting setting, String value);
    void applyValue(ColorSetting setting, int value);
    void applyValue(IntegerSetting setting, int value);
}
