package de.wasserball.wabaclock.main;

import android.view.View;
import android.widget.Button;

import java.io.IOException;

import de.wasserball.wabaclock.R;
import de.wasserball.wabaclock.settings.AppSettings;
import msg.command.CommandMessage;
import msg.sensor.GetSensorMessage;

public class ShotclockRemoteControl extends NetworkBoard {

    long shotClock = 0;

    private Button btnShotclock;
    private Button btnResetMajor;
    private Button btnResetMinor;
    private Button btnResetExclusion;

    @Override
    protected void defineContentView() {
        setContentView(R.layout.shotclock_remote_control);

        btnShotclock = findViewById(R.id.shotclock_zn2);

        btnResetMajor = findViewById(R.id.resetMajorZN2);
        btnResetMinor = findViewById(R.id.resetMinorZN2);
        btnResetExclusion = findViewById(R.id.resetExclusionZN2);
    }

    @Override
    protected void updateData() {
        sendIfConnected(new GetSensorMessage(WaterPoloTimer.SHOT_CLOCK_DEVICE_NAME));
    }

    @Override
    protected void updateGuiElements() {
        if (btnShotclock != null) {
            String timeString;
            if (AppSettings.ENABLE_DECIMAL.value)
                timeString  = WaterPoloTimer.getOffenceTimeString(shotClock);
            else{
                if(AppSettings.ENABLE_DECIMAL_DURING_LAST.value)
                    timeString =  WaterPoloTimer.getOffenceTimeStringDecimalDuringLast(shotClock);
                else
                    timeString  = WaterPoloTimer.getOffenceTimeStringNoDecimal(shotClock);
            }
            btnShotclock.setText(timeString);
        }

        btnResetMajor.setText(String.format(getString(R.string.reset_placeholder), AppSettings.OFFENCE_TIME_DURATION.value));
        btnResetMinor.setText(String.format(getString(R.string.reset_placeholder), AppSettings.OFFENCE_TIME_MINOR_DURATION.value));
        boolean offenceTimeMinorReset = AppSettings.OFFENCE_TIME_MINOR_DURATION_RESET.value;
        if (offenceTimeMinorReset)
            btnResetExclusion.setVisibility(View.GONE);
        else {
            boolean offenceTimeMinorResetExclusion = AppSettings.OFFENCE_TIME_MINOR_DURATION_RESET_EXCLUSION.value;
            btnResetExclusion.setVisibility(offenceTimeMinorResetExclusion ? View.VISIBLE : View.GONE);
        }
    }

    void setShotClock(long time_ms){
        shotClock = time_ms;
    }

    public void startStopShotclock(View view){
        if (client != null){
            try{
                client.sendIfConnected(new CommandMessage("Shotclock", 1,
                        "START_STOP_SHOTCLOCK", ""));
            }catch (IOException e){}
        }
    }

    public void resetShotclockMajor(View view){
        if (client != null){
            try{
                client.sendIfConnected(new CommandMessage("Shotclock", 2,
                        "RESET_SHOTCLOCK_MAJOR", ""));
            }catch (IOException e){}
        }
    }

    public void resetShotclockMinor(View view){
        if (client != null){
            try{
                client.sendIfConnected(new CommandMessage("Shotclock", 3,
                        "RESET_SHOTCLOCK_MINOR", ""));
            }catch (IOException e){}
        }
    }
    public void resetShotclockMinorExclusion(View view){
        if (client != null){
            try{
                client.sendIfConnected(new CommandMessage("Shotclock", 4,
                        "RESET_SHOTCLOCK_MINOR_EXCLUSION", ""));
            }catch (IOException e){}
        }
    }
}
