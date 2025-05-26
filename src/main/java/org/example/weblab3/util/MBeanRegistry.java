package org.example.weblab3.util;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import javax.management.MBeanServer;
import java.lang.management.ManagementFactory;

@WebListener
public class MBeanRegistry implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
    }
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }
}
