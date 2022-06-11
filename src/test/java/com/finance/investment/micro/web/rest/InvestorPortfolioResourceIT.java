package com.finance.investment.micro.web.rest;

import static com.finance.investment.micro.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.finance.investment.micro.IntegrationTest;
import com.finance.investment.micro.domain.InvestorPortfolio;
import com.finance.investment.micro.repository.InvestorPortfolioRepository;
import com.finance.investment.micro.service.dto.InvestorPortfolioDTO;
import com.finance.investment.micro.service.mapper.InvestorPortfolioMapper;
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
 * Integration tests for the {@link InvestorPortfolioResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class InvestorPortfolioResourceIT {

    private static final BigDecimal DEFAULT_UNITS = new BigDecimal(1);
    private static final BigDecimal UPDATED_UNITS = new BigDecimal(2);

    private static final BigDecimal DEFAULT_CURRENT_UNIT_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_CURRENT_UNIT_PRICE = new BigDecimal(2);

    private static final BigDecimal DEFAULT_BALANCE = new BigDecimal(1);
    private static final BigDecimal UPDATED_BALANCE = new BigDecimal(2);

    private static final BigDecimal DEFAULT_CURRENT_INVESTED_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_CURRENT_INVESTED_AMOUNT = new BigDecimal(2);

    private static final Instant DEFAULT_CREATED_ON = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_ON = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_ON = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_ON = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/investor-portfolios";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private InvestorPortfolioRepository investorPortfolioRepository;

    @Autowired
    private InvestorPortfolioMapper investorPortfolioMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInvestorPortfolioMockMvc;

    private InvestorPortfolio investorPortfolio;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InvestorPortfolio createEntity(EntityManager em) {
        InvestorPortfolio investorPortfolio = new InvestorPortfolio()
            .units(DEFAULT_UNITS)
            .currentUnitPrice(DEFAULT_CURRENT_UNIT_PRICE)
            .balance(DEFAULT_BALANCE)
            .currentInvestedAmount(DEFAULT_CURRENT_INVESTED_AMOUNT)
            .createdOn(DEFAULT_CREATED_ON)
            .updatedOn(DEFAULT_UPDATED_ON);
        return investorPortfolio;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InvestorPortfolio createUpdatedEntity(EntityManager em) {
        InvestorPortfolio investorPortfolio = new InvestorPortfolio()
            .units(UPDATED_UNITS)
            .currentUnitPrice(UPDATED_CURRENT_UNIT_PRICE)
            .balance(UPDATED_BALANCE)
            .currentInvestedAmount(UPDATED_CURRENT_INVESTED_AMOUNT)
            .createdOn(UPDATED_CREATED_ON)
            .updatedOn(UPDATED_UPDATED_ON);
        return investorPortfolio;
    }

    @BeforeEach
    public void initTest() {
        investorPortfolio = createEntity(em);
    }

    @Test
    @Transactional
    void createInvestorPortfolio() throws Exception {
        int databaseSizeBeforeCreate = investorPortfolioRepository.findAll().size();
        // Create the InvestorPortfolio
        InvestorPortfolioDTO investorPortfolioDTO = investorPortfolioMapper.toDto(investorPortfolio);
        restInvestorPortfolioMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(investorPortfolioDTO))
            )
            .andExpect(status().isCreated());

        // Validate the InvestorPortfolio in the database
        List<InvestorPortfolio> investorPortfolioList = investorPortfolioRepository.findAll();
        assertThat(investorPortfolioList).hasSize(databaseSizeBeforeCreate + 1);
        InvestorPortfolio testInvestorPortfolio = investorPortfolioList.get(investorPortfolioList.size() - 1);
        assertThat(testInvestorPortfolio.getUnits()).isEqualByComparingTo(DEFAULT_UNITS);
        assertThat(testInvestorPortfolio.getCurrentUnitPrice()).isEqualByComparingTo(DEFAULT_CURRENT_UNIT_PRICE);
        assertThat(testInvestorPortfolio.getBalance()).isEqualByComparingTo(DEFAULT_BALANCE);
        assertThat(testInvestorPortfolio.getCurrentInvestedAmount()).isEqualByComparingTo(DEFAULT_CURRENT_INVESTED_AMOUNT);
        assertThat(testInvestorPortfolio.getCreatedOn()).isEqualTo(DEFAULT_CREATED_ON);
        assertThat(testInvestorPortfolio.getUpdatedOn()).isEqualTo(DEFAULT_UPDATED_ON);
    }

    @Test
    @Transactional
    void createInvestorPortfolioWithExistingId() throws Exception {
        // Create the InvestorPortfolio with an existing ID
        investorPortfolio.setId(1L);
        InvestorPortfolioDTO investorPortfolioDTO = investorPortfolioMapper.toDto(investorPortfolio);

        int databaseSizeBeforeCreate = investorPortfolioRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInvestorPortfolioMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(investorPortfolioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvestorPortfolio in the database
        List<InvestorPortfolio> investorPortfolioList = investorPortfolioRepository.findAll();
        assertThat(investorPortfolioList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllInvestorPortfolios() throws Exception {
        // Initialize the database
        investorPortfolioRepository.saveAndFlush(investorPortfolio);

        // Get all the investorPortfolioList
        restInvestorPortfolioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(investorPortfolio.getId().intValue())))
            .andExpect(jsonPath("$.[*].units").value(hasItem(sameNumber(DEFAULT_UNITS))))
            .andExpect(jsonPath("$.[*].currentUnitPrice").value(hasItem(sameNumber(DEFAULT_CURRENT_UNIT_PRICE))))
            .andExpect(jsonPath("$.[*].balance").value(hasItem(sameNumber(DEFAULT_BALANCE))))
            .andExpect(jsonPath("$.[*].currentInvestedAmount").value(hasItem(sameNumber(DEFAULT_CURRENT_INVESTED_AMOUNT))))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].updatedOn").value(hasItem(DEFAULT_UPDATED_ON.toString())));
    }

    @Test
    @Transactional
    void getInvestorPortfolio() throws Exception {
        // Initialize the database
        investorPortfolioRepository.saveAndFlush(investorPortfolio);

        // Get the investorPortfolio
        restInvestorPortfolioMockMvc
            .perform(get(ENTITY_API_URL_ID, investorPortfolio.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(investorPortfolio.getId().intValue()))
            .andExpect(jsonPath("$.units").value(sameNumber(DEFAULT_UNITS)))
            .andExpect(jsonPath("$.currentUnitPrice").value(sameNumber(DEFAULT_CURRENT_UNIT_PRICE)))
            .andExpect(jsonPath("$.balance").value(sameNumber(DEFAULT_BALANCE)))
            .andExpect(jsonPath("$.currentInvestedAmount").value(sameNumber(DEFAULT_CURRENT_INVESTED_AMOUNT)))
            .andExpect(jsonPath("$.createdOn").value(DEFAULT_CREATED_ON.toString()))
            .andExpect(jsonPath("$.updatedOn").value(DEFAULT_UPDATED_ON.toString()));
    }

    @Test
    @Transactional
    void getNonExistingInvestorPortfolio() throws Exception {
        // Get the investorPortfolio
        restInvestorPortfolioMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewInvestorPortfolio() throws Exception {
        // Initialize the database
        investorPortfolioRepository.saveAndFlush(investorPortfolio);

        int databaseSizeBeforeUpdate = investorPortfolioRepository.findAll().size();

        // Update the investorPortfolio
        InvestorPortfolio updatedInvestorPortfolio = investorPortfolioRepository.findById(investorPortfolio.getId()).get();
        // Disconnect from session so that the updates on updatedInvestorPortfolio are not directly saved in db
        em.detach(updatedInvestorPortfolio);
        updatedInvestorPortfolio
            .units(UPDATED_UNITS)
            .currentUnitPrice(UPDATED_CURRENT_UNIT_PRICE)
            .balance(UPDATED_BALANCE)
            .currentInvestedAmount(UPDATED_CURRENT_INVESTED_AMOUNT)
            .createdOn(UPDATED_CREATED_ON)
            .updatedOn(UPDATED_UPDATED_ON);
        InvestorPortfolioDTO investorPortfolioDTO = investorPortfolioMapper.toDto(updatedInvestorPortfolio);

        restInvestorPortfolioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, investorPortfolioDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(investorPortfolioDTO))
            )
            .andExpect(status().isOk());

        // Validate the InvestorPortfolio in the database
        List<InvestorPortfolio> investorPortfolioList = investorPortfolioRepository.findAll();
        assertThat(investorPortfolioList).hasSize(databaseSizeBeforeUpdate);
        InvestorPortfolio testInvestorPortfolio = investorPortfolioList.get(investorPortfolioList.size() - 1);
        assertThat(testInvestorPortfolio.getUnits()).isEqualByComparingTo(UPDATED_UNITS);
        assertThat(testInvestorPortfolio.getCurrentUnitPrice()).isEqualByComparingTo(UPDATED_CURRENT_UNIT_PRICE);
        assertThat(testInvestorPortfolio.getBalance()).isEqualByComparingTo(UPDATED_BALANCE);
        assertThat(testInvestorPortfolio.getCurrentInvestedAmount()).isEqualByComparingTo(UPDATED_CURRENT_INVESTED_AMOUNT);
        assertThat(testInvestorPortfolio.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testInvestorPortfolio.getUpdatedOn()).isEqualTo(UPDATED_UPDATED_ON);
    }

    @Test
    @Transactional
    void putNonExistingInvestorPortfolio() throws Exception {
        int databaseSizeBeforeUpdate = investorPortfolioRepository.findAll().size();
        investorPortfolio.setId(count.incrementAndGet());

        // Create the InvestorPortfolio
        InvestorPortfolioDTO investorPortfolioDTO = investorPortfolioMapper.toDto(investorPortfolio);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInvestorPortfolioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, investorPortfolioDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(investorPortfolioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvestorPortfolio in the database
        List<InvestorPortfolio> investorPortfolioList = investorPortfolioRepository.findAll();
        assertThat(investorPortfolioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInvestorPortfolio() throws Exception {
        int databaseSizeBeforeUpdate = investorPortfolioRepository.findAll().size();
        investorPortfolio.setId(count.incrementAndGet());

        // Create the InvestorPortfolio
        InvestorPortfolioDTO investorPortfolioDTO = investorPortfolioMapper.toDto(investorPortfolio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvestorPortfolioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(investorPortfolioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvestorPortfolio in the database
        List<InvestorPortfolio> investorPortfolioList = investorPortfolioRepository.findAll();
        assertThat(investorPortfolioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInvestorPortfolio() throws Exception {
        int databaseSizeBeforeUpdate = investorPortfolioRepository.findAll().size();
        investorPortfolio.setId(count.incrementAndGet());

        // Create the InvestorPortfolio
        InvestorPortfolioDTO investorPortfolioDTO = investorPortfolioMapper.toDto(investorPortfolio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvestorPortfolioMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(investorPortfolioDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the InvestorPortfolio in the database
        List<InvestorPortfolio> investorPortfolioList = investorPortfolioRepository.findAll();
        assertThat(investorPortfolioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInvestorPortfolioWithPatch() throws Exception {
        // Initialize the database
        investorPortfolioRepository.saveAndFlush(investorPortfolio);

        int databaseSizeBeforeUpdate = investorPortfolioRepository.findAll().size();

        // Update the investorPortfolio using partial update
        InvestorPortfolio partialUpdatedInvestorPortfolio = new InvestorPortfolio();
        partialUpdatedInvestorPortfolio.setId(investorPortfolio.getId());

        partialUpdatedInvestorPortfolio
            .balance(UPDATED_BALANCE)
            .currentInvestedAmount(UPDATED_CURRENT_INVESTED_AMOUNT)
            .createdOn(UPDATED_CREATED_ON)
            .updatedOn(UPDATED_UPDATED_ON);

        restInvestorPortfolioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInvestorPortfolio.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInvestorPortfolio))
            )
            .andExpect(status().isOk());

        // Validate the InvestorPortfolio in the database
        List<InvestorPortfolio> investorPortfolioList = investorPortfolioRepository.findAll();
        assertThat(investorPortfolioList).hasSize(databaseSizeBeforeUpdate);
        InvestorPortfolio testInvestorPortfolio = investorPortfolioList.get(investorPortfolioList.size() - 1);
        assertThat(testInvestorPortfolio.getUnits()).isEqualByComparingTo(DEFAULT_UNITS);
        assertThat(testInvestorPortfolio.getCurrentUnitPrice()).isEqualByComparingTo(DEFAULT_CURRENT_UNIT_PRICE);
        assertThat(testInvestorPortfolio.getBalance()).isEqualByComparingTo(UPDATED_BALANCE);
        assertThat(testInvestorPortfolio.getCurrentInvestedAmount()).isEqualByComparingTo(UPDATED_CURRENT_INVESTED_AMOUNT);
        assertThat(testInvestorPortfolio.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testInvestorPortfolio.getUpdatedOn()).isEqualTo(UPDATED_UPDATED_ON);
    }

    @Test
    @Transactional
    void fullUpdateInvestorPortfolioWithPatch() throws Exception {
        // Initialize the database
        investorPortfolioRepository.saveAndFlush(investorPortfolio);

        int databaseSizeBeforeUpdate = investorPortfolioRepository.findAll().size();

        // Update the investorPortfolio using partial update
        InvestorPortfolio partialUpdatedInvestorPortfolio = new InvestorPortfolio();
        partialUpdatedInvestorPortfolio.setId(investorPortfolio.getId());

        partialUpdatedInvestorPortfolio
            .units(UPDATED_UNITS)
            .currentUnitPrice(UPDATED_CURRENT_UNIT_PRICE)
            .balance(UPDATED_BALANCE)
            .currentInvestedAmount(UPDATED_CURRENT_INVESTED_AMOUNT)
            .createdOn(UPDATED_CREATED_ON)
            .updatedOn(UPDATED_UPDATED_ON);

        restInvestorPortfolioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInvestorPortfolio.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInvestorPortfolio))
            )
            .andExpect(status().isOk());

        // Validate the InvestorPortfolio in the database
        List<InvestorPortfolio> investorPortfolioList = investorPortfolioRepository.findAll();
        assertThat(investorPortfolioList).hasSize(databaseSizeBeforeUpdate);
        InvestorPortfolio testInvestorPortfolio = investorPortfolioList.get(investorPortfolioList.size() - 1);
        assertThat(testInvestorPortfolio.getUnits()).isEqualByComparingTo(UPDATED_UNITS);
        assertThat(testInvestorPortfolio.getCurrentUnitPrice()).isEqualByComparingTo(UPDATED_CURRENT_UNIT_PRICE);
        assertThat(testInvestorPortfolio.getBalance()).isEqualByComparingTo(UPDATED_BALANCE);
        assertThat(testInvestorPortfolio.getCurrentInvestedAmount()).isEqualByComparingTo(UPDATED_CURRENT_INVESTED_AMOUNT);
        assertThat(testInvestorPortfolio.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testInvestorPortfolio.getUpdatedOn()).isEqualTo(UPDATED_UPDATED_ON);
    }

    @Test
    @Transactional
    void patchNonExistingInvestorPortfolio() throws Exception {
        int databaseSizeBeforeUpdate = investorPortfolioRepository.findAll().size();
        investorPortfolio.setId(count.incrementAndGet());

        // Create the InvestorPortfolio
        InvestorPortfolioDTO investorPortfolioDTO = investorPortfolioMapper.toDto(investorPortfolio);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInvestorPortfolioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, investorPortfolioDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(investorPortfolioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvestorPortfolio in the database
        List<InvestorPortfolio> investorPortfolioList = investorPortfolioRepository.findAll();
        assertThat(investorPortfolioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInvestorPortfolio() throws Exception {
        int databaseSizeBeforeUpdate = investorPortfolioRepository.findAll().size();
        investorPortfolio.setId(count.incrementAndGet());

        // Create the InvestorPortfolio
        InvestorPortfolioDTO investorPortfolioDTO = investorPortfolioMapper.toDto(investorPortfolio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvestorPortfolioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(investorPortfolioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvestorPortfolio in the database
        List<InvestorPortfolio> investorPortfolioList = investorPortfolioRepository.findAll();
        assertThat(investorPortfolioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInvestorPortfolio() throws Exception {
        int databaseSizeBeforeUpdate = investorPortfolioRepository.findAll().size();
        investorPortfolio.setId(count.incrementAndGet());

        // Create the InvestorPortfolio
        InvestorPortfolioDTO investorPortfolioDTO = investorPortfolioMapper.toDto(investorPortfolio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvestorPortfolioMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(investorPortfolioDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the InvestorPortfolio in the database
        List<InvestorPortfolio> investorPortfolioList = investorPortfolioRepository.findAll();
        assertThat(investorPortfolioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInvestorPortfolio() throws Exception {
        // Initialize the database
        investorPortfolioRepository.saveAndFlush(investorPortfolio);

        int databaseSizeBeforeDelete = investorPortfolioRepository.findAll().size();

        // Delete the investorPortfolio
        restInvestorPortfolioMockMvc
            .perform(delete(ENTITY_API_URL_ID, investorPortfolio.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<InvestorPortfolio> investorPortfolioList = investorPortfolioRepository.findAll();
        assertThat(investorPortfolioList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
