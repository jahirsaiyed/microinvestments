package com.finance.investment.micro.repository;

import com.finance.investment.micro.domain.Promotions;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Promotions entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PromotionsRepository extends JpaRepository<Promotions, Long> {}
