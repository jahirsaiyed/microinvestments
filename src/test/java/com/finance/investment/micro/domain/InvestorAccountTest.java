package com.finance.investment.micro.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.finance.investment.micro.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InvestorAccountTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InvestorAccount.class);
        InvestorAccount investorAccount1 = new InvestorAccount();
        investorAccount1.setId(1L);
        InvestorAccount investorAccount2 = new InvestorAccount();
        investorAccount2.setId(investorAccount1.getId());
        assertThat(investorAccount1).isEqualTo(investorAccount2);
        investorAccount2.setId(2L);
        assertThat(investorAccount1).isNotEqualTo(investorAccount2);
        investorAccount1.setId(null);
        assertThat(investorAccount1).isNotEqualTo(investorAccount2);
    }
}
