package de.wasserball.wabaclock.main;

import android.widget.Button;

import de.tvdarmsheim.wabaclock.R;
import de.wasserball.wabaclock.settings.WaterpoloTimerSettings;
import msg.sensor.GetSensorMessage;
import msg.string.GetStringMessage;

public class TimeAndScoreBoard extends NetworkBoard {

    String homeTeam = "home";
    String guestTeam = "guest";
    long time_ms = 0;
    int goalsHome = 0;
    int goalsGuest = 0;

    private Button btnTeamHome;
    private Button btnTeamGuest;
    private Button mainTime;
    private Button btnGoalsHome;
    private Button btnGoalsGuest;

    @Override
    protected void defineContentView() {
        setContentView(R.layout.time_and_score_board);

        btnTeamHome = findViewById(R.id.btnTeamHome);
        btnTeamGuest = findViewById(R.id.btnTeamGuest);
        mainTime = findViewById(R.id.mainTimeBoard);
        btnGoalsHome = findViewById(R.id.toreHeimBoard);
        btnGoalsGuest = findViewById(R.id.toreGastBoard);
    }

    @Override
    protected void updateData() {
        sendIfConnected(new GetSensorMessage(WaterpoloTimer.MAIN_TIME_DEVICE_NAME));
        sendIfConnected(new GetSensorMessage(WaterpoloTimer.SCOREBOARD_DEVICE_NAME));
    }

    @Override
    protected void updateDataSlow() {
        sendIfConnected(new GetStringMessage(WaterpoloTimer.HOME_TEAM_DEVICE_NAME));
        sendIfConnected(new GetStringMessage(WaterpoloTimer.GUEST_TEAM_DEVICE_NAME));
    }

    @Override
    protected void updateGuiElements() {
        if (mainTime != null && btnGoalsHome != null && btnGoalsHome != null) {
            btnTeamHome.setText(homeTeam);
            btnTeamGuest.setText(guestTeam);
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

    void setHomeTeamName(String val){
        this.homeTeam = val;
    }
    void setGuestTeamName(String val){
        this.guestTeam = val;
    }

    void setMainTime(long time_ms){
        this.time_ms = time_ms;
    }

    void setScore(int home, int guest){
        goalsHome = home;
        goalsGuest = guest;
    }

}
