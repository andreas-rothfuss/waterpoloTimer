package de.wasserball.wabaclock.main;

import android.content.SharedPreferences;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import de.wasserball.wabaclock.settings.AppSettings;

public class WaterPoloTimer{

    public static final String SHOT_CLOCK_DEVICE_NAME = "ShotClock";
    public static final String MAIN_TIME_DEVICE_NAME = "MainTime";
    public static final String SCOREBOARD_DEVICE_NAME = "Scoreboard";
    public static final String HOME_TEAM_DEVICE_NAME = "HomeTeam";
    public static final String GUEST_TEAM_DEVICE_NAME = "GuestTeam";

    public static final int TIMER_UPDATE_PERIOD = 50;

    WaterpoloClockServer server;

    private final WaterpoloClock activity;

    private boolean timerRunning = false;

    int period;
    boolean isBreak = false;

    long timeout;
    long mainTime;
    long offenceTime;
    long[][] exclusionTime;

    long lastTimerUpdateTime;

    int timeoutsHome;
    int timeoutsGuest;

    int goalsHome;
    int goalsGuest;

    Logger log = LoggerFactory.getLogger(WaterPoloTimer.class);

    public WaterPoloTimer(WaterpoloClock activity, int period, boolean isBreak, long mainTime,
                          long offenceTime, long timeout, int timeoutsHome, int timeoutsGuest,
                          int goalsHome, int goalsGuest, long [][] exclusionTime) {
        this.activity = activity;

        lastTimerUpdateTime = System.currentTimeMillis();
        this.period = (int) period;

        this.isBreak = isBreak;

        this.timeout = timeout;
        this.mainTime = mainTime;
        this.offenceTime = offenceTime;
        this.exclusionTime = exclusionTime;

        this.timeoutsHome = (int) timeoutsHome;
        this.timeoutsGuest = (int) timeoutsGuest;

        this.goalsHome = (int) goalsHome;
        this.goalsGuest = (int) goalsGuest;

        startServer();
    }
    public WaterPoloTimer(WaterpoloClock activity) {
        this(activity, 0, false, 0, 0, 0,
                0, 0, 0, 0,
                new long[][] {{-11000, -11000, -11000}, {-11000, -11000, -11000}});
        setTimersToStartOfPeriod(0);
    }

    public void startServer(){
        if (server == null || !server.isRunning()) {
            try {
                server = new WaterpoloClockServer(this);
            } catch (IOException e) {
                log.warn("Caught an IOException", e);
            } catch (RuntimeException e) {
                log.warn("Server not started. " + e.getMessage(), e);
            }
        }
    }

    public void stopServer(){
        if (server != null) {
            server.stop();
        }
    }

    void dispose(){
        stopServer();
        server = null;
    }

    private void setTimersToStartOfPeriod(int i){
        lastTimerUpdateTime = System.currentTimeMillis();
        period = i;

        timeout = Long.MIN_VALUE;
        if (period < AppSettings.NUMBER_OF_PERIODS.value) {
            mainTime = minutes2ms(AppSettings.PERIOD_DURATION.value);
            offenceTime = seconds2ms(AppSettings.OFFENCE_TIME_DURATION.value);
        }else{
            mainTime = 0;
            offenceTime = 0;
        }
    }

    private void setTimersToStartOfBreak(){
        lastTimerUpdateTime = System.currentTimeMillis();
        if (period < AppSettings.NUMBER_OF_PERIODS.value - 1){
            if (period + 1 == AppSettings.NUMBER_OF_PERIODS.value / 2)
                mainTime = minutes2ms(AppSettings.HALF_TIME_DURATION.value);
            else
                mainTime = minutes2ms(AppSettings.BREAK_TIME_DURATION.value);
        }
        else
            mainTime = 0;
        offenceTime = 0;
    }

    void updateTime() {
        long currentTime = System.currentTimeMillis();
        if (timerRunning) {
            long timeDiff = currentTime - lastTimerUpdateTime;
            if (timeout == Long.MIN_VALUE) {
                mainTime = mainTime - timeDiff;
                if (!isBreak) {
                    offenceTime = offenceTime - timeDiff;

                    for (int i0 = 0; i0 < exclusionTime.length; i0++) {
                        for (int i1 = 0; i1 < exclusionTime[i0].length; i1++) {
                            exclusionTime[i0][i1] = exclusionTime[i0][i1] - timeDiff;
                        }
                    }
                }
                if (offenceTime <= TIMER_UPDATE_PERIOD) {
                    if (!isBreak) {
                        stop();
                        offenceTime = seconds2ms(AppSettings.OFFENCE_TIME_DURATION.value);
                        activity.trippleBeepSound("Offence-Time is over");
                        stop();
                    } else {
                        offenceTime = 0;
                    }
                }
                if (mainTime <= 0) {
                    isBreak = !isBreak;
                    if (isBreak) {
                        setTimersToStartOfBreak();
                    } else {
                        stop();
                        period++;
                        setTimersToStartOfPeriod(period);
                        stop();
                    }
                    activity.sound("Period or break is over");
                }
            }else{
                timeout = timeout - timeDiff;
                if (timeout <= seconds2ms(AppSettings.TIMEOUT_END_WARNING.value) +
                        TIMER_UPDATE_PERIOD && timeout > seconds2ms(AppSettings.TIMEOUT_END_WARNING.value)) {
                    activity.sound("Timeout end warning");
                }
                if (timeout <= TIMER_UPDATE_PERIOD) {
                    stop();
                    timeout = Long.MIN_VALUE;
                    activity.sound("Timeout is over");
                    stop();
                }
            }
        }
        lastTimerUpdateTime = currentTime;
    }

    static long minutes2ms(long minutes){
        return minutes * 60000;
    }

    static long seconds2ms(long seconds){
        return seconds * 1000;
    }

    void startTimeout(){
        timeout = seconds2ms(AppSettings.TIMEOUT_DURATION.value);
        timerRunning = true;
    }

    void start(){
        timerRunning = true;
    }

    void stop(){
        timerRunning = false;
        storeState();
    }
    private void storeState() {
        AppSettings.PERIOD.applyValue(getSharedPreferences(), period);
        AppSettings.IS_BREAK.applyValue(getSharedPreferences(), isBreak);
        AppSettings.MAIN_TIME.applyValue(getSharedPreferences(), mainTime);
        AppSettings.OFFENCE_TIME.applyValue(getSharedPreferences(), offenceTime);
        AppSettings.TIMEOUT.applyValue(getSharedPreferences(), timeout);
        AppSettings.TIMEOUTS_HOME.applyValue(getSharedPreferences(), timeoutsHome);
        AppSettings.TIMEOUTS_GUEST.applyValue(getSharedPreferences(), timeoutsGuest);
        AppSettings.GOALS_HOME.applyValue(getSharedPreferences(), goalsHome);
        AppSettings.GOALS_GUEST.applyValue(getSharedPreferences(), goalsGuest);
    }

    public SharedPreferences getSharedPreferences(){
        return AppSettings.getSharedPreferences(activity.getApplicationContext());
    }

    void startStop(){
        if (timerRunning){
            if ((isBreak || isTimeout()) && !AppSettings.STOP_BREAK_AND_TIMEOUT.value){
                return;
            }
            stop();
        }
        else
            start();
        if (timerRunning) {
            lastTimerUpdateTime = System.currentTimeMillis();
        }
    }

    boolean isTimeout(){
        return timeout != Long.MIN_VALUE;
    }

    void mainTimeAdd(int minutes, int seconds){
        long mainTime = this.mainTime + minutes2ms(minutes) + seconds2ms(seconds);
        if (isBreak){
            if (period + 1 == AppSettings.NUMBER_OF_PERIODS.value / 2)
                this.mainTime = Math.min(mainTime, minutes2ms(AppSettings.HALF_TIME_DURATION.value));
            else
                this.mainTime = Math.min(mainTime, minutes2ms(AppSettings.BREAK_TIME_DURATION.value));
        }
        else
            this.mainTime = Math.min(mainTime, minutes2ms(AppSettings.PERIOD_DURATION.value));
    }

    void mainTimeSub(int minutes, int seconds){
        long mainTime = this.mainTime - minutes2ms(minutes) - seconds2ms(seconds);
        this.mainTime = Math.max(mainTime, 0);
    }

    void offenceTimeAdd(int minutes, int seconds){
        long offenceTime = this.offenceTime + minutes2ms(minutes) + seconds2ms(seconds);
        this.offenceTime = Math.min(offenceTime, seconds2ms(AppSettings.OFFENCE_TIME_DURATION.value));
    }

    void offenceTimeSub(int minutes, int seconds){
        long offenceTime = this.offenceTime - minutes2ms(minutes) - seconds2ms(seconds);
        this.offenceTime = Math.max(offenceTime, 0);
    }

    void timeoutAdd(int minutes, int seconds){
        long timeout = this.timeout + minutes2ms(minutes) + seconds2ms(seconds);
        this.timeout = Math.min(timeout, seconds2ms(AppSettings.TIMEOUT_DURATION.value));
    }

    void timeoutSub(int minutes, int seconds){
        long timeout = this.timeout - minutes2ms(minutes) - seconds2ms(seconds);
        this.timeout = Math.max(timeout, 0);
    }

    void goalsHomeIncrement(){
        goalsHome++;
        if (AppSettings.RESET_SHOTCLOCK_ON_GOAL.value){
            resetOffenceTimeMajor();
        }
    }

    void goalsHomeDecrement(){
        if (goalsHome > 0)
            goalsHome--;
    }

    void goalsGuestIncrement(){
        goalsGuest++;
        if (AppSettings.RESET_SHOTCLOCK_ON_GOAL.value){
            resetOffenceTimeMajor();
        }
    }

    void goalsGuestDecrement(){
        if (goalsGuest > 0)
            goalsGuest--;
    }

    protected void resetOffenceTimeMajor(){
        offenceTime = seconds2ms(AppSettings.OFFENCE_TIME_DURATION.value);
    }
    protected void resetOffenceTimeMinor(){
        boolean offenceTimeMinorReset = AppSettings.OFFENCE_TIME_MINOR_DURATION_RESET.value;
        long offenceTimeMinorDuration = seconds2ms(AppSettings.OFFENCE_TIME_MINOR_DURATION.value);
        boolean resetOffenceTime = true;
        if (offenceTimeMinorReset) {
            resetOffenceTime = offenceTime < offenceTimeMinorDuration;
        }
        if (resetOffenceTime) {
            offenceTime = offenceTimeMinorDuration;
        }
    }

    protected void resetExclusionTimeHome(int i){
        resetExclusionTime(0, i);
    }

    protected void resetExclusionTimeGuest(int i){
        resetExclusionTime(1, i);
    }
    protected void resetExclusionTime(int i0, int i1){
        exclusionTime[i0][i1] = seconds2ms(
                AppSettings.EXCLUSION_TIME_DURATION.value);
    }

    void periodPlus(){
        if (timeout == Long.MIN_VALUE){
            timerRunning = false;
            if (isBreak){
                if (period < AppSettings.NUMBER_OF_PERIODS.value) {
                    isBreak = false;
                    period++;
                    setTimersToStartOfPeriod(period);
                }
            }
            else {
                isBreak = true;
                setTimersToStartOfBreak();
            }
        }
    }
    void periodMinus(){
        if (timeout == Long.MIN_VALUE){
            timerRunning = false;
            if (isBreak){
                isBreak = false;
                setTimersToStartOfPeriod(period);
            }
            else {
                if (period > 0) {
                    isBreak = true;
                    period--;
                    setTimersToStartOfBreak();
                }
            }
        }
    }

    String getPeriodString() {
        String periodString;
        if (isTimeout())
            periodString = "Timeout";
        else {
            if (period < AppSettings.NUMBER_OF_PERIODS.value - 1 ||
                    (period == AppSettings.NUMBER_OF_PERIODS.value - 1 && !isBreak)) {
                if (!isBreak)
                    periodString = "Period " + (period + 1);
                else {
                    if (period + 1 == AppSettings.NUMBER_OF_PERIODS.value / 2)
                        periodString = "Halftime";
                    else
                        periodString = "Break between periods " +
                                (period + 1) + " and " + (period + 2);
                }
            } else
                periodString = "End";
        }
        return periodString;
    }

    String getMainTimeString() {
        long mainTime = this.mainTime;
        if (isTimeout()) {
            mainTime = this.timeout;
        }
        String timeString;
        if (AppSettings.ENABLE_DECIMAL.value)
            timeString  = WaterPoloTimer.getMainTimeString(mainTime);
        else {
            if(AppSettings.ENABLE_DECIMAL_DURING_LAST.value)
                timeString =  WaterPoloTimer.getMainTimeStringDecimalDuringLast(mainTime);
            else
                timeString  = WaterPoloTimer.getMainTimeStringNoDecimal(mainTime);
        }
        return timeString;
    }
    static String getMainTimeString(long mainTime){
        SimpleDateFormat formatter;

        long mainTimeRounded = Math.round(new Double(mainTime).doubleValue() / 100) * 100;
        if (mainTime < TimeUnit.MINUTES.toMillis(10))
            formatter = new SimpleDateFormat("m:ss.S");
        else
            formatter = new SimpleDateFormat("mm:ss.S");

        Date date = new Date(mainTimeRounded);
        String timeString = formatter.format(date);

        return timeString;
    }
    public static String getMainTimeStringNoDecimal(long mainTime){
        SimpleDateFormat formatter;

        long mainTimeRounded = Math.round(new Double(mainTime).doubleValue() / 1000) * 1000;
        if (mainTime < TimeUnit.MINUTES.toMillis(10))
            formatter = new SimpleDateFormat("m:ss");
        else
            formatter = new SimpleDateFormat("mm:ss");

        Date date = new Date(mainTimeRounded);
        String timeString = formatter.format(date);

        return timeString;
    }
    public static String getMainTimeStringDecimalDuringLast(long mainTime){
        long mainTimeRounded;
        SimpleDateFormat formatter;

        if (mainTime < TimeUnit.MINUTES.toMillis(1)){
            mainTimeRounded = Math.round(new Double(mainTime).doubleValue() / 100) * 100;
            formatter = new SimpleDateFormat("ss.S");
        }
        else {
            mainTimeRounded = Math.round(new Double(mainTime).doubleValue() / 1000) * 1000;
            if (mainTime < TimeUnit.MINUTES.toMillis(10))
                formatter = new SimpleDateFormat("m:ss");
            else
                formatter = new SimpleDateFormat("mm:ss");
        }

        Date date = new Date(mainTimeRounded);
        String timeString = formatter.format(date);

        return timeString;
    }

    String getOffenceTimeString(){
        long offenceTime = this.offenceTime;
        String timeString;
        if (AppSettings.ENABLE_DECIMAL.value)
            timeString  = WaterPoloTimer.getOffenceTimeString(offenceTime);
        else{
            if(AppSettings.ENABLE_DECIMAL_DURING_LAST.value)
                timeString =  WaterPoloTimer.getOffenceTimeStringDecimalDuringLast(offenceTime);
            else
                timeString  = WaterPoloTimer.getOffenceTimeStringNoDecimal(offenceTime);
        }
        return timeString;
    }
    static String getOffenceTimeString(long time){
        SimpleDateFormat formatter;

        long timeRounded = Math.round(new Double(time).doubleValue() / 100) * 100;
        if (time < TimeUnit.MINUTES.toMillis(1)){
            formatter = new SimpleDateFormat("ss.S");
        }
        else {
            if (time < TimeUnit.MINUTES.toMillis(10))
                formatter = new SimpleDateFormat("m:ss.S");
            else
                formatter = new SimpleDateFormat("mm:ss.S");
        }

        Date date = new Date(timeRounded);
        String timeString = formatter.format(date);

        return timeString;
    }
    static String getOffenceTimeStringNoDecimal(long time){
        SimpleDateFormat formatter;

        long timeRounded = Math.round(new Double(time).doubleValue() / 1000) * 1000;
        if (time < TimeUnit.MINUTES.toMillis(1)){
            formatter = new SimpleDateFormat("ss");
        }
        else {
            if (time < TimeUnit.MINUTES.toMillis(10))
                formatter = new SimpleDateFormat("m:ss");
            else
                formatter = new SimpleDateFormat("mm:ss");
        }

        Date date = new Date(timeRounded);
        String timeString = formatter.format(date);

        return timeString;
    }
    public static String getOffenceTimeStringDecimalDuringLast(long time){
        long timeRounded;
        SimpleDateFormat formatter;

        if (time < TimeUnit.SECONDS.toMillis(10)){
            timeRounded = Math.round(new Double(time).doubleValue() / 100) * 100;
            formatter = new SimpleDateFormat("s.S");
        }
        else {
            timeRounded = Math.round(new Double(time).doubleValue() / 1000) * 1000;
            if (time < TimeUnit.MINUTES.toMillis(1)){
                formatter = new SimpleDateFormat("ss");
            }
            else {
                if (time < TimeUnit.MINUTES.toMillis(10))
                    formatter = new SimpleDateFormat("m:ss");
                else
                    formatter = new SimpleDateFormat("mm:ss");
            }
        }

        Date date = new Date(timeRounded);
        String timeString = formatter.format(date);

        return timeString;
    }
    static String getExclusionTimeString(long time){
        SimpleDateFormat formatter;

        long timeRounded = Math.round(new Double(time).doubleValue() / 1000) * 1000;
        formatter = new SimpleDateFormat("ss");

        Date date = new Date(timeRounded);
        String timeString = formatter.format(date);

        return timeString;
    }

    long getExclusionTime(int i0, int i1) {
        long time_ms = this.exclusionTime[i0][i1];
        return time_ms;
    }

    String getGoalsHomeString() {
        return getGoalsString(goalsHome);
    }
    String getGoalsGuestString() {
        return getGoalsString(goalsGuest);
    }
    static String getGoalsString(int goals) {
        String text = Integer.toString(goals);
        text = text.length() > 1 ? text : "0" + text;
        return text;
    }

}
