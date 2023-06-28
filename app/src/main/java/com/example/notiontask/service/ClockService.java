package com.example.notiontask.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import java.util.Locale;

public class ClockService extends Service {
    private final IBinder binder;
    private Handler handler;
    private Runnable runnable;
    private int elapsedTimeInSeconds;

    public ClockService() {
        binder = new ClockBinder(this);
    }

    public int getElapsedTimeInSeconds(){
        return elapsedTimeInSeconds;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        handler = new Handler();
        elapsedTimeInSeconds = 0;
        startClock();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    private void startClock() {
        runnable = new Runnable() {
            @Override
            public void run() {
                elapsedTimeInSeconds++;
                updateClock();
                handler.postDelayed(this, 1000);
            }
        };
        handler.postDelayed(runnable, 1000);
    }

    private void updateClock() {
        String currentTime = formatTime(elapsedTimeInSeconds);

        Intent intent = new Intent("CLOCK_UPDATE");
        intent.putExtra("current_time", currentTime);
        intent.putExtra("elapsed_time", elapsedTimeInSeconds);
        sendBroadcast(intent);
    }

    private String formatTime(int elapsedTimeInSeconds) {
        int hours = elapsedTimeInSeconds / 3600;
        int minutes = (elapsedTimeInSeconds % 3600) / 60;
        int seconds = elapsedTimeInSeconds % 60;

        return String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }
}