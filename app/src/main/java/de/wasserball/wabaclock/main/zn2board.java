package de.wasserball.wabaclock.main;

import android.view.View;
import android.widget.Button;

import java.io.IOException;

import de.wasserball.wabaclock.R;
import de.wasserball.wabaclock.settings.AppSettings;
import msg.command.CommandMessage;
import msg.sensor.GetSensorMessage;

public class zn2board extends NetworkBoard {

    long shotClock = 0;

    private Button btnShotclock;

    @Override
    protected void defineContentView() {
        setContentView(R.layout.zn2board);

        btnShotclock = findViewById(R.id.shotclock_zn2);

        overlayForNavigationBar = findViewById(R.id.layoutZN2);
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
            else
                timeString  = WaterPoloTimer.getOffenceTimeStringNoDecimal(shotClock);
            btnShotclock.setText(timeString);
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
        hideNavigationBar();
    }

    public void resetShotclockMajor(View view){
        if (client != null){
            try{
                client.sendIfConnected(new CommandMessage("Shotclock", 2,
                        "RESET_SHOTCLOCK_MAJOR", ""));
            }catch (IOException e){}
        }
        hideNavigationBar();
    }

    public void resetShotclockMinor(View view){
        if (client != null){
            try{
                client.sendIfConnected(new CommandMessage("Shotclock", 3,
                        "RESET_SHOTCLOCK_MINOR", ""));
            }catch (IOException e){}
        }
        hideNavigationBar();
    }
}
