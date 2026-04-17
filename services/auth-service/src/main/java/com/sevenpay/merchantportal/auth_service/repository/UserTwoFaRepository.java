package com.sevenpay.merchantportal.auth_service.repository;

import com.sevenpay.merchantportal.auth_service.model.entity.UserTwoFa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserTwoFaRepository extends JpaRepository<UserTwoFa, UUID> {

    Optional<UserTwoFa> findByUserId(UUID userId);

    boolean existsByUserId(UUID userId);

    void deleteByUserId(UUID userId);
}
