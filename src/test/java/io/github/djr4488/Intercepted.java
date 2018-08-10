package io.github.djr4488;

import io.github.djr4488.log.AuditLogger;
import io.github.djr4488.log.AuditLoggerInterceptor;
import org.slf4j.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.interceptor.Interceptors;

@Interceptors({ AuditLoggerInterceptor.class })
@ApplicationScoped
public class Intercepted {
    @Inject
    private Logger log;

    @AuditLogger
    public String testIntercept(String testing) {
        return "tested";
    }

    @AuditLogger
    public void testInterceptNullReturn(String testing) {
        System.out.println(testing);
    }
}
