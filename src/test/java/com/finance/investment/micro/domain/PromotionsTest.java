package com.finance.investment.micro.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.finance.investment.micro.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PromotionsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Promotions.class);
        Promotions promotions1 = new Promotions();
        promotions1.setId(1L);
        Promotions promotions2 = new Promotions();
        promotions2.setId(promotions1.getId());
        assertThat(promotions1).isEqualTo(promotions2);
        promotions2.setId(2L);
        assertThat(promotions1).isNotEqualTo(promotions2);
        promotions1.setId(null);
        assertThat(promotions1).isNotEqualTo(promotions2);
    }
}
