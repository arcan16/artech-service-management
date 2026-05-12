package com.Ar_Tech.models;

import com.Ar_Tech.dto.persons.FullPersonDTO;
import com.Ar_Tech.models.enums.EAuditAction;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "audit_log")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuditLogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = true)
    private UserEntity user;

    @Column(name = "user_snapshot", length = 255)
    private String userSnapshot;

    @Enumerated(EnumType.STRING)
    @Column(name = "action")
    private EAuditAction action;

    @Column(name = "table_name", length = 100)
    private String tableName;

    @Column(name = "record_id")
    private Long recordId;

    @Column(name = "old_values", columnDefinition = "JSON")
    private String oldValues;

    @Column(name = "new_values", columnDefinition = "JSON")
    private String newValues;

    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public AuditLogEntity(UserEntity author, String userSnapshot, EAuditAction action, String tableName, Long recordId,
                          String oldValues, String newValues) {
        this.user = author;
        this.userSnapshot = userSnapshot;
        this.action = action;
        this.tableName = tableName;
        if(recordId != null)
            this.recordId = recordId;
        if(action.equals(EAuditAction.INSERT)) {
            this.newValues = new ObjectMapper().writeValueAsString(newValues);
            this.oldValues = null;
        }else if(action.equals(EAuditAction.UPDATE)) {
            this.newValues = new ObjectMapper().writeValueAsString(newValues);
            this.oldValues = new ObjectMapper().writeValueAsString(oldValues);
        }else if(action.equals(EAuditAction.DELETE)) {
            this.oldValues = new ObjectMapper().writeValueAsString(oldValues);
            this.newValues = null;
        }else if(action.equals(EAuditAction.SELECT)) {
            this.oldValues = null;
            this.newValues = null;
        }
    }
}
