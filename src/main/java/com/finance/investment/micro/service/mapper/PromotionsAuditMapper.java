package com.finance.investment.micro.service.mapper;

import com.finance.investment.micro.domain.PromotionsAudit;
import com.finance.investment.micro.service.dto.PromotionsAuditDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PromotionsAudit} and its DTO {@link PromotionsAuditDTO}.
 */
@Mapper(componentModel = "spring")
public interface PromotionsAuditMapper extends EntityMapper<PromotionsAuditDTO, PromotionsAudit> {}
