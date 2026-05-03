package com.Ar_Tech.services;

import com.Ar_Tech.models.AuditLogEntity;
import com.Ar_Tech.repositories.AuditLogRepository;
import com.Ar_Tech.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuditLogService {

    @Autowired
    private AuditLogRepository auditLogRepository;

    @Autowired
    private UserRepository userRepository;

    public void create(AuditLogEntity auditLog){
        auditLogRepository.save(auditLog);
    }
}
