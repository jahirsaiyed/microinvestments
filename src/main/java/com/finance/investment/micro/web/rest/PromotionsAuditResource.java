package com.finance.investment.micro.web.rest;

import com.finance.investment.micro.repository.PromotionsAuditRepository;
import com.finance.investment.micro.service.PromotionsAuditService;
import com.finance.investment.micro.service.dto.PromotionsAuditDTO;
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
 * REST controller for managing {@link com.finance.investment.micro.domain.PromotionsAudit}.
 */
@RestController
@RequestMapping("/api")
public class PromotionsAuditResource {

    private final Logger log = LoggerFactory.getLogger(PromotionsAuditResource.class);

    private static final String ENTITY_NAME = "promotionsAudit";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PromotionsAuditService promotionsAuditService;

    private final PromotionsAuditRepository promotionsAuditRepository;

    public PromotionsAuditResource(PromotionsAuditService promotionsAuditService, PromotionsAuditRepository promotionsAuditRepository) {
        this.promotionsAuditService = promotionsAuditService;
        this.promotionsAuditRepository = promotionsAuditRepository;
    }

    /**
     * {@code POST  /promotions-audits} : Create a new promotionsAudit.
     *
     * @param promotionsAuditDTO the promotionsAuditDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new promotionsAuditDTO, or with status {@code 400 (Bad Request)} if the promotionsAudit has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/promotions-audits")
    public ResponseEntity<PromotionsAuditDTO> createPromotionsAudit(@RequestBody PromotionsAuditDTO promotionsAuditDTO)
        throws URISyntaxException {
        log.debug("REST request to save PromotionsAudit : {}", promotionsAuditDTO);
        if (promotionsAuditDTO.getId() != null) {
            throw new BadRequestAlertException("A new promotionsAudit cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PromotionsAuditDTO result = promotionsAuditService.save(promotionsAuditDTO);
        return ResponseEntity
            .created(new URI("/api/promotions-audits/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /promotions-audits/:id} : Updates an existing promotionsAudit.
     *
     * @param id the id of the promotionsAuditDTO to save.
     * @param promotionsAuditDTO the promotionsAuditDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated promotionsAuditDTO,
     * or with status {@code 400 (Bad Request)} if the promotionsAuditDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the promotionsAuditDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/promotions-audits/{id}")
    public ResponseEntity<PromotionsAuditDTO> updatePromotionsAudit(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PromotionsAuditDTO promotionsAuditDTO
    ) throws URISyntaxException {
        log.debug("REST request to update PromotionsAudit : {}, {}", id, promotionsAuditDTO);
        if (promotionsAuditDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, promotionsAuditDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!promotionsAuditRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PromotionsAuditDTO result = promotionsAuditService.update(promotionsAuditDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, promotionsAuditDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /promotions-audits/:id} : Partial updates given fields of an existing promotionsAudit, field will ignore if it is null
     *
     * @param id the id of the promotionsAuditDTO to save.
     * @param promotionsAuditDTO the promotionsAuditDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated promotionsAuditDTO,
     * or with status {@code 400 (Bad Request)} if the promotionsAuditDTO is not valid,
     * or with status {@code 404 (Not Found)} if the promotionsAuditDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the promotionsAuditDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/promotions-audits/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PromotionsAuditDTO> partialUpdatePromotionsAudit(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PromotionsAuditDTO promotionsAuditDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update PromotionsAudit partially : {}, {}", id, promotionsAuditDTO);
        if (promotionsAuditDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, promotionsAuditDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!promotionsAuditRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PromotionsAuditDTO> result = promotionsAuditService.partialUpdate(promotionsAuditDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, promotionsAuditDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /promotions-audits} : get all the promotionsAudits.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of promotionsAudits in body.
     */
    @GetMapping("/promotions-audits")
    public ResponseEntity<List<PromotionsAuditDTO>> getAllPromotionsAudits(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of PromotionsAudits");
        Page<PromotionsAuditDTO> page = promotionsAuditService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /promotions-audits/:id} : get the "id" promotionsAudit.
     *
     * @param id the id of the promotionsAuditDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the promotionsAuditDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/promotions-audits/{id}")
    public ResponseEntity<PromotionsAuditDTO> getPromotionsAudit(@PathVariable Long id) {
        log.debug("REST request to get PromotionsAudit : {}", id);
        Optional<PromotionsAuditDTO> promotionsAuditDTO = promotionsAuditService.findOne(id);
        return ResponseUtil.wrapOrNotFound(promotionsAuditDTO);
    }

    /**
     * {@code DELETE  /promotions-audits/:id} : delete the "id" promotionsAudit.
     *
     * @param id the id of the promotionsAuditDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/promotions-audits/{id}")
    public ResponseEntity<Void> deletePromotionsAudit(@PathVariable Long id) {
        log.debug("REST request to delete PromotionsAudit : {}", id);
        promotionsAuditService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
