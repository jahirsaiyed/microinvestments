package com.finance.investment.micro.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.finance.investment.micro.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AssetDetailsDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AssetDetailsDTO.class);
        AssetDetailsDTO assetDetailsDTO1 = new AssetDetailsDTO();
        assetDetailsDTO1.setId(1L);
        AssetDetailsDTO assetDetailsDTO2 = new AssetDetailsDTO();
        assertThat(assetDetailsDTO1).isNotEqualTo(assetDetailsDTO2);
        assetDetailsDTO2.setId(assetDetailsDTO1.getId());
        assertThat(assetDetailsDTO1).isEqualTo(assetDetailsDTO2);
        assetDetailsDTO2.setId(2L);
        assertThat(assetDetailsDTO1).isNotEqualTo(assetDetailsDTO2);
        assetDetailsDTO1.setId(null);
        assertThat(assetDetailsDTO1).isNotEqualTo(assetDetailsDTO2);
    }
}
