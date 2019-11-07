package de.tvdarmsheim.wabaclock.settings;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

import de.tvdarmsheim.wabaclock.R;

public class Settings extends AppCompatActivity implements ParameterDialogInteger.DialogListener,
        ParameterDialogString.DialogListener {

    Button btnNbOfPeriodsVal;
    Button btnPeriodVal;
    Button btnHalftimeVal;
    Button btnBreaktimeVal;
    Button btnTimeoutVal;
    Button btnTimeoutWarningVal;
    Button btnOffenceTimeMajorVal;
    Button btnOffenceTimeMinorVal;
    Switch btnSoundEnabledVal;
    Switch btnDecimalEnabledVal;
    Button btnMasterIPVal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        btnNbOfPeriodsVal = findViewById(R.id.textViewNbOfPeriodsValue);
        btnPeriodVal = findViewById(R.id.textViewTimePerPeriodValue);
        btnHalftimeVal = findViewById(R.id.textViewHalfTimeValue);
        btnBreaktimeVal = findViewById(R.id.textViewBreakTimeValue);
        btnTimeoutVal = findViewById(R.id.textViewTimeoutValue);
        btnTimeoutWarningVal = findViewById(R.id.textViewTimeoutEndWarningValue);
        btnOffenceTimeMajorVal = findViewById(R.id.textViewOffenceTimeMajorValue);
        btnOffenceTimeMinorVal = findViewById(R.id.textViewOffennceTimeMinorValue);
        btnDecimalEnabledVal = findViewById(R.id.textViewDecimalEnabledValue);
        btnSoundEnabledVal = findViewById(R.id.textViewSoundEnabledValue);
        btnMasterIPVal = findViewById(R.id.textViewEditIP);

        updateSettingsValueDisplay();
    }

    void updateSettingsValueDisplay() {
        btnNbOfPeriodsVal.setText(Integer.toString(WaterpoloTimerSettings.NUMBER_OF_PERIODS.value));
        btnPeriodVal.setText(Integer.toString(WaterpoloTimerSettings.PERIOD_DURATION.value));
        btnHalftimeVal.setText(Integer.toString(WaterpoloTimerSettings.HALF_TIME_DURATION.value));
        btnBreaktimeVal.setText(Integer.toString(WaterpoloTimerSettings.BREAK_TIME_DURATION.value));
        btnTimeoutVal.setText(Integer.toString(WaterpoloTimerSettings.TIMEOUT_DURATION.value));
        btnTimeoutWarningVal.setText(Integer.toString(WaterpoloTimerSettings.TIMEOUT_END_WARNING.value));
        btnOffenceTimeMajorVal.setText(Integer.toString(WaterpoloTimerSettings.OFFENCE_TIME_DURATION.value));
        btnOffenceTimeMinorVal.setText(Integer.toString(WaterpoloTimerSettings.OFFENCE_TIME_MINOR_DURATION.value));
        btnSoundEnabledVal.setChecked(WaterpoloTimerSettings.ENABLE_SOUND.value);
        btnDecimalEnabledVal.setChecked(WaterpoloTimerSettings.ENABLE_DECIMAL.value);
        btnMasterIPVal.setText(WaterpoloTimerSettings.MASTER_IP.value);
    }

    public void onNbOfPeriodsClicked(View view){
        ParameterDialogInteger dialog = new ParameterDialogInteger(WaterpoloTimerSettings.NUMBER_OF_PERIODS);
        dialog.show(getSupportFragmentManager(), "");
    }
    public void onTimeperPeriodClicked(View view){
        ParameterDialogInteger dialog = new ParameterDialogInteger(WaterpoloTimerSettings.PERIOD_DURATION);
        dialog.show(getSupportFragmentManager(), "");
    }
    public void onHalftimeDurationClicked(View view){
        ParameterDialogInteger dialog = new ParameterDialogInteger(WaterpoloTimerSettings.HALF_TIME_DURATION);
        dialog.show(getSupportFragmentManager(), "");
    }
    public void onBreakDurationClicked(View view){
        ParameterDialogInteger dialog = new ParameterDialogInteger(WaterpoloTimerSettings.BREAK_TIME_DURATION);
        dialog.show(getSupportFragmentManager(), "");
    }
    public void onTimeoutDurationClicked(View view){
        ParameterDialogInteger dialog = new ParameterDialogInteger(WaterpoloTimerSettings.TIMEOUT_DURATION);
        dialog.show(getSupportFragmentManager(), "");
    }
    public void onWarningPriorToTimeoutEndClicked(View view){
        ParameterDialogInteger dialog = new ParameterDialogInteger(WaterpoloTimerSettings.TIMEOUT_END_WARNING);
        dialog.show(getSupportFragmentManager(), "");
    }
    public void onOffenceTimeDurationClicked(View view){
        ParameterDialogInteger dialog = new ParameterDialogInteger(WaterpoloTimerSettings.OFFENCE_TIME_DURATION);
        dialog.show(getSupportFragmentManager(), "");
    }
    public void onOffenceTimeMinorDurationClicked(View view){
        ParameterDialogInteger dialog = new ParameterDialogInteger(WaterpoloTimerSettings.OFFENCE_TIME_MINOR_DURATION);
        dialog.show(getSupportFragmentManager(), "");
    }
    public void onEnableSounClicked(View view){
        BooleanSetting setting = WaterpoloTimerSettings.ENABLE_SOUND;
        setting.applyValue(WaterpoloTimerSettings.getSharedPreferences(
                getApplicationContext()), !setting.value);
        updateSettingsValueDisplay();
    }
    public void onEnableDecimalClicked(View view){
        BooleanSetting setting = WaterpoloTimerSettings.ENABLE_DECIMAL;
        setting.applyValue(WaterpoloTimerSettings.getSharedPreferences(
                getApplicationContext()), !setting.value);
        updateSettingsValueDisplay();
    }
    public void onMasterIPClicked(View view){
        ParameterDialogString dialog = new ParameterDialogString(WaterpoloTimerSettings.MASTER_IP);
        dialog.show(getSupportFragmentManager(), "");
    }
    public void onDoneClicked(View view){
        finish();
    }


    @Override
    public void applyValue(IntegerSetting setting, int value) {
        setting.applyValue(WaterpoloTimerSettings.getSharedPreferences(
                getApplicationContext()), value);
        updateSettingsValueDisplay();
    }

    @Override
    public void applyValue(StringSetting setting, String value) {
        setting.applyValue(WaterpoloTimerSettings.getSharedPreferences(
                getApplicationContext()), value);
        updateSettingsValueDisplay();
    }

}
