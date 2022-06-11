package com.finance.investment.micro.service;

import com.finance.investment.micro.service.dto.PromotionsDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.finance.investment.micro.domain.Promotions}.
 */
public interface PromotionsService {
    /**
     * Save a promotions.
     *
     * @param promotionsDTO the entity to save.
     * @return the persisted entity.
     */
    PromotionsDTO save(PromotionsDTO promotionsDTO);

    /**
     * Updates a promotions.
     *
     * @param promotionsDTO the entity to update.
     * @return the persisted entity.
     */
    PromotionsDTO update(PromotionsDTO promotionsDTO);

    /**
     * Partially updates a promotions.
     *
     * @param promotionsDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PromotionsDTO> partialUpdate(PromotionsDTO promotionsDTO);

    /**
     * Get all the promotions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PromotionsDTO> findAll(Pageable pageable);

    /**
     * Get the "id" promotions.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PromotionsDTO> findOne(Long id);

    /**
     * Delete the "id" promotions.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
