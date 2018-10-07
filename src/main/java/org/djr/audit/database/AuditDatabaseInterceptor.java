package org.djr.audit.database;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.djr.audit.MethodParameterExtractor;
import org.djr.cdi.converter.json.jackson.JsonConverter;
import org.djr.cdi.logs.Slf4jLogger;
import org.joda.time.DateTime;
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
    @Slf4jLogger
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
        String method = null;
        String className = null;
        Long startTimeMillis = DateTime.now().getMillis();
        try {
            method = invocationContext.getMethod().getName();
            className = invocationContext.getTarget().getClass().getSimpleName();
        } catch (Exception ex) {
            log.error("aroundInvoke() unable to retrieve method and / or class information", ex);
        }
        JsonNode returned = null;
        methodParameterExtractor.extractParameters(invocationContext, parameters);
        Object object = null;
        Exception exception = null;
        try {
            object = invocationContext.proceed();
        } catch (Exception ex) {
            exception = ex;
        }
        Long endTimeMillis = DateTime.now().getMillis();
        Long executeTimeMillis = endTimeMillis - startTimeMillis;
        returned = methodParameterExtractor.getJsonNodeForReturnOrException(object, exception);
        log.trace("aroundInvoke() for parameters:{}, method:{}, className:{}, appName:{}, returned:{}", parameters, method, className, resourceAppName, returned);
        try {
            AuditRecord auditRecord = getAuditRecord(parameters, method, className, returned, exception, executeTimeMillis);
            auditDatabaseService.writeAuditRecord(auditRecord);
        } catch (Exception ex) {
            log.error("aroundInvoke() failed with ", ex);
        }
        if (null != exception) {
            throw exception;
        }
        return object;
    }

    private AuditRecord getAuditRecord(List<JsonNode> parameters, String method, String className, JsonNode returned,
                                       Exception exception, Long executeTimeMillis)
    throws JsonProcessingException {
        String parametersJson = jsonConverter.toJsonString(parameters);
        String returnedJson = returned.toString();
        return new AuditRecord(resourceAppName, className, method, null != exception, executeTimeMillis,
                parametersJson, returnedJson);
    }
}
