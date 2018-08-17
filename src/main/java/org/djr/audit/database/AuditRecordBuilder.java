package org.djr.audit.database;

public class AuditRecordBuilder {
    private String applicationName;
    private String className;
    private String methodName;
    private Boolean isException;
    private String parameters;
    private String returned;

    public AuditRecordBuilder setApplicationName(String applicationName) {
        this.applicationName = applicationName;
        return this;
    }

    public AuditRecordBuilder setClassName(String className) {
        this.className = className;
        return this;
    }

    public AuditRecordBuilder setMethodName(String methodName) {
        this.methodName = methodName;
        return this;
    }

    public AuditRecordBuilder setException(Boolean isException) {
        this.isException = isException;
        return this;
    }

    public AuditRecordBuilder setParameters(String parameters) {
        this.parameters = parameters;
        return this;
    }

    public AuditRecordBuilder setReturned(String returned) {
        this.returned = returned;
        return this;
    }

    public AuditRecord build() {
        return new AuditRecord(applicationName, className, methodName, isException, parameters, returned);
    }
}
