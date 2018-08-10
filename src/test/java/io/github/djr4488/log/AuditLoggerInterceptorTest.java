package io.github.djr4488.log;

import com.fasterxml.jackson.databind.JsonNode;
import io.github.djr4488.Intercepted;
import io.github.djr4488.TestLogProducer;
import org.djr.cdi.converter.json.jackson.JsonConverter;
import org.djr.cdi.converter.json.jackson.ObjectMapperProducer;
import org.jglue.cdiunit.ActivatedAlternatives;
import org.jglue.cdiunit.AdditionalClasses;
import org.jglue.cdiunit.CdiRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;

import javax.inject.Inject;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@RunWith(CdiRunner.class)
@AdditionalClasses({ AuditLoggerInterceptor.class, Intercepted.class, JsonConverter.class, ObjectMapperProducer.class})
@ActivatedAlternatives({ TestLogProducer.class })
public class AuditLoggerInterceptorTest {
    @Inject
    private Logger log;
    @Inject
    private Intercepted intercepted;

    @Test
    public void testIntercepted() {
        intercepted.testIntercept("in progress");
        verify(log, times(2)).info(anyString(), anyString(), anyString(), anyString(),
                anyString(), any(JsonNode.class), any(JsonNode.class));
    }
}
