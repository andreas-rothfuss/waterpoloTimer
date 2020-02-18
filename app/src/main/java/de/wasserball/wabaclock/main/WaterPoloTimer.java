package de.wasserball.wabaclock.main;

import android.content.SharedPreferences;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import de.wasserball.wabaclock.settings.AppSettings;

public class WaterPoloTimer {

    public static final String SHOT_CLOCK_DEVICE_NAME = "ShotClock";
    public static final String MAIN_TIME_DEVICE_NAME = "MainTime";
    public static final String SCOREBOARD_DEVICE_NAME = "Scoreboard";
    public static final String HOME_TEAM_DEVICE_NAME = "HomeTeam";
    public static final String GUEST_TEAM_DEVICE_NAME = "GuestTeam";

    public static final int TIMER_UPDATE_PERIOD = 50;

    WaterpoloclockServer server;

    private final WaterpoloClock activity;

    private boolean timerRunning = false;
    private boolean offenseTimerRunning = false;

    int period;
    boolean isBreak = false;

    CountdownTimer timeout;
    CountdownTimer mainTime;
    CountdownTimer offenceTime;

    long lastTimerUpdateTime;
    long lastOffenseTimerUpdateTime;

    int timeoutsHome;
    int timeoutsGuest;

    int goalsHome;
    int goalsGuest;

    Logger log = LoggerFactory.getLogger(WaterPoloTimer.class);

    public WaterPoloTimer(WaterpoloClock activity, int period, boolean isBreak, long mainTime,
                          long offenceTime, long timeout, int timeoutsHome, int timeoutsGuest,
                          int goalsHome, int goalsGuest) {
        this.activity = activity;

        lastTimerUpdateTime = System.currentTimeMillis();
        this.period = (int) period;

        this.isBreak = isBreak;

        this.timeout = null;
        this.mainTime = new CountdownTimer(mainTime);
        this.offenceTime = new CountdownTimer(offenceTime);

        this.timeoutsHome = timeoutsHome;
        this.timeoutsGuest = timeoutsGuest;

        this.goalsHome = goalsHome;
        this.goalsGuest = goalsGuest;

        try {
            server = new WaterpoloclockServer(this);
        } catch (IOException e) {
            log.warn("Caught an IOException", e);
        }catch (RuntimeException e) {
            log.warn("Server not started. " + e.getMessage(), e);
        }
    }
    public WaterPoloTimer(WaterpoloClock activity) {
        this(activity, 0, false, 0, 0, 0,
                0, 0, 0, 0);
        setTimersToStartOfPeriod(0);
    }


    void dispose(){
        if (server != null) {
            server.stop();
            server = null;
        }
    }

    private void setLastTimerUpdateTimes(long currentTime) {
        if (timeout != null)
            timeout.setLastTimerUpdateTime(currentTime);
        if (mainTime != null)
            mainTime.setLastTimerUpdateTime(currentTime);
        if (offenceTime != null)
            offenceTime.setLastTimerUpdateTime(currentTime);
    }

    private void setTimersToStartOfPeriod(int i){
        setLastTimerUpdateTimes(System.currentTimeMillis());
        period = i;

        timeout = null;
        if (period < AppSettings.NUMBER_OF_PERIODS.value) {
            mainTime = new CountdownTimer(minutes2ms(AppSettings.PERIOD_DURATION.value));
            offenceTime = new CountdownTimer(seconds2ms(AppSettings.OFFENCE_TIME_DURATION.value));
        }else{
            mainTime = null;
            offenceTime = null;
        }
    }

    private void setTimersToStartOfBreak(){
        setLastTimerUpdateTimes(System.currentTimeMillis());
        if (period < AppSettings.NUMBER_OF_PERIODS.value - 1){
            if (period + 1 == AppSettings.NUMBER_OF_PERIODS.value / 2)
                mainTime = new CountdownTimer(minutes2ms(AppSettings.HALF_TIME_DURATION.value));
            else
                mainTime = new CountdownTimer(minutes2ms(AppSettings.BREAK_TIME_DURATION.value));
        }
        else
            mainTime = null;
        offenceTime = null;
    }

    void updateTime() {
        long currentTime = System.currentTimeMillis();
        if (timeout == null) {
            if (!isBreak)
                if (offenceTime != null)
                    offenceTime.update(currentTime);
            if (offenceTime != null)
                mainTime.update(currentTime);

            if (offenceTime == null || offenceTime.getTime() <= TIMER_UPDATE_PERIOD) {
                if (!isBreak) {
                    stop();
                    offenceTime = new CountdownTimer(seconds2ms(AppSettings.OFFENCE_TIME_DURATION.value));
                    activity.sound("Offence-Time is over");
                    stop();
                } else {
                    offenceTime = null;
                }
            }
            if (mainTime == null || mainTime.getTime() <= 0) {
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
            timeout.update(currentTime);
            long timeoutTime = timeout.getTime();
            if (timeoutTime <= seconds2ms(AppSettings.TIMEOUT_END_WARNING.value) +
                    TIMER_UPDATE_PERIOD && timeoutTime > seconds2ms(AppSettings.TIMEOUT_END_WARNING.value)) {
                activity.sound("Timeout end warning");
            }
            if (timeoutTime <= TIMER_UPDATE_PERIOD) {
                stop();
                timeout = null;
                activity.sound("Timeout is over");
                stop();
            }
        }

        setLastTimerUpdateTimes(currentTime);
    }

    static long minutes2ms(long minutes){
        return minutes * 60000;
    }

    static long seconds2ms(long seconds){
        return seconds * 1000;
    }

    void startTimeout(){
        timeout = new CountdownTimer(seconds2ms(AppSettings.TIMEOUT_DURATION.value));
        timeout.start();
        if (mainTime != null) mainTime.stop();
        if (offenceTime != null) offenceTime.stop();
    }

    void start(){
        if (mainTime != null) mainTime.start();
        if (offenceTime != null) offenceTime.start();
    }

    void stop(){
        if (mainTime != null) mainTime.stop();
        if (offenceTime != null) offenceTime.stop();
        storeState();
    }
    private void storeState() {
        AppSettings.PERIOD.applyValue(getSharedPreferences(), period);
        AppSettings.IS_BREAK.applyValue(getSharedPreferences(), isBreak);
        AppSettings.MAIN_TIME.applyValue(getSharedPreferences(), mainTime != null ? mainTime.getTime() : 0);
        AppSettings.OFFENCE_TIME.applyValue(getSharedPreferences(), offenceTime != null ? offenceTime.getTime() : 0);
        AppSettings.TIMEOUT.applyValue(getSharedPreferences(), timeout != null ? timeout.getTime() : 0);
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
        return timeout != null;
    }

    void mainTimeAdd(int minutes, int seconds){
        if (mainTime != null) {
            long maxTimerVal;
            if (isBreak) {
                if (period + 1 == AppSettings.NUMBER_OF_PERIODS.value / 2)
                    maxTimerVal = minutes2ms(AppSettings.HALF_TIME_DURATION.value);
                else
                    maxTimerVal = minutes2ms(AppSettings.BREAK_TIME_DURATION.value);
            } else
                maxTimerVal = minutes2ms(AppSettings.PERIOD_DURATION.value);

            mainTime.add(minutes, seconds, maxTimerVal);
        }
    }

    void mainTimeSub(int minutes, int seconds){
        if (mainTime != null) mainTime.sub(minutes, seconds);
    }

    void offenceTimeAdd(int minutes, int seconds){
        if (offenceTime != null) offenceTime.add(minutes, seconds, seconds2ms(AppSettings.OFFENCE_TIME_DURATION.value));
    }

    void offenceTimeSub(int minutes, int seconds){
        if (offenceTime != null) offenceTime.sub(minutes, seconds);
    }

    void timeoutAdd(int minutes, int seconds){
        if (timeout != null) timeout.add(minutes, seconds, seconds2ms(AppSettings.TIMEOUT_DURATION.value));
    }

    void timeoutSub(int minutes, int seconds){
        if (timeout != null) timeout.sub(minutes, seconds);
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
        offenceTime = new CountdownTimer(
                seconds2ms(AppSettings.OFFENCE_TIME_DURATION.value), offenceTime);
    }
    protected void resetOffenceTimeMinor(){
        offenceTime = new CountdownTimer(
                seconds2ms(AppSettings.OFFENCE_TIME_MINOR_DURATION.value), offenceTime);
    }

    void periodPlus(){
        if (!isTimeout()){
            stop();
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
        if (!isTimeout()){
            stop();
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
        long time = 0;
        if (timeout != null)
            time = this.timeout.getTime();
        else
            if (mainTime != null) time = mainTime.getTime();
        String timeString;
        if (AppSettings.ENABLE_DECIMAL.value)
            timeString  = WaterPoloTimer.getMainTimeString(time);
        else
            timeString  = WaterPoloTimer.getMainTimeStringNoDecimal(time);
        return timeString;
    }
    static String getMainTimeString(long mainTime){
        if (mainTime > 600000) {
            return String.format("%02d:%02d.%01d",
                    TimeUnit.MILLISECONDS.toMinutes(mainTime) -
                            TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(mainTime)),
                    TimeUnit.MILLISECONDS.toSeconds(mainTime) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(mainTime)),
                    mainTime % 1000 / 100);
        }
        else {
            return String.format("%2d:%02d.%01d",
                    TimeUnit.MILLISECONDS.toMinutes(mainTime) -
                            TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(mainTime)),
                    TimeUnit.MILLISECONDS.toSeconds(mainTime) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(mainTime)),
                    mainTime % 1000 / 100);
        }
    }
    public static String getMainTimeStringNoDecimal(long mainTime){
        if (mainTime > 600000) {
            return String.format("%02d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(mainTime) -
                            TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(mainTime)),
                    TimeUnit.MILLISECONDS.toSeconds(mainTime) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(mainTime)));
        }
        else {
            return String.format("%2d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(mainTime) -
                            TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(mainTime)),
                    TimeUnit.MILLISECONDS.toSeconds(mainTime) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(mainTime)));
        }
    }

    String getOffenceTimeString(){
        long time =  0;
        if (offenceTime != null) time = offenceTime.getTime();
        String timeString;
        if (AppSettings.ENABLE_DECIMAL.value)
            timeString  = WaterPoloTimer.getOffenceTimeString(time);
        else
            timeString  = WaterPoloTimer.getOffenceTimeStringNoDecimal(time);
        return timeString;
    }
    static String getOffenceTimeString(long offenceTime){
        String textOffenceTime;
        if (offenceTime > minutes2ms(1))
            textOffenceTime= String.format("%02d:%02d.%01d",
                    TimeUnit.MILLISECONDS.toMinutes(offenceTime) -
                            TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(offenceTime)),
                    TimeUnit.MILLISECONDS.toSeconds(offenceTime) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(offenceTime)),
                    offenceTime % 1000 / 100);
        else
            textOffenceTime= String.format("%02d.%01d",
                    TimeUnit.MILLISECONDS.toSeconds(offenceTime) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(offenceTime)),
                    offenceTime % 1000 / 100);
        return textOffenceTime;
    }
    static String getOffenceTimeStringNoDecimal(long offenceTime){
        String textOffenceTime;
        if (offenceTime > minutes2ms(1))
            textOffenceTime= String.format("%02d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(offenceTime) -
                            TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(offenceTime)),
                    TimeUnit.MILLISECONDS.toSeconds(offenceTime) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(offenceTime)));
        else
            textOffenceTime= String.format("%02d",
                    TimeUnit.MILLISECONDS.toSeconds(offenceTime) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(offenceTime)));
        return textOffenceTime;
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
