package com.finance.investment.micro.service.impl;

import com.finance.investment.micro.domain.AssetDetails;
import com.finance.investment.micro.repository.AssetDetailsRepository;
import com.finance.investment.micro.service.AssetDetailsService;
import com.finance.investment.micro.service.dto.AssetDetailsDTO;
import com.finance.investment.micro.service.mapper.AssetDetailsMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link AssetDetails}.
 */
@Service
@Transactional
public class AssetDetailsServiceImpl implements AssetDetailsService {

    private final Logger log = LoggerFactory.getLogger(AssetDetailsServiceImpl.class);

    private final AssetDetailsRepository assetDetailsRepository;

    private final AssetDetailsMapper assetDetailsMapper;

    public AssetDetailsServiceImpl(AssetDetailsRepository assetDetailsRepository, AssetDetailsMapper assetDetailsMapper) {
        this.assetDetailsRepository = assetDetailsRepository;
        this.assetDetailsMapper = assetDetailsMapper;
    }

    @Override
    public AssetDetailsDTO save(AssetDetailsDTO assetDetailsDTO) {
        log.debug("Request to save AssetDetails : {}", assetDetailsDTO);
        AssetDetails assetDetails = assetDetailsMapper.toEntity(assetDetailsDTO);
        assetDetails = assetDetailsRepository.save(assetDetails);
        return assetDetailsMapper.toDto(assetDetails);
    }

    @Override
    public AssetDetailsDTO update(AssetDetailsDTO assetDetailsDTO) {
        log.debug("Request to save AssetDetails : {}", assetDetailsDTO);
        AssetDetails assetDetails = assetDetailsMapper.toEntity(assetDetailsDTO);
        assetDetails = assetDetailsRepository.save(assetDetails);
        return assetDetailsMapper.toDto(assetDetails);
    }

    @Override
    public Optional<AssetDetailsDTO> partialUpdate(AssetDetailsDTO assetDetailsDTO) {
        log.debug("Request to partially update AssetDetails : {}", assetDetailsDTO);

        return assetDetailsRepository
            .findById(assetDetailsDTO.getId())
            .map(existingAssetDetails -> {
                assetDetailsMapper.partialUpdate(existingAssetDetails, assetDetailsDTO);

                return existingAssetDetails;
            })
            .map(assetDetailsRepository::save)
            .map(assetDetailsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AssetDetailsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AssetDetails");
        return assetDetailsRepository.findAll(pageable).map(assetDetailsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AssetDetailsDTO> findOne(Long id) {
        log.debug("Request to get AssetDetails : {}", id);
        return assetDetailsRepository.findById(id).map(assetDetailsMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AssetDetails : {}", id);
        assetDetailsRepository.deleteById(id);
    }
}
