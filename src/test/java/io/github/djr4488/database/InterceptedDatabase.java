package io.github.djr4488.database;

import org.slf4j.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.interceptor.Interceptors;

@Interceptors({ AuditDatabaseInterceptor.class })
@ApplicationScoped
public class InterceptedDatabase {
    @Inject
    private Logger log;

    @AuditDatabase
    public String testIntercept(String testing) {
        return "tested";
    }

    @AuditDatabase
    public void testInterceptNullReturn(String testing) {
        System.out.println(testing);
    }

    @AuditDatabase
    public void testInterceptWithExceptionThrown(String testing) {
        throw new RuntimeException("testing exceptions");
    }
}
