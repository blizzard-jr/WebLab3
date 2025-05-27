package org.example.weblab3.mbeans;

import javax.management.ObjectName;

public interface HitAdminMBean {
    long getCommonCount();
    Long getFailCount();
    void ToNotify();
    static ObjectName getObjectName() throws Exception{
        return new ObjectName("org.example.weblab3.mbeans:type=HitAdminMBean");
    }
}
