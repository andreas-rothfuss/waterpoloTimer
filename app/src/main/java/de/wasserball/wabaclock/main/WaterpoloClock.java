package de.wasserball.wabaclock.main;

import android.app.AlertDialog;
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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import de.wasserball.wabaclock.R;
import de.wasserball.wabaclock.settings.ParameterDialogString;
import de.wasserball.wabaclock.settings.SettingsView;
import de.wasserball.wabaclock.settings.StringSetting;
import de.wasserball.wabaclock.settings.AppSettings;

public class WaterpoloClock extends AppCompatActivity implements ParameterDialogString.DialogListener,
        PersonalFouls.OnFragmentInteractionListener {

    public static final int GUI_UPDATE_PERIOD = 100;

    WaterPoloTimer waterpoloTimer;
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
    String toastText;

    private long disclamerVersion = 1;
    private String disclaimerText = "Last updated: November 11, 2019\n" +
            "The information contained on Waterpolo CountdownTimer and Scoreboard mobile app (the \"Service\") is for general information purposes only.\n" +
            "assumes no responsibility for errors or omissions in the contents on the Service.\n" +
            "In no event shall be liable for any special, direct, indirect, consequential, or incidental damages or any damages whatsoever, whether in an action of contract, negligence or other tort, arising out of or in connection with the use of the Service or the contents of the Service. reserves the right to make additions, deletions, or modification to the contents on the Service at any time without prior notice. This Disclaimer has been created with the help of Disclaimer Generator.\n" +
            "does not warrant that the website is free of viruses or other harmful components.";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.waterpolo_clock);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        AppSettings.updateAllFromSettings(getApplicationContext());

        waterpoloTimer = new WaterPoloTimer(this, AppSettings.PERIOD.value,
                AppSettings.IS_BREAK.value, AppSettings.MAIN_TIME.value,
                AppSettings.OFFENCE_TIME.value, AppSettings.TIMEOUT.value,
                AppSettings.TIMEOUTS_HOME.value, AppSettings.TIMEOUTS_GUEST.value,
                AppSettings.GOALS_HOME.value, AppSettings.GOALS_GUEST.value);

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
        }, 0, WaterPoloTimer.TIMER_UPDATE_PERIOD);

        disclaimerDialog();

    }

    private void disclaimerDialog() {
        if (AppSettings.DISCLAIMER_DISPLAYED.value < disclamerVersion) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setMessage(disclaimerText);
            dialog.setTitle("Disclaimer");
            dialog.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int which) {
                            //Store that the disclaimer has been displayed
                            AppSettings.DISCLAIMER_DISPLAYED.applyValue(AppSettings.getSharedPreferences(
                                    getApplicationContext()), disclamerVersion);
                        }
                    });
            AlertDialog alertDialog = dialog.create();
            alertDialog.show();
        }
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

                if (toastText != null) {
                    Toast.makeText(getApplicationContext(), toastText, Toast.LENGTH_LONG).show();
                    toastText = null;
                }
            }
        };
    }

    private void alertDialogTimeoutHome() {
        AlertDialog.Builder dialog=new AlertDialog.Builder(this);
        dialog.setMessage("Do you want to start a Timeout for the Home team (" +
                AppSettings.TIMEOUT_DURATION.value + " seconds)?");
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
                AppSettings.TIMEOUT_DURATION.value + "  seconds)?");
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

    private void alertDialogAbortTimeout() {
        AlertDialog.Builder dialog=new AlertDialog.Builder(this);
        dialog.setMessage("Do you want to abort the running timeout?");
        dialog.setTitle("Abort Timeout");
        dialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        waterpoloTimer.timeout = null;
                        waterpoloTimer.stop();
                        Toast.makeText(getApplicationContext(),"timeout aborted",Toast.LENGTH_LONG).show();
                    }
                });
        dialog.setNegativeButton("No",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(),"timeout abort aborted",Toast.LENGTH_LONG).show();
            }
        });
        AlertDialog alertDialog=dialog.create();
        alertDialog.show();
    }

    void sound(String toastText) {
        if (AppSettings.ENABLE_SOUND.value)
            notificationSound.start();
        this.toastText = toastText;
    }

    public void onClickTime(View view){
        waterpoloTimer.startStop();
    }
    public void onClickReset30(View view){
        waterpoloTimer.resetOffenceTimeMajor();
    }
    public void onClickReset20(View view){ waterpoloTimer.resetOffenceTimeMinor();}

    public void onClickTimeoutHome(View view){
        if (waterpoloTimer.isTimeout())
            alertDialogAbortTimeout();
        else{
            if (!waterpoloTimer.isBreak)
                alertDialogTimeoutHome();
        }
    }
    public void onClickTimeoutGuest(View view){
        if (waterpoloTimer.isTimeout())
            alertDialogAbortTimeout();
        else{
            if (!waterpoloTimer.isBreak)
                alertDialogTimeoutGuest();
        }
    }

    public void resetAll(View view){
        alertDialogResetAll();
    }
    void resetAll(){
        if (waterpoloTimer != null)
            waterpoloTimer.dispose();
        waterpoloTimer = new WaterPoloTimer(this);
        waterpoloTimer.stop();
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
        ParameterDialogString dialog = new ParameterDialogString(AppSettings.HOME_TEAM_NAME);
        dialog.show(getSupportFragmentManager(), "");
    }

    public void onGuestTeamNameClicked(View view){
        ParameterDialogString dialog = new ParameterDialogString(AppSettings.GUEST_TEAM_NAME);
        dialog.show(getSupportFragmentManager(), "");
    }

    @Override
    public void applyValue(StringSetting setting, String value) {
        setting.applyValue(AppSettings.getSharedPreferences(
                getApplicationContext()), value);
        updateSettingsValueDisplay();
    }

    void updateSettingsValueDisplay() {
        btnTeamHome.setText(AppSettings.HOME_TEAM_NAME.value);
        btnTeamGuest.setText(AppSettings.GUEST_TEAM_NAME.value);
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

    public void onClickToreHomePlus(View view){waterpoloTimer.goalsHomeIncrement();}
    public void onClickToreHomeMinus(View view){waterpoloTimer.goalsHomeDecrement();}
    public void onClickToreGuestPlus(View view){
        waterpoloTimer.goalsGuestIncrement();
    }
    public void onClickToreGuestMinus(View view){waterpoloTimer.goalsGuestDecrement();}


    public void openSettings(View view){
        Intent intent = new Intent(WaterpoloClock.this, SettingsView.class);
        startActivity(intent);
    }

    public void openBoards(View view){
        Intent intent = new Intent(this, Boards.class);
        startActivity(intent);
    }

    public void onFragmentInteraction(Uri uri){}
}
