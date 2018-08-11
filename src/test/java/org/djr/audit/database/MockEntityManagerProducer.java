package org.djr.audit.database;

import org.mockito.Mock;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;

@ApplicationScoped
@Alternative
public class MockEntityManagerProducer {
    @Mock
    private EntityManager entityManager;

    @Produces
    @AuditDatabaseEM
    public EntityManager produceMockEntityManager() {
        return entityManager;
    }
}
