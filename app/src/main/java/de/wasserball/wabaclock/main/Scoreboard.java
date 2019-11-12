package de.wasserball.wabaclock.main;

import android.widget.Button;

import de.wasserball.wabaclock.R;
import msg.sensor.GetSensorMessage;
import msg.string.GetStringMessage;

public class Scoreboard extends NetworkBoard {

    String homeTeam = "home";
    String guestTeam = "guest";
    private Button btnTeamHome;
    private Button btnTeamGuest;

    int goalsHome = 0;
    int goalsGuest = 0;

    private Button btnGoalsHome;
    private Button btnGoalsGuest;

    @Override
    protected void defineContentView() {
        setContentView(R.layout.scoreboard);

        btnTeamHome = findViewById(R.id.btnTeamHome);
        btnTeamGuest = findViewById(R.id.btnTeamGuest);

        btnGoalsHome = findViewById(R.id.toreHeimScoreboard);
        btnGoalsGuest = findViewById(R.id.toreGastScoreboard);
    }

    @Override
    protected void updateData() {
        sendIfConnected(new GetSensorMessage(WaterPoloTimer.SCOREBOARD_DEVICE_NAME));
    }

    @Override
    protected void updateDataSlow() {
        sendIfConnected(new GetStringMessage(WaterPoloTimer.HOME_TEAM_DEVICE_NAME));
        sendIfConnected(new GetStringMessage(WaterPoloTimer.GUEST_TEAM_DEVICE_NAME));
    }


    @Override
    protected void updateGuiElements() {
        if (btnGoalsGuest != null && btnGoalsHome != null) {
            btnTeamHome.setText(homeTeam);
            btnTeamGuest.setText(guestTeam);
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

    void setScore(int home, int guest){
        goalsHome = home;
        goalsGuest = guest;
    }

}
