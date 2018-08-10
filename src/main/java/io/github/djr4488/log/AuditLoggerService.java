package io.github.djr4488.log;

import com.fasterxml.jackson.databind.JsonNode;
import io.github.djr4488.MethodParameterExtractor;
import org.djr.cdi.converter.json.jackson.JsonConverter;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;


import javax.annotation.Resource;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import java.util.ArrayList;
import java.util.List;

@AuditLogger
@Interceptor
public class AuditLoggerService {
    @Inject
    private JsonConverter jsonConverter;
    @Inject
    private Logger log;
    @Resource(lookup="java:app/AppName")
    private String resourceAppName;
    @Inject
    private MethodParameterExtractor methodParameterExtractor;

    @AroundInvoke
    public Object aroundInvoke(InvocationContext invocationContext)
            throws Exception {
        List<JsonNode> parameters = new ArrayList<>();
        String method = invocationContext.getMethod().getName();
        String className = invocationContext.getTarget().getClass().getSimpleName();
        JsonNode returned = null;
        methodParameterExtractor.extractParameters(invocationContext, parameters);
        Object object = invocationContext.proceed();
        try {
            returned = jsonConverter.toObjectFromString(jsonConverter.toJsonString(object), JsonNode.class);
        } catch (Exception ex) {
            log.error("Failed to capture return", ex);
        }
        log.info("AUDIT_LOG -- [{}, {}, {}, {}] parameters:{}, returned:{}", resourceAppName,
                DateTime.now().withZone(DateTimeZone.UTC).toString(),
                className, method, parameters, returned);
        return object;
    }
}
