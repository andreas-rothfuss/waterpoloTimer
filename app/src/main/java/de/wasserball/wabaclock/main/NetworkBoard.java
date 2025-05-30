package de.wasserball.wabaclock.main;

import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import de.wasserball.wabaclock.settings.AppSettings;
import msg.OpenIGTMessage;
import msg.capability.GetCapabilityMessage;
import msg.command.CommandMessage;
import msg.sensor.GetSensorMessage;
import msg.string.GetStringMessage;
import network.Client;
import network.ServerAdress;

abstract class NetworkBoard extends AppCompatActivity {

    static final int DATA_UPDATE_PERIOD = 50;
    static final int SLOW_DATA_UPDATE_PERIOD = 1000;
    static final int GUI_UPDATE_PERIOD = 100;

    public static WaterPoloTimer waterpoloTimer;

    View overlayForNavigationBar;

    ClientViewClient client;

    final Handler myHandler = new Handler();

    Logger log;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        log = LoggerFactory.getLogger(this.getClass());

        defineContentView();
        hideNavigationBar();

        try {
            ServerAdress host = null;

            if (AppSettings.USE_AUTODISCOVERY.value) {
                if (waterpoloTimer != null){
                    waterpoloTimer.dispose();
                }

                Toast.makeText(getApplicationContext(),
                        "Server stopped, autodiscovery started", Toast.LENGTH_LONG).show();

                ServerAdress[] matchingIPs = Client.autodiscover(
                        WaterpoloClockServer.SERVER_PORT,
                        GetCapabilityMessage.DATA_TYPE, GetSensorMessage.DATA_TYPE,
                        GetStringMessage.DATA_TYPE, CommandMessage.DATA_TYPE);

                for (int i = 0; i < matchingIPs.length; i++) {
                    /** found a non-loopback address */
                    if (!matchingIPs[i].isLoopback) {
                        host = matchingIPs[i];
                        Toast.makeText(getApplicationContext(),
                                "Connecting to found remote ip: " +
                                        host.ip + ":" + host.port,
                                Toast.LENGTH_LONG).show();
                        break;
                    }
                }
                if (host == null) {
                    for (int i = 0; i < matchingIPs.length; i++) {
                        /** found a loopback address */
                        host = matchingIPs[i];
                        Toast.makeText(getApplicationContext(),
                                "Connecting to localhost ip: " +
                                        host.ip + ":" + host.port,
                                Toast.LENGTH_LONG).show();
                        break;
                    }
                }
            }
            if (host == null){
                String ip = AppSettings.MASTER_IP.value;
                host = new ServerAdress(ip,
                        WaterpoloClockServer.SERVER_PORT,
                        ip.equals("127.0.0.1"), false);
                if (host.isLoopback) {
                    /** restart server */
                    Toast.makeText(getApplicationContext(),
                            "No remote server found, restarting local server", Toast.LENGTH_LONG).show();
                    waterpoloTimer.startServer();
                }

                Toast.makeText(getApplicationContext(),
                        "Connecting to set ip: " +
                        host.ip + ":" + host.port,
                        Toast.LENGTH_LONG).show();
            }

            client = new ClientViewClient(this, host.ip, host.port) ;
            client.start();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(),"No connection to main unit",Toast.LENGTH_LONG).show();
        }

        Timer dataUpdateTimer = new Timer();
        dataUpdateTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                updateData();
            }
        }, 0, DATA_UPDATE_PERIOD);
        Timer slowDataUpdateTimer = new Timer();
        slowDataUpdateTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                updateDataSlow();
            }
        }, 0, SLOW_DATA_UPDATE_PERIOD);
        Timer guiUpdateTimer = new Timer();
        guiUpdateTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                guiUpdate();
            }
        }, 0, GUI_UPDATE_PERIOD);
    }

    protected void hideNavigationBar() {
        if (overlayForNavigationBar != null) {
            overlayForNavigationBar.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_FULLSCREEN);
        }
    }

    protected abstract void defineContentView();

    private void guiUpdate() {
        myHandler.post(guiUpdateRunnable);
    }
    final Runnable guiUpdateRunnable;
    {
        guiUpdateRunnable = new Runnable() {
            public void run() {
                updateGuiElements();
            }
        };
    }

    protected abstract void updateData();

    protected void updateDataSlow(){};

    protected abstract void updateGuiElements();

    void sendIfConnected(OpenIGTMessage msg){
        if (client != null && client.isConnected()) {
            try {
                client.send(msg);
            } catch (IOException e) {
                log.warn("Caught an IOException", e);
            }
        }
    }

    @Override
    public void finish() {
        super.finish();
        waterpoloTimer.startServer();
    }

    void setHomeTeamName(String val){}
    void setHomeTeamColor(String colorARGBInt){}
    void setGuestTeamName(String val){}
    void setGuestTeamColor(String colorARGBInt){}
    void setMainTime(long time_ms){}
    void setShotClock(long time_ms){}
    void setScore(int home, int guest){}
}
