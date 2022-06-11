package com.finance.investment.micro.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.finance.investment.micro.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PromotionsAuditTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PromotionsAudit.class);
        PromotionsAudit promotionsAudit1 = new PromotionsAudit();
        promotionsAudit1.setId(1L);
        PromotionsAudit promotionsAudit2 = new PromotionsAudit();
        promotionsAudit2.setId(promotionsAudit1.getId());
        assertThat(promotionsAudit1).isEqualTo(promotionsAudit2);
        promotionsAudit2.setId(2L);
        assertThat(promotionsAudit1).isNotEqualTo(promotionsAudit2);
        promotionsAudit1.setId(null);
        assertThat(promotionsAudit1).isNotEqualTo(promotionsAudit2);
    }
}
