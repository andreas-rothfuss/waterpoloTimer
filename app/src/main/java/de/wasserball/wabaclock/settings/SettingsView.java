package de.wasserball.wabaclock.settings;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

import de.wasserball.wabaclock.R;

public class SettingsView extends AppCompatActivity implements ParameterDialogInteger.DialogListener,
        ParameterDialogString.DialogListener {

    Button btnNbOfPeriodsVal;
    Button btnPeriodVal;
    Button btnHalftimeVal;
    Button btnBreaktimeVal;
    Button btnTimeoutVal;
    Button btnTimeoutWarningVal;
    Button btnOffenceTimeMajorVal;
    Button btnOffenceTimeMinorVal;
    Button btnExclusionTimeDuration;
    Switch btnSoundEnabledVal;
    Switch btnDecimalEnabledVal;
    Switch btnDecimalEnabledDuringLastMinuteVal;
    Switch btnShotclockResetEnabledVal;
    Switch btnPauseDuringBreakVal;
    Button btnMasterIPVal;


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
        btnExclusionTimeDuration = findViewById(R.id.textViewExclusionTimeValue);
        btnDecimalEnabledVal = findViewById(R.id.btnViewDecimalEnabledValue);
        btnDecimalEnabledDuringLastMinuteVal = findViewById(R.id.btnDecimalEnabledDuringLastMinuteValue);
        btnSoundEnabledVal = findViewById(R.id.textViewSoundEnabledValue);
        btnShotclockResetEnabledVal = findViewById(R.id.switchShotResetEnabledValue);
        btnPauseDuringBreakVal = findViewById(R.id.switchPauseDuringBreakValue);
        btnMasterIPVal = findViewById(R.id.textViewEditIP);

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
        btnExclusionTimeDuration.setText(Integer.toString(AppSettings.EXCLUSION_TIME_DURATION.value));
        btnSoundEnabledVal.setChecked(AppSettings.ENABLE_SOUND.value);
        btnDecimalEnabledVal.setChecked(AppSettings.ENABLE_DECIMAL.value);
        btnDecimalEnabledDuringLastMinuteVal.setChecked(AppSettings.ENABLE_DECIMAL_DURING_LAST.value);
        btnShotclockResetEnabledVal.setChecked(AppSettings.RESET_SHOTCLOCK_ON_GOAL.value);
        btnPauseDuringBreakVal.setChecked(AppSettings.STOP_BREAK_AND_TIMEOUT.value);
        btnMasterIPVal.setText(AppSettings.MASTER_IP.value);
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
    public void applyValue(StringSetting setting, String value) {
        setting.applyValue(AppSettings.getSharedPreferences(
                getApplicationContext()), value);
        updateSettingsValueDisplay();
    }

}
