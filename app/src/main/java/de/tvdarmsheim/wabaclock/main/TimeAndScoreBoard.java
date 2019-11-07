package de.tvdarmsheim.wabaclock.main;

import android.widget.Button;

import java.io.IOException;

import de.tvdarmsheim.wabaclock.R;
import de.tvdarmsheim.wabaclock.settings.WaterpoloTimerSettings;
import msg.sensor.GetSensorMessage;

public class TimeAndScoreBoard extends NetworkBoard {

    long time_ms = 0;
    int goalsHome = 0;
    int goalsGuest = 0;

    private Button mainTime;
    private Button btnGoalsHome;
    private Button btnGoalsGuest;

    @Override
    protected void defineContentView() {
        setContentView(R.layout.time_and_score_board);

        mainTime = findViewById(R.id.mainTimeBoard);
        btnGoalsHome = findViewById(R.id.toreHeimBoard);
        btnGoalsGuest = findViewById(R.id.toreGastBoard);
    }

    @Override
    protected ClientViewClient initClient() throws IOException {
        TimeAndScoreboardClient client = new TimeAndScoreboardClient(this);
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
        if (mainTime != null && btnGoalsHome != null && btnGoalsHome != null) {
            String timeString;
            if (WaterpoloTimerSettings.ENABLE_DECIMAL.value)
                timeString  = WaterpoloTimer.getMainTimeString(time_ms);
            else
                timeString  = WaterpoloTimer.getMainTimeStringNoDecimal(time_ms);
            mainTime.setText(timeString);
            btnGoalsHome.setText(WaterpoloTimer.getGoalsString(goalsHome));
            btnGoalsGuest.setText(WaterpoloTimer.getGoalsString(goalsGuest));
        }
    }

    void setMainTime(long time_ms){
        this.time_ms = time_ms;
    }

    void setScore(int home, int guest){
        goalsHome = home;
        goalsGuest = guest;
    }

}
