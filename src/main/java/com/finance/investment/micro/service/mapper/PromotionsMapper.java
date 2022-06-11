package com.finance.investment.micro.service.mapper;

import com.finance.investment.micro.domain.Promotions;
import com.finance.investment.micro.service.dto.PromotionsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Promotions} and its DTO {@link PromotionsDTO}.
 */
@Mapper(componentModel = "spring")
public interface PromotionsMapper extends EntityMapper<PromotionsDTO, Promotions> {}
