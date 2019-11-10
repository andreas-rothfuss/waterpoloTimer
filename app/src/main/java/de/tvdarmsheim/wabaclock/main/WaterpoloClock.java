package de.tvdarmsheim.wabaclock.main;

import android.app.AlertDialog;
import android.app.MediaRouteButton;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
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
import de.tvdarmsheim.wabaclock.settings.ParameterDialogString;
import de.tvdarmsheim.wabaclock.settings.Settings;
import de.tvdarmsheim.wabaclock.settings.StringSetting;
import de.tvdarmsheim.wabaclock.settings.WaterpoloTimerSettings;
import de.tvdarmsheim.zeitnehmerwasserball.PersonalFouls;

public class WaterpoloClock extends AppCompatActivity implements ParameterDialogString.DialogListener,
        PersonalFouls.OnFragmentInteractionListener {

    public static final int GUI_UPDATE_PERIOD = 100;

    WaterpoloTimer waterpoloTimer;
    private boolean timeIsEditable = false;

    final Handler myHandler = new Handler();

    private View layoutTimeoutsHome;
    private View layoutTimeoutsGuest;
    private View layoutEditMinutes;
    private View layoutEditSeconds;
    private View layoutEditOffenceTime;
    private View layoutEditGoalsHome;
    private View layoutEditGoalsGuest;

    private Button btnPeriod;
    private Button btnPeriodPlus;
    private Button btnPeriodMinus;

    private Button btnTimeoutGuest;
    private Button btnTimeoutHome;

    private Button btnMainTime;
    private Button btnOffenceTime;

    private Button btnGoalsHome;
    private Button btnGoalsGuest;

    Button btnTeamHome;
    Button btnTeamGuest;

    MediaPlayer notificationSound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.waterpolo_clock);

        WaterpoloTimerSettings.updateAllFromSettings(getApplicationContext());

        resetAll();

        layoutTimeoutsHome = findViewById(R.id.editTimeoutsHome);
        layoutTimeoutsGuest = findViewById(R.id.editTimeoutsGuest);
        layoutEditMinutes = findViewById(R.id.layoutEditMinutes);
        layoutEditSeconds = findViewById(R.id.layoutEditSeconds);
        layoutEditOffenceTime = findViewById(R.id.layoutEditAngriffzeit);
        layoutEditGoalsHome = findViewById(R.id.editGoalsHome);
        layoutEditGoalsGuest = findViewById(R.id.editGoalsGuest);

        btnPeriod = findViewById(R.id.viertelOderPause);
        btnPeriodPlus = findViewById(R.id.editPeriodPlus);
        btnPeriodMinus = findViewById(R.id.editPeriodMinus);

        btnTimeoutHome =  findViewById(R.id.timeoutHeim);
        btnTimeoutGuest =  findViewById(R.id.timeoutGast);

        btnOffenceTime =  findViewById(R.id.angriffzeit);
        btnMainTime =  findViewById(R.id.mainTime);

        btnGoalsHome =  findViewById(R.id.toreHeim);
        btnGoalsGuest =  findViewById(R.id.toreGast);

        layoutTimeoutsHome.setVisibility(View.GONE);
        layoutTimeoutsGuest.setVisibility(View.GONE);
        layoutEditMinutes.setVisibility(View.GONE);
        layoutEditSeconds.setVisibility(View.GONE);
        layoutEditOffenceTime.setVisibility(View.GONE);
        layoutEditGoalsHome.setVisibility(View.GONE);
        layoutEditGoalsGuest.setVisibility(View.GONE);
        btnPeriodPlus.setVisibility(View.GONE);
        btnPeriodMinus.setVisibility(View.GONE);

        btnTeamHome = findViewById(R.id.teamHome);
        btnTeamGuest = findViewById(R.id.teamGuest);
        updateSettingsValueDisplay();

        notificationSound = MediaPlayer.create(this, R.raw.beep_09);

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

    //TODO: RESET ALL bitte mit bestätigung oder dort weg und in die Setting.
    public void resetAll(View view){
        alertDialogResetAll();
    }
    void resetAll(){
        if (waterpoloTimer != null)
            waterpoloTimer.dispose();
        waterpoloTimer = new WaterpoloTimer(this);
    }

    private void alertDialogResetAll() {
        AlertDialog.Builder dialog=new AlertDialog.Builder(this);
        dialog.setMessage("Do you want reset all timers?");
        dialog.setTitle("Reset all");
        dialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        resetAll();
                        Toast.makeText(getApplicationContext(),"Reset all done",Toast.LENGTH_LONG).show();
                    }
                });
        dialog.setNegativeButton("No",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(),"Reset all declined",Toast.LENGTH_LONG).show();
            }
        });
        AlertDialog alertDialog=dialog.create();
        alertDialog.show();
    }


    public void onHomeTeamNameClicked(View view){
        ParameterDialogString dialog = new ParameterDialogString(WaterpoloTimerSettings.HOME_TEAM_NAME);
        dialog.show(getSupportFragmentManager(), "");
    }

    public void onGuestTeamNameClicked(View view){
        ParameterDialogString dialog = new ParameterDialogString(WaterpoloTimerSettings.GUEST_TEAM_NAME);
        dialog.show(getSupportFragmentManager(), "");
    }

    @Override
    public void applyValue(StringSetting setting, String value) {
        setting.applyValue(WaterpoloTimerSettings.getSharedPreferences(
                getApplicationContext()), value);
        updateSettingsValueDisplay();
    }

    void updateSettingsValueDisplay() {
        btnTeamHome.setText(WaterpoloTimerSettings.HOME_TEAM_NAME.value);
        btnTeamGuest.setText(WaterpoloTimerSettings.GUEST_TEAM_NAME.value);
    }

    public void editTime(View view){
        timeIsEditable = !timeIsEditable;
        int viewState = timeIsEditable ? View.VISIBLE : View.GONE;
        layoutTimeoutsHome.setVisibility(viewState);
        layoutTimeoutsGuest.setVisibility(viewState);
        layoutEditMinutes.setVisibility(viewState);
        layoutEditSeconds.setVisibility(viewState);
        layoutEditOffenceTime.setVisibility(viewState);
        layoutEditGoalsHome.setVisibility(viewState);
        layoutEditGoalsGuest.setVisibility(viewState);
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
    public void onClickTimeoutsHomePlus(View view){
        waterpoloTimer.timeoutsHome++;
    }
    public void onClickTimeoutsHomeMinus(View view){
        if (waterpoloTimer.timeoutsHome > 0)
            waterpoloTimer.timeoutsHome--;
    }
    public void onClickTimeoutsGuestPlus(View view){
        waterpoloTimer.timeoutsGuest++;
    }
    public void onClickTimeoutsGuestMinus(View view){
        if (waterpoloTimer.timeoutsGuest > 0)
            waterpoloTimer.timeoutsGuest--;
    }

    public void onClickToreHomePlus(View view){
        waterpoloTimer.goalsHome++;
    }
    public void onClickToreHomeMinus(View view){
        if (waterpoloTimer.goalsHome > 0)
            waterpoloTimer.goalsHome--;
    }
    public void onClickToreGuestPlus(View view){
        waterpoloTimer.goalsGuest++;
    }
    public void onClickToreGuestMinus(View view){
        if (waterpoloTimer.goalsGuest > 0)
            waterpoloTimer.goalsGuest--;
    }


    public void openSettings(View view){
        Intent intent = new Intent(WaterpoloClock.this, Settings.class);
        startActivity(intent);
    }

    public void openBoards(View view){
        Intent intent = new Intent(this, Boards.class);
        startActivity(intent);
    }

    public void onFragmentInteraction(Uri uri){}
}
