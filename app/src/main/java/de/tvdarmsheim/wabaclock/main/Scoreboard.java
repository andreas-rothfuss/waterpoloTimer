package de.tvdarmsheim.wabaclock.main;

import android.widget.Button;

import java.io.IOException;

import de.tvdarmsheim.wabaclock.R;
import msg.sensor.GetSensorMessage;

public class Scoreboard extends NetworkBoard {

    int goalsHome = 0;
    int goalsGuest = 0;

    private Button btnGoalsHome;
    private Button btnGoalsGuest;

    @Override
    protected void defineContentView() {
        setContentView(R.layout.scoreboard);

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
    protected void updateGuiElements() {
        if (btnGoalsGuest != null && btnGoalsHome != null) {
            btnGoalsHome.setText(WaterpoloTimer.getGoalsString(goalsHome));
            btnGoalsGuest.setText(WaterpoloTimer.getGoalsString(goalsGuest));
        }
    }

    void setScore(int home, int guest){
        goalsHome = home;
        goalsGuest = guest;
    }

}
