package org.djr.audit.database;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;

@Stateless
public class AuditDatabaseService {
    @Inject
    @AuditDatabaseEM
    private EntityManager entityManager;

    public void writeAuditRecord(AuditRecord auditRecord) {
        entityManager.persist(auditRecord);
    }
}
