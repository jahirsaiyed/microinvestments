package com.finance.investment.micro.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.finance.investment.micro.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PromotionsAuditDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PromotionsAuditDTO.class);
        PromotionsAuditDTO promotionsAuditDTO1 = new PromotionsAuditDTO();
        promotionsAuditDTO1.setId(1L);
        PromotionsAuditDTO promotionsAuditDTO2 = new PromotionsAuditDTO();
        assertThat(promotionsAuditDTO1).isNotEqualTo(promotionsAuditDTO2);
        promotionsAuditDTO2.setId(promotionsAuditDTO1.getId());
        assertThat(promotionsAuditDTO1).isEqualTo(promotionsAuditDTO2);
        promotionsAuditDTO2.setId(2L);
        assertThat(promotionsAuditDTO1).isNotEqualTo(promotionsAuditDTO2);
        promotionsAuditDTO1.setId(null);
        assertThat(promotionsAuditDTO1).isNotEqualTo(promotionsAuditDTO2);
    }
}
