package io.github.djr4488.database;

import com.fasterxml.jackson.databind.JsonNode;
import io.github.djr4488.MethodParameterExtractor;
import org.djr.cdi.converter.json.jackson.JsonConverter;
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
    private JsonConverter jsonConverter;
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
        Object object = invocationContext.proceed();
        try {
            returned = jsonConverter.toObjectFromString(jsonConverter.toJsonString(object), JsonNode.class);
        } catch (Exception ex) {
            log.error("Failed to capture return", ex);
        }
        log.trace("doAudit() for parameters:{}, method:{}, className:{}, returned:{}", parameters, method, className, returned);
        AuditRecord auditRecord = new AuditRecord(className, method, parameters, returned);
        entityManager.persist(auditRecord);
        return object;
    }
}
