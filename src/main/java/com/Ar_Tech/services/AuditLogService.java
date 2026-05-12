package com.Ar_Tech.services;

import com.Ar_Tech.infra.security.utils.JwtUtils;
import com.Ar_Tech.models.AuditLogEntity;
import com.Ar_Tech.models.UserEntity;
import com.Ar_Tech.models.enums.EAuditAction;
import com.Ar_Tech.repositories.AuditLogRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

        AuditLogEntity auditLog = new AuditLogEntity(author, author.getPerson().getFirstName() + " " +
                author.getPerson().getLastName(), action, tableName, recordId, oldValues, newValues);

        auditLogRepository.save(auditLog);
    }
}
