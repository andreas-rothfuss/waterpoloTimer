package de.tvdarmsheim.wabaclock.main;

import java.io.IOException;

import msg.sensor.SI_EXP;
import msg.sensor.SI_UNIT;
import msg.sensor.SensorData;
import msg.sensor.Unit;

public class TimeAndScoreboardClient extends ClientViewClient {

    private TimeAndScoreBoard activity;

    public TimeAndScoreboardClient(TimeAndScoreBoard activity) throws IOException {
        super();
        this.activity = activity;
    }

    public void onRxSensor(String deviceName, SensorData sensorData) {
        if (sensorData != null){
            double[] data;
            if (deviceName.equals(WaterpoloTimer.MAIN_TIME_DEVICE_NAME)) {
                long time_ms = 0;
                data = sensorData.getArray();
                Unit unit = sensorData.getUnit();
                if (data.length == 1 && unit.equals(new Unit(SI_UNIT.BASE_SECOND, SI_EXP.MINUS3)))
                    time_ms = Double.valueOf(data[0]).longValue();
                if (data.length == 1 && unit.equals(new Unit(SI_UNIT.BASE_SECOND, SI_EXP.PLUS0)))
                    time_ms = Double.valueOf(data[0] * 1000).longValue();
                activity.setMainTime(time_ms);
            }
            if (deviceName.equals(WaterpoloTimer.SCOREBOARD_DEVICE_NAME)) {
                int goalsHome = 0;
                int goalsGuest = 0;
                data = sensorData.getArray();
                Unit unit = sensorData.getUnit();
                if (data.length == 2) {
                    goalsHome = Double.valueOf(data[0]).intValue();
                    goalsGuest = Double.valueOf(data[1]).intValue();
                }
                activity.setScore(goalsHome, goalsGuest);
            }
        }
    }

    protected void onRxString(String deviceName, String data){
        if (data != null){
            if (deviceName.equals(WaterpoloTimer.HOME_TEAM_DEVICE_NAME)) {
                activity.setHomeTeamName(data);
            }
            if (deviceName.equals(WaterpoloTimer.GUEST_TEAM_DEVICE_NAME)) {
                activity.setGuestTeamName(data);
            }
        }
    }

}
