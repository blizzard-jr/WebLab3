package org.example.weblab3;

import org.example.weblab3.mbeans.AverageIntervalMBean;
import org.example.weblab3.mbeans.HitAdmin;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;

import javax.management.MBeanServer;
import java.io.Serializable;
import java.lang.management.ManagementFactory;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.logging.Logger;
import java.util.List;


@Named("mainBean")
@ViewScoped
public class MainBean implements Serializable {
    private static final Logger logger = Logger.getLogger(MainBean.class.getName());

    @Inject
    private ResultManager resultManager;

    @Inject
    private HitAdmin hitAdmin;

    private Double x;
    private Double y;
    private Double r;

    public String checkPoint() {
        long startTime = System.nanoTime();
        logger.info("Начало проверки точки");
        logger.info(getX().getClass().getName());
        logger.info(getX().toString());
        PointResult result = new PointResult();
        result.setX(x);
        result.setY(y);
        result.setR(r);
        result.setCheckTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        try {
            Object res = mbs.invoke(AverageIntervalMBean.getObjectName(), "click", new Object[]{new Date()}, new String[]{"java.util.Date"});
        } catch (Exception e){
            logger.info(e.toString());
        }
        // Логика проверки попадания точки
        boolean hits = checkArea(x, y, r);
        result.setResult(hits);

        result.setExecutionTime(System.nanoTime() - startTime);

        resultManager.addResult(result);

        logger.info("Проверка завершена. Результат: " + hits + ", Время выполнения: " + result.getExecutionTime() + " нс");

        return null;
    }



    // Геттеры и сеттеры

        private boolean checkArea(Double x, Double y, Double r) {
            if (x == 0 && y == 0) {
                return true;
            } else if (x < 0 && y > 0) {
                return false;
            } else if (x > 0 && y < 0) {
                return x <= r / 2 && Math.abs(y) <= r;
            } else if (x < 0 && y < 0) {
                return r * r >= x * x + y * y;
            } else if (x > 0 && y > 0) {
                double func = -4 * x - r;
                return func <= y;
            } else {
                return false;
            }

        }

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        logger.info("Установка X: " + x);
        logger.info(x.getClass().getName());
        this.x = x;
    }
    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        logger.info("Установка Y: " + y);
        logger.info(y.getClass().getName());
        this.y = y;
    }

    public Double getR() {
        return r;
    }

    private void updatePointsForNewR(Double newR) {
        if (newR != null && resultManager != null) {
            List<PointResult> allPoints = resultManager.getAllResults();
            // Сначала получаем уведомление от MBean с текущими значениями
            String notification = hitAdmin.ToNotify();
            if (!notification.isEmpty()) {
                logger.info("Уведомление при изменении R: " + notification);
                // Здесь можно добавить логику отображения уведомления пользователю
            }
            
            // Затем обновляем точки
            for (PointResult point : allPoints) {
                boolean newResult = checkArea(point.getX(), point.getY(), newR);
                point.setResult(newResult);
                resultManager.updateResult(point);
            }
        }
    }

    public void setR(Double r) {
        logger.info("Установка R: " + r);
        logger.info(r.getClass().getName());
        if (this.r != null && !this.r.equals(r)) {
            updatePointsForNewR(r);
        }
        this.r = r;
    }
}
