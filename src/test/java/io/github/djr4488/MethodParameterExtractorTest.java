package io.github.djr4488;

import com.fasterxml.jackson.databind.JsonNode;
import org.djr.cdi.converter.json.jackson.JsonConverter;
import org.djr.cdi.converter.json.jackson.ObjectMapperProducer;
import org.jglue.cdiunit.ActivatedAlternatives;
import org.jglue.cdiunit.AdditionalClasses;
import org.jglue.cdiunit.CdiRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.interceptor.InvocationContext;
import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@RunWith(CdiRunner.class)
@AdditionalClasses({ JsonConverter.class, ObjectMapperProducer.class})
@ActivatedAlternatives({ TestLogProducer.class })
public class MethodParameterExtractorTest {
    @Inject
    private Logger log;
    @Inject
    private MethodParameterExtractor methodParameterExtractor;

    @Test
    public void testMethodParameterExtractorWithNoHttpServletRequest() {
        //given
        InvocationContext invocationContext = mock(InvocationContext.class);
        List<JsonNode> jsonNodeList = new ArrayList<>();
        Object[] parameters = new Object[1];
        String testString = "Test";
        parameters[0] = testString;
        when(invocationContext.getParameters()).thenReturn(parameters);
        //when
        methodParameterExtractor.extractParameters(invocationContext, jsonNodeList);
        //then
        assertEquals(1, jsonNodeList.size());
    }

    @Test
    public void testMethodParameterExtractorWithHttpServletRequest() {
        //given
        InvocationContext invocationContext = mock(InvocationContext.class);
        HttpServletRequest req = mock(HttpServletRequest.class);
        List<JsonNode> jsonNodeList = new ArrayList<>();
        Object[] parameters = new Object[1];
        parameters[0] = req;
        when(invocationContext.getParameters()).thenReturn(parameters);
        when(req.getRemoteAddr()).thenReturn("127.0.0.1");
        when(req.getRemoteUser()).thenReturn("user");
        //when
        methodParameterExtractor.extractParameters(invocationContext, jsonNodeList);
        //then
        assertEquals(1, jsonNodeList.size());
        assertTrue(jsonNodeList.get(0).hasNonNull("httpServletRequest"));
        JsonNode jnReq = jsonNodeList.get(0).get("httpServletRequest");
        assertTrue(jnReq.hasNonNull("remote_addr"));
        assertEquals("127.0.0.1", jnReq.get("remote_addr").textValue());
        assertTrue(jnReq.hasNonNull("remote_user"));
        assertEquals("user", jnReq.get("remote_user").textValue());
    }

    @Test
    public void testMethodParameterExtractorWithNoParameters() {
        //given
        InvocationContext invocationContext = mock(InvocationContext.class);
        List<JsonNode> jsonNodeList = new ArrayList<>();
        Object[] parameters = null;
        when(invocationContext.getParameters()).thenReturn(parameters);
        //when
        methodParameterExtractor.extractParameters(invocationContext, jsonNodeList);
        //then
        assertEquals(0, jsonNodeList.size());
        verify(log, times(1)).trace("extractParameters() no parameters for method/constructor");
    }

    @Test
    public void testMethodParametersExtractorThrowsException() {
        //given
        InvocationContext invocationContext = mock(InvocationContext.class);
        List<JsonNode> jsonNodeList = new ArrayList<>();
        RuntimeException rtEx = new RuntimeException("Test");
        when(invocationContext.getParameters()).thenThrow(rtEx);
        //when
        methodParameterExtractor.extractParameters(invocationContext, jsonNodeList);
        //then
        verify(log, times(1)).error("Failed to capture parameters", rtEx);
    }
}
