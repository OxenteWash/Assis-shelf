package br.com.oxentech.web.rest;

import static br.com.oxentech.domain.LoanAsserts.*;
import static br.com.oxentech.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.oxentech.IntegrationTest;
import br.com.oxentech.domain.Loan;
import br.com.oxentech.repository.LoanRepository;
import br.com.oxentech.repository.UserRepository;
import br.com.oxentech.service.dto.LoanDTO;
import br.com.oxentech.service.mapper.LoanMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link LoanResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LoanResourceIT {

    private static final Instant DEFAULT_LOAN_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LOAN_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_RETURN_TIME_EXPECTED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_RETURN_TIME_EXPECTED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_REAL_RETURN_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_REAL_RETURN_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/loans";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLoanMockMvc;

    private Loan loan;

    private Loan insertedLoan;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Loan createEntity() {
        return new Loan()
            .loanTime(DEFAULT_LOAN_TIME)
            .returnTimeExpected(DEFAULT_RETURN_TIME_EXPECTED)
            .realReturnTime(DEFAULT_REAL_RETURN_TIME);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Loan createUpdatedEntity() {
        return new Loan()
            .loanTime(UPDATED_LOAN_TIME)
            .returnTimeExpected(UPDATED_RETURN_TIME_EXPECTED)
            .realReturnTime(UPDATED_REAL_RETURN_TIME);
    }

    @BeforeEach
    public void initTest() {
        loan = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedLoan != null) {
            loanRepository.delete(insertedLoan);
            insertedLoan = null;
        }
    }

    @Test
    @Transactional
    void createLoan() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Loan
        LoanDTO loanDTO = loanMapper.toDto(loan);
        var returnedLoanDTO = om.readValue(
            restLoanMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(loanDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            LoanDTO.class
        );

        // Validate the Loan in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedLoan = loanMapper.toEntity(returnedLoanDTO);
        assertLoanUpdatableFieldsEquals(returnedLoan, getPersistedLoan(returnedLoan));

        insertedLoan = returnedLoan;
    }

    @Test
    @Transactional
    void createLoanWithExistingId() throws Exception {
        // Create the Loan with an existing ID
        loan.setId(1L);
        LoanDTO loanDTO = loanMapper.toDto(loan);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLoanMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(loanDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Loan in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllLoans() throws Exception {
        // Initialize the database
        insertedLoan = loanRepository.saveAndFlush(loan);

        // Get all the loanList
        restLoanMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(loan.getId().intValue())))
            .andExpect(jsonPath("$.[*].loanTime").value(hasItem(DEFAULT_LOAN_TIME.toString())))
            .andExpect(jsonPath("$.[*].returnTimeExpected").value(hasItem(DEFAULT_RETURN_TIME_EXPECTED.toString())))
            .andExpect(jsonPath("$.[*].realReturnTime").value(hasItem(DEFAULT_REAL_RETURN_TIME.toString())));
    }

    @Test
    @Transactional
    void getLoan() throws Exception {
        // Initialize the database
        insertedLoan = loanRepository.saveAndFlush(loan);

        // Get the loan
        restLoanMockMvc
            .perform(get(ENTITY_API_URL_ID, loan.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(loan.getId().intValue()))
            .andExpect(jsonPath("$.loanTime").value(DEFAULT_LOAN_TIME.toString()))
            .andExpect(jsonPath("$.returnTimeExpected").value(DEFAULT_RETURN_TIME_EXPECTED.toString()))
            .andExpect(jsonPath("$.realReturnTime").value(DEFAULT_REAL_RETURN_TIME.toString()));
    }

    @Test
    @Transactional
    void getNonExistingLoan() throws Exception {
        // Get the loan
        restLoanMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingLoan() throws Exception {
        // Initialize the database
        insertedLoan = loanRepository.saveAndFlush(loan);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the loan
        Loan updatedLoan = loanRepository.findById(loan.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedLoan are not directly saved in db
        em.detach(updatedLoan);
        updatedLoan.loanTime(UPDATED_LOAN_TIME).returnTimeExpected(UPDATED_RETURN_TIME_EXPECTED).realReturnTime(UPDATED_REAL_RETURN_TIME);
        LoanDTO loanDTO = loanMapper.toDto(updatedLoan);

        restLoanMockMvc
            .perform(put(ENTITY_API_URL_ID, loanDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(loanDTO)))
            .andExpect(status().isOk());

        // Validate the Loan in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedLoanToMatchAllProperties(updatedLoan);
    }

    @Test
    @Transactional
    void putNonExistingLoan() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        loan.setId(longCount.incrementAndGet());

        // Create the Loan
        LoanDTO loanDTO = loanMapper.toDto(loan);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLoanMockMvc
            .perform(put(ENTITY_API_URL_ID, loanDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(loanDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Loan in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLoan() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        loan.setId(longCount.incrementAndGet());

        // Create the Loan
        LoanDTO loanDTO = loanMapper.toDto(loan);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLoanMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(loanDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Loan in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLoan() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        loan.setId(longCount.incrementAndGet());

        // Create the Loan
        LoanDTO loanDTO = loanMapper.toDto(loan);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLoanMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(loanDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Loan in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLoanWithPatch() throws Exception {
        // Initialize the database
        insertedLoan = loanRepository.saveAndFlush(loan);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the loan using partial update
        Loan partialUpdatedLoan = new Loan();
        partialUpdatedLoan.setId(loan.getId());

        partialUpdatedLoan.returnTimeExpected(UPDATED_RETURN_TIME_EXPECTED).realReturnTime(UPDATED_REAL_RETURN_TIME);

        restLoanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLoan.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLoan))
            )
            .andExpect(status().isOk());

        // Validate the Loan in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLoanUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedLoan, loan), getPersistedLoan(loan));
    }

    @Test
    @Transactional
    void fullUpdateLoanWithPatch() throws Exception {
        // Initialize the database
        insertedLoan = loanRepository.saveAndFlush(loan);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the loan using partial update
        Loan partialUpdatedLoan = new Loan();
        partialUpdatedLoan.setId(loan.getId());

        partialUpdatedLoan
            .loanTime(UPDATED_LOAN_TIME)
            .returnTimeExpected(UPDATED_RETURN_TIME_EXPECTED)
            .realReturnTime(UPDATED_REAL_RETURN_TIME);

        restLoanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLoan.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLoan))
            )
            .andExpect(status().isOk());

        // Validate the Loan in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLoanUpdatableFieldsEquals(partialUpdatedLoan, getPersistedLoan(partialUpdatedLoan));
    }

    @Test
    @Transactional
    void patchNonExistingLoan() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        loan.setId(longCount.incrementAndGet());

        // Create the Loan
        LoanDTO loanDTO = loanMapper.toDto(loan);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLoanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, loanDTO.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(loanDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Loan in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLoan() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        loan.setId(longCount.incrementAndGet());

        // Create the Loan
        LoanDTO loanDTO = loanMapper.toDto(loan);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLoanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(loanDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Loan in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLoan() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        loan.setId(longCount.incrementAndGet());

        // Create the Loan
        LoanDTO loanDTO = loanMapper.toDto(loan);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLoanMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(loanDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Loan in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLoan() throws Exception {
        // Initialize the database
        insertedLoan = loanRepository.saveAndFlush(loan);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the loan
        restLoanMockMvc
            .perform(delete(ENTITY_API_URL_ID, loan.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return loanRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Loan getPersistedLoan(Loan loan) {
        return loanRepository.findById(loan.getId()).orElseThrow();
    }

    protected void assertPersistedLoanToMatchAllProperties(Loan expectedLoan) {
        assertLoanAllPropertiesEquals(expectedLoan, getPersistedLoan(expectedLoan));
    }

    protected void assertPersistedLoanToMatchUpdatableProperties(Loan expectedLoan) {
        assertLoanAllUpdatablePropertiesEquals(expectedLoan, getPersistedLoan(expectedLoan));
    }
}
