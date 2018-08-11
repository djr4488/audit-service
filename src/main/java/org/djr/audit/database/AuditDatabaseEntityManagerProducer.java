package org.djr.audit.database;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceUnit;

@ApplicationScoped
public class AuditDatabaseEntityManagerProducer {
    @PersistenceUnit(unitName = "audit_database")
    private EntityManager entityManager;

    @Produces
    @AuditDatabaseEM
    public EntityManager produceEntityManager(InjectionPoint injectionPoint) {
        return entityManager;
    }
}
