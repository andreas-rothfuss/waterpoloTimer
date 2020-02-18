package de.wasserball.wabaclock.main;

import static de.wasserball.wabaclock.main.WaterPoloTimer.minutes2ms;
import static de.wasserball.wabaclock.main.WaterPoloTimer.seconds2ms;

public class CountdownTimer {

    long time;
    long lastUpdateTime;
    boolean isRunning;

    public CountdownTimer(long initialValue_ms){
        time = initialValue_ms;
        lastUpdateTime = System.currentTimeMillis();
        isRunning = false;
    }

    public CountdownTimer(long initialValue_ms, CountdownTimer other){
        time = initialValue_ms;
        lastUpdateTime = other.lastUpdateTime;
        isRunning = other.isRunning;
    }

    public void update(long currentTime){
        if (isRunning) {
            time = time - currentTime - lastUpdateTime;;
        }
    }

    public void setLastTimerUpdateTime(long time){
        lastUpdateTime = time;
    }

    public long getTime(){
        return time;
    }

    public void start(){
        isRunning = true;
    }

    public void stop(){
        isRunning = false;
    }

    public void add(int minutes, int seconds, long maxVal){
        time = time + minutes2ms(minutes) + seconds2ms(seconds);
        if (getTime() >  maxVal)
            time = maxVal;
    }

    public void sub(int minutes, int seconds){
        time = Math.max(time - minutes2ms(minutes) - seconds2ms(seconds), 0);
    }
}
