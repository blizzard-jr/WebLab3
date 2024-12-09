package org.example.weblab3;

import jakarta.ejb.Stateless;
import jakarta.faces.bean.ApplicationScoped;
import jakarta.faces.bean.ManagedBean;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@ManagedBean
@ApplicationScoped
public class PointResultDAO {
    private static final Logger logger = Logger.getLogger(PointResultDAO.class.getName());

    @PersistenceContext(unitName = "pointPU")
    private EntityManager entityManager;

    @Transactional
    public void save(PointResult point) {
        logger.info("Saving PointResult: " + point);
        entityManager.persist(point);
    }

    public List<PointResult> getAllResults() {
        entityManager.clear();
        logger.info("Fetching all PointResults");
        List<PointResult> list = entityManager.createQuery("SELECT p FROM PointResult p ORDER BY p.checkTime DESC",
                        PointResult.class)
                .getResultList();
        if(list == null) {
            logger.warning("No PointResults found, returning empty list");
            return new ArrayList<>();
        } else {
            logger.info("Found " + list.size() + " PointResults");
            return list;
        }
    }
}
