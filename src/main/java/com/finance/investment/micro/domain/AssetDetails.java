package com.finance.investment.micro.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AssetDetails.
 */
@Entity
@Table(name = "asset_details")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AssetDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "units", precision = 21, scale = 2)
    private BigDecimal units;

    @Column(name = "unit_price", precision = 21, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "balance", precision = 21, scale = 2)
    private BigDecimal balance;

    @Column(name = "current_invested_amount", precision = 21, scale = 2)
    private BigDecimal currentInvestedAmount;

    @Column(name = "profit_loss", precision = 21, scale = 2)
    private BigDecimal profitLoss;

    @Column(name = "created_on")
    private Instant createdOn;

    @Column(name = "updated_on")
    private Instant updatedOn;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AssetDetails id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getUnits() {
        return this.units;
    }

    public AssetDetails units(BigDecimal units) {
        this.setUnits(units);
        return this;
    }

    public void setUnits(BigDecimal units) {
        this.units = units;
    }

    public BigDecimal getUnitPrice() {
        return this.unitPrice;
    }

    public AssetDetails unitPrice(BigDecimal unitPrice) {
        this.setUnitPrice(unitPrice);
        return this;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getBalance() {
        return this.balance;
    }

    public AssetDetails balance(BigDecimal balance) {
        this.setBalance(balance);
        return this;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getCurrentInvestedAmount() {
        return this.currentInvestedAmount;
    }

    public AssetDetails currentInvestedAmount(BigDecimal currentInvestedAmount) {
        this.setCurrentInvestedAmount(currentInvestedAmount);
        return this;
    }

    public void setCurrentInvestedAmount(BigDecimal currentInvestedAmount) {
        this.currentInvestedAmount = currentInvestedAmount;
    }

    public BigDecimal getProfitLoss() {
        return this.profitLoss;
    }

    public AssetDetails profitLoss(BigDecimal profitLoss) {
        this.setProfitLoss(profitLoss);
        return this;
    }

    public void setProfitLoss(BigDecimal profitLoss) {
        this.profitLoss = profitLoss;
    }

    public Instant getCreatedOn() {
        return this.createdOn;
    }

    public AssetDetails createdOn(Instant createdOn) {
        this.setCreatedOn(createdOn);
        return this;
    }

    public void setCreatedOn(Instant createdOn) {
        this.createdOn = createdOn;
    }

    public Instant getUpdatedOn() {
        return this.updatedOn;
    }

    public AssetDetails updatedOn(Instant updatedOn) {
        this.setUpdatedOn(updatedOn);
        return this;
    }

    public void setUpdatedOn(Instant updatedOn) {
        this.updatedOn = updatedOn;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AssetDetails)) {
            return false;
        }
        return id != null && id.equals(((AssetDetails) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AssetDetails{" +
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
