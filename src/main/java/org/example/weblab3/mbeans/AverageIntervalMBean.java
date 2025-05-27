package org.example.weblab3.mbeans;

import javax.management.ObjectName;
import java.util.Date;

public interface AverageIntervalMBean {
    Long getAverageInterval();
    void click(Date currTime);
    String getError();
    static ObjectName getObjectName() throws Exception{
        return new ObjectName("org.example.weblab3.mbeans:type=AverageIntervalMBean");
    }
}
