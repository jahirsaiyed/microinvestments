package com.finance.investment.micro.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InvestorAccountMapperTest {

    private InvestorAccountMapper investorAccountMapper;

    @BeforeEach
    public void setUp() {
        investorAccountMapper = new InvestorAccountMapperImpl();
    }
}
