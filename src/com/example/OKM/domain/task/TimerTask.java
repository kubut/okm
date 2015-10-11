package com.example.OKM.domain.task;

import android.os.Handler;
import com.example.OKM.domain.service.LocationHelper;

import java.util.concurrent.Callable;

/**
 * Created by Jakub on 04.10.2015.
 */
public class TimerTask implements Runnable {
    private Handler handler;
    private Callable callback;
    private int time;

    public TimerTask(Handler handler, int time, Callable callable){
        this.handler = handler;
        this.callback = callable;
        this.time = time;
    }

    @Override
    public void run() {
        while(!Thread.currentThread().isInterrupted()){
            try{
                Thread.sleep(this.time);
            } catch (InterruptedException e){
                e.printStackTrace();
                break;
            }
            this.handler.post(new Runnable() {
                @Override
                public void run() {
                    try{
                        callback.call();
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });

            this.time = LocationHelper.getInterval();
        }
    }
}
