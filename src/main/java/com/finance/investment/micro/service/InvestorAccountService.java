package com.finance.investment.micro.service;

import com.finance.investment.micro.service.dto.InvestorAccountDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.finance.investment.micro.domain.InvestorAccount}.
 */
public interface InvestorAccountService {
    /**
     * Save a investorAccount.
     *
     * @param investorAccountDTO the entity to save.
     * @return the persisted entity.
     */
    InvestorAccountDTO save(InvestorAccountDTO investorAccountDTO);

    /**
     * Updates a investorAccount.
     *
     * @param investorAccountDTO the entity to update.
     * @return the persisted entity.
     */
    InvestorAccountDTO update(InvestorAccountDTO investorAccountDTO);

    /**
     * Partially updates a investorAccount.
     *
     * @param investorAccountDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<InvestorAccountDTO> partialUpdate(InvestorAccountDTO investorAccountDTO);

    /**
     * Get all the investorAccounts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<InvestorAccountDTO> findAll(Pageable pageable);

    /**
     * Get the "id" investorAccount.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<InvestorAccountDTO> findOne(Long id);

    /**
     * Delete the "id" investorAccount.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
