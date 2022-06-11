package com.finance.investment.micro.service.mapper;

import com.finance.investment.micro.domain.Investor;
import com.finance.investment.micro.domain.InvestorAccount;
import com.finance.investment.micro.service.dto.InvestorAccountDTO;
import com.finance.investment.micro.service.dto.InvestorDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link InvestorAccount} and its DTO {@link InvestorAccountDTO}.
 */
@Mapper(componentModel = "spring")
public interface InvestorAccountMapper extends EntityMapper<InvestorAccountDTO, InvestorAccount> {
    @Mapping(target = "investor", source = "investor", qualifiedByName = "investorId")
    InvestorAccountDTO toDto(InvestorAccount s);

    @Named("investorId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    InvestorDTO toDtoInvestorId(Investor investor);
}
