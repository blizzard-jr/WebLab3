package org.example.weblab3.mbeans;

import org.example.weblab3.PointResult;
import org.example.weblab3.ResultManager;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import jakarta.inject.Inject;

import java.util.List;
import java.util.logging.Logger;

@Named("hitAdmin")
@ApplicationScoped
public class HitAdmin implements HitAdminMBean {
    private static final Logger logger = Logger.getLogger(HitAdmin.class.getName());
    
    @Inject
    private ResultManager resultManager;

    @Override
    public long getCommonCount() {
        return resultManager.getAllResults().size();
    }

    @Override
    public Long getFailCount() {
        List<PointResult> list = resultManager.getAllResults();
        long count = list.stream()
                .filter(r -> !r.getResult())
                .count();
        return count;
    }

    @Override
    public String ToNotify() {
        List<PointResult> allPoints = resultManager.getAllResults();
        if (allPoints.isEmpty()) {
            return "Нет сохраненных точек";
        }

        StringBuilder notification = new StringBuilder();
        Double currentR = allPoints.get(0).getR();
        
        if (currentR != null) {
            for (PointResult point : allPoints) {
                boolean currentResult = point.getResult();
                boolean wouldBeValid = point.checkHit();
                
                if (currentResult && !wouldBeValid) {
                    notification.append(String.format(
                        "Точка (%.2f, %.2f) выйдет за пределы области при R=%.2f\n",
                        point.getX(), point.getY(), currentR
                    ));
                }
            }
        }
        
        return notification.toString().isEmpty() ? 
               "Все точки останутся в допустимой области" : 
               notification.toString();
    }
}
