package com.finance.investment.micro.repository;

import com.finance.investment.micro.domain.PromotionsAudit;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PromotionsAudit entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PromotionsAuditRepository extends JpaRepository<PromotionsAudit, Long> {}
