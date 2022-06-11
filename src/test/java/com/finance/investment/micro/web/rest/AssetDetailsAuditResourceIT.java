package com.finance.investment.micro.web.rest;

import static com.finance.investment.micro.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.finance.investment.micro.IntegrationTest;
import com.finance.investment.micro.domain.AssetDetailsAudit;
import com.finance.investment.micro.repository.AssetDetailsAuditRepository;
import com.finance.investment.micro.service.dto.AssetDetailsAuditDTO;
import com.finance.investment.micro.service.mapper.AssetDetailsAuditMapper;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link AssetDetailsAuditResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class AssetDetailsAuditResourceIT {

    private static final BigDecimal DEFAULT_UNITS = new BigDecimal(1);
    private static final BigDecimal UPDATED_UNITS = new BigDecimal(2);

    private static final BigDecimal DEFAULT_UNIT_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_UNIT_PRICE = new BigDecimal(2);

    private static final BigDecimal DEFAULT_BALANCE = new BigDecimal(1);
    private static final BigDecimal UPDATED_BALANCE = new BigDecimal(2);

    private static final BigDecimal DEFAULT_CURRENT_INVESTED_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_CURRENT_INVESTED_AMOUNT = new BigDecimal(2);

    private static final BigDecimal DEFAULT_PROFIT_LOSS = new BigDecimal(1);
    private static final BigDecimal UPDATED_PROFIT_LOSS = new BigDecimal(2);

    private static final Instant DEFAULT_CREATED_ON = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_ON = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_ON = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_ON = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/asset-details-audits";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AssetDetailsAuditRepository assetDetailsAuditRepository;

    @Autowired
    private AssetDetailsAuditMapper assetDetailsAuditMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAssetDetailsAuditMockMvc;

    private AssetDetailsAudit assetDetailsAudit;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AssetDetailsAudit createEntity(EntityManager em) {
        AssetDetailsAudit assetDetailsAudit = new AssetDetailsAudit()
            .units(DEFAULT_UNITS)
            .unitPrice(DEFAULT_UNIT_PRICE)
            .balance(DEFAULT_BALANCE)
            .currentInvestedAmount(DEFAULT_CURRENT_INVESTED_AMOUNT)
            .profitLoss(DEFAULT_PROFIT_LOSS)
            .createdOn(DEFAULT_CREATED_ON)
            .updatedOn(DEFAULT_UPDATED_ON);
        return assetDetailsAudit;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AssetDetailsAudit createUpdatedEntity(EntityManager em) {
        AssetDetailsAudit assetDetailsAudit = new AssetDetailsAudit()
            .units(UPDATED_UNITS)
            .unitPrice(UPDATED_UNIT_PRICE)
            .balance(UPDATED_BALANCE)
            .currentInvestedAmount(UPDATED_CURRENT_INVESTED_AMOUNT)
            .profitLoss(UPDATED_PROFIT_LOSS)
            .createdOn(UPDATED_CREATED_ON)
            .updatedOn(UPDATED_UPDATED_ON);
        return assetDetailsAudit;
    }

    @BeforeEach
    public void initTest() {
        assetDetailsAudit = createEntity(em);
    }

    @Test
    @Transactional
    void createAssetDetailsAudit() throws Exception {
        int databaseSizeBeforeCreate = assetDetailsAuditRepository.findAll().size();
        // Create the AssetDetailsAudit
        AssetDetailsAuditDTO assetDetailsAuditDTO = assetDetailsAuditMapper.toDto(assetDetailsAudit);
        restAssetDetailsAuditMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(assetDetailsAuditDTO))
            )
            .andExpect(status().isCreated());

        // Validate the AssetDetailsAudit in the database
        List<AssetDetailsAudit> assetDetailsAuditList = assetDetailsAuditRepository.findAll();
        assertThat(assetDetailsAuditList).hasSize(databaseSizeBeforeCreate + 1);
        AssetDetailsAudit testAssetDetailsAudit = assetDetailsAuditList.get(assetDetailsAuditList.size() - 1);
        assertThat(testAssetDetailsAudit.getUnits()).isEqualByComparingTo(DEFAULT_UNITS);
        assertThat(testAssetDetailsAudit.getUnitPrice()).isEqualByComparingTo(DEFAULT_UNIT_PRICE);
        assertThat(testAssetDetailsAudit.getBalance()).isEqualByComparingTo(DEFAULT_BALANCE);
        assertThat(testAssetDetailsAudit.getCurrentInvestedAmount()).isEqualByComparingTo(DEFAULT_CURRENT_INVESTED_AMOUNT);
        assertThat(testAssetDetailsAudit.getProfitLoss()).isEqualByComparingTo(DEFAULT_PROFIT_LOSS);
        assertThat(testAssetDetailsAudit.getCreatedOn()).isEqualTo(DEFAULT_CREATED_ON);
        assertThat(testAssetDetailsAudit.getUpdatedOn()).isEqualTo(DEFAULT_UPDATED_ON);
    }

    @Test
    @Transactional
    void createAssetDetailsAuditWithExistingId() throws Exception {
        // Create the AssetDetailsAudit with an existing ID
        assetDetailsAudit.setId(1L);
        AssetDetailsAuditDTO assetDetailsAuditDTO = assetDetailsAuditMapper.toDto(assetDetailsAudit);

        int databaseSizeBeforeCreate = assetDetailsAuditRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAssetDetailsAuditMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(assetDetailsAuditDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssetDetailsAudit in the database
        List<AssetDetailsAudit> assetDetailsAuditList = assetDetailsAuditRepository.findAll();
        assertThat(assetDetailsAuditList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAssetDetailsAudits() throws Exception {
        // Initialize the database
        assetDetailsAuditRepository.saveAndFlush(assetDetailsAudit);

        // Get all the assetDetailsAuditList
        restAssetDetailsAuditMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(assetDetailsAudit.getId().intValue())))
            .andExpect(jsonPath("$.[*].units").value(hasItem(sameNumber(DEFAULT_UNITS))))
            .andExpect(jsonPath("$.[*].unitPrice").value(hasItem(sameNumber(DEFAULT_UNIT_PRICE))))
            .andExpect(jsonPath("$.[*].balance").value(hasItem(sameNumber(DEFAULT_BALANCE))))
            .andExpect(jsonPath("$.[*].currentInvestedAmount").value(hasItem(sameNumber(DEFAULT_CURRENT_INVESTED_AMOUNT))))
            .andExpect(jsonPath("$.[*].profitLoss").value(hasItem(sameNumber(DEFAULT_PROFIT_LOSS))))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].updatedOn").value(hasItem(DEFAULT_UPDATED_ON.toString())));
    }

    @Test
    @Transactional
    void getAssetDetailsAudit() throws Exception {
        // Initialize the database
        assetDetailsAuditRepository.saveAndFlush(assetDetailsAudit);

        // Get the assetDetailsAudit
        restAssetDetailsAuditMockMvc
            .perform(get(ENTITY_API_URL_ID, assetDetailsAudit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(assetDetailsAudit.getId().intValue()))
            .andExpect(jsonPath("$.units").value(sameNumber(DEFAULT_UNITS)))
            .andExpect(jsonPath("$.unitPrice").value(sameNumber(DEFAULT_UNIT_PRICE)))
            .andExpect(jsonPath("$.balance").value(sameNumber(DEFAULT_BALANCE)))
            .andExpect(jsonPath("$.currentInvestedAmount").value(sameNumber(DEFAULT_CURRENT_INVESTED_AMOUNT)))
            .andExpect(jsonPath("$.profitLoss").value(sameNumber(DEFAULT_PROFIT_LOSS)))
            .andExpect(jsonPath("$.createdOn").value(DEFAULT_CREATED_ON.toString()))
            .andExpect(jsonPath("$.updatedOn").value(DEFAULT_UPDATED_ON.toString()));
    }

    @Test
    @Transactional
    void getNonExistingAssetDetailsAudit() throws Exception {
        // Get the assetDetailsAudit
        restAssetDetailsAuditMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAssetDetailsAudit() throws Exception {
        // Initialize the database
        assetDetailsAuditRepository.saveAndFlush(assetDetailsAudit);

        int databaseSizeBeforeUpdate = assetDetailsAuditRepository.findAll().size();

        // Update the assetDetailsAudit
        AssetDetailsAudit updatedAssetDetailsAudit = assetDetailsAuditRepository.findById(assetDetailsAudit.getId()).get();
        // Disconnect from session so that the updates on updatedAssetDetailsAudit are not directly saved in db
        em.detach(updatedAssetDetailsAudit);
        updatedAssetDetailsAudit
            .units(UPDATED_UNITS)
            .unitPrice(UPDATED_UNIT_PRICE)
            .balance(UPDATED_BALANCE)
            .currentInvestedAmount(UPDATED_CURRENT_INVESTED_AMOUNT)
            .profitLoss(UPDATED_PROFIT_LOSS)
            .createdOn(UPDATED_CREATED_ON)
            .updatedOn(UPDATED_UPDATED_ON);
        AssetDetailsAuditDTO assetDetailsAuditDTO = assetDetailsAuditMapper.toDto(updatedAssetDetailsAudit);

        restAssetDetailsAuditMockMvc
            .perform(
                put(ENTITY_API_URL_ID, assetDetailsAuditDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(assetDetailsAuditDTO))
            )
            .andExpect(status().isOk());

        // Validate the AssetDetailsAudit in the database
        List<AssetDetailsAudit> assetDetailsAuditList = assetDetailsAuditRepository.findAll();
        assertThat(assetDetailsAuditList).hasSize(databaseSizeBeforeUpdate);
        AssetDetailsAudit testAssetDetailsAudit = assetDetailsAuditList.get(assetDetailsAuditList.size() - 1);
        assertThat(testAssetDetailsAudit.getUnits()).isEqualByComparingTo(UPDATED_UNITS);
        assertThat(testAssetDetailsAudit.getUnitPrice()).isEqualByComparingTo(UPDATED_UNIT_PRICE);
        assertThat(testAssetDetailsAudit.getBalance()).isEqualByComparingTo(UPDATED_BALANCE);
        assertThat(testAssetDetailsAudit.getCurrentInvestedAmount()).isEqualByComparingTo(UPDATED_CURRENT_INVESTED_AMOUNT);
        assertThat(testAssetDetailsAudit.getProfitLoss()).isEqualByComparingTo(UPDATED_PROFIT_LOSS);
        assertThat(testAssetDetailsAudit.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testAssetDetailsAudit.getUpdatedOn()).isEqualTo(UPDATED_UPDATED_ON);
    }

    @Test
    @Transactional
    void putNonExistingAssetDetailsAudit() throws Exception {
        int databaseSizeBeforeUpdate = assetDetailsAuditRepository.findAll().size();
        assetDetailsAudit.setId(count.incrementAndGet());

        // Create the AssetDetailsAudit
        AssetDetailsAuditDTO assetDetailsAuditDTO = assetDetailsAuditMapper.toDto(assetDetailsAudit);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAssetDetailsAuditMockMvc
            .perform(
                put(ENTITY_API_URL_ID, assetDetailsAuditDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(assetDetailsAuditDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssetDetailsAudit in the database
        List<AssetDetailsAudit> assetDetailsAuditList = assetDetailsAuditRepository.findAll();
        assertThat(assetDetailsAuditList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAssetDetailsAudit() throws Exception {
        int databaseSizeBeforeUpdate = assetDetailsAuditRepository.findAll().size();
        assetDetailsAudit.setId(count.incrementAndGet());

        // Create the AssetDetailsAudit
        AssetDetailsAuditDTO assetDetailsAuditDTO = assetDetailsAuditMapper.toDto(assetDetailsAudit);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssetDetailsAuditMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(assetDetailsAuditDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssetDetailsAudit in the database
        List<AssetDetailsAudit> assetDetailsAuditList = assetDetailsAuditRepository.findAll();
        assertThat(assetDetailsAuditList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAssetDetailsAudit() throws Exception {
        int databaseSizeBeforeUpdate = assetDetailsAuditRepository.findAll().size();
        assetDetailsAudit.setId(count.incrementAndGet());

        // Create the AssetDetailsAudit
        AssetDetailsAuditDTO assetDetailsAuditDTO = assetDetailsAuditMapper.toDto(assetDetailsAudit);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssetDetailsAuditMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(assetDetailsAuditDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AssetDetailsAudit in the database
        List<AssetDetailsAudit> assetDetailsAuditList = assetDetailsAuditRepository.findAll();
        assertThat(assetDetailsAuditList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAssetDetailsAuditWithPatch() throws Exception {
        // Initialize the database
        assetDetailsAuditRepository.saveAndFlush(assetDetailsAudit);

        int databaseSizeBeforeUpdate = assetDetailsAuditRepository.findAll().size();

        // Update the assetDetailsAudit using partial update
        AssetDetailsAudit partialUpdatedAssetDetailsAudit = new AssetDetailsAudit();
        partialUpdatedAssetDetailsAudit.setId(assetDetailsAudit.getId());

        partialUpdatedAssetDetailsAudit
            .units(UPDATED_UNITS)
            .balance(UPDATED_BALANCE)
            .currentInvestedAmount(UPDATED_CURRENT_INVESTED_AMOUNT);

        restAssetDetailsAuditMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAssetDetailsAudit.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAssetDetailsAudit))
            )
            .andExpect(status().isOk());

        // Validate the AssetDetailsAudit in the database
        List<AssetDetailsAudit> assetDetailsAuditList = assetDetailsAuditRepository.findAll();
        assertThat(assetDetailsAuditList).hasSize(databaseSizeBeforeUpdate);
        AssetDetailsAudit testAssetDetailsAudit = assetDetailsAuditList.get(assetDetailsAuditList.size() - 1);
        assertThat(testAssetDetailsAudit.getUnits()).isEqualByComparingTo(UPDATED_UNITS);
        assertThat(testAssetDetailsAudit.getUnitPrice()).isEqualByComparingTo(DEFAULT_UNIT_PRICE);
        assertThat(testAssetDetailsAudit.getBalance()).isEqualByComparingTo(UPDATED_BALANCE);
        assertThat(testAssetDetailsAudit.getCurrentInvestedAmount()).isEqualByComparingTo(UPDATED_CURRENT_INVESTED_AMOUNT);
        assertThat(testAssetDetailsAudit.getProfitLoss()).isEqualByComparingTo(DEFAULT_PROFIT_LOSS);
        assertThat(testAssetDetailsAudit.getCreatedOn()).isEqualTo(DEFAULT_CREATED_ON);
        assertThat(testAssetDetailsAudit.getUpdatedOn()).isEqualTo(DEFAULT_UPDATED_ON);
    }

    @Test
    @Transactional
    void fullUpdateAssetDetailsAuditWithPatch() throws Exception {
        // Initialize the database
        assetDetailsAuditRepository.saveAndFlush(assetDetailsAudit);

        int databaseSizeBeforeUpdate = assetDetailsAuditRepository.findAll().size();

        // Update the assetDetailsAudit using partial update
        AssetDetailsAudit partialUpdatedAssetDetailsAudit = new AssetDetailsAudit();
        partialUpdatedAssetDetailsAudit.setId(assetDetailsAudit.getId());

        partialUpdatedAssetDetailsAudit
            .units(UPDATED_UNITS)
            .unitPrice(UPDATED_UNIT_PRICE)
            .balance(UPDATED_BALANCE)
            .currentInvestedAmount(UPDATED_CURRENT_INVESTED_AMOUNT)
            .profitLoss(UPDATED_PROFIT_LOSS)
            .createdOn(UPDATED_CREATED_ON)
            .updatedOn(UPDATED_UPDATED_ON);

        restAssetDetailsAuditMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAssetDetailsAudit.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAssetDetailsAudit))
            )
            .andExpect(status().isOk());

        // Validate the AssetDetailsAudit in the database
        List<AssetDetailsAudit> assetDetailsAuditList = assetDetailsAuditRepository.findAll();
        assertThat(assetDetailsAuditList).hasSize(databaseSizeBeforeUpdate);
        AssetDetailsAudit testAssetDetailsAudit = assetDetailsAuditList.get(assetDetailsAuditList.size() - 1);
        assertThat(testAssetDetailsAudit.getUnits()).isEqualByComparingTo(UPDATED_UNITS);
        assertThat(testAssetDetailsAudit.getUnitPrice()).isEqualByComparingTo(UPDATED_UNIT_PRICE);
        assertThat(testAssetDetailsAudit.getBalance()).isEqualByComparingTo(UPDATED_BALANCE);
        assertThat(testAssetDetailsAudit.getCurrentInvestedAmount()).isEqualByComparingTo(UPDATED_CURRENT_INVESTED_AMOUNT);
        assertThat(testAssetDetailsAudit.getProfitLoss()).isEqualByComparingTo(UPDATED_PROFIT_LOSS);
        assertThat(testAssetDetailsAudit.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testAssetDetailsAudit.getUpdatedOn()).isEqualTo(UPDATED_UPDATED_ON);
    }

    @Test
    @Transactional
    void patchNonExistingAssetDetailsAudit() throws Exception {
        int databaseSizeBeforeUpdate = assetDetailsAuditRepository.findAll().size();
        assetDetailsAudit.setId(count.incrementAndGet());

        // Create the AssetDetailsAudit
        AssetDetailsAuditDTO assetDetailsAuditDTO = assetDetailsAuditMapper.toDto(assetDetailsAudit);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAssetDetailsAuditMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, assetDetailsAuditDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(assetDetailsAuditDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssetDetailsAudit in the database
        List<AssetDetailsAudit> assetDetailsAuditList = assetDetailsAuditRepository.findAll();
        assertThat(assetDetailsAuditList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAssetDetailsAudit() throws Exception {
        int databaseSizeBeforeUpdate = assetDetailsAuditRepository.findAll().size();
        assetDetailsAudit.setId(count.incrementAndGet());

        // Create the AssetDetailsAudit
        AssetDetailsAuditDTO assetDetailsAuditDTO = assetDetailsAuditMapper.toDto(assetDetailsAudit);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssetDetailsAuditMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(assetDetailsAuditDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssetDetailsAudit in the database
        List<AssetDetailsAudit> assetDetailsAuditList = assetDetailsAuditRepository.findAll();
        assertThat(assetDetailsAuditList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAssetDetailsAudit() throws Exception {
        int databaseSizeBeforeUpdate = assetDetailsAuditRepository.findAll().size();
        assetDetailsAudit.setId(count.incrementAndGet());

        // Create the AssetDetailsAudit
        AssetDetailsAuditDTO assetDetailsAuditDTO = assetDetailsAuditMapper.toDto(assetDetailsAudit);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssetDetailsAuditMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(assetDetailsAuditDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AssetDetailsAudit in the database
        List<AssetDetailsAudit> assetDetailsAuditList = assetDetailsAuditRepository.findAll();
        assertThat(assetDetailsAuditList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAssetDetailsAudit() throws Exception {
        // Initialize the database
        assetDetailsAuditRepository.saveAndFlush(assetDetailsAudit);

        int databaseSizeBeforeDelete = assetDetailsAuditRepository.findAll().size();

        // Delete the assetDetailsAudit
        restAssetDetailsAuditMockMvc
            .perform(delete(ENTITY_API_URL_ID, assetDetailsAudit.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AssetDetailsAudit> assetDetailsAuditList = assetDetailsAuditRepository.findAll();
        assertThat(assetDetailsAuditList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
