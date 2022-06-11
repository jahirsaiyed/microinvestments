package com.finance.investment.micro.service;

import com.finance.investment.micro.service.dto.AssetDetailsDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.finance.investment.micro.domain.AssetDetails}.
 */
public interface AssetDetailsService {
    /**
     * Save a assetDetails.
     *
     * @param assetDetailsDTO the entity to save.
     * @return the persisted entity.
     */
    AssetDetailsDTO save(AssetDetailsDTO assetDetailsDTO);

    /**
     * Updates a assetDetails.
     *
     * @param assetDetailsDTO the entity to update.
     * @return the persisted entity.
     */
    AssetDetailsDTO update(AssetDetailsDTO assetDetailsDTO);

    /**
     * Partially updates a assetDetails.
     *
     * @param assetDetailsDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AssetDetailsDTO> partialUpdate(AssetDetailsDTO assetDetailsDTO);

    /**
     * Get all the assetDetails.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AssetDetailsDTO> findAll(Pageable pageable);

    /**
     * Get the "id" assetDetails.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AssetDetailsDTO> findOne(Long id);

    /**
     * Delete the "id" assetDetails.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
