package com.finance.investment.micro.web.rest;

import static com.finance.investment.micro.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.finance.investment.micro.IntegrationTest;
import com.finance.investment.micro.domain.Promotions;
import com.finance.investment.micro.domain.enumeration.PROMOTIONTYPE;
import com.finance.investment.micro.repository.PromotionsRepository;
import com.finance.investment.micro.service.dto.PromotionsDTO;
import com.finance.investment.micro.service.mapper.PromotionsMapper;
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
 * Integration tests for the {@link PromotionsResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PromotionsResourceIT {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final PROMOTIONTYPE DEFAULT_TYPE = PROMOTIONTYPE.CASHBACK;
    private static final PROMOTIONTYPE UPDATED_TYPE = PROMOTIONTYPE.BONUS;

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);

    private static final Instant DEFAULT_CREATED_ON = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_ON = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_ON = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_ON = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/promotions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PromotionsRepository promotionsRepository;

    @Autowired
    private PromotionsMapper promotionsMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPromotionsMockMvc;

    private Promotions promotions;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Promotions createEntity(EntityManager em) {
        Promotions promotions = new Promotions()
            .description(DEFAULT_DESCRIPTION)
            .type(DEFAULT_TYPE)
            .amount(DEFAULT_AMOUNT)
            .createdOn(DEFAULT_CREATED_ON)
            .updatedOn(DEFAULT_UPDATED_ON);
        return promotions;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Promotions createUpdatedEntity(EntityManager em) {
        Promotions promotions = new Promotions()
            .description(UPDATED_DESCRIPTION)
            .type(UPDATED_TYPE)
            .amount(UPDATED_AMOUNT)
            .createdOn(UPDATED_CREATED_ON)
            .updatedOn(UPDATED_UPDATED_ON);
        return promotions;
    }

    @BeforeEach
    public void initTest() {
        promotions = createEntity(em);
    }

    @Test
    @Transactional
    void createPromotions() throws Exception {
        int databaseSizeBeforeCreate = promotionsRepository.findAll().size();
        // Create the Promotions
        PromotionsDTO promotionsDTO = promotionsMapper.toDto(promotions);
        restPromotionsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(promotionsDTO)))
            .andExpect(status().isCreated());

        // Validate the Promotions in the database
        List<Promotions> promotionsList = promotionsRepository.findAll();
        assertThat(promotionsList).hasSize(databaseSizeBeforeCreate + 1);
        Promotions testPromotions = promotionsList.get(promotionsList.size() - 1);
        assertThat(testPromotions.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testPromotions.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testPromotions.getAmount()).isEqualByComparingTo(DEFAULT_AMOUNT);
        assertThat(testPromotions.getCreatedOn()).isEqualTo(DEFAULT_CREATED_ON);
        assertThat(testPromotions.getUpdatedOn()).isEqualTo(DEFAULT_UPDATED_ON);
    }

    @Test
    @Transactional
    void createPromotionsWithExistingId() throws Exception {
        // Create the Promotions with an existing ID
        promotions.setId(1L);
        PromotionsDTO promotionsDTO = promotionsMapper.toDto(promotions);

        int databaseSizeBeforeCreate = promotionsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPromotionsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(promotionsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Promotions in the database
        List<Promotions> promotionsList = promotionsRepository.findAll();
        assertThat(promotionsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPromotions() throws Exception {
        // Initialize the database
        promotionsRepository.saveAndFlush(promotions);

        // Get all the promotionsList
        restPromotionsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(promotions.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].updatedOn").value(hasItem(DEFAULT_UPDATED_ON.toString())));
    }

    @Test
    @Transactional
    void getPromotions() throws Exception {
        // Initialize the database
        promotionsRepository.saveAndFlush(promotions);

        // Get the promotions
        restPromotionsMockMvc
            .perform(get(ENTITY_API_URL_ID, promotions.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(promotions.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.amount").value(sameNumber(DEFAULT_AMOUNT)))
            .andExpect(jsonPath("$.createdOn").value(DEFAULT_CREATED_ON.toString()))
            .andExpect(jsonPath("$.updatedOn").value(DEFAULT_UPDATED_ON.toString()));
    }

    @Test
    @Transactional
    void getNonExistingPromotions() throws Exception {
        // Get the promotions
        restPromotionsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPromotions() throws Exception {
        // Initialize the database
        promotionsRepository.saveAndFlush(promotions);

        int databaseSizeBeforeUpdate = promotionsRepository.findAll().size();

        // Update the promotions
        Promotions updatedPromotions = promotionsRepository.findById(promotions.getId()).get();
        // Disconnect from session so that the updates on updatedPromotions are not directly saved in db
        em.detach(updatedPromotions);
        updatedPromotions
            .description(UPDATED_DESCRIPTION)
            .type(UPDATED_TYPE)
            .amount(UPDATED_AMOUNT)
            .createdOn(UPDATED_CREATED_ON)
            .updatedOn(UPDATED_UPDATED_ON);
        PromotionsDTO promotionsDTO = promotionsMapper.toDto(updatedPromotions);

        restPromotionsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, promotionsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(promotionsDTO))
            )
            .andExpect(status().isOk());

        // Validate the Promotions in the database
        List<Promotions> promotionsList = promotionsRepository.findAll();
        assertThat(promotionsList).hasSize(databaseSizeBeforeUpdate);
        Promotions testPromotions = promotionsList.get(promotionsList.size() - 1);
        assertThat(testPromotions.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPromotions.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testPromotions.getAmount()).isEqualByComparingTo(UPDATED_AMOUNT);
        assertThat(testPromotions.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testPromotions.getUpdatedOn()).isEqualTo(UPDATED_UPDATED_ON);
    }

    @Test
    @Transactional
    void putNonExistingPromotions() throws Exception {
        int databaseSizeBeforeUpdate = promotionsRepository.findAll().size();
        promotions.setId(count.incrementAndGet());

        // Create the Promotions
        PromotionsDTO promotionsDTO = promotionsMapper.toDto(promotions);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPromotionsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, promotionsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(promotionsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Promotions in the database
        List<Promotions> promotionsList = promotionsRepository.findAll();
        assertThat(promotionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPromotions() throws Exception {
        int databaseSizeBeforeUpdate = promotionsRepository.findAll().size();
        promotions.setId(count.incrementAndGet());

        // Create the Promotions
        PromotionsDTO promotionsDTO = promotionsMapper.toDto(promotions);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPromotionsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(promotionsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Promotions in the database
        List<Promotions> promotionsList = promotionsRepository.findAll();
        assertThat(promotionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPromotions() throws Exception {
        int databaseSizeBeforeUpdate = promotionsRepository.findAll().size();
        promotions.setId(count.incrementAndGet());

        // Create the Promotions
        PromotionsDTO promotionsDTO = promotionsMapper.toDto(promotions);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPromotionsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(promotionsDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Promotions in the database
        List<Promotions> promotionsList = promotionsRepository.findAll();
        assertThat(promotionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePromotionsWithPatch() throws Exception {
        // Initialize the database
        promotionsRepository.saveAndFlush(promotions);

        int databaseSizeBeforeUpdate = promotionsRepository.findAll().size();

        // Update the promotions using partial update
        Promotions partialUpdatedPromotions = new Promotions();
        partialUpdatedPromotions.setId(promotions.getId());

        partialUpdatedPromotions.description(UPDATED_DESCRIPTION).type(UPDATED_TYPE).createdOn(UPDATED_CREATED_ON);

        restPromotionsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPromotions.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPromotions))
            )
            .andExpect(status().isOk());

        // Validate the Promotions in the database
        List<Promotions> promotionsList = promotionsRepository.findAll();
        assertThat(promotionsList).hasSize(databaseSizeBeforeUpdate);
        Promotions testPromotions = promotionsList.get(promotionsList.size() - 1);
        assertThat(testPromotions.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPromotions.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testPromotions.getAmount()).isEqualByComparingTo(DEFAULT_AMOUNT);
        assertThat(testPromotions.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testPromotions.getUpdatedOn()).isEqualTo(DEFAULT_UPDATED_ON);
    }

    @Test
    @Transactional
    void fullUpdatePromotionsWithPatch() throws Exception {
        // Initialize the database
        promotionsRepository.saveAndFlush(promotions);

        int databaseSizeBeforeUpdate = promotionsRepository.findAll().size();

        // Update the promotions using partial update
        Promotions partialUpdatedPromotions = new Promotions();
        partialUpdatedPromotions.setId(promotions.getId());

        partialUpdatedPromotions
            .description(UPDATED_DESCRIPTION)
            .type(UPDATED_TYPE)
            .amount(UPDATED_AMOUNT)
            .createdOn(UPDATED_CREATED_ON)
            .updatedOn(UPDATED_UPDATED_ON);

        restPromotionsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPromotions.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPromotions))
            )
            .andExpect(status().isOk());

        // Validate the Promotions in the database
        List<Promotions> promotionsList = promotionsRepository.findAll();
        assertThat(promotionsList).hasSize(databaseSizeBeforeUpdate);
        Promotions testPromotions = promotionsList.get(promotionsList.size() - 1);
        assertThat(testPromotions.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPromotions.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testPromotions.getAmount()).isEqualByComparingTo(UPDATED_AMOUNT);
        assertThat(testPromotions.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testPromotions.getUpdatedOn()).isEqualTo(UPDATED_UPDATED_ON);
    }

    @Test
    @Transactional
    void patchNonExistingPromotions() throws Exception {
        int databaseSizeBeforeUpdate = promotionsRepository.findAll().size();
        promotions.setId(count.incrementAndGet());

        // Create the Promotions
        PromotionsDTO promotionsDTO = promotionsMapper.toDto(promotions);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPromotionsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, promotionsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(promotionsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Promotions in the database
        List<Promotions> promotionsList = promotionsRepository.findAll();
        assertThat(promotionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPromotions() throws Exception {
        int databaseSizeBeforeUpdate = promotionsRepository.findAll().size();
        promotions.setId(count.incrementAndGet());

        // Create the Promotions
        PromotionsDTO promotionsDTO = promotionsMapper.toDto(promotions);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPromotionsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(promotionsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Promotions in the database
        List<Promotions> promotionsList = promotionsRepository.findAll();
        assertThat(promotionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPromotions() throws Exception {
        int databaseSizeBeforeUpdate = promotionsRepository.findAll().size();
        promotions.setId(count.incrementAndGet());

        // Create the Promotions
        PromotionsDTO promotionsDTO = promotionsMapper.toDto(promotions);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPromotionsMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(promotionsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Promotions in the database
        List<Promotions> promotionsList = promotionsRepository.findAll();
        assertThat(promotionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePromotions() throws Exception {
        // Initialize the database
        promotionsRepository.saveAndFlush(promotions);

        int databaseSizeBeforeDelete = promotionsRepository.findAll().size();

        // Delete the promotions
        restPromotionsMockMvc
            .perform(delete(ENTITY_API_URL_ID, promotions.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Promotions> promotionsList = promotionsRepository.findAll();
        assertThat(promotionsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
