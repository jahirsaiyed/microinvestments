package com.finance.investment.micro.service.impl;

import com.finance.investment.micro.domain.PromotionsAudit;
import com.finance.investment.micro.repository.PromotionsAuditRepository;
import com.finance.investment.micro.service.PromotionsAuditService;
import com.finance.investment.micro.service.dto.PromotionsAuditDTO;
import com.finance.investment.micro.service.mapper.PromotionsAuditMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PromotionsAudit}.
 */
@Service
@Transactional
public class PromotionsAuditServiceImpl implements PromotionsAuditService {

    private final Logger log = LoggerFactory.getLogger(PromotionsAuditServiceImpl.class);

    private final PromotionsAuditRepository promotionsAuditRepository;

    private final PromotionsAuditMapper promotionsAuditMapper;

    public PromotionsAuditServiceImpl(PromotionsAuditRepository promotionsAuditRepository, PromotionsAuditMapper promotionsAuditMapper) {
        this.promotionsAuditRepository = promotionsAuditRepository;
        this.promotionsAuditMapper = promotionsAuditMapper;
    }

    @Override
    public PromotionsAuditDTO save(PromotionsAuditDTO promotionsAuditDTO) {
        log.debug("Request to save PromotionsAudit : {}", promotionsAuditDTO);
        PromotionsAudit promotionsAudit = promotionsAuditMapper.toEntity(promotionsAuditDTO);
        promotionsAudit = promotionsAuditRepository.save(promotionsAudit);
        return promotionsAuditMapper.toDto(promotionsAudit);
    }

    @Override
    public PromotionsAuditDTO update(PromotionsAuditDTO promotionsAuditDTO) {
        log.debug("Request to save PromotionsAudit : {}", promotionsAuditDTO);
        PromotionsAudit promotionsAudit = promotionsAuditMapper.toEntity(promotionsAuditDTO);
        promotionsAudit = promotionsAuditRepository.save(promotionsAudit);
        return promotionsAuditMapper.toDto(promotionsAudit);
    }

    @Override
    public Optional<PromotionsAuditDTO> partialUpdate(PromotionsAuditDTO promotionsAuditDTO) {
        log.debug("Request to partially update PromotionsAudit : {}", promotionsAuditDTO);

        return promotionsAuditRepository
            .findById(promotionsAuditDTO.getId())
            .map(existingPromotionsAudit -> {
                promotionsAuditMapper.partialUpdate(existingPromotionsAudit, promotionsAuditDTO);

                return existingPromotionsAudit;
            })
            .map(promotionsAuditRepository::save)
            .map(promotionsAuditMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PromotionsAuditDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PromotionsAudits");
        return promotionsAuditRepository.findAll(pageable).map(promotionsAuditMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PromotionsAuditDTO> findOne(Long id) {
        log.debug("Request to get PromotionsAudit : {}", id);
        return promotionsAuditRepository.findById(id).map(promotionsAuditMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PromotionsAudit : {}", id);
        promotionsAuditRepository.deleteById(id);
    }
}
