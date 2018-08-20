package org.djr.audit.database;


import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Date;

@Converter(autoApply = true)
public class JodaDateTimeConverter implements AttributeConverter<DateTime, Date> {
    @Override
    public Date convertToDatabaseColumn(DateTime dateTime) {
        return dateTime.toDate();
    }

    @Override
    public DateTime convertToEntityAttribute(Date date) {
        return new DateTime(date).withZone(DateTimeZone.UTC);
    }
}
