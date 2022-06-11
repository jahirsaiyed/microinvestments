package com.finance.investment.micro.service.impl;

import com.finance.investment.micro.domain.Promotions;
import com.finance.investment.micro.repository.PromotionsRepository;
import com.finance.investment.micro.service.PromotionsService;
import com.finance.investment.micro.service.dto.PromotionsDTO;
import com.finance.investment.micro.service.mapper.PromotionsMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Promotions}.
 */
@Service
@Transactional
public class PromotionsServiceImpl implements PromotionsService {

    private final Logger log = LoggerFactory.getLogger(PromotionsServiceImpl.class);

    private final PromotionsRepository promotionsRepository;

    private final PromotionsMapper promotionsMapper;

    public PromotionsServiceImpl(PromotionsRepository promotionsRepository, PromotionsMapper promotionsMapper) {
        this.promotionsRepository = promotionsRepository;
        this.promotionsMapper = promotionsMapper;
    }

    @Override
    public PromotionsDTO save(PromotionsDTO promotionsDTO) {
        log.debug("Request to save Promotions : {}", promotionsDTO);
        Promotions promotions = promotionsMapper.toEntity(promotionsDTO);
        promotions = promotionsRepository.save(promotions);
        return promotionsMapper.toDto(promotions);
    }

    @Override
    public PromotionsDTO update(PromotionsDTO promotionsDTO) {
        log.debug("Request to save Promotions : {}", promotionsDTO);
        Promotions promotions = promotionsMapper.toEntity(promotionsDTO);
        promotions = promotionsRepository.save(promotions);
        return promotionsMapper.toDto(promotions);
    }

    @Override
    public Optional<PromotionsDTO> partialUpdate(PromotionsDTO promotionsDTO) {
        log.debug("Request to partially update Promotions : {}", promotionsDTO);

        return promotionsRepository
            .findById(promotionsDTO.getId())
            .map(existingPromotions -> {
                promotionsMapper.partialUpdate(existingPromotions, promotionsDTO);

                return existingPromotions;
            })
            .map(promotionsRepository::save)
            .map(promotionsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PromotionsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Promotions");
        return promotionsRepository.findAll(pageable).map(promotionsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PromotionsDTO> findOne(Long id) {
        log.debug("Request to get Promotions : {}", id);
        return promotionsRepository.findById(id).map(promotionsMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Promotions : {}", id);
        promotionsRepository.deleteById(id);
    }
}
