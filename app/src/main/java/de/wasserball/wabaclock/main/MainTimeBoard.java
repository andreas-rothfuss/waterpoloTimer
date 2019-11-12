package de.wasserball.wabaclock.main;

import android.widget.Button;

import de.wasserball.wabaclock.R;
import de.wasserball.wabaclock.settings.AppSettings;
import msg.sensor.GetSensorMessage;

public class MainTimeBoard extends NetworkBoard {

    long time_ms = 0;

    private Button mainTime;

    @Override
    protected void defineContentView() {
        setContentView(R.layout.main_time_board);

        mainTime = findViewById(R.id.mainTimeBoard);
    }

    @Override
    protected void updateData() {
        sendIfConnected(new GetSensorMessage(WaterPoloTimer.MAIN_TIME_DEVICE_NAME));
        sendIfConnected(new GetSensorMessage(WaterPoloTimer.SCOREBOARD_DEVICE_NAME));
    }

    @Override
    protected void updateGuiElements() {
        if (mainTime != null){
            String timeString;
            if (AppSettings.ENABLE_DECIMAL.value)
                timeString  = WaterPoloTimer.getMainTimeString(time_ms);
            else
                timeString  = WaterPoloTimer.getMainTimeStringNoDecimal(time_ms);
            mainTime.setText(timeString);
        }
    }

    void setMainTime(long time_ms){
        this.time_ms = time_ms;
    }

}
