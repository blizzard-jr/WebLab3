package org.example.weblab3.mbeans;

import jakarta.annotation.PostConstruct;

import java.io.Serializable;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

public class AverageInterval implements AverageIntervalMBean, Serializable {
    private AtomicLong prev = new AtomicLong(new Date().getTime());
    private AtomicLong averageInterval = new AtomicLong();
    private String error = "";

    @Override
    public Date getAverageInterval() {
        return new Date(this.averageInterval.get());
    }

    @Override
    public synchronized void click(Date currTime) {
        error = "pauuuuuuuuu";
        this.averageInterval.set((Math.abs(currTime.getTime() - prev.get()) + averageInterval.get()) / 2);
        prev.set(currTime.getTime());
    }

    @Override
    public String getError(){
        return error;
    }
}
