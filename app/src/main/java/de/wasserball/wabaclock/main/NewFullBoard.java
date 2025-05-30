package de.wasserball.wabaclock.main;

import android.widget.Button;

import de.wasserball.wabaclock.R;
import de.wasserball.wabaclock.settings.AppSettings;
import msg.sensor.GetSensorMessage;
import msg.string.GetStringMessage;

public class NewFullBoard extends NetworkBoard {

    String homeTeam = "home";
    String homeTeamColor = "";
    String guestTeam = "guest";
    String guestTeamColor = "";
    private Button btnTeamHome;
    private Button btnTeamGuest;

    long time_ms = 0;
    long shotClock = 0;
    int goalsHome = 0;
    int goalsGuest = 0;

    private Button mainTime;
    private Button btnShotclock;
    private Button btnGoalsHome;
    private Button btnGoalsGuest;

    @Override
    protected void defineContentView() {
        setContentView(R.layout.new_full_board);

        btnTeamHome = findViewById(R.id.btnTeamHome);
        btnTeamGuest = findViewById(R.id.btnTeamGuest);

        mainTime = findViewById(R.id.mainTimeBoard);
        btnShotclock = findViewById(R.id.shotclock_angriffszeit2);
        btnGoalsHome = findViewById(R.id.toreHeimBoard);
        btnGoalsGuest = findViewById(R.id.toreGastBoard);

        overlayForNavigationBar = findViewById(R.id.newfullBoardLayout);
    }

    @Override
    protected void updateData() {
        sendIfConnected(new GetSensorMessage(WaterPoloTimer.MAIN_TIME_DEVICE_NAME));
        sendIfConnected(new GetSensorMessage(WaterPoloTimer.SHOT_CLOCK_DEVICE_NAME));
        sendIfConnected(new GetSensorMessage(WaterPoloTimer.SCOREBOARD_DEVICE_NAME));
    }

    @Override
    protected void updateDataSlow() {
        sendIfConnected(new GetStringMessage(WaterPoloTimer.HOME_TEAM_DEVICE_NAME));
        sendIfConnected(new GetStringMessage(WaterPoloTimer.GUEST_TEAM_DEVICE_NAME));
    }


    @Override
    protected void updateGuiElements() {
        if (mainTime != null && btnGoalsHome != null && btnGoalsGuest != null) {
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

            String timeString;
            if (AppSettings.ENABLE_DECIMAL.value)
                timeString  = WaterPoloTimer.getMainTimeString(time_ms);
            else{
                if(AppSettings.ENABLE_DECIMAL_DURING_LAST.value)
                    timeString =  WaterPoloTimer.getMainTimeStringDecimalDuringLast(time_ms);
                else
                    timeString  = WaterPoloTimer.getMainTimeStringNoDecimal(time_ms);
            }
            mainTime.setText(timeString);
            String shotClockString;
            if (AppSettings.ENABLE_DECIMAL.value)
                shotClockString  = WaterPoloTimer.getOffenceTimeString(shotClock);
            else{
                if(AppSettings.ENABLE_DECIMAL_DURING_LAST.value)
                    shotClockString =  WaterPoloTimer.getOffenceTimeStringDecimalDuringLast(shotClock);
                else
                    shotClockString  = WaterPoloTimer.getOffenceTimeStringNoDecimal(shotClock);
            }
            btnShotclock.setText(shotClockString);

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

    void setMainTime(long time_ms){
        this.time_ms = time_ms;
    }

    void setShotClock(long time_ms){
        shotClock = time_ms;
    }

    void setScore(int home, int guest){
        goalsHome = home;
        goalsGuest = guest;
    }

}
