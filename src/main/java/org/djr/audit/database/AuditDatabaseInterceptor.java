package org.djr.audit.database;

import com.fasterxml.jackson.databind.JsonNode;
import org.djr.audit.MethodParameterExtractor;
import org.djr.cdi.converter.json.jackson.JsonConverter;
import org.slf4j.Logger;

import javax.annotation.Resource;;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import java.util.ArrayList;
import java.util.List;

@AuditDatabase
@Interceptor
public class AuditDatabaseInterceptor {
    @Inject
    private Logger log;
    @Resource(lookup="java:app/AppName")
    private String resourceAppName;
    @Inject
    private MethodParameterExtractor methodParameterExtractor;
    @Inject
    private AuditDatabaseService auditDatabaseService;
    @Inject
    private JsonConverter jsonConverter;

    @AroundInvoke
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public Object aroundInvoke(InvocationContext invocationContext)
    throws Exception {
        List<JsonNode> parameters = new ArrayList<>();
        String method = invocationContext.getMethod().getName();
        String className = invocationContext.getTarget().getClass().getSimpleName();
        JsonNode returned = null;
        methodParameterExtractor.extractParameters(invocationContext, parameters);
        Object object = null;
        Exception exception = null;
        try {
            object = invocationContext.proceed();
        } catch (Exception ex) {
            exception = ex;
        }
        returned = methodParameterExtractor.getJsonNodeForReturnOrException(object, exception);
        log.trace("aroundInvoke() for parameters:{}, method:{}, className:{}, appName:{}, returned:{}", parameters, method, className, resourceAppName, returned);
        try {
            AuditRecord auditRecord = getAuditRecord(parameters, method, className, returned, exception);
            auditDatabaseService.writeAuditRecord(auditRecord);
        } catch (Exception ex) {
            log.error("aroundInvoke() failed with ", ex);
        }
        if (null != exception) {
            throw exception;
        }
        return object;
    }

    private AuditRecord getAuditRecord(List<JsonNode> parameters, String method, String className, JsonNode returned, Exception exception)
    throws com.fasterxml.jackson.core.JsonProcessingException {
        AuditRecordBuilder arb = new AuditRecordBuilder();
        arb.setApplicationName(resourceAppName)
           .setClassName(className)
           .setMethodName(method)
           .setException(null != exception)
           .setParameters(jsonConverter.toJsonString(parameters))
           .setReturned(jsonConverter.toJsonString(returned));
        return arb.build();
    }
}
