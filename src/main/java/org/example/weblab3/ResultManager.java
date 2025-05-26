package org.example.weblab3;


import jakarta.annotation.Resource;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;

import jakarta.faces.bean.ApplicationScoped;
import jakarta.faces.bean.ManagedBean;
import jakarta.faces.bean.ManagedProperty;
import jakarta.inject.Inject;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.UserTransaction;

@ManagedBean
@ApplicationScoped
public class ResultManager {
    private static final Logger logger = Logger.getLogger(ResultManager.class.getName());

    @PersistenceContext(unitName = "pointPU")
    private EntityManager entityManager;

    @Resource
    private UserTransaction userTransaction;

    public List<PointResult> getAllResults() {
        List<PointResult> list = new ArrayList<>();
        try {
            userTransaction.begin();
            logger.info("Fetching all PointResults");
            list = entityManager.createQuery("SELECT p FROM PointResult p ORDER BY p.checkTime DESC", PointResult.class)
                    .getResultList();
            userTransaction.commit();
        } catch (Exception e) {
            logger.severe("Error fetching PointResults: " + e.getMessage());
            try {
                userTransaction.rollback();
            } catch (Exception rollbackEx) {
                logger.severe("Rollback failed: " + rollbackEx.getMessage());
            }
        }
        return list;
    }

    public void addResult(PointResult point) {
        try {
            userTransaction.begin();
            logger.info("Saving PointResult: " + point);
            entityManager.persist(point);
            userTransaction.commit();
        } catch (Exception e) {
            logger.severe("Error saving PointResult: " + e.getMessage());
            try {
                userTransaction.rollback();
            } catch (Exception rollbackEx) {
                logger.severe("Rollback failed: " + rollbackEx.getMessage());
            }
        }
    }

    public void updateResult(PointResult point) {
        try {
            userTransaction.begin();
            logger.info("Updating PointResult: " + point);
            entityManager.merge(point);
            userTransaction.commit();
        } catch (Exception e) {
            logger.severe("Error updating PointResult: " + e.getMessage());
            try {
                userTransaction.rollback();
            } catch (Exception rollbackEx) {
                logger.severe("Rollback failed: " + rollbackEx.getMessage());
            }
        }
    }
}
