package com.example.OKM.domain.task;

import android.os.Handler;
import com.example.OKM.domain.service.LocationHelper;

import java.util.concurrent.Callable;

/**
 * Created by Jakub on 04.10.2015.
 */
public class TimerTask implements Runnable {
    private final Handler handler;
    private final Callable callback;
    private int time;

    public TimerTask(final Handler handler, final int time, final Callable callable){
        this.handler = handler;
        this.callback = callable;
        this.time = time;
    }

    @SuppressWarnings("ObjectAllocationInLoop")
    @Override
    public void run() {
        while(!Thread.currentThread().isInterrupted()){
            try{
                Thread.sleep(this.time);
            } catch (final InterruptedException e){
                e.printStackTrace();
                break;
            }
            this.handler.post(new Runnable() {
                @Override
                public void run() {
                    try{
                        TimerTask.this.callback.call();
                    } catch (final Exception e){
                        e.printStackTrace();
                    }
                }
            });

            this.time = LocationHelper.getInterval();
        }
    }
}
