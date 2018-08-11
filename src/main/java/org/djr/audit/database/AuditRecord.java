package org.djr.audit.database;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Lob;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "audit_records",
       indexes = { @Index(name = "class_name_idx", columnList = "class_name"),
                   @Index(name = "method_name_idx", columnList = "method_name")})
public class AuditRecord extends AuditIdentifier {
    @Column(name = "application_name")
    private String applicationName;
    @Column(name = "class_name")
    private String className;
    @Column(name = "method")
    private String method;
    @Lob
    @Column(name = "parameters")
    private String parameters;
    @Lob
    @Column(name = "returned")
    private String returned;

    public AuditRecord() {
    }

    public AuditRecord(String applicationName, String className, String method, String parameters, String returned) {
        this.applicationName = applicationName;
        this.className = className;
        this.method = method;
        this.parameters = parameters;
        this.returned = returned;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    public String getReturned() {
        return returned;
    }

    public void setReturned(String returned) {
        this.returned = returned;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
