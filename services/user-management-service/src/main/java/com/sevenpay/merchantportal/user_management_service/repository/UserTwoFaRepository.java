package com.sevenpay.merchantportal.user_management_service.repository;

import com.sevenpay.merchantportal.user_management_service.model.entity.UserTwoFa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserTwoFaRepository extends JpaRepository<UserTwoFa, UUID> {

    boolean existsByUserId(UUID userId);
}
