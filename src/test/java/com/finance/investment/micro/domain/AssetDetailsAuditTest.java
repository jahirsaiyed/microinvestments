package com.finance.investment.micro.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.finance.investment.micro.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AssetDetailsAuditTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AssetDetailsAudit.class);
        AssetDetailsAudit assetDetailsAudit1 = new AssetDetailsAudit();
        assetDetailsAudit1.setId(1L);
        AssetDetailsAudit assetDetailsAudit2 = new AssetDetailsAudit();
        assetDetailsAudit2.setId(assetDetailsAudit1.getId());
        assertThat(assetDetailsAudit1).isEqualTo(assetDetailsAudit2);
        assetDetailsAudit2.setId(2L);
        assertThat(assetDetailsAudit1).isNotEqualTo(assetDetailsAudit2);
        assetDetailsAudit1.setId(null);
        assertThat(assetDetailsAudit1).isNotEqualTo(assetDetailsAudit2);
    }
}
