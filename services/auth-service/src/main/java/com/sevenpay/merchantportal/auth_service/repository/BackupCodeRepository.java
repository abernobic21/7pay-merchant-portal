package com.sevenpay.merchantportal.auth_service.repository;

import com.sevenpay.merchantportal.auth_service.model.entity.BackupCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BackupCodeRepository extends JpaRepository<BackupCode, UUID> {

    List<BackupCode> findByUserId(UUID userId);

    List<BackupCode> findByUserIdAndUsedAtIsNull(UUID userId);

    void deleteByUserId(UUID userId);
}
