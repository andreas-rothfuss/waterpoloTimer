package de.tvdarmsheim.wabaclock.main;

import java.io.IOException;

import msg.sensor.SI_EXP;
import msg.sensor.SI_UNIT;
import msg.sensor.SensorData;
import msg.sensor.Unit;

public class ShotclockClient extends ClientViewClient {

    private Shotclock activity;

    public ShotclockClient(Shotclock activity) throws IOException {
        super();
        this.activity = activity;
    }

    public void onRxSensor(String deviceName, SensorData sensorData) {
        if (sensorData != null){
            double[] data;
            if (deviceName.equals(WaterpoloTimer.SHOTCLOCK_DEVICE_NAME)) {
                long time_ms = 0;
                data = sensorData.getArray();
                Unit unit = sensorData.getUnit();
                if (data.length == 1 && unit.equals(new Unit(SI_UNIT.BASE_SECOND, SI_EXP.MINUS3)))
                    time_ms = Double.valueOf(data[0]).longValue();
                if (data.length == 1 && unit.equals(new Unit(SI_UNIT.BASE_SECOND, SI_EXP.PLUS0)))
                    time_ms = Double.valueOf(data[0] * 1000).longValue();
                activity.setShotclock(time_ms);
            }
        }
    }
}
