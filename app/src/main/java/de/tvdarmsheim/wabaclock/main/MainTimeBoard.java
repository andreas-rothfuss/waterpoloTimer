package de.tvdarmsheim.wabaclock.main;

import android.widget.Button;

import java.io.IOException;

import de.tvdarmsheim.wabaclock.R;
import de.tvdarmsheim.wabaclock.settings.WaterpoloTimerSettings;
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
    protected ClientViewClient initClient() throws IOException {
        MainTimeClient client = new MainTimeClient(this);
        client.start();
        return client;
    }

    @Override
    protected void updateData() {
        sendIfConnected(new GetSensorMessage(WaterpoloTimer.MAIN_TIME_DEVICE_NAME));
        sendIfConnected(new GetSensorMessage(WaterpoloTimer.SCOREBOARD_DEVICE_NAME));
    }

    @Override
    protected void updateGuiElements() {
        if (mainTime != null){
            String timeString;
            if (WaterpoloTimerSettings.ENABLE_DECIMAL.value)
                timeString  = WaterpoloTimer.getMainTimeString(time_ms);
            else
                timeString  = WaterpoloTimer.getMainTimeStringNoDecimal(time_ms);
            mainTime.setText(timeString);
        }
    }

    void setMainTime(long time_ms){
        this.time_ms = time_ms;
    }

}
