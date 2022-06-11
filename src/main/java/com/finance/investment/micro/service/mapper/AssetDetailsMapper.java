package com.finance.investment.micro.service.mapper;

import com.finance.investment.micro.domain.AssetDetails;
import com.finance.investment.micro.service.dto.AssetDetailsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AssetDetails} and its DTO {@link AssetDetailsDTO}.
 */
@Mapper(componentModel = "spring")
public interface AssetDetailsMapper extends EntityMapper<AssetDetailsDTO, AssetDetails> {}
