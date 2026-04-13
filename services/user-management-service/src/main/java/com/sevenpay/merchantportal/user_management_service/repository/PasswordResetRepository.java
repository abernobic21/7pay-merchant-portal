package com.sevenpay.merchantportal.user_management_service.repository;

import com.sevenpay.merchantportal.user_management_service.model.entity.PasswordReset;
import com.sevenpay.merchantportal.user_management_service.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PasswordResetRepository extends JpaRepository<PasswordReset, UUID> , JpaSpecificationExecutor<User> {
}
