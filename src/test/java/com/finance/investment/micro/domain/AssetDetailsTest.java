package com.finance.investment.micro.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.finance.investment.micro.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AssetDetailsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AssetDetails.class);
        AssetDetails assetDetails1 = new AssetDetails();
        assetDetails1.setId(1L);
        AssetDetails assetDetails2 = new AssetDetails();
        assetDetails2.setId(assetDetails1.getId());
        assertThat(assetDetails1).isEqualTo(assetDetails2);
        assetDetails2.setId(2L);
        assertThat(assetDetails1).isNotEqualTo(assetDetails2);
        assetDetails1.setId(null);
        assertThat(assetDetails1).isNotEqualTo(assetDetails2);
    }
}
