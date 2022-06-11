package com.finance.investment.micro.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.finance.investment.micro.domain.InvestorAccount} entity.
 */
public class InvestorAccountDTO implements Serializable {

    private Long id;

    private String accountNo;

    private String iBAN;

    private String type;

    private String walletAddress;

    private String walletNetwork;

    private InvestorDTO investor;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getiBAN() {
        return iBAN;
    }

    public void setiBAN(String iBAN) {
        this.iBAN = iBAN;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getWalletAddress() {
        return walletAddress;
    }

    public void setWalletAddress(String walletAddress) {
        this.walletAddress = walletAddress;
    }

    public String getWalletNetwork() {
        return walletNetwork;
    }

    public void setWalletNetwork(String walletNetwork) {
        this.walletNetwork = walletNetwork;
    }

    public InvestorDTO getInvestor() {
        return investor;
    }

    public void setInvestor(InvestorDTO investor) {
        this.investor = investor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InvestorAccountDTO)) {
            return false;
        }

        InvestorAccountDTO investorAccountDTO = (InvestorAccountDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, investorAccountDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InvestorAccountDTO{" +
            "id=" + getId() +
            ", accountNo='" + getAccountNo() + "'" +
            ", iBAN='" + getiBAN() + "'" +
            ", type='" + getType() + "'" +
            ", walletAddress='" + getWalletAddress() + "'" +
            ", walletNetwork='" + getWalletNetwork() + "'" +
            ", investor=" + getInvestor() +
            "}";
    }
}
