package de.tvdarmsheim.wabaclock.main;

import java.io.IOException;

import de.tvdarmsheim.wabaclock.settings.WaterpoloTimerSettings;
import msg.OpenIGTMessage;
import msg.sensor.SensorData;
import msg.sensor.SensorMessage;
import network.OpenIGTLinkClient;

public abstract class ClientViewClient extends OpenIGTLinkClient {

    public ClientViewClient() throws IOException {
        super(WaterpoloTimerSettings.MASTER_IP.value, WaterpoloclockServer.SERVER_PORT);
    }

    @Override
    public void messageReceived(OpenIGTMessage message) {
        log.debug("Message received: " + message.toString());

        /* check message data type and do something with the message */
        if (message instanceof SensorMessage) {
            //Log.info("Message received: " + message.toString());
            onRxSensor(message.getDeviceName(), ((SensorMessage)message).getSensorData());
        }
    }

    protected abstract void onRxSensor(String deviceName, SensorData sensorData);

    @Override
    public String[] getCapability() {
        return new String[]{SensorMessage.DATA_TYPE};
    }
}
