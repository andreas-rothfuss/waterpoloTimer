package de.tvdarmsheim.wabaclock.main;

import android.widget.Button;

import java.io.IOException;

import de.tvdarmsheim.wabaclock.R;
import msg.sensor.GetSensorMessage;
import msg.string.GetStringMessage;

public class Scoreboard extends NetworkBoard {

    String homeTeam;
    String guestTeam;

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
    protected ClientViewClient initClient() throws IOException {
        ScoreboardClient client = new ScoreboardClient(this);
        client.start();
        return client;
    }

    @Override
    protected void updateData() {
        sendIfConnected(new GetSensorMessage(WaterpoloTimer.SCOREBOARD_DEVICE_NAME));
    }

    @Override
    protected void updateDataSlow() {
        sendIfConnected(new GetStringMessage(WaterpoloTimer.HOME_TEAM_DEVICE_NAME));
        sendIfConnected(new GetStringMessage(WaterpoloTimer.GUEST_TEAM_DEVICE_NAME));
    }


    @Override
    protected void updateGuiElements() {
        if (btnGoalsGuest != null && btnGoalsHome != null) {
            btnTeamHome.setText(homeTeam);
            btnTeamGuest.setText(guestTeam);
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

    void setScore(int home, int guest){
        goalsHome = home;
        goalsGuest = guest;
    }

}
