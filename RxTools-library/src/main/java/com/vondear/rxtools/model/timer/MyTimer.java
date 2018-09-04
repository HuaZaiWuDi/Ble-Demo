package com.vondear.rxtools.model.timer;


import android.os.Looper;

import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by Oden on 2016/6/16.
 */
public class MyTimer {
    private Timer timer = null;
    private TimerTask task = null;
    private MyTimerListener myTimerListener;
    private long period = 1000;
    private long delay = 0;
    private android.os.Handler sHandler = new android.os.Handler(Looper.getMainLooper());

    public MyTimer(long delay, long period, MyTimerListener l) {
        this.delay = delay;
        this.period = period;
        this.myTimerListener = l;
    }

    public MyTimer(long period, MyTimerListener myTimerListener) {
        this.myTimerListener = myTimerListener;
        this.period = period;
    }

    public MyTimer(MyTimerListener myTimerListener, long delay) {
        this.myTimerListener = myTimerListener;
        this.delay = delay;
        this.period = 1000;
    }


    public void setPeriodAndDelay(long period, long delay) {
        this.period = period;
        this.delay = delay;
    }

    public MyTimer(MyTimerListener myTimerListener) {
        this.myTimerListener = myTimerListener;
    }

    public void startTimer() {
        if (timer == null) {
            timer = new Timer();
            task = new TimerTask() {
                @Override
                public void run() {
                    sHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            myTimerListener.enterTimer();
                        }
                    });
                }
            };
            if (period != 0)
                timer.schedule(task, delay, period);
            else
                timer.schedule(task, delay);
        }
    }


    public void stopTimer() {
        if (timer != null) {
            task.cancel();
            timer.cancel();

            task = null;
            timer = null;
        }
    }

}
