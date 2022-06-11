package com.finance.investment.micro.web.rest;

import com.finance.investment.micro.repository.PromotionsRepository;
import com.finance.investment.micro.service.PromotionsService;
import com.finance.investment.micro.service.dto.PromotionsDTO;
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
 * REST controller for managing {@link com.finance.investment.micro.domain.Promotions}.
 */
@RestController
@RequestMapping("/api")
public class PromotionsResource {

    private final Logger log = LoggerFactory.getLogger(PromotionsResource.class);

    private static final String ENTITY_NAME = "promotions";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PromotionsService promotionsService;

    private final PromotionsRepository promotionsRepository;

    public PromotionsResource(PromotionsService promotionsService, PromotionsRepository promotionsRepository) {
        this.promotionsService = promotionsService;
        this.promotionsRepository = promotionsRepository;
    }

    /**
     * {@code POST  /promotions} : Create a new promotions.
     *
     * @param promotionsDTO the promotionsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new promotionsDTO, or with status {@code 400 (Bad Request)} if the promotions has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/promotions")
    public ResponseEntity<PromotionsDTO> createPromotions(@RequestBody PromotionsDTO promotionsDTO) throws URISyntaxException {
        log.debug("REST request to save Promotions : {}", promotionsDTO);
        if (promotionsDTO.getId() != null) {
            throw new BadRequestAlertException("A new promotions cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PromotionsDTO result = promotionsService.save(promotionsDTO);
        return ResponseEntity
            .created(new URI("/api/promotions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /promotions/:id} : Updates an existing promotions.
     *
     * @param id the id of the promotionsDTO to save.
     * @param promotionsDTO the promotionsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated promotionsDTO,
     * or with status {@code 400 (Bad Request)} if the promotionsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the promotionsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/promotions/{id}")
    public ResponseEntity<PromotionsDTO> updatePromotions(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PromotionsDTO promotionsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Promotions : {}, {}", id, promotionsDTO);
        if (promotionsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, promotionsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!promotionsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PromotionsDTO result = promotionsService.update(promotionsDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, promotionsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /promotions/:id} : Partial updates given fields of an existing promotions, field will ignore if it is null
     *
     * @param id the id of the promotionsDTO to save.
     * @param promotionsDTO the promotionsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated promotionsDTO,
     * or with status {@code 400 (Bad Request)} if the promotionsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the promotionsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the promotionsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/promotions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PromotionsDTO> partialUpdatePromotions(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PromotionsDTO promotionsDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Promotions partially : {}, {}", id, promotionsDTO);
        if (promotionsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, promotionsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!promotionsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PromotionsDTO> result = promotionsService.partialUpdate(promotionsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, promotionsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /promotions} : get all the promotions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of promotions in body.
     */
    @GetMapping("/promotions")
    public ResponseEntity<List<PromotionsDTO>> getAllPromotions(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Promotions");
        Page<PromotionsDTO> page = promotionsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /promotions/:id} : get the "id" promotions.
     *
     * @param id the id of the promotionsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the promotionsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/promotions/{id}")
    public ResponseEntity<PromotionsDTO> getPromotions(@PathVariable Long id) {
        log.debug("REST request to get Promotions : {}", id);
        Optional<PromotionsDTO> promotionsDTO = promotionsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(promotionsDTO);
    }

    /**
     * {@code DELETE  /promotions/:id} : delete the "id" promotions.
     *
     * @param id the id of the promotionsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/promotions/{id}")
    public ResponseEntity<Void> deletePromotions(@PathVariable Long id) {
        log.debug("REST request to delete Promotions : {}", id);
        promotionsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
