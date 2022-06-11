package com.finance.investment.micro.service;

import com.finance.investment.micro.service.dto.PromotionsAuditDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.finance.investment.micro.domain.PromotionsAudit}.
 */
public interface PromotionsAuditService {
    /**
     * Save a promotionsAudit.
     *
     * @param promotionsAuditDTO the entity to save.
     * @return the persisted entity.
     */
    PromotionsAuditDTO save(PromotionsAuditDTO promotionsAuditDTO);

    /**
     * Updates a promotionsAudit.
     *
     * @param promotionsAuditDTO the entity to update.
     * @return the persisted entity.
     */
    PromotionsAuditDTO update(PromotionsAuditDTO promotionsAuditDTO);

    /**
     * Partially updates a promotionsAudit.
     *
     * @param promotionsAuditDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PromotionsAuditDTO> partialUpdate(PromotionsAuditDTO promotionsAuditDTO);

    /**
     * Get all the promotionsAudits.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PromotionsAuditDTO> findAll(Pageable pageable);

    /**
     * Get the "id" promotionsAudit.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PromotionsAuditDTO> findOne(Long id);

    /**
     * Delete the "id" promotionsAudit.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
