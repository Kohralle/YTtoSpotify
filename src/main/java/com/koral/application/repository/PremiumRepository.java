package com.koral.application.repository;

import com.koral.application.model.PremiumStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PremiumRepository extends JpaRepository<PremiumStatus, Long> {
}
