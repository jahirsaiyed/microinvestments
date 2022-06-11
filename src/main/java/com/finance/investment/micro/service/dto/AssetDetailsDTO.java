package com.finance.investment.micro.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.finance.investment.micro.domain.AssetDetails} entity.
 */
public class AssetDetailsDTO implements Serializable {

    private Long id;

    private BigDecimal units;

    private BigDecimal unitPrice;

    private BigDecimal balance;

    private BigDecimal currentInvestedAmount;

    private BigDecimal profitLoss;

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

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
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

    public BigDecimal getProfitLoss() {
        return profitLoss;
    }

    public void setProfitLoss(BigDecimal profitLoss) {
        this.profitLoss = profitLoss;
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
        if (!(o instanceof AssetDetailsDTO)) {
            return false;
        }

        AssetDetailsDTO assetDetailsDTO = (AssetDetailsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, assetDetailsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AssetDetailsDTO{" +
            "id=" + getId() +
            ", units=" + getUnits() +
            ", unitPrice=" + getUnitPrice() +
            ", balance=" + getBalance() +
            ", currentInvestedAmount=" + getCurrentInvestedAmount() +
            ", profitLoss=" + getProfitLoss() +
            ", createdOn='" + getCreatedOn() + "'" +
            ", updatedOn='" + getUpdatedOn() + "'" +
            "}";
    }
}
