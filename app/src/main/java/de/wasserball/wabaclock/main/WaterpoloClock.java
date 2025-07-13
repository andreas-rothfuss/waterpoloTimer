package de.wasserball.wabaclock.main;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import de.wasserball.wabaclock.R;
import de.wasserball.wabaclock.settings.ColorSetting;
import de.wasserball.wabaclock.settings.DialogListener;
import de.wasserball.wabaclock.settings.IntegerSetting;
import de.wasserball.wabaclock.settings.ParameterDialogString;
import de.wasserball.wabaclock.settings.ParameterDialogStringAndColor;
import de.wasserball.wabaclock.settings.SettingsView;
import de.wasserball.wabaclock.settings.StringSetting;
import de.wasserball.wabaclock.settings.AppSettings;

public class WaterpoloClock extends AppCompatActivity implements DialogListener,
        PersonalFouls.OnFragmentInteractionListener {

    public static final int GUI_UPDATE_PERIOD = 100;
    private static final int WHITE = 0xFFFFFFFF;
    private static final int BLUE = 0xFF1080FF;

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

    View overlayForNavigationBar;

    private Button btnPeriod;
    private Button btnPeriodPlus;
    private Button btnPeriodMinus;

    private TextView titleTimeoutHome;
    private Button btnTimeoutHome;
    private Button btnTimeoutHomePlus;
    private Button btnTimeoutHomeMinus;
    private TextView titleTimeoutGuest;
    private Button btnTimeoutGuest;
    private Button btnTimeoutGuestPlus;
    private Button btnTimeoutGuestMinus;

    private Button btnMainTime;
    private Button btnOffenceTime;
    private Button[][] btnExclusionTime;
    private Button btnResetMajor;
    private Button btnResetMinor;

    private Button btnGoalsHome;
    private Button btnGoalsHomePlus;
    private Button btnGoalsHomeMinus;
    private Button btnGoalsGuest;
    private Button btnGoalsGuestPlus;
    private Button btnGoalsGuestMinus;

    Button btnTeamHome;
    Button btnTeamGuest;

    MediaPlayer trippleBeepSound;
    MediaPlayer notificationSound;
    String toastText;

    private long disclamerVersion = 1;
    private String disclaimerText = "Last updated: November 11, 2019\n" +
            "The information contained on Waterpolo Timer and Scoreboard mobile app (the \"Service\") is for general information purposes only.\n" +
            "assumes no responsibility for errors or omissions in the contents on the Service.\n" +
            "In no event shall be liable for any special, direct, indirect, consequential, or incidental damages or any damages whatsoever, whether in an action of contract, negligence or other tort, arising out of or in connection with the use of the Service or the contents of the Service. reserves the right to make additions, deletions, or modification to the contents on the Service at any time without prior notice. This Disclaimer has been created with the help of Disclaimer Generator.\n" +
            "does not warrant that the website is free of viruses or other harmful components.";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.waterpolo_clock);

        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        overlayForNavigationBar = findViewById(R.id.mainLayout);
        hideNavigationBar();
        WindowCompat.setDecorFitsSystemWindows(getWindow(), true);

        AppSettings.updateAllFromSettings(getApplicationContext());

        waterpoloTimer = new WaterPoloTimer(this, AppSettings.PERIOD.value,
                AppSettings.IS_BREAK.value, AppSettings.MAIN_TIME.value,
                AppSettings.OFFENCE_TIME.value, AppSettings.TIMEOUT.value,
                AppSettings.TIMEOUTS_HOME.value, AppSettings.TIMEOUTS_GUEST.value,
                AppSettings.GOALS_HOME.value, AppSettings.GOALS_GUEST.value,
                new long[][] {{-11000, -11000, -11000}, {-11000, -11000, -11000}});

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

        titleTimeoutHome =  findViewById(R.id.titelTimeoutHeim);
        btnTimeoutHome =  findViewById(R.id.timeoutHeim);
        btnTimeoutHomePlus = findViewById(R.id.timeoutHeimPlus);
        btnTimeoutHomeMinus = findViewById(R.id.timeoutHeimMinus);
        titleTimeoutGuest =  findViewById(R.id.titelTimeoutGast);
        btnTimeoutGuest =  findViewById(R.id.timeoutGast);
        btnTimeoutGuestPlus = findViewById(R.id.timeoutGuestPlus);
        btnTimeoutGuestMinus = findViewById(R.id.timeoutGuestMinus);

        btnOffenceTime =  findViewById(R.id.angriffzeit);
        btnMainTime =  findViewById(R.id.mainTime);
        btnExclusionTime = new Button[2][3];
        btnExclusionTime[0][0] =  findViewById(R.id.exclusionTimerHome1);
        btnExclusionTime[0][1] =  findViewById(R.id.exclusionTimerHome2);
        btnExclusionTime[0][2] =  findViewById(R.id.exclusionTimerHome3);
        btnExclusionTime[1][0] =  findViewById(R.id.exclusionTimerGuest1);
        btnExclusionTime[1][1] =  findViewById(R.id.exclusionTimerGuest2);
        btnExclusionTime[1][2] =  findViewById(R.id.exclusionTimerGuest3);

        btnResetMajor = findViewById(R.id.reset30);
        btnResetMinor = findViewById(R.id.reset20);

        btnGoalsHome =  findViewById(R.id.toreHeim);
        btnGoalsHomePlus = findViewById(R.id.toreHeimPlus);
        btnGoalsHomeMinus = findViewById(R.id.toreHeimMinus);
        btnGoalsGuest =  findViewById(R.id.toreGast);
        btnGoalsGuestPlus = findViewById(R.id.toreGastPlus);
        btnGoalsGuestMinus = findViewById(R.id.toreGastMinus);

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

        trippleBeepSound = MediaPlayer.create(this, R.raw.beep_beep_beep);
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

    private void hideNavigationBar() {
        overlayForNavigationBar.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_FULLSCREEN);
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
                if (AppSettings.EXCLUSION_TIME_DURATION.value == 0){
                    for (int i0 = 0; i0 < btnExclusionTime.length; i0++) {
                        for (int i1 = 0; i1 < btnExclusionTime[i1].length; i1++)
                            btnExclusionTime[i0][i1].setVisibility(View.GONE);
                    }
                }
                else {
                    long[][] exclusionTime = new long[btnExclusionTime.length][btnExclusionTime[0].length];
                    for (int i0 = 0; i0 < btnExclusionTime.length; i0++) {
                        for (int i1 = 0; i1 < btnExclusionTime[i0].length; i1++){
                            exclusionTime[i0][i1] = waterpoloTimer.getExclusionTime(i0, i1);
                            if (exclusionTime[i0][i1] < 0)
                                btnExclusionTime[i0][i1].setText("00");
                            else
                                btnExclusionTime[i0][i1].setText(WaterPoloTimer.getExclusionTimeString(exclusionTime[i0][i1]));

                            if (i1 == 0)
                                btnExclusionTime[i0][i1].setVisibility(View.VISIBLE);
                            if (i1 > 0){
                                if (exclusionTime[i0][i1]< -TimeUnit.SECONDS.toMillis(10) && exclusionTime[i0][i1-1] < 0)
                                    btnExclusionTime[i0][i1].setVisibility(View.GONE);
                                else
                                    btnExclusionTime[i0][i1].setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }

                btnPeriod.setText(waterpoloTimer.getPeriodString());

                btnTimeoutHome.setText(Integer.toString(waterpoloTimer.timeoutsHome));
                btnTimeoutGuest.setText(Integer.toString(waterpoloTimer.timeoutsGuest));
                btnGoalsHome.setText(waterpoloTimer.getGoalsHomeString());
                btnGoalsGuest.setText(waterpoloTimer.getGoalsGuestString());

                btnResetMajor.setText(String.format(getString(R.string.reset_placeholder), AppSettings.OFFENCE_TIME_DURATION.value));
                btnResetMinor.setText(String.format(getString(R.string.reset_placeholder), AppSettings.OFFENCE_TIME_MINOR_DURATION.value));

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
                        waterpoloTimer.timeout = Long.MIN_VALUE;
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

    void trippleBeepSound(String toastText) {
        if (AppSettings.ENABLE_SOUND.value){
            trippleBeepSound.start();
        }
        this.toastText = toastText;
    }

    void sound(String toastText) {
        if (AppSettings.ENABLE_SOUND.value)
            notificationSound.start();
        this.toastText = toastText;
    }

    public void onClickTime(View view){
        waterpoloTimer.startStop();
        hideNavigationBar();
    }
    public void onClickOffenceTime(View view){
        if (AppSettings.DECOUPLE_TIMERS.value){
            waterpoloTimer.startStopOffenceTime();
        }
        else {
            waterpoloTimer.startStop();
        }
        hideNavigationBar();
    }
    public void onClickResetMajor(View view){
        waterpoloTimer.resetOffenceTimeMajor();
        hideNavigationBar();
    }
    public void onClickResetMinor(View view){
        waterpoloTimer.resetOffenceTimeMinor();
        hideNavigationBar();
    }

    public void onClickTimeoutHome(View view){
        if (waterpoloTimer.isTimeout())
            alertDialogAbortTimeout();
        else{
            if (!waterpoloTimer.isBreak)
                alertDialogTimeoutHome();
        }
        hideNavigationBar();
    }
    public void onClickTimeoutGuest(View view){
        if (waterpoloTimer.isTimeout())
            alertDialogAbortTimeout();
        else{
            if (!waterpoloTimer.isBreak)
                alertDialogTimeoutGuest();
        }
        hideNavigationBar();
    }

    public void onClickTeamColor(View view){
        Button buttonTeamColor = view.findViewById(R.id.buttonTeamColor);
        int alternativeColor;
        Drawable buttonBackground = buttonTeamColor.getBackground();
        ColorDrawable buttonColor = (ColorDrawable) buttonBackground;
        int color = buttonColor.getColor();
        if (color == WHITE){
            alternativeColor = BLUE;
        }
        else {
            alternativeColor = WHITE;
        }
        buttonTeamColor.setBackgroundColor(alternativeColor);
    }

    public void resetAll(View view){
        alertDialogResetAll();
        hideNavigationBar();
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
        ParameterDialogStringAndColor dialog = new ParameterDialogStringAndColor(
                AppSettings.HOME_TEAM_NAME, AppSettings.HOME_TEAM_COLOR);
        dialog.show(getSupportFragmentManager(), "");
        hideNavigationBar();
    }

    public void onGuestTeamNameClicked(View view){
        ParameterDialogStringAndColor dialog = new ParameterDialogStringAndColor(
                AppSettings.GUEST_TEAM_NAME, AppSettings.GUEST_TEAM_COLOR);
        dialog.show(getSupportFragmentManager(), "");
        hideNavigationBar();
    }
    public void onExclusionTimeHome1Clicked(View view){
        waterpoloTimer.resetExclusionTimeHome(0);
        hideNavigationBar();
    }
    public void onExclusionTimeHome2Clicked(View view){
        waterpoloTimer.resetExclusionTimeHome(1);
        hideNavigationBar();
    }
    public void onExclusionTimeHome3Clicked(View view){
        waterpoloTimer.resetExclusionTimeHome(2);
        hideNavigationBar();
    }

    public void onExclusionTimeGuest1Clicked(View view){
        waterpoloTimer.resetExclusionTimeGuest(0);
        hideNavigationBar();
    }
    public void onExclusionTimeGuest2Clicked(View view){
        waterpoloTimer.resetExclusionTimeGuest(1);
        hideNavigationBar();
    }
    public void onExclusionTimeGuest3Clicked(View view){
        waterpoloTimer.resetExclusionTimeGuest(2);
        hideNavigationBar();
    }

    @Override
    public void applyValue(StringSetting setting, String value) {
        setting.applyValue(AppSettings.getSharedPreferences(
                getApplicationContext()), value);
        updateSettingsValueDisplay();
    }

    @Override
    public void applyValue(ColorSetting setting, int value) {
        setting.applyValue(AppSettings.getSharedPreferences(
                getApplicationContext()), value);
        updateSettingsValueDisplay();
    }

    @Override
    public void applyValue(IntegerSetting setting, int value) {
        setting.applyValue(AppSettings.getSharedPreferences(
                getApplicationContext()), value);
        updateSettingsValueDisplay();
    }

    void updateSettingsValueDisplay() {
        btnTeamHome.setText(AppSettings.HOME_TEAM_NAME.value);
        int homeTeamColor = AppSettings.HOME_TEAM_COLOR.value;
        btnTeamHome.setTextColor(homeTeamColor);
        for (int i = 0; i < btnExclusionTime[0].length; i++) {
            btnExclusionTime[0][i].setTextColor(homeTeamColor);
        }
        titleTimeoutHome.setTextColor(homeTeamColor);
        btnTimeoutHome.setTextColor(homeTeamColor);
        btnTimeoutHomePlus.setTextColor(homeTeamColor);
        btnTimeoutHomeMinus.setTextColor(homeTeamColor);
        btnGoalsHome.setTextColor(homeTeamColor);
        btnGoalsHomePlus.setTextColor(homeTeamColor);
        btnGoalsHomeMinus.setTextColor(homeTeamColor);

        btnTeamGuest.setText(AppSettings.GUEST_TEAM_NAME.value);
        int guestTeamColor = AppSettings.GUEST_TEAM_COLOR.value;
        btnTeamGuest.setTextColor(guestTeamColor);
        for (int i = 0; i < btnExclusionTime[1].length; i++) {
            btnExclusionTime[1][i].setTextColor(guestTeamColor);
        }
        titleTimeoutGuest.setTextColor(guestTeamColor);
        btnTimeoutGuest.setTextColor(guestTeamColor);
        btnTimeoutGuestPlus.setTextColor(guestTeamColor);
        btnTimeoutGuestMinus.setTextColor(guestTeamColor);
        btnGoalsGuest.setTextColor(guestTeamColor);
        btnGoalsGuestPlus.setTextColor(guestTeamColor);
        btnGoalsGuestMinus.setTextColor(guestTeamColor);
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
        hideNavigationBar();
    }
    public void onClickPeriodMinus(View view){
        waterpoloTimer.periodMinus();
        hideNavigationBar();
    }
    public void onClickMinutesPlus(View view){
        if (!waterpoloTimer.isTimeout())
            waterpoloTimer.mainTimeAdd(1, 0);
        else
            waterpoloTimer.timeoutAdd(1,0);
        hideNavigationBar();
    }
    public void onClickMinutesMinus(View view){
        if (!waterpoloTimer.isTimeout())
            waterpoloTimer.mainTimeSub(1, 0);
        else
            waterpoloTimer.timeoutSub(1,0);
        hideNavigationBar();
    }
    public void onClickSecondsPlus(View view){
        if (!waterpoloTimer.isTimeout())
            waterpoloTimer.mainTimeAdd(0, 1);
        else
            waterpoloTimer.timeoutAdd(0,1);
        hideNavigationBar();
    }
    public void onClickSecondsMinus(View view){
        if (!waterpoloTimer.isTimeout())
            waterpoloTimer.mainTimeSub(0, 1);
        else
            waterpoloTimer.timeoutSub(0,1);
        hideNavigationBar();
    }
    public void onClickOffenceTimePlus(View view){
        if (!waterpoloTimer.isTimeout())
            waterpoloTimer.offenceTimeAdd(0, 1);
        hideNavigationBar();
    }

    public void onClickOffenceTimeMinus(View view){
        if (!waterpoloTimer.isTimeout())
            waterpoloTimer.offenceTimeSub(0, 1);
        hideNavigationBar();
    }
    public void onClickTimeoutsHomePlus(View view){
        waterpoloTimer.timeoutsHome++;
        hideNavigationBar();
    }
    public void onClickTimeoutsHomeMinus(View view){
        if (waterpoloTimer.timeoutsHome > 0)
            waterpoloTimer.timeoutsHome--;
        hideNavigationBar();
    }
    public void onClickTimeoutsGuestPlus(View view){
        waterpoloTimer.timeoutsGuest++;
        hideNavigationBar();
    }
    public void onClickTimeoutsGuestMinus(View view){
        if (waterpoloTimer.timeoutsGuest > 0)
            waterpoloTimer.timeoutsGuest--;
    }

    public void onClickToreHomePlus(View view){
        waterpoloTimer.goalsHomeIncrement();
        hideNavigationBar();
    }
    public void onClickToreHomeMinus(View view){
        waterpoloTimer.goalsHomeDecrement();
        hideNavigationBar();
    }
    public void onClickToreGuestPlus(View view){
        waterpoloTimer.goalsGuestIncrement();
        hideNavigationBar();
    }
    public void onClickToreGuestMinus(View view){
        waterpoloTimer.goalsGuestDecrement();
        hideNavigationBar();
    }


    public void openSettings(View view){
        Intent intent = new Intent(WaterpoloClock.this, SettingsView.class);
        startActivity(intent);
        hideNavigationBar();
    }

    public void openBoards(View view){
        Boards.waterpoloTimer = waterpoloTimer;
        Intent intent = new Intent(this, Boards.class);
        startActivity(intent);
        hideNavigationBar();
    }

    public void onFragmentInteraction(Uri uri){}
}
