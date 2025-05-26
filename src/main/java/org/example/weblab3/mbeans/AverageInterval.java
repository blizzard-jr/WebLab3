package org.example.weblab3.mbeans;

import jakarta.annotation.PostConstruct;

import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

public class AverageInterval implements AverageIntervalMBean{
    private AtomicLong prev;
    private AtomicLong averageInterval;

    @Override
    public Date getAverageInterval() {
        return new Date (this.averageInterval.get());
    }

    @Override
    public synchronized void click(Date currTime) {
        this.averageInterval.set((Math.abs(currTime.getTime() - prev.get()) + averageInterval.get()) / 2);
        prev.set(currTime.getTime());
    }
}
