package com.finance.investment.micro.repository;

import com.finance.investment.micro.domain.InvestorAccount;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the InvestorAccount entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InvestorAccountRepository extends JpaRepository<InvestorAccount, Long> {}
