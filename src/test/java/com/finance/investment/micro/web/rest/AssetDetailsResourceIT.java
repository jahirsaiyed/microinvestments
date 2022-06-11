package com.finance.investment.micro.web.rest;

import static com.finance.investment.micro.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.finance.investment.micro.IntegrationTest;
import com.finance.investment.micro.domain.AssetDetails;
import com.finance.investment.micro.repository.AssetDetailsRepository;
import com.finance.investment.micro.service.dto.AssetDetailsDTO;
import com.finance.investment.micro.service.mapper.AssetDetailsMapper;
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
 * Integration tests for the {@link AssetDetailsResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class AssetDetailsResourceIT {

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

    private static final String ENTITY_API_URL = "/api/asset-details";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AssetDetailsRepository assetDetailsRepository;

    @Autowired
    private AssetDetailsMapper assetDetailsMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAssetDetailsMockMvc;

    private AssetDetails assetDetails;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AssetDetails createEntity(EntityManager em) {
        AssetDetails assetDetails = new AssetDetails()
            .units(DEFAULT_UNITS)
            .unitPrice(DEFAULT_UNIT_PRICE)
            .balance(DEFAULT_BALANCE)
            .currentInvestedAmount(DEFAULT_CURRENT_INVESTED_AMOUNT)
            .profitLoss(DEFAULT_PROFIT_LOSS)
            .createdOn(DEFAULT_CREATED_ON)
            .updatedOn(DEFAULT_UPDATED_ON);
        return assetDetails;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AssetDetails createUpdatedEntity(EntityManager em) {
        AssetDetails assetDetails = new AssetDetails()
            .units(UPDATED_UNITS)
            .unitPrice(UPDATED_UNIT_PRICE)
            .balance(UPDATED_BALANCE)
            .currentInvestedAmount(UPDATED_CURRENT_INVESTED_AMOUNT)
            .profitLoss(UPDATED_PROFIT_LOSS)
            .createdOn(UPDATED_CREATED_ON)
            .updatedOn(UPDATED_UPDATED_ON);
        return assetDetails;
    }

    @BeforeEach
    public void initTest() {
        assetDetails = createEntity(em);
    }

    @Test
    @Transactional
    void createAssetDetails() throws Exception {
        int databaseSizeBeforeCreate = assetDetailsRepository.findAll().size();
        // Create the AssetDetails
        AssetDetailsDTO assetDetailsDTO = assetDetailsMapper.toDto(assetDetails);
        restAssetDetailsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(assetDetailsDTO))
            )
            .andExpect(status().isCreated());

        // Validate the AssetDetails in the database
        List<AssetDetails> assetDetailsList = assetDetailsRepository.findAll();
        assertThat(assetDetailsList).hasSize(databaseSizeBeforeCreate + 1);
        AssetDetails testAssetDetails = assetDetailsList.get(assetDetailsList.size() - 1);
        assertThat(testAssetDetails.getUnits()).isEqualByComparingTo(DEFAULT_UNITS);
        assertThat(testAssetDetails.getUnitPrice()).isEqualByComparingTo(DEFAULT_UNIT_PRICE);
        assertThat(testAssetDetails.getBalance()).isEqualByComparingTo(DEFAULT_BALANCE);
        assertThat(testAssetDetails.getCurrentInvestedAmount()).isEqualByComparingTo(DEFAULT_CURRENT_INVESTED_AMOUNT);
        assertThat(testAssetDetails.getProfitLoss()).isEqualByComparingTo(DEFAULT_PROFIT_LOSS);
        assertThat(testAssetDetails.getCreatedOn()).isEqualTo(DEFAULT_CREATED_ON);
        assertThat(testAssetDetails.getUpdatedOn()).isEqualTo(DEFAULT_UPDATED_ON);
    }

    @Test
    @Transactional
    void createAssetDetailsWithExistingId() throws Exception {
        // Create the AssetDetails with an existing ID
        assetDetails.setId(1L);
        AssetDetailsDTO assetDetailsDTO = assetDetailsMapper.toDto(assetDetails);

        int databaseSizeBeforeCreate = assetDetailsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAssetDetailsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(assetDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssetDetails in the database
        List<AssetDetails> assetDetailsList = assetDetailsRepository.findAll();
        assertThat(assetDetailsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAssetDetails() throws Exception {
        // Initialize the database
        assetDetailsRepository.saveAndFlush(assetDetails);

        // Get all the assetDetailsList
        restAssetDetailsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(assetDetails.getId().intValue())))
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
    void getAssetDetails() throws Exception {
        // Initialize the database
        assetDetailsRepository.saveAndFlush(assetDetails);

        // Get the assetDetails
        restAssetDetailsMockMvc
            .perform(get(ENTITY_API_URL_ID, assetDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(assetDetails.getId().intValue()))
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
    void getNonExistingAssetDetails() throws Exception {
        // Get the assetDetails
        restAssetDetailsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAssetDetails() throws Exception {
        // Initialize the database
        assetDetailsRepository.saveAndFlush(assetDetails);

        int databaseSizeBeforeUpdate = assetDetailsRepository.findAll().size();

        // Update the assetDetails
        AssetDetails updatedAssetDetails = assetDetailsRepository.findById(assetDetails.getId()).get();
        // Disconnect from session so that the updates on updatedAssetDetails are not directly saved in db
        em.detach(updatedAssetDetails);
        updatedAssetDetails
            .units(UPDATED_UNITS)
            .unitPrice(UPDATED_UNIT_PRICE)
            .balance(UPDATED_BALANCE)
            .currentInvestedAmount(UPDATED_CURRENT_INVESTED_AMOUNT)
            .profitLoss(UPDATED_PROFIT_LOSS)
            .createdOn(UPDATED_CREATED_ON)
            .updatedOn(UPDATED_UPDATED_ON);
        AssetDetailsDTO assetDetailsDTO = assetDetailsMapper.toDto(updatedAssetDetails);

        restAssetDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, assetDetailsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(assetDetailsDTO))
            )
            .andExpect(status().isOk());

        // Validate the AssetDetails in the database
        List<AssetDetails> assetDetailsList = assetDetailsRepository.findAll();
        assertThat(assetDetailsList).hasSize(databaseSizeBeforeUpdate);
        AssetDetails testAssetDetails = assetDetailsList.get(assetDetailsList.size() - 1);
        assertThat(testAssetDetails.getUnits()).isEqualByComparingTo(UPDATED_UNITS);
        assertThat(testAssetDetails.getUnitPrice()).isEqualByComparingTo(UPDATED_UNIT_PRICE);
        assertThat(testAssetDetails.getBalance()).isEqualByComparingTo(UPDATED_BALANCE);
        assertThat(testAssetDetails.getCurrentInvestedAmount()).isEqualByComparingTo(UPDATED_CURRENT_INVESTED_AMOUNT);
        assertThat(testAssetDetails.getProfitLoss()).isEqualByComparingTo(UPDATED_PROFIT_LOSS);
        assertThat(testAssetDetails.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testAssetDetails.getUpdatedOn()).isEqualTo(UPDATED_UPDATED_ON);
    }

    @Test
    @Transactional
    void putNonExistingAssetDetails() throws Exception {
        int databaseSizeBeforeUpdate = assetDetailsRepository.findAll().size();
        assetDetails.setId(count.incrementAndGet());

        // Create the AssetDetails
        AssetDetailsDTO assetDetailsDTO = assetDetailsMapper.toDto(assetDetails);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAssetDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, assetDetailsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(assetDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssetDetails in the database
        List<AssetDetails> assetDetailsList = assetDetailsRepository.findAll();
        assertThat(assetDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAssetDetails() throws Exception {
        int databaseSizeBeforeUpdate = assetDetailsRepository.findAll().size();
        assetDetails.setId(count.incrementAndGet());

        // Create the AssetDetails
        AssetDetailsDTO assetDetailsDTO = assetDetailsMapper.toDto(assetDetails);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssetDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(assetDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssetDetails in the database
        List<AssetDetails> assetDetailsList = assetDetailsRepository.findAll();
        assertThat(assetDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAssetDetails() throws Exception {
        int databaseSizeBeforeUpdate = assetDetailsRepository.findAll().size();
        assetDetails.setId(count.incrementAndGet());

        // Create the AssetDetails
        AssetDetailsDTO assetDetailsDTO = assetDetailsMapper.toDto(assetDetails);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssetDetailsMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(assetDetailsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AssetDetails in the database
        List<AssetDetails> assetDetailsList = assetDetailsRepository.findAll();
        assertThat(assetDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAssetDetailsWithPatch() throws Exception {
        // Initialize the database
        assetDetailsRepository.saveAndFlush(assetDetails);

        int databaseSizeBeforeUpdate = assetDetailsRepository.findAll().size();

        // Update the assetDetails using partial update
        AssetDetails partialUpdatedAssetDetails = new AssetDetails();
        partialUpdatedAssetDetails.setId(assetDetails.getId());

        partialUpdatedAssetDetails
            .unitPrice(UPDATED_UNIT_PRICE)
            .balance(UPDATED_BALANCE)
            .currentInvestedAmount(UPDATED_CURRENT_INVESTED_AMOUNT)
            .profitLoss(UPDATED_PROFIT_LOSS)
            .createdOn(UPDATED_CREATED_ON);

        restAssetDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAssetDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAssetDetails))
            )
            .andExpect(status().isOk());

        // Validate the AssetDetails in the database
        List<AssetDetails> assetDetailsList = assetDetailsRepository.findAll();
        assertThat(assetDetailsList).hasSize(databaseSizeBeforeUpdate);
        AssetDetails testAssetDetails = assetDetailsList.get(assetDetailsList.size() - 1);
        assertThat(testAssetDetails.getUnits()).isEqualByComparingTo(DEFAULT_UNITS);
        assertThat(testAssetDetails.getUnitPrice()).isEqualByComparingTo(UPDATED_UNIT_PRICE);
        assertThat(testAssetDetails.getBalance()).isEqualByComparingTo(UPDATED_BALANCE);
        assertThat(testAssetDetails.getCurrentInvestedAmount()).isEqualByComparingTo(UPDATED_CURRENT_INVESTED_AMOUNT);
        assertThat(testAssetDetails.getProfitLoss()).isEqualByComparingTo(UPDATED_PROFIT_LOSS);
        assertThat(testAssetDetails.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testAssetDetails.getUpdatedOn()).isEqualTo(DEFAULT_UPDATED_ON);
    }

    @Test
    @Transactional
    void fullUpdateAssetDetailsWithPatch() throws Exception {
        // Initialize the database
        assetDetailsRepository.saveAndFlush(assetDetails);

        int databaseSizeBeforeUpdate = assetDetailsRepository.findAll().size();

        // Update the assetDetails using partial update
        AssetDetails partialUpdatedAssetDetails = new AssetDetails();
        partialUpdatedAssetDetails.setId(assetDetails.getId());

        partialUpdatedAssetDetails
            .units(UPDATED_UNITS)
            .unitPrice(UPDATED_UNIT_PRICE)
            .balance(UPDATED_BALANCE)
            .currentInvestedAmount(UPDATED_CURRENT_INVESTED_AMOUNT)
            .profitLoss(UPDATED_PROFIT_LOSS)
            .createdOn(UPDATED_CREATED_ON)
            .updatedOn(UPDATED_UPDATED_ON);

        restAssetDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAssetDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAssetDetails))
            )
            .andExpect(status().isOk());

        // Validate the AssetDetails in the database
        List<AssetDetails> assetDetailsList = assetDetailsRepository.findAll();
        assertThat(assetDetailsList).hasSize(databaseSizeBeforeUpdate);
        AssetDetails testAssetDetails = assetDetailsList.get(assetDetailsList.size() - 1);
        assertThat(testAssetDetails.getUnits()).isEqualByComparingTo(UPDATED_UNITS);
        assertThat(testAssetDetails.getUnitPrice()).isEqualByComparingTo(UPDATED_UNIT_PRICE);
        assertThat(testAssetDetails.getBalance()).isEqualByComparingTo(UPDATED_BALANCE);
        assertThat(testAssetDetails.getCurrentInvestedAmount()).isEqualByComparingTo(UPDATED_CURRENT_INVESTED_AMOUNT);
        assertThat(testAssetDetails.getProfitLoss()).isEqualByComparingTo(UPDATED_PROFIT_LOSS);
        assertThat(testAssetDetails.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testAssetDetails.getUpdatedOn()).isEqualTo(UPDATED_UPDATED_ON);
    }

    @Test
    @Transactional
    void patchNonExistingAssetDetails() throws Exception {
        int databaseSizeBeforeUpdate = assetDetailsRepository.findAll().size();
        assetDetails.setId(count.incrementAndGet());

        // Create the AssetDetails
        AssetDetailsDTO assetDetailsDTO = assetDetailsMapper.toDto(assetDetails);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAssetDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, assetDetailsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(assetDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssetDetails in the database
        List<AssetDetails> assetDetailsList = assetDetailsRepository.findAll();
        assertThat(assetDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAssetDetails() throws Exception {
        int databaseSizeBeforeUpdate = assetDetailsRepository.findAll().size();
        assetDetails.setId(count.incrementAndGet());

        // Create the AssetDetails
        AssetDetailsDTO assetDetailsDTO = assetDetailsMapper.toDto(assetDetails);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssetDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(assetDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssetDetails in the database
        List<AssetDetails> assetDetailsList = assetDetailsRepository.findAll();
        assertThat(assetDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAssetDetails() throws Exception {
        int databaseSizeBeforeUpdate = assetDetailsRepository.findAll().size();
        assetDetails.setId(count.incrementAndGet());

        // Create the AssetDetails
        AssetDetailsDTO assetDetailsDTO = assetDetailsMapper.toDto(assetDetails);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssetDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(assetDetailsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AssetDetails in the database
        List<AssetDetails> assetDetailsList = assetDetailsRepository.findAll();
        assertThat(assetDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAssetDetails() throws Exception {
        // Initialize the database
        assetDetailsRepository.saveAndFlush(assetDetails);

        int databaseSizeBeforeDelete = assetDetailsRepository.findAll().size();

        // Delete the assetDetails
        restAssetDetailsMockMvc
            .perform(delete(ENTITY_API_URL_ID, assetDetails.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AssetDetails> assetDetailsList = assetDetailsRepository.findAll();
        assertThat(assetDetailsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
