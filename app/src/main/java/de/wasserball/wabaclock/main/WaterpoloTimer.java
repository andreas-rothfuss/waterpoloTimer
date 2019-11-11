package de.wasserball.wabaclock.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import de.wasserball.wabaclock.settings.WaterpoloTimerSettings;

public class WaterpoloTimer {

    public static final String SHOTCLOCK_DEVICE_NAME = "Shotclock";
    public static final String MAIN_TIME_DEVICE_NAME = "MainTime";
    public static final String SCOREBOARD_DEVICE_NAME = "Scoreboard";
    public static final String HOME_TEAM_DEVICE_NAME = "HomeTeam";
    public static final String GUEST_TEAM_DEVICE_NAME = "GuestTeam";

    public static final int TIMER_UPDATE_PERIOD = 50;

    WaterpoloclockServer server;

    private final WaterpoloClock activity;

    private boolean timerRunning = false;

    int period;
    boolean isBreak = false;

    long timeout;
    long mainTime;
    long offenceTime;

    long lastTimerUpdateTime;

    int timeoutsHome;
    int timeoutsGuest;

    int goalsHome;
    int goalsGuest;

    Logger log = LoggerFactory.getLogger(WaterpoloTimer.class);

    public WaterpoloTimer(WaterpoloClock activity) {
        this.activity = activity;

        setTimersToStartOfPeriod(0);

        timeoutsHome = 0;
        timeoutsGuest = 0;

        goalsHome = 0;
        goalsGuest = 0;

        try {
            server = new WaterpoloclockServer(this);
        } catch (IOException e) {
            log.warn("Caught an IOException", e);
        }catch (RuntimeException e) {
            log.warn("Server not started. " + e.getMessage(), e);
        }
    }


    void dispose(){
        if (server != null) {
            server.stop();
            server = null;
        }
    }

    private void setTimersToStartOfPeriod(int i){
        lastTimerUpdateTime = System.currentTimeMillis();
        period = i;

        timeout = Long.MIN_VALUE;
        if (period < WaterpoloTimerSettings.NUMBER_OF_PERIODS.value) {
            mainTime = minutes2ms(WaterpoloTimerSettings.PERIOD_DURATION.value);
            offenceTime = seconds2ms(WaterpoloTimerSettings.OFFENCE_TIME_DURATION.value);
        }else{
            mainTime = 0;
            offenceTime = 0;
        }
    }

    private void setTimersToStartOfBreak(){
        lastTimerUpdateTime = System.currentTimeMillis();
        if (period < WaterpoloTimerSettings.NUMBER_OF_PERIODS.value - 1){
            if (period + 1 == WaterpoloTimerSettings.NUMBER_OF_PERIODS.value / 2)
                mainTime = minutes2ms(WaterpoloTimerSettings.HALF_TIME_DURATION.value);
            else
                mainTime = minutes2ms(WaterpoloTimerSettings.BREAK_TIME_DURATION.value);
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
                offenceTime = offenceTime - timeDiff;

                if (offenceTime <= TIMER_UPDATE_PERIOD) {
                    if (!isBreak) {
                        timerRunning = false;
                        offenceTime = seconds2ms(WaterpoloTimerSettings.OFFENCE_TIME_DURATION.value);
                        activity.sound("Offence-Time is over");
                    } else {
                        offenceTime = 0;
                    }
                }
                if (mainTime <= 0) {
                    isBreak = !isBreak;
                    if (isBreak) {
                        setTimersToStartOfBreak();
                    } else {
                        timerRunning = false;
                        period++;
                        setTimersToStartOfPeriod(period);
                    }
                    activity.sound("Period or break is over");
                }
            }else{
                timeout = timeout - timeDiff;
                if (timeout <= seconds2ms(WaterpoloTimerSettings.TIMEOUT_END_WARNING.value) +
                        TIMER_UPDATE_PERIOD && timeout > seconds2ms(WaterpoloTimerSettings.TIMEOUT_END_WARNING.value)) {
                    activity.sound("Timeout end warning");
                }
                if (timeout <= TIMER_UPDATE_PERIOD) {
                    timerRunning = false;
                    timeout = Long.MIN_VALUE;
                    activity.sound("Timeout is over");
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
        timeout = seconds2ms(WaterpoloTimerSettings.TIMEOUT_DURATION.value);
        timerRunning = true;
    }

    void stop(){
        timerRunning = false;
    }

    void startStop(){
        timerRunning = !timerRunning;
        if (timerRunning) {
            lastTimerUpdateTime = System.currentTimeMillis();
        }
    }

    boolean isTimeout(){
        return timeout != Long.MIN_VALUE;
    }

    void mainTimeAdd(int minutes, int seconds){
        mainTime = mainTime + minutes2ms(minutes) + seconds2ms(seconds);
    }

    void mainTimeSub(int minutes, int seconds){
        mainTime = mainTime - minutes2ms(minutes) - seconds2ms(seconds);
    }

    void offenceTimeAdd(int minutes, int seconds){
        offenceTime = offenceTime + minutes2ms(minutes) + seconds2ms(seconds);
    }

    void offenceTimeSub(int minutes, int seconds){
        offenceTime = offenceTime - minutes2ms(minutes) - seconds2ms(seconds);
    }

    void timeoutAdd(int minutes, int seconds){
        timeout = timeout + minutes2ms(minutes) + seconds2ms(seconds);
    }

    void timeoutSub(int minutes, int seconds){
        timeout = timeout - minutes2ms(minutes) - seconds2ms(seconds);
    }

    protected void resetOffenceTimeMajor(){
        offenceTime = seconds2ms(WaterpoloTimerSettings.OFFENCE_TIME_DURATION.value);
    }
    protected void resetOffenceTimeMinor(){
        offenceTime = seconds2ms(
                WaterpoloTimerSettings.OFFENCE_TIME_MINOR_DURATION.value);
    }

    void periodPlus(){
        if (timeout == Long.MIN_VALUE){
            timerRunning = false;
            if (isBreak){
                if (period < WaterpoloTimerSettings.NUMBER_OF_PERIODS.value) {
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
            if (period < WaterpoloTimerSettings.NUMBER_OF_PERIODS.value - 1 ||
                    (period == WaterpoloTimerSettings.NUMBER_OF_PERIODS.value - 1 && !isBreak)) {
                if (!isBreak)
                    periodString = "Period " + (period + 1);
                else {
                    if (period + 1 == WaterpoloTimerSettings.NUMBER_OF_PERIODS.value / 2)
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
        if (WaterpoloTimerSettings.ENABLE_DECIMAL.value)
            timeString  = WaterpoloTimer.getMainTimeString(mainTime);
        else
            timeString  = WaterpoloTimer.getMainTimeStringNoDecimal(mainTime);
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
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(mainTime) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(mainTime)),
                TimeUnit.MILLISECONDS.toSeconds(mainTime) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(mainTime)));
    }

    String getOffenceTimeString(){
        long offenceTime = this.offenceTime;
        String timeString;
        if (WaterpoloTimerSettings.ENABLE_DECIMAL.value)
            timeString  = WaterpoloTimer.getOffenceTimeString(offenceTime);
        else
            timeString  = WaterpoloTimer.getOffenceTimeStringNoDecimal(offenceTime);
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
