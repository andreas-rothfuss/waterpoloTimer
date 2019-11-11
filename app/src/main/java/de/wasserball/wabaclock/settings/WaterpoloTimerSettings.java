package de.wasserball.wabaclock.settings;

import android.content.Context;
import android.content.SharedPreferences;

public class WaterpoloTimerSettings {

    private static final String PREFS_NAME = "WaterpoloClockSettings";

    public static final IntegerSetting NUMBER_OF_PERIODS = new IntegerSetting(
            "number_of_periods",1, 10, 4,
            "Set number of periods");

    public static final IntegerSetting PERIOD_DURATION = new IntegerSetting(
            "period_duration", 1, 60, 6,
            "Set period duration in seconds");

    public static final IntegerSetting HALF_TIME_DURATION = new IntegerSetting(
            "half_time_duration", 1, 30, 3,
            "Set half-time duration in minutes");

    public static final IntegerSetting BREAK_TIME_DURATION = new IntegerSetting(
            "not_half_time_break_duration", 1, 30, 2,
            "Set break duration (not half-time) in minutes");

    public static final IntegerSetting TIMEOUT_DURATION = new IntegerSetting(
            "timeout_duration", 1, 120, 60,
            "Set timeout duration in seconds");

    public static final IntegerSetting TIMEOUT_END_WARNING = new IntegerSetting(
            "timeout_end_warning", 1, 60, 15,
            "Set time for warning prior to timeout end seconds");

    public static final IntegerSetting OFFENCE_TIME_DURATION = new IntegerSetting(
            "offence_major_duration", 1, 60, 30,
            "Set offence duration in seconds");

    public static final IntegerSetting OFFENCE_TIME_MINOR_DURATION = new IntegerSetting(
            "offence_minor_duration", 1, 59, 20,
            "Set minor offence duration in seconds");

    public static final BooleanSetting ENABLE_SOUND = new BooleanSetting(
            "enable_sound", true,
            "Sound enabled?");

    public static final BooleanSetting ENABLE_DECIMAL = new BooleanSetting(
            "enable_decimal", false,
            "Decimal seconds enabled?");

    public static final StringSetting MASTER_IP = new StringSetting(
            "master_ip", "127.0.0.1",
            "Set ip of master unit");

    public static final StringSetting HOME_TEAM_NAME = new StringSetting(
            "home_team_name", "Home",
            "Name of the home team");

    public static final StringSetting GUEST_TEAM_NAME = new StringSetting(
            "guest_team_name", "Guest",
            "Name of the guest team");

    public static SharedPreferences getSharedPreferences(Context context){
        return context.getSharedPreferences(PREFS_NAME, 0);
    }

    public static final IntegerSetting DISCLAIMER_DISPLAYED = new IntegerSetting(
            "disclaimer_displayed_version", 0, Integer.MAX_VALUE, 0,
            "Disclaimer version previously displayed");

    public static void updateAllFromSettings(Context context) {
        SharedPreferences settings = getSharedPreferences(context);
        NUMBER_OF_PERIODS.readFromSettings(settings);
        PERIOD_DURATION.readFromSettings(settings);
        HALF_TIME_DURATION.readFromSettings(settings);
        BREAK_TIME_DURATION.readFromSettings(settings);
        TIMEOUT_DURATION.readFromSettings(settings);
        TIMEOUT_END_WARNING.readFromSettings(settings);
        OFFENCE_TIME_DURATION.readFromSettings(settings);
        OFFENCE_TIME_MINOR_DURATION.readFromSettings(settings);
        ENABLE_SOUND.readFromSettings(settings);
        ENABLE_DECIMAL.readFromSettings(settings);
        MASTER_IP.readFromSettings(settings);
        HOME_TEAM_NAME.readFromSettings(settings);
        GUEST_TEAM_NAME.readFromSettings(settings);
        DISCLAIMER_DISPLAYED.readFromSettings(settings);
    }
}
