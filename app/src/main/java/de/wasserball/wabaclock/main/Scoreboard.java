package de.wasserball.wabaclock.main;

import android.widget.Button;

import de.wasserball.wabaclock.R;
import de.wasserball.wabaclock.settings.AppSettings;
import msg.sensor.GetSensorMessage;
import msg.string.GetStringMessage;

public class Scoreboard extends NetworkBoard {

    String homeTeam = "home";
    String homeTeamColor = "";
    String guestTeam = "guest";
    String guestTeamColor = "";
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

        overlayForNavigationBar = findViewById(R.id.scoreBoardLayout);
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
            int homeTeamColor = AppSettings.HOME_TEAM_COLOR.value;
            try{
                homeTeamColor = Integer.valueOf(this.homeTeamColor).intValue();
            }
            catch (NumberFormatException e){}
            int guestTeamColor = AppSettings.GUEST_TEAM_COLOR.value;
            try{
                guestTeamColor = Integer.valueOf(this.guestTeamColor).intValue();
            }
            catch (NumberFormatException e){}

            btnTeamHome.setText(homeTeam);
            btnTeamHome.setTextColor(homeTeamColor);
            btnTeamGuest.setText(guestTeam);
            btnTeamGuest.setTextColor(guestTeamColor);

            btnGoalsHome.setText(WaterPoloTimer.getGoalsString(goalsHome));
            btnGoalsHome.setTextColor(homeTeamColor);
            btnGoalsGuest.setText(WaterPoloTimer.getGoalsString(goalsGuest));
            btnGoalsGuest.setTextColor(guestTeamColor);
        }
    }

    void setHomeTeamName(String val){
        this.homeTeam = val;
    }
    void setHomeTeamColor(String colorARGBInt){
        this.homeTeamColor = colorARGBInt;
    }
    void setGuestTeamName(String val){
        this.guestTeam = val;
    }
    void setGuestTeamColor(String colorARGBInt){
        this.guestTeamColor = colorARGBInt;
    }

    void setScore(int home, int guest){
        goalsHome = home;
        goalsGuest = guest;
    }

}
