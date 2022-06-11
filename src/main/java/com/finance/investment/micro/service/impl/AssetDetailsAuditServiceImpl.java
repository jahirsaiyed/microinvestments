package com.finance.investment.micro.service.impl;

import com.finance.investment.micro.domain.AssetDetailsAudit;
import com.finance.investment.micro.repository.AssetDetailsAuditRepository;
import com.finance.investment.micro.service.AssetDetailsAuditService;
import com.finance.investment.micro.service.dto.AssetDetailsAuditDTO;
import com.finance.investment.micro.service.mapper.AssetDetailsAuditMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link AssetDetailsAudit}.
 */
@Service
@Transactional
public class AssetDetailsAuditServiceImpl implements AssetDetailsAuditService {

    private final Logger log = LoggerFactory.getLogger(AssetDetailsAuditServiceImpl.class);

    private final AssetDetailsAuditRepository assetDetailsAuditRepository;

    private final AssetDetailsAuditMapper assetDetailsAuditMapper;

    public AssetDetailsAuditServiceImpl(
        AssetDetailsAuditRepository assetDetailsAuditRepository,
        AssetDetailsAuditMapper assetDetailsAuditMapper
    ) {
        this.assetDetailsAuditRepository = assetDetailsAuditRepository;
        this.assetDetailsAuditMapper = assetDetailsAuditMapper;
    }

    @Override
    public AssetDetailsAuditDTO save(AssetDetailsAuditDTO assetDetailsAuditDTO) {
        log.debug("Request to save AssetDetailsAudit : {}", assetDetailsAuditDTO);
        AssetDetailsAudit assetDetailsAudit = assetDetailsAuditMapper.toEntity(assetDetailsAuditDTO);
        assetDetailsAudit = assetDetailsAuditRepository.save(assetDetailsAudit);
        return assetDetailsAuditMapper.toDto(assetDetailsAudit);
    }

    @Override
    public AssetDetailsAuditDTO update(AssetDetailsAuditDTO assetDetailsAuditDTO) {
        log.debug("Request to save AssetDetailsAudit : {}", assetDetailsAuditDTO);
        AssetDetailsAudit assetDetailsAudit = assetDetailsAuditMapper.toEntity(assetDetailsAuditDTO);
        assetDetailsAudit = assetDetailsAuditRepository.save(assetDetailsAudit);
        return assetDetailsAuditMapper.toDto(assetDetailsAudit);
    }

    @Override
    public Optional<AssetDetailsAuditDTO> partialUpdate(AssetDetailsAuditDTO assetDetailsAuditDTO) {
        log.debug("Request to partially update AssetDetailsAudit : {}", assetDetailsAuditDTO);

        return assetDetailsAuditRepository
            .findById(assetDetailsAuditDTO.getId())
            .map(existingAssetDetailsAudit -> {
                assetDetailsAuditMapper.partialUpdate(existingAssetDetailsAudit, assetDetailsAuditDTO);

                return existingAssetDetailsAudit;
            })
            .map(assetDetailsAuditRepository::save)
            .map(assetDetailsAuditMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AssetDetailsAuditDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AssetDetailsAudits");
        return assetDetailsAuditRepository.findAll(pageable).map(assetDetailsAuditMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AssetDetailsAuditDTO> findOne(Long id) {
        log.debug("Request to get AssetDetailsAudit : {}", id);
        return assetDetailsAuditRepository.findById(id).map(assetDetailsAuditMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AssetDetailsAudit : {}", id);
        assetDetailsAuditRepository.deleteById(id);
    }
}
