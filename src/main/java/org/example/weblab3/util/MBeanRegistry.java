package org.example.weblab3.util;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.example.weblab3.mbeans.AverageInterval;
import org.example.weblab3.mbeans.AverageIntervalMBean;

import javax.management.MBeanServer;
import java.lang.management.ManagementFactory;

@WebListener
public class MBeanRegistry implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
            AverageInterval averageBean = new AverageInterval();
            mbs.registerMBean(averageBean, AverageIntervalMBean.getObjectName());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        try{
            MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
            mbs.unregisterMBean(AverageIntervalMBean.getObjectName());
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
