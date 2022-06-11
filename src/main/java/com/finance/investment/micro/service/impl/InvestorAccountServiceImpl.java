package com.finance.investment.micro.service.impl;

import com.finance.investment.micro.domain.InvestorAccount;
import com.finance.investment.micro.repository.InvestorAccountRepository;
import com.finance.investment.micro.service.InvestorAccountService;
import com.finance.investment.micro.service.dto.InvestorAccountDTO;
import com.finance.investment.micro.service.mapper.InvestorAccountMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link InvestorAccount}.
 */
@Service
@Transactional
public class InvestorAccountServiceImpl implements InvestorAccountService {

    private final Logger log = LoggerFactory.getLogger(InvestorAccountServiceImpl.class);

    private final InvestorAccountRepository investorAccountRepository;

    private final InvestorAccountMapper investorAccountMapper;

    public InvestorAccountServiceImpl(InvestorAccountRepository investorAccountRepository, InvestorAccountMapper investorAccountMapper) {
        this.investorAccountRepository = investorAccountRepository;
        this.investorAccountMapper = investorAccountMapper;
    }

    @Override
    public InvestorAccountDTO save(InvestorAccountDTO investorAccountDTO) {
        log.debug("Request to save InvestorAccount : {}", investorAccountDTO);
        InvestorAccount investorAccount = investorAccountMapper.toEntity(investorAccountDTO);
        investorAccount = investorAccountRepository.save(investorAccount);
        return investorAccountMapper.toDto(investorAccount);
    }

    @Override
    public InvestorAccountDTO update(InvestorAccountDTO investorAccountDTO) {
        log.debug("Request to save InvestorAccount : {}", investorAccountDTO);
        InvestorAccount investorAccount = investorAccountMapper.toEntity(investorAccountDTO);
        investorAccount = investorAccountRepository.save(investorAccount);
        return investorAccountMapper.toDto(investorAccount);
    }

    @Override
    public Optional<InvestorAccountDTO> partialUpdate(InvestorAccountDTO investorAccountDTO) {
        log.debug("Request to partially update InvestorAccount : {}", investorAccountDTO);

        return investorAccountRepository
            .findById(investorAccountDTO.getId())
            .map(existingInvestorAccount -> {
                investorAccountMapper.partialUpdate(existingInvestorAccount, investorAccountDTO);

                return existingInvestorAccount;
            })
            .map(investorAccountRepository::save)
            .map(investorAccountMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InvestorAccountDTO> findAll(Pageable pageable) {
        log.debug("Request to get all InvestorAccounts");
        return investorAccountRepository.findAll(pageable).map(investorAccountMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<InvestorAccountDTO> findOne(Long id) {
        log.debug("Request to get InvestorAccount : {}", id);
        return investorAccountRepository.findById(id).map(investorAccountMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete InvestorAccount : {}", id);
        investorAccountRepository.deleteById(id);
    }
}
