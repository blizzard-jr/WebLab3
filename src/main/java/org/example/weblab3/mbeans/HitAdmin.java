package org.example.weblab3.mbeans;

import org.example.weblab3.PointResult;
import org.example.weblab3.ResultManager;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import javax.management.Notification;
import javax.management.NotificationBroadcasterSupport;
import java.util.List;
import java.util.logging.Logger;
import java.util.ArrayList;

@Named("hitAdmin")
@ApplicationScoped
public class HitAdmin extends NotificationBroadcasterSupport implements HitAdminMBean {
    private static final Logger logger = Logger.getLogger(HitAdmin.class.getName());
    private long sequenceNumber = 1;


    private List<PointResult> getAllResultsDirectly() {
        try {
            EntityManagerFactory emf = null;
            EntityManager em = null;
            try {
                emf = Persistence.createEntityManagerFactory("pointPU");
                em = emf.createEntityManager();
                
                List<Object[]> results = em.createNativeQuery(
                    "SELECT id, x, y, r, result, executiontime, checktime FROM pointresult ORDER BY checktime DESC"
                ).getResultList();
                
                List<PointResult> pointResults = new ArrayList<>();
                for (Object[] row : results) {
                    PointResult point = new PointResult();
                    point.setId(((Number) row[0]).longValue());
                    point.setX(row[1] != null ? ((Number) row[1]).doubleValue() : null);
                    point.setY(row[2] != null ? ((Number) row[2]).doubleValue() : null);
                    point.setR(row[3] != null ? ((Number) row[3]).doubleValue() : null);
                    point.setResult(row[4] != null ? (Boolean) row[4] : null);
                    point.setExecutionTime(row[5] != null ? ((Number) row[5]).longValue() : null);
                    point.setCheckTime(row[6] != null ? (String) row[6] : null);
                    pointResults.add(point);
                }
                
                return pointResults;
            } finally {
                if (em != null) {
                    em.close();
                }
                if (emf != null) {
                    emf.close();
                }
            }
        } catch (Exception e) {
            logger.severe("Error fetching PointResults directly: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public long getCommonCount() {
        try {
            List<PointResult> results = getAllResultsDirectly();
            return results.size();
        } catch (Exception e) {
            logger.severe("Error getting common count: " + e.getMessage());
            return 0;
        }
    }

    @Override
    public Long getFailCount() {
        try {
            List<PointResult> list = getAllResultsDirectly();
            long count = list.stream()
                    .filter(r -> r.getResult() != null && !r.getResult())
                    .count();
            return count;
        } catch (Exception e) {
            logger.severe("Error getting fail count: " + e.getMessage());
            return 0L;
        }
    }

    @Override
    public void ToNotify() {
        try {
            List<PointResult> allPoints = getAllResultsDirectly();
            if (allPoints.isEmpty()) return;

            Double currentR = allPoints.get(0).getR();
            if (currentR != null) {
                for (PointResult point : allPoints) {
                    boolean currentResult = point.getResult();
                    boolean wouldBeValid = point.checkHit();

                    if (currentResult && !wouldBeValid) {
                        String message = String.format(
                                "Точка (%.2f, %.2f) выйдет за пределы области при R=%.2f",
                                point.getX(), point.getY(), currentR
                        );
                        Notification notification = new Notification(
                                "point.out.of.bounds",         // тип уведомления
                                this,                          // источник
                                sequenceNumber++,              // уникальный номер
                                System.currentTimeMillis(),    // метка времени
                                message                        // сообщение
                        );
                        sendNotification(notification);
                    }
                }
            }
        } catch (Exception e) {
            logger.severe("Ошибка при отправке уведомления: " + e.getMessage());
        }
    }
}
