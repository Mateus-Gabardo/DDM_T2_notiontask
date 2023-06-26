package com.example.notiontask.service;

import android.os.Binder;

public class ClockBinder extends Binder {
    private ClockService clockService;
    private String timer;

    public ClockBinder(ClockService service) {
        clockService = service;
    }

    public ClockService getService() {
        return clockService;
    }

    public String getTimer() {
        return timer;
    }

    public void setTimer(String timer) {
        this.timer = timer;
    }
}
