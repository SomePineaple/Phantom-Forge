package dev.somepineaple.phantom.main.utils;

public class MSTimer {
    private long lastMS = System.currentTimeMillis();

    public void reset() {
        lastMS = System.currentTimeMillis();
    }

    public boolean hasTimePassed(long time) {
        return System.currentTimeMillis() - lastMS > time;
    }

    public long timePassed() {
        return System.currentTimeMillis() - lastMS;
    }

    public void setLastMS(long lastMS) {
        this.lastMS = lastMS;
    }
}
