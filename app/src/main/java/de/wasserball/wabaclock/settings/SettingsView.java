package de.wasserball.wabaclock.settings;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Switch;

import de.wasserball.wabaclock.R;

public class SettingsView extends AppCompatActivity implements DialogListener {

    Button btnNbOfPeriodsVal;
    Button btnPeriodVal;
    Button btnHalftimeVal;
    Button btnBreaktimeVal;
    Button btnTimeoutVal;
    Button btnTimeoutWarningVal;
    Button btnOffenceTimeMajorVal;
    Button btnOffenceTimeMinorVal;
    Switch btnOffenceTimeMinorResetVal;
    Switch btnDecoupleTimersVal;
    Button btnExclusionTimeDuration;
    Switch btnSoundEnabledVal;
    Switch btnDecimalEnabledVal;
    Switch btnDecimalEnabledDuringLastMinuteVal;
    Switch switchAlwaysUseDoubleDigitsForGoals;
    Switch btnShotclockResetEnabledVal;
    Switch btnPauseDuringBreakVal;
    Switch btnUseAutodiscovery;
    Button btnMasterIPVal;

    LinearLayout generalSettingsLayout;
    LinearLayout generalSettingsSummaryLayout;
    LinearLayout generalSettingsDetailLayout;
    LinearLayout offenceTimeSettingsLayout;
    LinearLayout offenceTimeSettingsSummaryLayout;
    LinearLayout offenceTimeSettingsDetailLayout;
    LinearLayout remoteSettingsLayout;
    LinearLayout remoteSettingsSummaryLayout;
    LinearLayout remoteSettingsDetailLayout;

    Button btnGeneralSettingsSummary;
    Button btnOffenceTimeSettingsSummary;
    Button btnRemoteSettingsSummary;
    private View overlayForNavigationBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //WindowCompat.setDecorFitsSystemWindows(getWindow(), true);
        setContentView(R.layout.settings_view);

        overlayForNavigationBar = findViewById(R.id.settingsLayout);
        overlayForNavigationBar.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_FULLSCREEN);

        btnNbOfPeriodsVal = findViewById(R.id.textViewNbOfPeriodsValue);
        btnPeriodVal = findViewById(R.id.textViewTimePerPeriodValue);
        btnHalftimeVal = findViewById(R.id.textViewHalfTimeValue);
        btnBreaktimeVal = findViewById(R.id.textViewBreakTimeValue);
        btnTimeoutVal = findViewById(R.id.textViewTimeoutValue);
        btnTimeoutWarningVal = findViewById(R.id.textViewTimeoutEndWarningValue);
        btnOffenceTimeMajorVal = findViewById(R.id.textViewOffenceTimeMajorValue);
        btnOffenceTimeMinorVal = findViewById(R.id.textViewOffennceTimeMinorValue);
        btnOffenceTimeMinorResetVal = findViewById(R.id.textViewOffenceTimeMinorResetSettingValue);
        btnDecoupleTimersVal = findViewById(R.id.textViewDecoupleTimersValue);
        btnExclusionTimeDuration = findViewById(R.id.textViewExclusionTimeValue);
        btnDecimalEnabledVal = findViewById(R.id.btnViewDecimalEnabledValue);
        btnDecimalEnabledDuringLastMinuteVal = findViewById(R.id.btnDecimalEnabledDuringLastMinuteValue);
        switchAlwaysUseDoubleDigitsForGoals = findViewById(R.id.switchAlwaysUseDoubleDigitsForGoalsValue);
        btnSoundEnabledVal = findViewById(R.id.textViewSoundEnabledValue);
        btnShotclockResetEnabledVal = findViewById(R.id.switchShotResetEnabledValue);
        btnPauseDuringBreakVal = findViewById(R.id.switchPauseDuringBreakValue);
        btnUseAutodiscovery = findViewById(R.id.switchAutodiscoveryEnabledValue);
        btnMasterIPVal = findViewById(R.id.textViewEditIP);

        btnGeneralSettingsSummary =findViewById(R.id.textViewGeneralSettingsSummary);
        btnOffenceTimeSettingsSummary =findViewById(R.id.textViewOffenceTimeSettingsSummary);
        btnRemoteSettingsSummary =findViewById(R.id.textViewRemoteSettingsSummary);

        generalSettingsLayout = findViewById(R.id.GeneralSettingsLayout);
        generalSettingsSummaryLayout = findViewById(R.id.GeneralSettingsSummaryLayout);
        generalSettingsDetailLayout = findViewById(R.id.GeneralSettingsDetailLayout);
        offenceTimeSettingsLayout = findViewById(R.id.OffenceTimeSettingsLayout);
        offenceTimeSettingsSummaryLayout = findViewById(R.id.OffenceTimeSettingsSummaryLayout);
        offenceTimeSettingsDetailLayout = findViewById(R.id.OffenceTimeSettingsDetailLayout);
        remoteSettingsLayout = findViewById(R.id.RemoteSettingsLayout);
        remoteSettingsSummaryLayout = findViewById(R.id.RemoteSettingsSummaryLayout);
        remoteSettingsDetailLayout = findViewById(R.id.RemoteSettingsDetailLayout);

        updateSettingsValueDisplay();

        onGeneralSettingsClicked(null);
        onOffenceTimeSettingsClicked(null);
        onRemoteSettingsClicked(null);
    }

    void updateSettingsValueDisplay() {
        btnNbOfPeriodsVal.setText(Integer.toString(AppSettings.NUMBER_OF_PERIODS.value));
        btnPeriodVal.setText(Integer.toString(AppSettings.PERIOD_DURATION.value));
        btnHalftimeVal.setText(Integer.toString(AppSettings.HALF_TIME_DURATION.value));
        btnBreaktimeVal.setText(Integer.toString(AppSettings.BREAK_TIME_DURATION.value));
        btnTimeoutVal.setText(Integer.toString(AppSettings.TIMEOUT_DURATION.value));
        btnTimeoutWarningVal.setText(Integer.toString(AppSettings.TIMEOUT_END_WARNING.value));
        btnOffenceTimeMajorVal.setText(Integer.toString(AppSettings.OFFENCE_TIME_DURATION.value));
        btnOffenceTimeMinorVal.setText(Integer.toString(AppSettings.OFFENCE_TIME_MINOR_DURATION.value));
        btnOffenceTimeMinorResetVal.setChecked(AppSettings.OFFENCE_TIME_MINOR_DURATION_RESET.value);
        btnDecoupleTimersVal.setChecked(AppSettings.DECOUPLE_TIMERS.value);
        btnExclusionTimeDuration.setText(Integer.toString(AppSettings.EXCLUSION_TIME_DURATION.value));
        btnSoundEnabledVal.setChecked(AppSettings.ENABLE_SOUND.value);
        btnDecimalEnabledVal.setChecked(AppSettings.ENABLE_DECIMAL.value);
        btnDecimalEnabledDuringLastMinuteVal.setChecked(AppSettings.ENABLE_DECIMAL_DURING_LAST.value);
        switchAlwaysUseDoubleDigitsForGoals.setChecked(AppSettings.ALWASY_USE_DOUBLE_DIGITS_FOR_GOALS.value);
        btnShotclockResetEnabledVal.setChecked(AppSettings.RESET_SHOTCLOCK_ON_GOAL.value);
        btnPauseDuringBreakVal.setChecked(AppSettings.STOP_BREAK_AND_TIMEOUT.value);
        btnUseAutodiscovery.setChecked(AppSettings.USE_AUTODISCOVERY.value);
        btnMasterIPVal.setText(AppSettings.MASTER_IP.value);

//        generalSettingsLayout.setWeightSum(2);
//        generalSettingsDetailLayout.setWeightSum(0);
//        generalSettingsDetailLayout.setVisibility(View.GONE);
//        offenceTimeSettingsLayout.setWeightSum(2);
//        offenceTimeSettingsDetailLayout.setWeightSum(0);
//        offenceTimeSettingsDetailLayout.setVisibility(View.GONE);
//        remoteSettingsLayout.setWeightSum(2);
//        remoteSettingsDetailLayout.setWeightSum(0);
//        remoteSettingsDetailLayout.setVisibility(View.GONE);
    }

    public int toggleLayoutVisibility(LinearLayout overallLayout,
                                       LinearLayout detailsLayout){
        int viewTargetStatus;
        if (detailsLayout.getVisibility() == View.VISIBLE){
            viewTargetStatus = View.GONE;
            detailsLayout.setVisibility(viewTargetStatus);
            //overallLayout.setWeightSum(2);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) overallLayout.getLayoutParams();
            params.weight = 2.0f;
            overallLayout.setLayoutParams(params);
        }
        else {
            viewTargetStatus = View.VISIBLE;
            LinearLayout.LayoutParams paramsDetails = (LinearLayout.LayoutParams) detailsLayout.getLayoutParams();
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) overallLayout.getLayoutParams();
            params.weight = 2.0f + paramsDetails.weight;
            overallLayout.setLayoutParams(params);
            //overallLayout.setWeightSum(2 + weightSum);
            detailsLayout.setVisibility(viewTargetStatus);
        }
        return viewTargetStatus;
    }
    public void onGeneralSettingsClicked(View view){
        int viewStatus = toggleLayoutVisibility(generalSettingsLayout,
                generalSettingsDetailLayout);
        if (viewStatus == View.VISIBLE){
            btnGeneralSettingsSummary.setText("");
        }
        if (viewStatus == View.GONE){
            int numberOfPeriods = AppSettings.NUMBER_OF_PERIODS.value;
            int peridDuration = AppSettings.PERIOD_DURATION.value;
            int breakMinor = AppSettings.BREAK_TIME_DURATION.value;
            int breakMayor = AppSettings.HALF_TIME_DURATION.value;

            String summary = "";
            for (int i = 1; i <= numberOfPeriods; i++){
                summary += peridDuration + "|";
                if (i < numberOfPeriods) {
                    int i1 = i % (numberOfPeriods / 2);
                    if (i1 == 0) {
                        summary += breakMayor + "|";
                    } else {
                        summary += breakMinor + "|";
                    }
                }
                else {
                    summary = summary.substring(0, summary.length()-1);
                }
            }
            summary += " Timeout: " + AppSettings.TIMEOUT_DURATION.value;
            btnGeneralSettingsSummary.setText(summary);
        }
    }
    public void onOffenceTimeSettingsClicked(View view){
        int viewStatus = toggleLayoutVisibility(offenceTimeSettingsLayout,
                offenceTimeSettingsDetailLayout);
        if (viewStatus == View.VISIBLE){
            btnOffenceTimeSettingsSummary.setText("");
        }
        if (viewStatus == View.GONE){
            int offenceTimeDurationMajor = AppSettings.OFFENCE_TIME_DURATION.value;
            int offenceTimeDurationMinor = AppSettings.OFFENCE_TIME_MINOR_DURATION.value;
            int exclusionTimeDuration = AppSettings.EXCLUSION_TIME_DURATION.value;

            String summary = "Offence: " + offenceTimeDurationMajor + "|" +
                    offenceTimeDurationMinor + " Exclusion: " + exclusionTimeDuration;
            btnOffenceTimeSettingsSummary.setText(summary);
        }
    }
    public void onRemoteSettingsClicked(View view){
        int viewStatus =  toggleLayoutVisibility(remoteSettingsLayout,
                remoteSettingsDetailLayout);
        if (viewStatus == View.VISIBLE){
            btnRemoteSettingsSummary.setText("");
        }
        if (viewStatus == View.GONE){
            boolean useAutodiscovery = AppSettings.USE_AUTODISCOVERY.value;
            String masterIP = AppSettings.MASTER_IP.value;

            String summary = "";
            if (useAutodiscovery){
                summary = "Autodiscovery enabled";
            }
            else {
                summary = "Master-IP:" + masterIP;
            }
            btnRemoteSettingsSummary.setText(summary);
        }
    }

    public void onNbOfPeriodsClicked(View view){
        ParameterDialogInteger dialog = new ParameterDialogInteger(AppSettings.NUMBER_OF_PERIODS);
        dialog.show(getSupportFragmentManager(), "");
    }
    public void onTimePerPeriodClicked(View view){
        ParameterDialogInteger dialog = new ParameterDialogInteger(AppSettings.PERIOD_DURATION);
        dialog.show(getSupportFragmentManager(), "");
    }
    public void onHalftimeDurationClicked(View view){
        ParameterDialogInteger dialog = new ParameterDialogInteger(AppSettings.HALF_TIME_DURATION);
        dialog.show(getSupportFragmentManager(), "");
    }
    public void onBreakDurationClicked(View view){
        ParameterDialogInteger dialog = new ParameterDialogInteger(AppSettings.BREAK_TIME_DURATION);
        dialog.show(getSupportFragmentManager(), "");
    }
    public void onTimeoutDurationClicked(View view){
        ParameterDialogInteger dialog = new ParameterDialogInteger(AppSettings.TIMEOUT_DURATION);
        dialog.show(getSupportFragmentManager(), "");
    }
    public void onWarningPriorToTimeoutEndClicked(View view){
        ParameterDialogInteger dialog = new ParameterDialogInteger(AppSettings.TIMEOUT_END_WARNING);
        dialog.show(getSupportFragmentManager(), "");
    }
    public void onOffenceTimeDurationClicked(View view){
        ParameterDialogInteger dialog = new ParameterDialogInteger(AppSettings.OFFENCE_TIME_DURATION);
        dialog.show(getSupportFragmentManager(), "");
    }
    public void onOffenceTimeMinorDurationClicked(View view){
        ParameterDialogInteger dialog = new ParameterDialogInteger(AppSettings.OFFENCE_TIME_MINOR_DURATION);
        dialog.show(getSupportFragmentManager(), "");
    }
    public void onOffenceTimeMinorDurationResetClicked(View view){
        BooleanSetting setting = AppSettings.OFFENCE_TIME_MINOR_DURATION_RESET;
        setting.applyValue(AppSettings.getSharedPreferences(
                getApplicationContext()), !setting.value);
        updateSettingsValueDisplay();
    }
    public void onDecoupleTimersClicked(View view){
        BooleanSetting setting = AppSettings.DECOUPLE_TIMERS;
        setting.applyValue(AppSettings.getSharedPreferences(
                getApplicationContext()), !setting.value);
        updateSettingsValueDisplay();
    }
    public void onExclusionTimeDurationClicked(View view){
        ParameterDialogInteger dialog = new ParameterDialogInteger(AppSettings.EXCLUSION_TIME_DURATION);
        dialog.show(getSupportFragmentManager(), "");
    }
    public void onEnableSounClicked(View view){
        BooleanSetting setting = AppSettings.ENABLE_SOUND;
        setting.applyValue(AppSettings.getSharedPreferences(
                getApplicationContext()), !setting.value);
        updateSettingsValueDisplay();
    }
    public void onEnableDecimalClicked(View view){
        BooleanSetting setting = AppSettings.ENABLE_DECIMAL;
        setting.applyValue(AppSettings.getSharedPreferences(
                getApplicationContext()), !setting.value);
        updateSettingsValueDisplay();
    }
    public void onEnableDecimalDuringLastClicked(View view){
        BooleanSetting setting = AppSettings.ENABLE_DECIMAL_DURING_LAST;
        setting.applyValue(AppSettings.getSharedPreferences(
                getApplicationContext()), !setting.value);
        updateSettingsValueDisplay();
    }
    public void onResetShotClockClicked(View view){
        BooleanSetting setting = AppSettings.RESET_SHOTCLOCK_ON_GOAL;
        setting.applyValue(AppSettings.getSharedPreferences(
                getApplicationContext()), !setting.value);
        updateSettingsValueDisplay();
    }
    public void onAutodiscoveryClicked(View view){
        BooleanSetting setting = AppSettings.USE_AUTODISCOVERY;
        setting.applyValue(AppSettings.getSharedPreferences(
                getApplicationContext()), !setting.value);
        updateSettingsValueDisplay();
    }
    public void onEnablePauseDuringBreakClicked(View view){
        BooleanSetting setting = AppSettings.STOP_BREAK_AND_TIMEOUT;
        setting.applyValue(AppSettings.getSharedPreferences(
                getApplicationContext()), !setting.value);
        updateSettingsValueDisplay();
    }
    public void onAlwaysUseDoubleDigitsForGoalsClicked(View view){
        BooleanSetting setting = AppSettings.ALWASY_USE_DOUBLE_DIGITS_FOR_GOALS;
        setting.applyValue(AppSettings.getSharedPreferences(
                getApplicationContext()), !setting.value);
        updateSettingsValueDisplay();
    }
    public void onMasterIPClicked(View view){
        ParameterDialogString dialog = new ParameterDialogString(AppSettings.MASTER_IP);
        dialog.show(getSupportFragmentManager(), "");
    }
    public void onDoneClicked(View view){
        finish();
    }

    @Override
    public void applyValue(IntegerSetting setting, int value) {
        setting.applyValue(AppSettings.getSharedPreferences(
                getApplicationContext()), value);
        updateSettingsValueDisplay();
    }

    @Override
    public void applyValue(ColorSetting setting, int value) {
        setting.applyValue(AppSettings.getSharedPreferences(
                getApplicationContext()), value);
        updateSettingsValueDisplay();
    }

    @Override
    public void applyValue(StringSetting setting, String value) {
        setting.applyValue(AppSettings.getSharedPreferences(
                getApplicationContext()), value);
        updateSettingsValueDisplay();
    }

}
