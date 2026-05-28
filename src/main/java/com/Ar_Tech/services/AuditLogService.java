package com.Ar_Tech.services;

import com.Ar_Tech.infra.security.utils.JwtUtils;
import com.Ar_Tech.models.AuditLogEntity;
import com.Ar_Tech.models.UserEntity;
import com.Ar_Tech.models.enums.EAuditAction;
import com.Ar_Tech.repositories.AuditLogRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tools.jackson.databind.JsonNode;

@Service
public class AuditLogService {

    @Autowired
    private AuditLogRepository auditLogRepository;

    @Autowired
    private JwtUtils jwtUtils;

    public void creation(AuditLogEntity auditLog){
        auditLogRepository.save(auditLog);
    }

    public void create(HttpServletRequest request, EAuditAction action, String tableName, Long recordId,
                       String oldValues, String newValues){
        UserEntity author = jwtUtils.getUserFromRequest(request);

        doCreate(author, action, tableName, recordId, oldValues, newValues);
    }

    public void create(UserEntity author, EAuditAction action, String tableName, Long recordId,
                       String oldValues, String newValues){

        doCreate(author, action, tableName, recordId, oldValues, newValues);
    }

    private void doCreate(UserEntity author, EAuditAction action, String tableName, Long recordId,
                          String oldValues, String newValues){

        AuditLogEntity auditLog = new AuditLogEntity(author, author.getPerson().getFirstName() + " " +
                author.getPerson().getLastName(), action, tableName, recordId, oldValues, newValues);

        auditLogRepository.save(auditLog);
    }
}
