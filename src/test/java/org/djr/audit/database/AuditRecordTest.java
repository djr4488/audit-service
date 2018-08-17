package org.djr.audit.database;

import org.joda.time.DateTime;
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
        Date now = DateTime.now().withYear(2000).withMonthOfYear(2).withDayOfMonth(1).withTimeAtStartOfDay().toDate();
        ar.setCreatedAt(now);
        assertEquals(now, ar.getCreatedAt());
        ar.setLastUpdatedAt(now);
        assertEquals(now, ar.getLastUpdatedAt());
        ar.setId(1L);
        assertEquals(1L, ar.getId().longValue());
        ar.setVersion(1L);
        assertEquals(1L, ar.getVersion().longValue());
        ar.setException(false);
        assertFalse(ar.isException());
        assertEquals("AuditRecord[applicationName=test,className=test,method=test,isException=false,parameters=<null>,returned=<null>,id=1,createdAt=Tue Feb 01 00:00:00 CST 2000,lastUpdatedAt=Tue Feb 01 00:00:00 CST 2000,version=1]",
                ar.toString());
    }
}
