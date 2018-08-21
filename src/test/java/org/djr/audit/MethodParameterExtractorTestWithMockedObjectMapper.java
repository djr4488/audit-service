package org.djr.audit;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.djr.audit.database.AuditRecord;
import org.djr.cdi.converter.json.jackson.JacksonObjectMapper;
import org.djr.cdi.converter.json.jackson.JsonConverter;
import org.jglue.cdiunit.ActivatedAlternatives;
import org.jglue.cdiunit.AdditionalClasses;
import org.jglue.cdiunit.CdiRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;

import javax.inject.Inject;

import static junit.framework.TestCase.fail;
import static org.mockito.Mockito.when;

@RunWith(CdiRunner.class)
@AdditionalClasses({ JsonConverter.class })
@ActivatedAlternatives({ TestLogProducer.class, MockObjectMapperProducer.class })
public class MethodParameterExtractorTestWithMockedObjectMapper {
    @Inject
    @JacksonObjectMapper
    private ObjectMapper objectMapper;
    @Inject
    private MethodParameterExtractor methodParameterExtractor;
    @Inject
    private Logger log;

    @Test
    public void testWhenExceptionOnHandlingReturnConversion() {
        AuditRecord ar  = new AuditRecord("app", "test", "test", true, "test", "test");
        try {
            RuntimeException rtEx = new RuntimeException("Test");
            when(objectMapper.writeValueAsString(ar)).thenThrow(rtEx);
            methodParameterExtractor.getJsonNodeForReturnOrException(ar, null);
        } catch (Exception ex) {
            fail("no exception should occur");
            log.error("testWhenExceptionOnHandlingReturnConversion() ", ex);
        }
    }
}
