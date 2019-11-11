package de.wasserball.wabaclock.main;

import java.io.IOException;

import de.wasserball.wabaclock.settings.WaterpoloTimerSettings;
import msg.OpenIGTMessage;
import msg.sensor.SI_EXP;
import msg.sensor.SI_UNIT;
import msg.sensor.SensorData;
import msg.sensor.SensorMessage;
import msg.sensor.Unit;
import msg.string.StringMessage;
import network.OpenIGTLinkClient;

public class ClientViewClient extends OpenIGTLinkClient {

    private NetworkBoard activity;

    public ClientViewClient(NetworkBoard activity) throws IOException {
        super(WaterpoloTimerSettings.MASTER_IP.value, WaterpoloclockServer.SERVER_PORT);
        this.activity = activity;
    }

    @Override
    public void messageReceived(OpenIGTMessage message) {
        log.debug("Message received: " + message.toString());

        /* check message data type and do something with the message */
        if (message instanceof SensorMessage) {
            //Log.info("Message received: " + message.toString());
            onRxSensor(message.getDeviceName(), ((SensorMessage)message).getSensorData());
        }
        if (message instanceof StringMessage) {
            //Log.info("Message received: " + message.toString());
            onRxString(message.getDeviceName(), ((StringMessage)message).getMessage());
        }
    }

    @Override
    public String[] getCapability() {
        return new String[]{SensorMessage.DATA_TYPE, StringMessage.DATA_TYPE};
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
            if (deviceName.equals(WaterpoloTimer.SHOTCLOCK_DEVICE_NAME)) {
                long time_ms = 0;
                data = sensorData.getArray();
                Unit unit = sensorData.getUnit();
                if (data.length == 1 && unit.equals(new Unit(SI_UNIT.BASE_SECOND, SI_EXP.MINUS3)))
                    time_ms = Double.valueOf(data[0]).longValue();
                if (data.length == 1 && unit.equals(new Unit(SI_UNIT.BASE_SECOND, SI_EXP.PLUS0)))
                    time_ms = Double.valueOf(data[0] * 1000).longValue();
                activity.setShotClock(time_ms);
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
