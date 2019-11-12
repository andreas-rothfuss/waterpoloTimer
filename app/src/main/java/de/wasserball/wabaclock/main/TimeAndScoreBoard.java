package de.wasserball.wabaclock.main;

import android.widget.Button;

import de.wasserball.wabaclock.R;
import de.wasserball.wabaclock.settings.AppSettings;
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
        sendIfConnected(new GetSensorMessage(WaterPoloTimer.MAIN_TIME_DEVICE_NAME));
        sendIfConnected(new GetSensorMessage(WaterPoloTimer.SCOREBOARD_DEVICE_NAME));
    }

    @Override
    protected void updateDataSlow() {
        sendIfConnected(new GetStringMessage(WaterPoloTimer.HOME_TEAM_DEVICE_NAME));
        sendIfConnected(new GetStringMessage(WaterPoloTimer.GUEST_TEAM_DEVICE_NAME));
    }

    @Override
    protected void updateGuiElements() {
        if (mainTime != null && btnGoalsHome != null && btnGoalsHome != null) {
            btnTeamHome.setText(homeTeam);
            btnTeamGuest.setText(guestTeam);
            String timeString;
            if (AppSettings.ENABLE_DECIMAL.value)
                timeString  = WaterPoloTimer.getMainTimeString(time_ms);
            else
                timeString  = WaterPoloTimer.getMainTimeStringNoDecimal(time_ms);
            mainTime.setText(timeString);
            btnGoalsHome.setText(WaterPoloTimer.getGoalsString(goalsHome));
            btnGoalsGuest.setText(WaterPoloTimer.getGoalsString(goalsGuest));
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
