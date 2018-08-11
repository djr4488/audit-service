package org.djr.audit.database;

import com.fasterxml.jackson.databind.JsonNode;
import org.djr.audit.MethodParameterExtractor;
import org.slf4j.Logger;

import javax.annotation.Resource;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.persistence.EntityManager;
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
    @AuditDatabaseEM
    private EntityManager entityManager;
    @Inject
    private MethodParameterExtractor methodParameterExtractor;

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
        log.trace("doAudit() for parameters:{}, method:{}, className:{}, appName:{}, returned:{}", parameters, method, className, resourceAppName, returned);
        AuditRecord auditRecord = new AuditRecord(resourceAppName, className, method, parameters, returned);
        try {
            entityManager.persist(auditRecord);
        } catch (Exception ex) {
            log.error("doAudit() failed", ex);
        }
        return object;
    }
}
