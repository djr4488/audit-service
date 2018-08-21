package org.djr.audit.database;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import javax.enterprise.inject.spi.InjectionPoint;
import javax.persistence.EntityManager;

import static junit.framework.TestCase.assertNotNull;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class AuditDatabaseManagerEntityManagerProducerTest {
    //kind of a dumb test honestly, but lets see if can get 100% for no good reason
    @Mock
    private EntityManager entityManager;
    @InjectMocks
    private AuditDatabaseEntityManagerProducer ademp = new AuditDatabaseEntityManagerProducer();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testEntityManagerNotNull() {
        InjectionPoint ip = mock(InjectionPoint.class);
        assertNotNull(ademp.produceEntityManager(ip));
    }
}
