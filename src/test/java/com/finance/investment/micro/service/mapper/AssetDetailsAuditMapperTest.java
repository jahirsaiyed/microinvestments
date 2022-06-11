package com.finance.investment.micro.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AssetDetailsAuditMapperTest {

    private AssetDetailsAuditMapper assetDetailsAuditMapper;

    @BeforeEach
    public void setUp() {
        assetDetailsAuditMapper = new AssetDetailsAuditMapperImpl();
    }
}
