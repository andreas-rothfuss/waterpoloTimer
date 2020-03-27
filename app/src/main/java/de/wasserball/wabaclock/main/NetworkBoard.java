package de.wasserball.wabaclock.main;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import de.wasserball.wabaclock.R;
import msg.OpenIGTMessage;

abstract class NetworkBoard extends AppCompatActivity {

    static final int DATA_UPDATE_PERIOD = 50;
    static final int SLOW_DATA_UPDATE_PERIOD = 1000;
    static final int GUI_UPDATE_PERIOD = 100;

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
            client = new ClientViewClient(this) ;
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

    void setHomeTeamName(String val){}
    void setGuestTeamName(String val){}
    void setMainTime(long time_ms){}
    void setShotClock(long time_ms){}
    void setScore(int home, int guest){}
}
