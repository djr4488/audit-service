package org.djr.audit.database;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "audit_records",
       indexes = { @Index(name = "class_name_idx", columnList = "class_name"),
                   @Index(name = "method_name_idx", columnList = "method_name")})
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
public class AuditRecord extends AuditIdentifier {
    @Column(name = "application_name")
    private String applicationName;
    @Column(name = "class_name")
    private String className;
    @Column(name = "method_name")
    private String method;
    @Column(name = "is_exception")
    private Boolean isException;
    @Column(name = "execute_time_millis")
    private Long executeTimeMillis;
    @Lob
    @Column(name = "parameters")
    private String parameters;
    @Lob
    @Column(name = "returned")
    private String returned;
}
