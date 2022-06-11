package com.finance.investment.micro.repository;

import com.finance.investment.micro.domain.InvestorPortfolio;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the InvestorPortfolio entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InvestorPortfolioRepository extends JpaRepository<InvestorPortfolio, Long> {}
