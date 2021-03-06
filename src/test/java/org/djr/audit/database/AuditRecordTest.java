package org.djr.audit.database;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

public class AuditRecordTest {
    @Test
    public void testGettersSetters() {
        AuditRecord ar = new AuditRecord();
        ar.setApplicationName("test");
        assertEquals("test", ar.getApplicationName());
        ar.setClassName("test");
        assertEquals("test", ar.getClassName());
        ar.setMethod("test");
        assertEquals("test", ar.getMethod());
        ar.setParameters(null);
        assertNull(ar.getParameters());
        ar.setReturned(null);
        assertNull(ar.getReturned());
        DateTime now = DateTime.now().withZone(DateTimeZone.UTC).withYear(2000).withMonthOfYear(2).withDayOfMonth(1).withTimeAtStartOfDay().withHourOfDay(0);
        ar.setCreatedAt(now);
        assertEquals(now, ar.getCreatedAt());
        ar.setLastUpdatedAt(now);
        assertEquals(now, ar.getLastUpdatedAt());
        ar.setId(1L);
        assertEquals(1L, ar.getId().longValue());
        ar.setVersion(1L);
        assertEquals(1L, ar.getVersion().longValue());
        ar.setIsException(false);
        assertFalse(ar.getIsException());
        assertEquals("AuditRecord(super=AuditIdentifier(id=1, createdAt=2000-02-01T00:00:00.000Z, lastUpdatedAt=2000-02-01T00:00:00.000Z, version=1), applicationName=test, className=test, method=test, isException=false, executeTimeMillis=null, parameters=null, returned=null)",
                ar.toString());
    }
}
