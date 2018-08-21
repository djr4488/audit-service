package org.djr.audit.database;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;

import java.util.Date;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;

public class JodaDateTimeConverterTest {
    JodaDateTimeConverter jdtc = new JodaDateTimeConverter();
    @Test
    public void testConversions() {
        DateTime now = DateTime.now().withZone(DateTimeZone.UTC).withYear(2000).withMonthOfYear(2).withDayOfMonth(1).withTimeAtStartOfDay().withHourOfDay(0);
        Date nowDate = jdtc.convertToDatabaseColumn(now);
        assertNotNull(nowDate);
        assertEquals(949363200000L, nowDate.getTime());
        DateTime fromDate = jdtc.convertToEntityAttribute(nowDate);
        assertEquals(949363200000L, fromDate.getMillis());
    }
}
