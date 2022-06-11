package com.finance.investment.micro.service.dto;

import com.finance.investment.micro.domain.enumeration.TRANSACTIONTYPE;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.finance.investment.micro.domain.Transaction} entity.
 */
public class TransactionDTO implements Serializable {

    private Long id;

    private TRANSACTIONTYPE type;

    private BigDecimal amount;

    private BigDecimal units;

    private BigDecimal unitPrice;

    private Instant createdOn;

    private Instant updatedOn;

    private InvestorDTO investor;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TRANSACTIONTYPE getType() {
        return type;
    }

    public void setType(TRANSACTIONTYPE type) {
        this.type = type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
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
        if (!(o instanceof TransactionDTO)) {
            return false;
        }

        TransactionDTO transactionDTO = (TransactionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, transactionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TransactionDTO{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", amount=" + getAmount() +
            ", units=" + getUnits() +
            ", unitPrice=" + getUnitPrice() +
            ", createdOn='" + getCreatedOn() + "'" +
            ", updatedOn='" + getUpdatedOn() + "'" +
            ", investor=" + getInvestor() +
            "}";
    }
}
