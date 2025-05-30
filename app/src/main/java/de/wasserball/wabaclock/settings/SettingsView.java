package de.wasserball.wabaclock.settings;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_view);

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
        btnSoundEnabledVal = findViewById(R.id.textViewSoundEnabledValue);
        btnShotclockResetEnabledVal = findViewById(R.id.switchShotResetEnabledValue);
        btnPauseDuringBreakVal = findViewById(R.id.switchPauseDuringBreakValue);
        btnUseAutodiscovery = findViewById(R.id.switchAutodiscoveryEnabledValue);
        btnMasterIPVal = findViewById(R.id.textViewEditIP);

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

    public void toggleLayoutVisibility(LinearLayout layout){
        if (layout.getVisibility() == View.VISIBLE){
            layout.setVisibility(View.GONE);
        }
        else {
            layout.setVisibility(View.VISIBLE);
        }
    }
    public void onGeneralSettingsClicked(View view){
        toggleLayoutVisibility(generalSettingsDetailLayout);
    }
    public void onOffenceTimeSettingsClicked(View view){
        toggleLayoutVisibility(offenceTimeSettingsDetailLayout);
    }
    public void onRemoteSettingsClicked(View view){
        toggleLayoutVisibility(remoteSettingsDetailLayout);
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
