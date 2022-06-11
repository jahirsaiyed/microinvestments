package com.finance.investment.micro.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.finance.investment.micro.IntegrationTest;
import com.finance.investment.micro.domain.InvestorAccount;
import com.finance.investment.micro.repository.InvestorAccountRepository;
import com.finance.investment.micro.service.dto.InvestorAccountDTO;
import com.finance.investment.micro.service.mapper.InvestorAccountMapper;
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
 * Integration tests for the {@link InvestorAccountResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class InvestorAccountResourceIT {

    private static final String DEFAULT_ACCOUNT_NO = "AAAAAAAAAA";
    private static final String UPDATED_ACCOUNT_NO = "BBBBBBBBBB";

    private static final String DEFAULT_I_BAN = "AAAAAAAAAA";
    private static final String UPDATED_I_BAN = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_WALLET_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_WALLET_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_WALLET_NETWORK = "AAAAAAAAAA";
    private static final String UPDATED_WALLET_NETWORK = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/investor-accounts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private InvestorAccountRepository investorAccountRepository;

    @Autowired
    private InvestorAccountMapper investorAccountMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInvestorAccountMockMvc;

    private InvestorAccount investorAccount;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InvestorAccount createEntity(EntityManager em) {
        InvestorAccount investorAccount = new InvestorAccount()
            .accountNo(DEFAULT_ACCOUNT_NO)
            .iBAN(DEFAULT_I_BAN)
            .type(DEFAULT_TYPE)
            .walletAddress(DEFAULT_WALLET_ADDRESS)
            .walletNetwork(DEFAULT_WALLET_NETWORK);
        return investorAccount;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InvestorAccount createUpdatedEntity(EntityManager em) {
        InvestorAccount investorAccount = new InvestorAccount()
            .accountNo(UPDATED_ACCOUNT_NO)
            .iBAN(UPDATED_I_BAN)
            .type(UPDATED_TYPE)
            .walletAddress(UPDATED_WALLET_ADDRESS)
            .walletNetwork(UPDATED_WALLET_NETWORK);
        return investorAccount;
    }

    @BeforeEach
    public void initTest() {
        investorAccount = createEntity(em);
    }

    @Test
    @Transactional
    void createInvestorAccount() throws Exception {
        int databaseSizeBeforeCreate = investorAccountRepository.findAll().size();
        // Create the InvestorAccount
        InvestorAccountDTO investorAccountDTO = investorAccountMapper.toDto(investorAccount);
        restInvestorAccountMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(investorAccountDTO))
            )
            .andExpect(status().isCreated());

        // Validate the InvestorAccount in the database
        List<InvestorAccount> investorAccountList = investorAccountRepository.findAll();
        assertThat(investorAccountList).hasSize(databaseSizeBeforeCreate + 1);
        InvestorAccount testInvestorAccount = investorAccountList.get(investorAccountList.size() - 1);
        assertThat(testInvestorAccount.getAccountNo()).isEqualTo(DEFAULT_ACCOUNT_NO);
        assertThat(testInvestorAccount.getiBAN()).isEqualTo(DEFAULT_I_BAN);
        assertThat(testInvestorAccount.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testInvestorAccount.getWalletAddress()).isEqualTo(DEFAULT_WALLET_ADDRESS);
        assertThat(testInvestorAccount.getWalletNetwork()).isEqualTo(DEFAULT_WALLET_NETWORK);
    }

    @Test
    @Transactional
    void createInvestorAccountWithExistingId() throws Exception {
        // Create the InvestorAccount with an existing ID
        investorAccount.setId(1L);
        InvestorAccountDTO investorAccountDTO = investorAccountMapper.toDto(investorAccount);

        int databaseSizeBeforeCreate = investorAccountRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInvestorAccountMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(investorAccountDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvestorAccount in the database
        List<InvestorAccount> investorAccountList = investorAccountRepository.findAll();
        assertThat(investorAccountList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllInvestorAccounts() throws Exception {
        // Initialize the database
        investorAccountRepository.saveAndFlush(investorAccount);

        // Get all the investorAccountList
        restInvestorAccountMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(investorAccount.getId().intValue())))
            .andExpect(jsonPath("$.[*].accountNo").value(hasItem(DEFAULT_ACCOUNT_NO)))
            .andExpect(jsonPath("$.[*].iBAN").value(hasItem(DEFAULT_I_BAN)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].walletAddress").value(hasItem(DEFAULT_WALLET_ADDRESS)))
            .andExpect(jsonPath("$.[*].walletNetwork").value(hasItem(DEFAULT_WALLET_NETWORK)));
    }

    @Test
    @Transactional
    void getInvestorAccount() throws Exception {
        // Initialize the database
        investorAccountRepository.saveAndFlush(investorAccount);

        // Get the investorAccount
        restInvestorAccountMockMvc
            .perform(get(ENTITY_API_URL_ID, investorAccount.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(investorAccount.getId().intValue()))
            .andExpect(jsonPath("$.accountNo").value(DEFAULT_ACCOUNT_NO))
            .andExpect(jsonPath("$.iBAN").value(DEFAULT_I_BAN))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.walletAddress").value(DEFAULT_WALLET_ADDRESS))
            .andExpect(jsonPath("$.walletNetwork").value(DEFAULT_WALLET_NETWORK));
    }

    @Test
    @Transactional
    void getNonExistingInvestorAccount() throws Exception {
        // Get the investorAccount
        restInvestorAccountMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewInvestorAccount() throws Exception {
        // Initialize the database
        investorAccountRepository.saveAndFlush(investorAccount);

        int databaseSizeBeforeUpdate = investorAccountRepository.findAll().size();

        // Update the investorAccount
        InvestorAccount updatedInvestorAccount = investorAccountRepository.findById(investorAccount.getId()).get();
        // Disconnect from session so that the updates on updatedInvestorAccount are not directly saved in db
        em.detach(updatedInvestorAccount);
        updatedInvestorAccount
            .accountNo(UPDATED_ACCOUNT_NO)
            .iBAN(UPDATED_I_BAN)
            .type(UPDATED_TYPE)
            .walletAddress(UPDATED_WALLET_ADDRESS)
            .walletNetwork(UPDATED_WALLET_NETWORK);
        InvestorAccountDTO investorAccountDTO = investorAccountMapper.toDto(updatedInvestorAccount);

        restInvestorAccountMockMvc
            .perform(
                put(ENTITY_API_URL_ID, investorAccountDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(investorAccountDTO))
            )
            .andExpect(status().isOk());

        // Validate the InvestorAccount in the database
        List<InvestorAccount> investorAccountList = investorAccountRepository.findAll();
        assertThat(investorAccountList).hasSize(databaseSizeBeforeUpdate);
        InvestorAccount testInvestorAccount = investorAccountList.get(investorAccountList.size() - 1);
        assertThat(testInvestorAccount.getAccountNo()).isEqualTo(UPDATED_ACCOUNT_NO);
        assertThat(testInvestorAccount.getiBAN()).isEqualTo(UPDATED_I_BAN);
        assertThat(testInvestorAccount.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testInvestorAccount.getWalletAddress()).isEqualTo(UPDATED_WALLET_ADDRESS);
        assertThat(testInvestorAccount.getWalletNetwork()).isEqualTo(UPDATED_WALLET_NETWORK);
    }

    @Test
    @Transactional
    void putNonExistingInvestorAccount() throws Exception {
        int databaseSizeBeforeUpdate = investorAccountRepository.findAll().size();
        investorAccount.setId(count.incrementAndGet());

        // Create the InvestorAccount
        InvestorAccountDTO investorAccountDTO = investorAccountMapper.toDto(investorAccount);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInvestorAccountMockMvc
            .perform(
                put(ENTITY_API_URL_ID, investorAccountDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(investorAccountDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvestorAccount in the database
        List<InvestorAccount> investorAccountList = investorAccountRepository.findAll();
        assertThat(investorAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInvestorAccount() throws Exception {
        int databaseSizeBeforeUpdate = investorAccountRepository.findAll().size();
        investorAccount.setId(count.incrementAndGet());

        // Create the InvestorAccount
        InvestorAccountDTO investorAccountDTO = investorAccountMapper.toDto(investorAccount);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvestorAccountMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(investorAccountDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvestorAccount in the database
        List<InvestorAccount> investorAccountList = investorAccountRepository.findAll();
        assertThat(investorAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInvestorAccount() throws Exception {
        int databaseSizeBeforeUpdate = investorAccountRepository.findAll().size();
        investorAccount.setId(count.incrementAndGet());

        // Create the InvestorAccount
        InvestorAccountDTO investorAccountDTO = investorAccountMapper.toDto(investorAccount);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvestorAccountMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(investorAccountDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the InvestorAccount in the database
        List<InvestorAccount> investorAccountList = investorAccountRepository.findAll();
        assertThat(investorAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInvestorAccountWithPatch() throws Exception {
        // Initialize the database
        investorAccountRepository.saveAndFlush(investorAccount);

        int databaseSizeBeforeUpdate = investorAccountRepository.findAll().size();

        // Update the investorAccount using partial update
        InvestorAccount partialUpdatedInvestorAccount = new InvestorAccount();
        partialUpdatedInvestorAccount.setId(investorAccount.getId());

        partialUpdatedInvestorAccount.iBAN(UPDATED_I_BAN);

        restInvestorAccountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInvestorAccount.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInvestorAccount))
            )
            .andExpect(status().isOk());

        // Validate the InvestorAccount in the database
        List<InvestorAccount> investorAccountList = investorAccountRepository.findAll();
        assertThat(investorAccountList).hasSize(databaseSizeBeforeUpdate);
        InvestorAccount testInvestorAccount = investorAccountList.get(investorAccountList.size() - 1);
        assertThat(testInvestorAccount.getAccountNo()).isEqualTo(DEFAULT_ACCOUNT_NO);
        assertThat(testInvestorAccount.getiBAN()).isEqualTo(UPDATED_I_BAN);
        assertThat(testInvestorAccount.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testInvestorAccount.getWalletAddress()).isEqualTo(DEFAULT_WALLET_ADDRESS);
        assertThat(testInvestorAccount.getWalletNetwork()).isEqualTo(DEFAULT_WALLET_NETWORK);
    }

    @Test
    @Transactional
    void fullUpdateInvestorAccountWithPatch() throws Exception {
        // Initialize the database
        investorAccountRepository.saveAndFlush(investorAccount);

        int databaseSizeBeforeUpdate = investorAccountRepository.findAll().size();

        // Update the investorAccount using partial update
        InvestorAccount partialUpdatedInvestorAccount = new InvestorAccount();
        partialUpdatedInvestorAccount.setId(investorAccount.getId());

        partialUpdatedInvestorAccount
            .accountNo(UPDATED_ACCOUNT_NO)
            .iBAN(UPDATED_I_BAN)
            .type(UPDATED_TYPE)
            .walletAddress(UPDATED_WALLET_ADDRESS)
            .walletNetwork(UPDATED_WALLET_NETWORK);

        restInvestorAccountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInvestorAccount.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInvestorAccount))
            )
            .andExpect(status().isOk());

        // Validate the InvestorAccount in the database
        List<InvestorAccount> investorAccountList = investorAccountRepository.findAll();
        assertThat(investorAccountList).hasSize(databaseSizeBeforeUpdate);
        InvestorAccount testInvestorAccount = investorAccountList.get(investorAccountList.size() - 1);
        assertThat(testInvestorAccount.getAccountNo()).isEqualTo(UPDATED_ACCOUNT_NO);
        assertThat(testInvestorAccount.getiBAN()).isEqualTo(UPDATED_I_BAN);
        assertThat(testInvestorAccount.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testInvestorAccount.getWalletAddress()).isEqualTo(UPDATED_WALLET_ADDRESS);
        assertThat(testInvestorAccount.getWalletNetwork()).isEqualTo(UPDATED_WALLET_NETWORK);
    }

    @Test
    @Transactional
    void patchNonExistingInvestorAccount() throws Exception {
        int databaseSizeBeforeUpdate = investorAccountRepository.findAll().size();
        investorAccount.setId(count.incrementAndGet());

        // Create the InvestorAccount
        InvestorAccountDTO investorAccountDTO = investorAccountMapper.toDto(investorAccount);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInvestorAccountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, investorAccountDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(investorAccountDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvestorAccount in the database
        List<InvestorAccount> investorAccountList = investorAccountRepository.findAll();
        assertThat(investorAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInvestorAccount() throws Exception {
        int databaseSizeBeforeUpdate = investorAccountRepository.findAll().size();
        investorAccount.setId(count.incrementAndGet());

        // Create the InvestorAccount
        InvestorAccountDTO investorAccountDTO = investorAccountMapper.toDto(investorAccount);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvestorAccountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(investorAccountDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvestorAccount in the database
        List<InvestorAccount> investorAccountList = investorAccountRepository.findAll();
        assertThat(investorAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInvestorAccount() throws Exception {
        int databaseSizeBeforeUpdate = investorAccountRepository.findAll().size();
        investorAccount.setId(count.incrementAndGet());

        // Create the InvestorAccount
        InvestorAccountDTO investorAccountDTO = investorAccountMapper.toDto(investorAccount);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvestorAccountMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(investorAccountDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the InvestorAccount in the database
        List<InvestorAccount> investorAccountList = investorAccountRepository.findAll();
        assertThat(investorAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInvestorAccount() throws Exception {
        // Initialize the database
        investorAccountRepository.saveAndFlush(investorAccount);

        int databaseSizeBeforeDelete = investorAccountRepository.findAll().size();

        // Delete the investorAccount
        restInvestorAccountMockMvc
            .perform(delete(ENTITY_API_URL_ID, investorAccount.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<InvestorAccount> investorAccountList = investorAccountRepository.findAll();
        assertThat(investorAccountList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
