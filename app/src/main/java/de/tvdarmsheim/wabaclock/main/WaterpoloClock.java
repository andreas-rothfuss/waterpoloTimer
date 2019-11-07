package de.tvdarmsheim.wabaclock.main;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import de.tvdarmsheim.wabaclock.R;
import de.tvdarmsheim.wabaclock.settings.Settings;
import de.tvdarmsheim.wabaclock.settings.WaterpoloTimerSettings;

public class WaterpoloClock extends AppCompatActivity{

    public static final int GUI_UPDATE_PERIOD = 100;

    WaterpoloTimer waterpoloTimer;
    private boolean timeIsEditable = false;

    final Handler myHandler = new Handler();

    private View layoutEditMinutes;
    private View layoutEditSeconds;
    private View layoutEditOffenceTime;

    private Button btnPeriod;
    private Button btnPeriodPlus;
    private Button btnPeriodMinus;

    private Button btnTimeoutGuest;
    private Button btnTimeoutHome;

    private Button btnMainTime;
    private Button btnOffenceTime;

    private Button btnGoalsHome;
    private Button btnGoalsGuest;
    MediaPlayer notificationSound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.waterpolo_clock);

        WaterpoloTimerSettings.updateAllFromSettings(getApplicationContext());

        resetAll(findViewById(R.id.mainTime));

        layoutEditMinutes = findViewById(R.id.layoutEditMinutes);
        layoutEditSeconds = findViewById(R.id.layoutEditSeconds);
        layoutEditOffenceTime = findViewById(R.id.layoutEditAngriffzeit);

        btnPeriod = findViewById(R.id.viertelOderPause);
        btnPeriodPlus = findViewById(R.id.editPeriodPlus);
        btnPeriodMinus = findViewById(R.id.editPeriodMinus);

        btnTimeoutHome =  findViewById(R.id.timeoutHeim);
        btnTimeoutGuest =  findViewById(R.id.timeoutGast);

        btnOffenceTime =  findViewById(R.id.angriffzeit);
        btnMainTime =  findViewById(R.id.mainTime);

        btnGoalsHome =  findViewById(R.id.toreHeim);
        btnGoalsGuest =  findViewById(R.id.toreGast);

        layoutEditMinutes.setVisibility(View.GONE);
        layoutEditSeconds.setVisibility(View.GONE);
        layoutEditOffenceTime.setVisibility(View.GONE);
        btnPeriodPlus.setVisibility(View.GONE);
        btnPeriodMinus.setVisibility(View.GONE);

        notificationSound = MediaPlayer.create(this, R.raw.new_button_option_sound);

        Timer guiUpdateTimer = new Timer();
        guiUpdateTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                guiUpdate();
            }
        }, 0, GUI_UPDATE_PERIOD);

        Timer timeUpdateTimer = new Timer();
        timeUpdateTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                waterpoloTimer.updateTime();
            }
        }, 0, WaterpoloTimer.TIMER_UPDATE_PERIOD);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu); //your file name
        return super.onCreateOptionsMenu(menu);
    }

    private void guiUpdate() {
        myHandler.post(guiUpdateRunnable);
    }
    final Runnable guiUpdateRunnable;
    {
        guiUpdateRunnable = new Runnable() {
            public void run() {

                btnOffenceTime.setText(waterpoloTimer.getOffenceTimeString());

                btnMainTime.setText(waterpoloTimer.getMainTimeString());

                btnPeriod.setText(waterpoloTimer.getPeriodString());

                btnTimeoutHome.setText(Integer.toString(waterpoloTimer.timeoutsHome));
                btnTimeoutGuest.setText(Integer.toString(waterpoloTimer.timeoutsGuest));
                btnGoalsHome.setText(waterpoloTimer.getGoalsHomeString());
                btnGoalsGuest.setText(waterpoloTimer.getGoalsGuestString());
            }
        };
    }

    private void alertDialogTimeoutHome() {
        AlertDialog.Builder dialog=new AlertDialog.Builder(this);
        dialog.setMessage("Do you want to start a Timeout for the Home team (" +
                WaterpoloTimerSettings.TIMEOUT_DURATION.value + " seconds)?");
        dialog.setTitle("Timeout");
        dialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        waterpoloTimer.stop();
                        waterpoloTimer.startTimeout();
                        waterpoloTimer.timeoutsHome++;
                        Toast.makeText(getApplicationContext(),"start timeout",Toast.LENGTH_LONG).show();
                    }
                });
        dialog.setNegativeButton("No",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(),"do not start timeout",Toast.LENGTH_LONG).show();
            }
        });
        AlertDialog alertDialog=dialog.create();
        alertDialog.show();
    }

    private void alertDialogTimeoutGuest() {
        AlertDialog.Builder dialog=new AlertDialog.Builder(this);
        dialog.setMessage("Do you want to start a Timeout for the Guest team ("  +
                WaterpoloTimerSettings.TIMEOUT_DURATION.value + "  seconds)?");
        dialog.setTitle("Timeout");
        dialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        waterpoloTimer.stop();
                        waterpoloTimer.startTimeout();
                        waterpoloTimer.timeoutsGuest++;
                        Toast.makeText(getApplicationContext(),"start timeout",Toast.LENGTH_LONG).show();
                    }
                });
        dialog.setNegativeButton("No",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(),"do not start timeout",Toast.LENGTH_LONG).show();
            }
        });
        AlertDialog alertDialog=dialog.create();
        alertDialog.show();
    }

    public void onClickTime(View view){
        waterpoloTimer.startStop();
    }
    public void onClickReset30(View view){
        waterpoloTimer.resetOffenceTimeMajor();
    }
    public void onClickReset20(View view){
        waterpoloTimer.resetOffenceTimeMinor();}

    public void onClickTimeoutHome(View view){
        alertDialogTimeoutHome();
    }
    public void onClickTimeoutGuest(View view){
        alertDialogTimeoutGuest();
    }
    public void resetAll(View view){
        if (waterpoloTimer != null)
            waterpoloTimer.dispose();
        waterpoloTimer = new WaterpoloTimer(this);
    }
    public void editTime(View view){
        timeIsEditable = !timeIsEditable;
        int viewState = timeIsEditable ? View.VISIBLE : View.GONE;
        layoutEditMinutes.setVisibility(viewState);
        layoutEditSeconds.setVisibility(viewState);
        layoutEditOffenceTime.setVisibility(viewState);
        btnPeriodPlus.setVisibility(viewState);
        btnPeriodMinus.setVisibility(viewState);
    }
    public void onClickPeriodPlus(View view){
        waterpoloTimer.periodPlus();
    }
    public void onClickPeriodMinus(View view){
        waterpoloTimer.periodMinus();
    }
    public void onClickMinutesPlus(View view){
        if (!waterpoloTimer.isTimeout())
            waterpoloTimer.mainTimeAdd(1, 0);
        else
            waterpoloTimer.timeoutAdd(1,0);
    }
    public void onClickMinutesMinus(View view){
        if (!waterpoloTimer.isTimeout())
            waterpoloTimer.mainTimeSub(1, 0);
        else
            waterpoloTimer.timeoutSub(1,0);
    }
    public void onClickSecondsPlus(View view){
        if (!waterpoloTimer.isTimeout())
            waterpoloTimer.mainTimeAdd(0, 1);
        else
            waterpoloTimer.timeoutAdd(0,1);
    }
    public void onClickSecondsMinus(View view){
        if (!waterpoloTimer.isTimeout())
            waterpoloTimer.mainTimeSub(0, 1);
        else
            waterpoloTimer.timeoutSub(0,1);
    }
    public void onClickOffenceTimePlus(View view){
        if (!waterpoloTimer.isTimeout())
            waterpoloTimer.offenceTimeAdd(0, 1);
    }
    public void onClickOffenceTimeMinus(View view){
        if (!waterpoloTimer.isTimeout())
            waterpoloTimer.offenceTimeSub(0, 1);
    }

    public void onClickToreHomePlus(View view){
        waterpoloTimer.goalsHome++;
    }
    public void onClickToreHomeMinus(View view){
        waterpoloTimer.goalsHome--;
    }
    public void onClickToreGuestPlus(View view){
        waterpoloTimer.goalsGuest++;
    }
    public void onClickToreGuestMinus(View view){
        waterpoloTimer.goalsGuest--;
    }

    public void openSettings(View view){
        Intent intent = new Intent(WaterpoloClock.this, Settings.class);
        startActivity(intent);
    }
    public void openMainTime(View view){
        Intent intent = new Intent(WaterpoloClock.this, MainTimeBoard.class);
        startActivity(intent);
    }
    public void openShotclock(View view){
        Intent intent = new Intent(WaterpoloClock.this, Shotclock.class);
        startActivity(intent);
    }
    public void openTimeAndScoreBoard(View view){
        Intent intent = new Intent(WaterpoloClock.this, TimeAndScoreBoard.class);
        startActivity(intent);
    }
    public void openScoreBoard(View view){
        Intent intent = new Intent(WaterpoloClock.this, Scoreboard.class);
        startActivity(intent);
    }
}
