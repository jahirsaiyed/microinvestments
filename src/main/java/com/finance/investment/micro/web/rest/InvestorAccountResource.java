package com.finance.investment.micro.web.rest;

import com.finance.investment.micro.repository.InvestorAccountRepository;
import com.finance.investment.micro.service.InvestorAccountService;
import com.finance.investment.micro.service.dto.InvestorAccountDTO;
import com.finance.investment.micro.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.finance.investment.micro.domain.InvestorAccount}.
 */
@RestController
@RequestMapping("/api")
public class InvestorAccountResource {

    private final Logger log = LoggerFactory.getLogger(InvestorAccountResource.class);

    private static final String ENTITY_NAME = "investorAccount";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InvestorAccountService investorAccountService;

    private final InvestorAccountRepository investorAccountRepository;

    public InvestorAccountResource(InvestorAccountService investorAccountService, InvestorAccountRepository investorAccountRepository) {
        this.investorAccountService = investorAccountService;
        this.investorAccountRepository = investorAccountRepository;
    }

    /**
     * {@code POST  /investor-accounts} : Create a new investorAccount.
     *
     * @param investorAccountDTO the investorAccountDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new investorAccountDTO, or with status {@code 400 (Bad Request)} if the investorAccount has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/investor-accounts")
    public ResponseEntity<InvestorAccountDTO> createInvestorAccount(@RequestBody InvestorAccountDTO investorAccountDTO)
        throws URISyntaxException {
        log.debug("REST request to save InvestorAccount : {}", investorAccountDTO);
        if (investorAccountDTO.getId() != null) {
            throw new BadRequestAlertException("A new investorAccount cannot already have an ID", ENTITY_NAME, "idexists");
        }
        InvestorAccountDTO result = investorAccountService.save(investorAccountDTO);
        return ResponseEntity
            .created(new URI("/api/investor-accounts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /investor-accounts/:id} : Updates an existing investorAccount.
     *
     * @param id the id of the investorAccountDTO to save.
     * @param investorAccountDTO the investorAccountDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated investorAccountDTO,
     * or with status {@code 400 (Bad Request)} if the investorAccountDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the investorAccountDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/investor-accounts/{id}")
    public ResponseEntity<InvestorAccountDTO> updateInvestorAccount(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody InvestorAccountDTO investorAccountDTO
    ) throws URISyntaxException {
        log.debug("REST request to update InvestorAccount : {}, {}", id, investorAccountDTO);
        if (investorAccountDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, investorAccountDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!investorAccountRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        InvestorAccountDTO result = investorAccountService.update(investorAccountDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, investorAccountDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /investor-accounts/:id} : Partial updates given fields of an existing investorAccount, field will ignore if it is null
     *
     * @param id the id of the investorAccountDTO to save.
     * @param investorAccountDTO the investorAccountDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated investorAccountDTO,
     * or with status {@code 400 (Bad Request)} if the investorAccountDTO is not valid,
     * or with status {@code 404 (Not Found)} if the investorAccountDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the investorAccountDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/investor-accounts/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<InvestorAccountDTO> partialUpdateInvestorAccount(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody InvestorAccountDTO investorAccountDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update InvestorAccount partially : {}, {}", id, investorAccountDTO);
        if (investorAccountDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, investorAccountDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!investorAccountRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<InvestorAccountDTO> result = investorAccountService.partialUpdate(investorAccountDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, investorAccountDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /investor-accounts} : get all the investorAccounts.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of investorAccounts in body.
     */
    @GetMapping("/investor-accounts")
    public ResponseEntity<List<InvestorAccountDTO>> getAllInvestorAccounts(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of InvestorAccounts");
        Page<InvestorAccountDTO> page = investorAccountService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /investor-accounts/:id} : get the "id" investorAccount.
     *
     * @param id the id of the investorAccountDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the investorAccountDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/investor-accounts/{id}")
    public ResponseEntity<InvestorAccountDTO> getInvestorAccount(@PathVariable Long id) {
        log.debug("REST request to get InvestorAccount : {}", id);
        Optional<InvestorAccountDTO> investorAccountDTO = investorAccountService.findOne(id);
        return ResponseUtil.wrapOrNotFound(investorAccountDTO);
    }

    /**
     * {@code DELETE  /investor-accounts/:id} : delete the "id" investorAccount.
     *
     * @param id the id of the investorAccountDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/investor-accounts/{id}")
    public ResponseEntity<Void> deleteInvestorAccount(@PathVariable Long id) {
        log.debug("REST request to delete InvestorAccount : {}", id);
        investorAccountService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
