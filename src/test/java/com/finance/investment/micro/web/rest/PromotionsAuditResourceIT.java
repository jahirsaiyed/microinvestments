package com.finance.investment.micro.web.rest;

import static com.finance.investment.micro.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.finance.investment.micro.IntegrationTest;
import com.finance.investment.micro.domain.PromotionsAudit;
import com.finance.investment.micro.domain.enumeration.PROMOTIONTYPE;
import com.finance.investment.micro.repository.PromotionsAuditRepository;
import com.finance.investment.micro.service.dto.PromotionsAuditDTO;
import com.finance.investment.micro.service.mapper.PromotionsAuditMapper;
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
 * Integration tests for the {@link PromotionsAuditResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PromotionsAuditResourceIT {

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

    private static final String ENTITY_API_URL = "/api/promotions-audits";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PromotionsAuditRepository promotionsAuditRepository;

    @Autowired
    private PromotionsAuditMapper promotionsAuditMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPromotionsAuditMockMvc;

    private PromotionsAudit promotionsAudit;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PromotionsAudit createEntity(EntityManager em) {
        PromotionsAudit promotionsAudit = new PromotionsAudit()
            .description(DEFAULT_DESCRIPTION)
            .type(DEFAULT_TYPE)
            .amount(DEFAULT_AMOUNT)
            .createdOn(DEFAULT_CREATED_ON)
            .updatedOn(DEFAULT_UPDATED_ON);
        return promotionsAudit;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PromotionsAudit createUpdatedEntity(EntityManager em) {
        PromotionsAudit promotionsAudit = new PromotionsAudit()
            .description(UPDATED_DESCRIPTION)
            .type(UPDATED_TYPE)
            .amount(UPDATED_AMOUNT)
            .createdOn(UPDATED_CREATED_ON)
            .updatedOn(UPDATED_UPDATED_ON);
        return promotionsAudit;
    }

    @BeforeEach
    public void initTest() {
        promotionsAudit = createEntity(em);
    }

    @Test
    @Transactional
    void createPromotionsAudit() throws Exception {
        int databaseSizeBeforeCreate = promotionsAuditRepository.findAll().size();
        // Create the PromotionsAudit
        PromotionsAuditDTO promotionsAuditDTO = promotionsAuditMapper.toDto(promotionsAudit);
        restPromotionsAuditMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(promotionsAuditDTO))
            )
            .andExpect(status().isCreated());

        // Validate the PromotionsAudit in the database
        List<PromotionsAudit> promotionsAuditList = promotionsAuditRepository.findAll();
        assertThat(promotionsAuditList).hasSize(databaseSizeBeforeCreate + 1);
        PromotionsAudit testPromotionsAudit = promotionsAuditList.get(promotionsAuditList.size() - 1);
        assertThat(testPromotionsAudit.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testPromotionsAudit.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testPromotionsAudit.getAmount()).isEqualByComparingTo(DEFAULT_AMOUNT);
        assertThat(testPromotionsAudit.getCreatedOn()).isEqualTo(DEFAULT_CREATED_ON);
        assertThat(testPromotionsAudit.getUpdatedOn()).isEqualTo(DEFAULT_UPDATED_ON);
    }

    @Test
    @Transactional
    void createPromotionsAuditWithExistingId() throws Exception {
        // Create the PromotionsAudit with an existing ID
        promotionsAudit.setId(1L);
        PromotionsAuditDTO promotionsAuditDTO = promotionsAuditMapper.toDto(promotionsAudit);

        int databaseSizeBeforeCreate = promotionsAuditRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPromotionsAuditMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(promotionsAuditDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PromotionsAudit in the database
        List<PromotionsAudit> promotionsAuditList = promotionsAuditRepository.findAll();
        assertThat(promotionsAuditList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPromotionsAudits() throws Exception {
        // Initialize the database
        promotionsAuditRepository.saveAndFlush(promotionsAudit);

        // Get all the promotionsAuditList
        restPromotionsAuditMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(promotionsAudit.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].updatedOn").value(hasItem(DEFAULT_UPDATED_ON.toString())));
    }

    @Test
    @Transactional
    void getPromotionsAudit() throws Exception {
        // Initialize the database
        promotionsAuditRepository.saveAndFlush(promotionsAudit);

        // Get the promotionsAudit
        restPromotionsAuditMockMvc
            .perform(get(ENTITY_API_URL_ID, promotionsAudit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(promotionsAudit.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.amount").value(sameNumber(DEFAULT_AMOUNT)))
            .andExpect(jsonPath("$.createdOn").value(DEFAULT_CREATED_ON.toString()))
            .andExpect(jsonPath("$.updatedOn").value(DEFAULT_UPDATED_ON.toString()));
    }

    @Test
    @Transactional
    void getNonExistingPromotionsAudit() throws Exception {
        // Get the promotionsAudit
        restPromotionsAuditMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPromotionsAudit() throws Exception {
        // Initialize the database
        promotionsAuditRepository.saveAndFlush(promotionsAudit);

        int databaseSizeBeforeUpdate = promotionsAuditRepository.findAll().size();

        // Update the promotionsAudit
        PromotionsAudit updatedPromotionsAudit = promotionsAuditRepository.findById(promotionsAudit.getId()).get();
        // Disconnect from session so that the updates on updatedPromotionsAudit are not directly saved in db
        em.detach(updatedPromotionsAudit);
        updatedPromotionsAudit
            .description(UPDATED_DESCRIPTION)
            .type(UPDATED_TYPE)
            .amount(UPDATED_AMOUNT)
            .createdOn(UPDATED_CREATED_ON)
            .updatedOn(UPDATED_UPDATED_ON);
        PromotionsAuditDTO promotionsAuditDTO = promotionsAuditMapper.toDto(updatedPromotionsAudit);

        restPromotionsAuditMockMvc
            .perform(
                put(ENTITY_API_URL_ID, promotionsAuditDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(promotionsAuditDTO))
            )
            .andExpect(status().isOk());

        // Validate the PromotionsAudit in the database
        List<PromotionsAudit> promotionsAuditList = promotionsAuditRepository.findAll();
        assertThat(promotionsAuditList).hasSize(databaseSizeBeforeUpdate);
        PromotionsAudit testPromotionsAudit = promotionsAuditList.get(promotionsAuditList.size() - 1);
        assertThat(testPromotionsAudit.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPromotionsAudit.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testPromotionsAudit.getAmount()).isEqualByComparingTo(UPDATED_AMOUNT);
        assertThat(testPromotionsAudit.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testPromotionsAudit.getUpdatedOn()).isEqualTo(UPDATED_UPDATED_ON);
    }

    @Test
    @Transactional
    void putNonExistingPromotionsAudit() throws Exception {
        int databaseSizeBeforeUpdate = promotionsAuditRepository.findAll().size();
        promotionsAudit.setId(count.incrementAndGet());

        // Create the PromotionsAudit
        PromotionsAuditDTO promotionsAuditDTO = promotionsAuditMapper.toDto(promotionsAudit);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPromotionsAuditMockMvc
            .perform(
                put(ENTITY_API_URL_ID, promotionsAuditDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(promotionsAuditDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PromotionsAudit in the database
        List<PromotionsAudit> promotionsAuditList = promotionsAuditRepository.findAll();
        assertThat(promotionsAuditList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPromotionsAudit() throws Exception {
        int databaseSizeBeforeUpdate = promotionsAuditRepository.findAll().size();
        promotionsAudit.setId(count.incrementAndGet());

        // Create the PromotionsAudit
        PromotionsAuditDTO promotionsAuditDTO = promotionsAuditMapper.toDto(promotionsAudit);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPromotionsAuditMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(promotionsAuditDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PromotionsAudit in the database
        List<PromotionsAudit> promotionsAuditList = promotionsAuditRepository.findAll();
        assertThat(promotionsAuditList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPromotionsAudit() throws Exception {
        int databaseSizeBeforeUpdate = promotionsAuditRepository.findAll().size();
        promotionsAudit.setId(count.incrementAndGet());

        // Create the PromotionsAudit
        PromotionsAuditDTO promotionsAuditDTO = promotionsAuditMapper.toDto(promotionsAudit);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPromotionsAuditMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(promotionsAuditDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PromotionsAudit in the database
        List<PromotionsAudit> promotionsAuditList = promotionsAuditRepository.findAll();
        assertThat(promotionsAuditList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePromotionsAuditWithPatch() throws Exception {
        // Initialize the database
        promotionsAuditRepository.saveAndFlush(promotionsAudit);

        int databaseSizeBeforeUpdate = promotionsAuditRepository.findAll().size();

        // Update the promotionsAudit using partial update
        PromotionsAudit partialUpdatedPromotionsAudit = new PromotionsAudit();
        partialUpdatedPromotionsAudit.setId(promotionsAudit.getId());

        partialUpdatedPromotionsAudit.description(UPDATED_DESCRIPTION).type(UPDATED_TYPE).amount(UPDATED_AMOUNT);

        restPromotionsAuditMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPromotionsAudit.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPromotionsAudit))
            )
            .andExpect(status().isOk());

        // Validate the PromotionsAudit in the database
        List<PromotionsAudit> promotionsAuditList = promotionsAuditRepository.findAll();
        assertThat(promotionsAuditList).hasSize(databaseSizeBeforeUpdate);
        PromotionsAudit testPromotionsAudit = promotionsAuditList.get(promotionsAuditList.size() - 1);
        assertThat(testPromotionsAudit.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPromotionsAudit.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testPromotionsAudit.getAmount()).isEqualByComparingTo(UPDATED_AMOUNT);
        assertThat(testPromotionsAudit.getCreatedOn()).isEqualTo(DEFAULT_CREATED_ON);
        assertThat(testPromotionsAudit.getUpdatedOn()).isEqualTo(DEFAULT_UPDATED_ON);
    }

    @Test
    @Transactional
    void fullUpdatePromotionsAuditWithPatch() throws Exception {
        // Initialize the database
        promotionsAuditRepository.saveAndFlush(promotionsAudit);

        int databaseSizeBeforeUpdate = promotionsAuditRepository.findAll().size();

        // Update the promotionsAudit using partial update
        PromotionsAudit partialUpdatedPromotionsAudit = new PromotionsAudit();
        partialUpdatedPromotionsAudit.setId(promotionsAudit.getId());

        partialUpdatedPromotionsAudit
            .description(UPDATED_DESCRIPTION)
            .type(UPDATED_TYPE)
            .amount(UPDATED_AMOUNT)
            .createdOn(UPDATED_CREATED_ON)
            .updatedOn(UPDATED_UPDATED_ON);

        restPromotionsAuditMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPromotionsAudit.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPromotionsAudit))
            )
            .andExpect(status().isOk());

        // Validate the PromotionsAudit in the database
        List<PromotionsAudit> promotionsAuditList = promotionsAuditRepository.findAll();
        assertThat(promotionsAuditList).hasSize(databaseSizeBeforeUpdate);
        PromotionsAudit testPromotionsAudit = promotionsAuditList.get(promotionsAuditList.size() - 1);
        assertThat(testPromotionsAudit.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPromotionsAudit.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testPromotionsAudit.getAmount()).isEqualByComparingTo(UPDATED_AMOUNT);
        assertThat(testPromotionsAudit.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testPromotionsAudit.getUpdatedOn()).isEqualTo(UPDATED_UPDATED_ON);
    }

    @Test
    @Transactional
    void patchNonExistingPromotionsAudit() throws Exception {
        int databaseSizeBeforeUpdate = promotionsAuditRepository.findAll().size();
        promotionsAudit.setId(count.incrementAndGet());

        // Create the PromotionsAudit
        PromotionsAuditDTO promotionsAuditDTO = promotionsAuditMapper.toDto(promotionsAudit);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPromotionsAuditMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, promotionsAuditDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(promotionsAuditDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PromotionsAudit in the database
        List<PromotionsAudit> promotionsAuditList = promotionsAuditRepository.findAll();
        assertThat(promotionsAuditList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPromotionsAudit() throws Exception {
        int databaseSizeBeforeUpdate = promotionsAuditRepository.findAll().size();
        promotionsAudit.setId(count.incrementAndGet());

        // Create the PromotionsAudit
        PromotionsAuditDTO promotionsAuditDTO = promotionsAuditMapper.toDto(promotionsAudit);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPromotionsAuditMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(promotionsAuditDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PromotionsAudit in the database
        List<PromotionsAudit> promotionsAuditList = promotionsAuditRepository.findAll();
        assertThat(promotionsAuditList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPromotionsAudit() throws Exception {
        int databaseSizeBeforeUpdate = promotionsAuditRepository.findAll().size();
        promotionsAudit.setId(count.incrementAndGet());

        // Create the PromotionsAudit
        PromotionsAuditDTO promotionsAuditDTO = promotionsAuditMapper.toDto(promotionsAudit);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPromotionsAuditMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(promotionsAuditDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PromotionsAudit in the database
        List<PromotionsAudit> promotionsAuditList = promotionsAuditRepository.findAll();
        assertThat(promotionsAuditList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePromotionsAudit() throws Exception {
        // Initialize the database
        promotionsAuditRepository.saveAndFlush(promotionsAudit);

        int databaseSizeBeforeDelete = promotionsAuditRepository.findAll().size();

        // Delete the promotionsAudit
        restPromotionsAuditMockMvc
            .perform(delete(ENTITY_API_URL_ID, promotionsAudit.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PromotionsAudit> promotionsAuditList = promotionsAuditRepository.findAll();
        assertThat(promotionsAuditList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
