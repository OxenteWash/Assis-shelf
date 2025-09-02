package br.com.oxentech.service;

import br.com.oxentech.domain.Loan;
import br.com.oxentech.repository.LoanRepository;
import br.com.oxentech.service.dto.LoanDTO;
import br.com.oxentech.service.mapper.LoanMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link br.com.oxentech.domain.Loan}.
 */
@Service
@Transactional
public class LoanService {

    private static final Logger LOG = LoggerFactory.getLogger(LoanService.class);

    private final LoanRepository loanRepository;

    private final LoanMapper loanMapper;

    public LoanService(LoanRepository loanRepository, LoanMapper loanMapper) {
        this.loanRepository = loanRepository;
        this.loanMapper = loanMapper;
    }

    /**
     * Save a loan.
     *
     * @param loanDTO the entity to save.
     * @return the persisted entity.
     */
    public LoanDTO save(LoanDTO loanDTO) {
        LOG.debug("Request to save Loan : {}", loanDTO);
        Loan loan = loanMapper.toEntity(loanDTO);
        loan = loanRepository.save(loan);
        return loanMapper.toDto(loan);
    }

    /**
     * Update a loan.
     *
     * @param loanDTO the entity to save.
     * @return the persisted entity.
     */
    public LoanDTO update(LoanDTO loanDTO) {
        LOG.debug("Request to update Loan : {}", loanDTO);
        Loan loan = loanMapper.toEntity(loanDTO);
        loan = loanRepository.save(loan);
        return loanMapper.toDto(loan);
    }

    /**
     * Partially update a loan.
     *
     * @param loanDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<LoanDTO> partialUpdate(LoanDTO loanDTO) {
        LOG.debug("Request to partially update Loan : {}", loanDTO);

        return loanRepository
            .findById(loanDTO.getId())
            .map(existingLoan -> {
                loanMapper.partialUpdate(existingLoan, loanDTO);

                return existingLoan;
            })
            .map(loanRepository::save)
            .map(loanMapper::toDto);
    }

    /**
     * Get all the loans.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<LoanDTO> findAll() {
        LOG.debug("Request to get all Loans");
        return loanRepository.findAll().stream().map(loanMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one loan by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<LoanDTO> findOne(Long id) {
        LOG.debug("Request to get Loan : {}", id);
        return loanRepository.findById(id).map(loanMapper::toDto);
    }

    /**
     * Delete the loan by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Loan : {}", id);
        loanRepository.deleteById(id);
    }
}
