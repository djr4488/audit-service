package org.djr.audit.database;

import com.fasterxml.jackson.databind.JsonNode;
import org.djr.audit.MethodParameterExtractor;
import org.djr.cdi.converter.json.jackson.JsonConverter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;

import javax.interceptor.InvocationContext;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AuditDatabaseInterceptorTestWithMockAuditDatabaseService {
    @Mock
    private AuditDatabaseService auditDatabaseService;
    @Mock
    private Logger log;
    @Mock
    private MethodParameterExtractor mpe;
    @Mock
    private JsonConverter jc;
    @InjectMocks
    private AuditDatabaseInterceptor adi = new AuditDatabaseInterceptor();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testInterceptedWithExceptionDuringDatabaseSave()  {
        InvocationContext invocationContext = mock(InvocationContext.class);
        HttpServletRequest req = mock(HttpServletRequest.class);
        List<JsonNode> jsonNodeList = new ArrayList<>();
        Object[] parameters = new Object[1];
        parameters[0] = req;
        when(invocationContext.getParameters()).thenReturn(parameters);
        when(req.getRemoteAddr()).thenReturn("127.0.0.1");
        when(req.getRemoteUser()).thenReturn("user");
        try {
            doThrow(new RuntimeException("test")).when(auditDatabaseService).writeAuditRecord(any(AuditRecord.class));
            adi.aroundInvoke(invocationContext);
            verify(log, times(2)).error(anyString(), any(Exception.class));
        } catch (Exception rtEx) {
            fail("expected no exception");
        }
    }
}
