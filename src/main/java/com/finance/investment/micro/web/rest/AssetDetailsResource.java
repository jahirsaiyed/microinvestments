package com.finance.investment.micro.web.rest;

import com.finance.investment.micro.repository.AssetDetailsRepository;
import com.finance.investment.micro.service.AssetDetailsService;
import com.finance.investment.micro.service.dto.AssetDetailsDTO;
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
 * REST controller for managing {@link com.finance.investment.micro.domain.AssetDetails}.
 */
@RestController
@RequestMapping("/api")
public class AssetDetailsResource {

    private final Logger log = LoggerFactory.getLogger(AssetDetailsResource.class);

    private static final String ENTITY_NAME = "assetDetails";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AssetDetailsService assetDetailsService;

    private final AssetDetailsRepository assetDetailsRepository;

    public AssetDetailsResource(AssetDetailsService assetDetailsService, AssetDetailsRepository assetDetailsRepository) {
        this.assetDetailsService = assetDetailsService;
        this.assetDetailsRepository = assetDetailsRepository;
    }

    /**
     * {@code POST  /asset-details} : Create a new assetDetails.
     *
     * @param assetDetailsDTO the assetDetailsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new assetDetailsDTO, or with status {@code 400 (Bad Request)} if the assetDetails has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/asset-details")
    public ResponseEntity<AssetDetailsDTO> createAssetDetails(@RequestBody AssetDetailsDTO assetDetailsDTO) throws URISyntaxException {
        log.debug("REST request to save AssetDetails : {}", assetDetailsDTO);
        if (assetDetailsDTO.getId() != null) {
            throw new BadRequestAlertException("A new assetDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AssetDetailsDTO result = assetDetailsService.save(assetDetailsDTO);
        return ResponseEntity
            .created(new URI("/api/asset-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /asset-details/:id} : Updates an existing assetDetails.
     *
     * @param id the id of the assetDetailsDTO to save.
     * @param assetDetailsDTO the assetDetailsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated assetDetailsDTO,
     * or with status {@code 400 (Bad Request)} if the assetDetailsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the assetDetailsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/asset-details/{id}")
    public ResponseEntity<AssetDetailsDTO> updateAssetDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AssetDetailsDTO assetDetailsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update AssetDetails : {}, {}", id, assetDetailsDTO);
        if (assetDetailsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, assetDetailsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!assetDetailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AssetDetailsDTO result = assetDetailsService.update(assetDetailsDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, assetDetailsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /asset-details/:id} : Partial updates given fields of an existing assetDetails, field will ignore if it is null
     *
     * @param id the id of the assetDetailsDTO to save.
     * @param assetDetailsDTO the assetDetailsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated assetDetailsDTO,
     * or with status {@code 400 (Bad Request)} if the assetDetailsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the assetDetailsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the assetDetailsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/asset-details/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AssetDetailsDTO> partialUpdateAssetDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AssetDetailsDTO assetDetailsDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update AssetDetails partially : {}, {}", id, assetDetailsDTO);
        if (assetDetailsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, assetDetailsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!assetDetailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AssetDetailsDTO> result = assetDetailsService.partialUpdate(assetDetailsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, assetDetailsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /asset-details} : get all the assetDetails.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of assetDetails in body.
     */
    @GetMapping("/asset-details")
    public ResponseEntity<List<AssetDetailsDTO>> getAllAssetDetails(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of AssetDetails");
        Page<AssetDetailsDTO> page = assetDetailsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /asset-details/:id} : get the "id" assetDetails.
     *
     * @param id the id of the assetDetailsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the assetDetailsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/asset-details/{id}")
    public ResponseEntity<AssetDetailsDTO> getAssetDetails(@PathVariable Long id) {
        log.debug("REST request to get AssetDetails : {}", id);
        Optional<AssetDetailsDTO> assetDetailsDTO = assetDetailsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(assetDetailsDTO);
    }

    /**
     * {@code DELETE  /asset-details/:id} : delete the "id" assetDetails.
     *
     * @param id the id of the assetDetailsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/asset-details/{id}")
    public ResponseEntity<Void> deleteAssetDetails(@PathVariable Long id) {
        log.debug("REST request to delete AssetDetails : {}", id);
        assetDetailsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
