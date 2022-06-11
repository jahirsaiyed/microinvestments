package com.finance.investment.micro.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.finance.investment.micro.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InvestorAccountDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(InvestorAccountDTO.class);
        InvestorAccountDTO investorAccountDTO1 = new InvestorAccountDTO();
        investorAccountDTO1.setId(1L);
        InvestorAccountDTO investorAccountDTO2 = new InvestorAccountDTO();
        assertThat(investorAccountDTO1).isNotEqualTo(investorAccountDTO2);
        investorAccountDTO2.setId(investorAccountDTO1.getId());
        assertThat(investorAccountDTO1).isEqualTo(investorAccountDTO2);
        investorAccountDTO2.setId(2L);
        assertThat(investorAccountDTO1).isNotEqualTo(investorAccountDTO2);
        investorAccountDTO1.setId(null);
        assertThat(investorAccountDTO1).isNotEqualTo(investorAccountDTO2);
    }
}
