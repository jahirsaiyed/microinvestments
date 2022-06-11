package com.finance.investment.micro.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.finance.investment.micro.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AssetDetailsAuditDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AssetDetailsAuditDTO.class);
        AssetDetailsAuditDTO assetDetailsAuditDTO1 = new AssetDetailsAuditDTO();
        assetDetailsAuditDTO1.setId(1L);
        AssetDetailsAuditDTO assetDetailsAuditDTO2 = new AssetDetailsAuditDTO();
        assertThat(assetDetailsAuditDTO1).isNotEqualTo(assetDetailsAuditDTO2);
        assetDetailsAuditDTO2.setId(assetDetailsAuditDTO1.getId());
        assertThat(assetDetailsAuditDTO1).isEqualTo(assetDetailsAuditDTO2);
        assetDetailsAuditDTO2.setId(2L);
        assertThat(assetDetailsAuditDTO1).isNotEqualTo(assetDetailsAuditDTO2);
        assetDetailsAuditDTO1.setId(null);
        assertThat(assetDetailsAuditDTO1).isNotEqualTo(assetDetailsAuditDTO2);
    }
}
