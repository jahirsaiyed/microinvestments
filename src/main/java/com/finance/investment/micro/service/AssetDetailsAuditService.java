package com.finance.investment.micro.service;

import com.finance.investment.micro.service.dto.AssetDetailsAuditDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.finance.investment.micro.domain.AssetDetailsAudit}.
 */
public interface AssetDetailsAuditService {
    /**
     * Save a assetDetailsAudit.
     *
     * @param assetDetailsAuditDTO the entity to save.
     * @return the persisted entity.
     */
    AssetDetailsAuditDTO save(AssetDetailsAuditDTO assetDetailsAuditDTO);

    /**
     * Updates a assetDetailsAudit.
     *
     * @param assetDetailsAuditDTO the entity to update.
     * @return the persisted entity.
     */
    AssetDetailsAuditDTO update(AssetDetailsAuditDTO assetDetailsAuditDTO);

    /**
     * Partially updates a assetDetailsAudit.
     *
     * @param assetDetailsAuditDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AssetDetailsAuditDTO> partialUpdate(AssetDetailsAuditDTO assetDetailsAuditDTO);

    /**
     * Get all the assetDetailsAudits.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AssetDetailsAuditDTO> findAll(Pageable pageable);

    /**
     * Get the "id" assetDetailsAudit.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AssetDetailsAuditDTO> findOne(Long id);

    /**
     * Delete the "id" assetDetailsAudit.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
