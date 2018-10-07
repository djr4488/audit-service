package org.djr.audit.log;

import com.fasterxml.jackson.databind.JsonNode;
import org.djr.audit.MethodParameterExtractor;
import org.djr.cdi.logs.Slf4jLogger;
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
public class AuditLoggerInterceptor {
    @Inject
    @Slf4jLogger
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
        Object object = null;
        Exception exception = null;
        Long startTimeMillis = DateTime.now().getMillis();
        try {
            object = invocationContext.proceed();
        } catch (Exception ex) {
            exception = ex;
        }
        Long endTimeMillis = DateTime.now().getMillis();
        Long totalTimeMillis = endTimeMillis - startTimeMillis;
        returned = methodParameterExtractor.getJsonNodeForReturnOrException(object, exception);
        log.info("AUDIT_LOG -- [{}, {}, {}, {}] parameters:{}, returned:{}", resourceAppName,
                totalTimeMillis, className, method, parameters, returned);
        if (null != exception) {
            throw exception;
        }
        return object;
    }
}
