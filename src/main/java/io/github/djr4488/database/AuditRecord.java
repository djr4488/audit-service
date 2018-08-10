package io.github.djr4488.database;

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
@Table(name = "AuditRecord",
       indexes = { @Index(name = "class_name_idx", columnList = "class_name"),
                   @Index(name = "method_name_idx", columnList = "method_name")})
public class AuditRecord extends AuditIdentifier {
    @Column(name = "class_name")
    private String className;
    @Column(name = "method")
    private String method;
    @Lob
    @Column(name = "parameters")
    private List<JsonNode> parameters;
    @Lob
    @Column(name = "returned")
    private JsonNode returned;

    public AuditRecord() {
    }

    public AuditRecord(String className, String method, List<JsonNode> parameters, JsonNode returned) {
        this.className = className;
        this.method = method;
        this.parameters = parameters;
        this.returned = returned;
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

    public List<JsonNode> getParameters() {
        return parameters;
    }

    public void setParameters(List<JsonNode> parameters) {
        this.parameters = parameters;
    }

    public JsonNode getReturned() {
        return returned;
    }

    public void setReturned(JsonNode returned) {
        this.returned = returned;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
