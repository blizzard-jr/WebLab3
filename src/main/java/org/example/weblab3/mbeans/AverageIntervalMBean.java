package org.example.weblab3.mbeans;

import java.util.Date;

public interface AverageIntervalMBean {
    Date getAverageInterval();
    void click(Date currTime);
}
