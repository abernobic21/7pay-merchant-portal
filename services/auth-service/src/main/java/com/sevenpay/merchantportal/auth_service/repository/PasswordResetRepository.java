package com.sevenpay.merchantportal.auth_service.repository;

import com.sevenpay.merchantportal.auth_service.model.entity.PasswordReset;
import com.sevenpay.merchantportal.auth_service.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PasswordResetRepository extends JpaRepository<PasswordReset, UUID> , JpaSpecificationExecutor<User> {

    Optional<PasswordReset> findByResetToken(String token);

}
