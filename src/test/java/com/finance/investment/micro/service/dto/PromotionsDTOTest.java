package com.finance.investment.micro.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.finance.investment.micro.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PromotionsDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PromotionsDTO.class);
        PromotionsDTO promotionsDTO1 = new PromotionsDTO();
        promotionsDTO1.setId(1L);
        PromotionsDTO promotionsDTO2 = new PromotionsDTO();
        assertThat(promotionsDTO1).isNotEqualTo(promotionsDTO2);
        promotionsDTO2.setId(promotionsDTO1.getId());
        assertThat(promotionsDTO1).isEqualTo(promotionsDTO2);
        promotionsDTO2.setId(2L);
        assertThat(promotionsDTO1).isNotEqualTo(promotionsDTO2);
        promotionsDTO1.setId(null);
        assertThat(promotionsDTO1).isNotEqualTo(promotionsDTO2);
    }
}
