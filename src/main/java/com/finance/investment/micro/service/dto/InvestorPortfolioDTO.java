package com.finance.investment.micro.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.finance.investment.micro.domain.InvestorPortfolio} entity.
 */
public class InvestorPortfolioDTO implements Serializable {

    private Long id;

    private BigDecimal units;

    private BigDecimal currentUnitPrice;

    private BigDecimal balance;

    private BigDecimal currentInvestedAmount;

    private Instant createdOn;

    private Instant updatedOn;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getUnits() {
        return units;
    }

    public void setUnits(BigDecimal units) {
        this.units = units;
    }

    public BigDecimal getCurrentUnitPrice() {
        return currentUnitPrice;
    }

    public void setCurrentUnitPrice(BigDecimal currentUnitPrice) {
        this.currentUnitPrice = currentUnitPrice;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getCurrentInvestedAmount() {
        return currentInvestedAmount;
    }

    public void setCurrentInvestedAmount(BigDecimal currentInvestedAmount) {
        this.currentInvestedAmount = currentInvestedAmount;
    }

    public Instant getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Instant createdOn) {
        this.createdOn = createdOn;
    }

    public Instant getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Instant updatedOn) {
        this.updatedOn = updatedOn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InvestorPortfolioDTO)) {
            return false;
        }

        InvestorPortfolioDTO investorPortfolioDTO = (InvestorPortfolioDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, investorPortfolioDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InvestorPortfolioDTO{" +
            "id=" + getId() +
            ", units=" + getUnits() +
            ", currentUnitPrice=" + getCurrentUnitPrice() +
            ", balance=" + getBalance() +
            ", currentInvestedAmount=" + getCurrentInvestedAmount() +
            ", createdOn='" + getCreatedOn() + "'" +
            ", updatedOn='" + getUpdatedOn() + "'" +
            "}";
    }
}
