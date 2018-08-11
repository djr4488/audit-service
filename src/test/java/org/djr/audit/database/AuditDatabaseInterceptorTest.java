package org.djr.audit.database;

import org.djr.audit.Intercepted;
import org.djr.audit.TestLogProducer;
import org.djr.cdi.converter.json.jackson.JsonConverter;
import org.djr.cdi.converter.json.jackson.ObjectMapperProducer;
import org.jglue.cdiunit.ActivatedAlternatives;
import org.jglue.cdiunit.AdditionalClasses;
import org.jglue.cdiunit.CdiRunner;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.ArgumentCaptor;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(CdiRunner.class)
@AdditionalClasses({ AuditDatabaseInterceptor.class, Intercepted.class, JsonConverter.class, ObjectMapperProducer.class})
@ActivatedAlternatives({ TestLogProducer.class, MockEntityManagerProducer.class })
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AuditDatabaseInterceptorTest {
    @Inject
    private InterceptedDatabase intercepted;
    @Inject
    @AuditDatabaseEM
    private EntityManager entityManager;

    @Test
    public void testIntercepted() {
        ArgumentCaptor<AuditRecord> arArgCaptor = ArgumentCaptor.forClass(AuditRecord.class);
        intercepted.testIntercept("in progress");
        verify(entityManager, times(2)).persist(arArgCaptor.capture());
        assertEquals("\"tested\"", arArgCaptor.getValue().getReturned());
    }

    @Test
    public void testInterceptedWithException() {
        ArgumentCaptor<AuditRecord> arArgCaptor = ArgumentCaptor.forClass(AuditRecord.class);
        try {
            intercepted.testInterceptWithExceptionThrown("in progress");
        } catch (RuntimeException rtEx) {
            assertTrue(true);  // we expect this exception since we threw it in our intercepted method
        }
        verify(entityManager, times(2)).persist(arArgCaptor.capture());
        assertEquals("[\"in progress\"]", arArgCaptor.getValue().getParameters());
    }
}
