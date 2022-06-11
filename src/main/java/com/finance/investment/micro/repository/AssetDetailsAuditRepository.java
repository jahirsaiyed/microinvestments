package com.finance.investment.micro.repository;

import com.finance.investment.micro.domain.AssetDetailsAudit;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the AssetDetailsAudit entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AssetDetailsAuditRepository extends JpaRepository<AssetDetailsAudit, Long> {}
