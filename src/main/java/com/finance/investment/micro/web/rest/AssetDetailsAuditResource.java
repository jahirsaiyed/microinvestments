package com.finance.investment.micro.web.rest;

import com.finance.investment.micro.repository.AssetDetailsAuditRepository;
import com.finance.investment.micro.service.AssetDetailsAuditService;
import com.finance.investment.micro.service.dto.AssetDetailsAuditDTO;
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
 * REST controller for managing {@link com.finance.investment.micro.domain.AssetDetailsAudit}.
 */
@RestController
@RequestMapping("/api")
public class AssetDetailsAuditResource {

    private final Logger log = LoggerFactory.getLogger(AssetDetailsAuditResource.class);

    private static final String ENTITY_NAME = "assetDetailsAudit";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AssetDetailsAuditService assetDetailsAuditService;

    private final AssetDetailsAuditRepository assetDetailsAuditRepository;

    public AssetDetailsAuditResource(
        AssetDetailsAuditService assetDetailsAuditService,
        AssetDetailsAuditRepository assetDetailsAuditRepository
    ) {
        this.assetDetailsAuditService = assetDetailsAuditService;
        this.assetDetailsAuditRepository = assetDetailsAuditRepository;
    }

    /**
     * {@code POST  /asset-details-audits} : Create a new assetDetailsAudit.
     *
     * @param assetDetailsAuditDTO the assetDetailsAuditDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new assetDetailsAuditDTO, or with status {@code 400 (Bad Request)} if the assetDetailsAudit has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/asset-details-audits")
    public ResponseEntity<AssetDetailsAuditDTO> createAssetDetailsAudit(@RequestBody AssetDetailsAuditDTO assetDetailsAuditDTO)
        throws URISyntaxException {
        log.debug("REST request to save AssetDetailsAudit : {}", assetDetailsAuditDTO);
        if (assetDetailsAuditDTO.getId() != null) {
            throw new BadRequestAlertException("A new assetDetailsAudit cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AssetDetailsAuditDTO result = assetDetailsAuditService.save(assetDetailsAuditDTO);
        return ResponseEntity
            .created(new URI("/api/asset-details-audits/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /asset-details-audits/:id} : Updates an existing assetDetailsAudit.
     *
     * @param id the id of the assetDetailsAuditDTO to save.
     * @param assetDetailsAuditDTO the assetDetailsAuditDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated assetDetailsAuditDTO,
     * or with status {@code 400 (Bad Request)} if the assetDetailsAuditDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the assetDetailsAuditDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/asset-details-audits/{id}")
    public ResponseEntity<AssetDetailsAuditDTO> updateAssetDetailsAudit(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AssetDetailsAuditDTO assetDetailsAuditDTO
    ) throws URISyntaxException {
        log.debug("REST request to update AssetDetailsAudit : {}, {}", id, assetDetailsAuditDTO);
        if (assetDetailsAuditDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, assetDetailsAuditDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!assetDetailsAuditRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AssetDetailsAuditDTO result = assetDetailsAuditService.update(assetDetailsAuditDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, assetDetailsAuditDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /asset-details-audits/:id} : Partial updates given fields of an existing assetDetailsAudit, field will ignore if it is null
     *
     * @param id the id of the assetDetailsAuditDTO to save.
     * @param assetDetailsAuditDTO the assetDetailsAuditDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated assetDetailsAuditDTO,
     * or with status {@code 400 (Bad Request)} if the assetDetailsAuditDTO is not valid,
     * or with status {@code 404 (Not Found)} if the assetDetailsAuditDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the assetDetailsAuditDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/asset-details-audits/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AssetDetailsAuditDTO> partialUpdateAssetDetailsAudit(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AssetDetailsAuditDTO assetDetailsAuditDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update AssetDetailsAudit partially : {}, {}", id, assetDetailsAuditDTO);
        if (assetDetailsAuditDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, assetDetailsAuditDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!assetDetailsAuditRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AssetDetailsAuditDTO> result = assetDetailsAuditService.partialUpdate(assetDetailsAuditDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, assetDetailsAuditDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /asset-details-audits} : get all the assetDetailsAudits.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of assetDetailsAudits in body.
     */
    @GetMapping("/asset-details-audits")
    public ResponseEntity<List<AssetDetailsAuditDTO>> getAllAssetDetailsAudits(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of AssetDetailsAudits");
        Page<AssetDetailsAuditDTO> page = assetDetailsAuditService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /asset-details-audits/:id} : get the "id" assetDetailsAudit.
     *
     * @param id the id of the assetDetailsAuditDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the assetDetailsAuditDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/asset-details-audits/{id}")
    public ResponseEntity<AssetDetailsAuditDTO> getAssetDetailsAudit(@PathVariable Long id) {
        log.debug("REST request to get AssetDetailsAudit : {}", id);
        Optional<AssetDetailsAuditDTO> assetDetailsAuditDTO = assetDetailsAuditService.findOne(id);
        return ResponseUtil.wrapOrNotFound(assetDetailsAuditDTO);
    }

    /**
     * {@code DELETE  /asset-details-audits/:id} : delete the "id" assetDetailsAudit.
     *
     * @param id the id of the assetDetailsAuditDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/asset-details-audits/{id}")
    public ResponseEntity<Void> deleteAssetDetailsAudit(@PathVariable Long id) {
        log.debug("REST request to delete AssetDetailsAudit : {}", id);
        assetDetailsAuditService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
