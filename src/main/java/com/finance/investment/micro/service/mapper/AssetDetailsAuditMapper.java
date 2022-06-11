package com.finance.investment.micro.service.mapper;

import com.finance.investment.micro.domain.AssetDetailsAudit;
import com.finance.investment.micro.service.dto.AssetDetailsAuditDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AssetDetailsAudit} and its DTO {@link AssetDetailsAuditDTO}.
 */
@Mapper(componentModel = "spring")
public interface AssetDetailsAuditMapper extends EntityMapper<AssetDetailsAuditDTO, AssetDetailsAudit> {}
