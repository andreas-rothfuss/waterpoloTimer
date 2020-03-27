package de.wasserball.wabaclock.main;

import java.io.IOException;

import de.wasserball.wabaclock.settings.AppSettings;
import msg.OIGTL_DataMessage;
import msg.OIGTL_GetMessage;
import msg.sensor.GetSensorMessage;
import msg.sensor.SI_EXP;
import msg.sensor.SI_UNIT;
import msg.sensor.SensorData;
import msg.sensor.SensorMessage;
import msg.sensor.Unit;
import msg.string.GetStringMessage;
import msg.string.StringMessage;
import network.stream.OpenIGTLinkStreamingServer;

public class WaterpoloclockServer extends OpenIGTLinkStreamingServer {

    public static final int SERVER_PORT = 30020;
    WaterPoloTimer timer;

    public WaterpoloclockServer(WaterPoloTimer timer) throws IOException {
        super(SERVER_PORT, null);
        this.timer = timer;
    }


    @Override
    public OIGTL_DataMessage getMessageReceived(OIGTL_GetMessage message) {
        log.debug("Get Message received: " + message.toString());

        /* check message data type and do something with the message */
        if (message instanceof GetSensorMessage) {
            //Log.info("Message received: " + message.toString());
            return onTxSensor(message.getDeviceName());
        }
        if (message instanceof GetStringMessage) {
            //Log.info("Message received: " + message.toString());
            return onTxString(message.getDeviceName());
        }
        return null;
    }

    private SensorMessage onTxSensor(String deviceName) {
        if (timer != null) {
            SensorData sensorData = new SensorData();
            double[] data;
            if (deviceName.equals(WaterPoloTimer.SHOT_CLOCK_DEVICE_NAME)) {
                sensorData.setArray(new double[]{timer.offenceTime});
                sensorData.setUnit(new Unit(SI_UNIT.BASE_SECOND, SI_EXP.MINUS3));
                return new SensorMessage(deviceName, sensorData);
            }
            if (deviceName.equals(WaterPoloTimer.MAIN_TIME_DEVICE_NAME)) {
                if (timer.isTimeout())
                    sensorData.setArray(new double[]{timer.timeout});
                else
                    sensorData.setArray(new double[]{timer.mainTime});
                sensorData.setUnit(new Unit(SI_UNIT.BASE_SECOND, SI_EXP.MINUS3));
                return new SensorMessage(deviceName, sensorData);
            }
            if (deviceName.equals(WaterPoloTimer.SCOREBOARD_DEVICE_NAME)) {
                sensorData.setArray(new double[]{timer.goalsHome, timer.goalsGuest});
                return new SensorMessage(deviceName, sensorData);
            }
        }
        return null;
    }

    protected StringMessage onTxString(String deviceName){
        if (deviceName.equals(WaterPoloTimer.HOME_TEAM_DEVICE_NAME)) {
            return new StringMessage(deviceName, AppSettings.HOME_TEAM_NAME.value);
        }
        if (deviceName.equals(WaterPoloTimer.GUEST_TEAM_DEVICE_NAME)) {
            return new StringMessage(deviceName, AppSettings.GUEST_TEAM_NAME.value);
        }
        return null;
    }

    @Override
    public String[] getCapability() {
        return new String[0];
    }
}
