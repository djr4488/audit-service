package org.djr.audit;


import org.djr.audit.log.AuditLogger;
import org.djr.audit.log.AuditLoggerInterceptor;
import org.djr.cdi.logs.Slf4jLogger;
import org.slf4j.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.interceptor.Interceptors;

@Interceptors({ AuditLoggerInterceptor.class })
@ApplicationScoped
public class Intercepted {
    @Inject
    @Slf4jLogger
    private Logger log;

    @AuditLogger
    public String testIntercept(String testing) {
        return "tested";
    }

    @AuditLogger
    public void testInterceptNullReturn(String testing) {
        System.out.println(testing);
    }

    @AuditLogger
    public void testInterceptWithExceptionThrown(String testing) {
        throw new RuntimeException("testing exceptions");
    }
}
