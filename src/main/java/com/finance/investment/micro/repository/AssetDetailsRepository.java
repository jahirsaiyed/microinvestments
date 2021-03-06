package com.finance.investment.micro.repository;

import com.finance.investment.micro.domain.AssetDetails;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the AssetDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AssetDetailsRepository extends JpaRepository<AssetDetails, Long> {}
