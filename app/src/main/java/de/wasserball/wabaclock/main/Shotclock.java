package de.wasserball.wabaclock.main;

import android.view.View;
import android.widget.Button;

import java.io.IOException;

import de.wasserball.wabaclock.R;
import de.wasserball.wabaclock.settings.AppSettings;
import msg.command.CommandMessage;
import msg.sensor.GetSensorMessage;

public class Shotclock extends NetworkBoard {

    long shotClock = 0;

    private Button btnShotclock;

    @Override
    protected void defineContentView() {
        setContentView(R.layout.shotclock);

        btnShotclock = findViewById(R.id.shotclock_angriffszeit);

        overlayForNavigationBar = findViewById(R.id.shotclock_angriffszeit);
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
    }

    void setShotClock(long time_ms){
        shotClock = time_ms;
    }
}
