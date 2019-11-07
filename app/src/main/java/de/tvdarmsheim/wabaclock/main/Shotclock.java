package de.tvdarmsheim.wabaclock.main;

import android.widget.Button;

import java.io.IOException;

import de.tvdarmsheim.wabaclock.R;
import de.tvdarmsheim.wabaclock.settings.WaterpoloTimerSettings;
import msg.sensor.GetSensorMessage;

public class Shotclock extends NetworkBoard {

    long shotClock = 0;

    private Button btnShotclock;

    @Override
    protected void defineContentView() {
        setContentView(R.layout.shotclock);

        btnShotclock = findViewById(R.id.shotclock_angriffszeit);
    }

    @Override
    protected ClientViewClient initClient() throws IOException {
        ShotclockClient client = new ShotclockClient(this);
        client.start();
        return client;
    }

    @Override
    protected void updateData() {
        sendIfConnected(new GetSensorMessage(WaterpoloTimer.SHOTCLOCK_DEVICE_NAME));
    }

    @Override
    protected void updateGuiElements() {
        if (btnShotclock != null) {
            String timeString;
            if (WaterpoloTimerSettings.ENABLE_DECIMAL.value)
                timeString  = WaterpoloTimer.getOffenceTimeString(shotClock);
            else
                timeString  = WaterpoloTimer.getOffenceTimeStringNoDecimal(shotClock);
            btnShotclock.setText(timeString);
        }
    }

    void setShotclock(long time_ms){
        shotClock = time_ms;
    }
}
