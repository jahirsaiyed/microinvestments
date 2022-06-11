package com.finance.investment.micro.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A InvestorPortfolio.
 */
@Entity
@Table(name = "investor_portfolio")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class InvestorPortfolio implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "units", precision = 21, scale = 2)
    private BigDecimal units;

    @Column(name = "current_unit_price", precision = 21, scale = 2)
    private BigDecimal currentUnitPrice;

    @Column(name = "balance", precision = 21, scale = 2)
    private BigDecimal balance;

    @Column(name = "current_invested_amount", precision = 21, scale = 2)
    private BigDecimal currentInvestedAmount;

    @Column(name = "created_on")
    private Instant createdOn;

    @Column(name = "updated_on")
    private Instant updatedOn;

    @JsonIgnoreProperties(value = { "portfolio", "accounts", "transactions" }, allowSetters = true)
    @OneToOne(mappedBy = "portfolio")
    private Investor investor;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public InvestorPortfolio id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getUnits() {
        return this.units;
    }

    public InvestorPortfolio units(BigDecimal units) {
        this.setUnits(units);
        return this;
    }

    public void setUnits(BigDecimal units) {
        this.units = units;
    }

    public BigDecimal getCurrentUnitPrice() {
        return this.currentUnitPrice;
    }

    public InvestorPortfolio currentUnitPrice(BigDecimal currentUnitPrice) {
        this.setCurrentUnitPrice(currentUnitPrice);
        return this;
    }

    public void setCurrentUnitPrice(BigDecimal currentUnitPrice) {
        this.currentUnitPrice = currentUnitPrice;
    }

    public BigDecimal getBalance() {
        return this.balance;
    }

    public InvestorPortfolio balance(BigDecimal balance) {
        this.setBalance(balance);
        return this;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getCurrentInvestedAmount() {
        return this.currentInvestedAmount;
    }

    public InvestorPortfolio currentInvestedAmount(BigDecimal currentInvestedAmount) {
        this.setCurrentInvestedAmount(currentInvestedAmount);
        return this;
    }

    public void setCurrentInvestedAmount(BigDecimal currentInvestedAmount) {
        this.currentInvestedAmount = currentInvestedAmount;
    }

    public Instant getCreatedOn() {
        return this.createdOn;
    }

    public InvestorPortfolio createdOn(Instant createdOn) {
        this.setCreatedOn(createdOn);
        return this;
    }

    public void setCreatedOn(Instant createdOn) {
        this.createdOn = createdOn;
    }

    public Instant getUpdatedOn() {
        return this.updatedOn;
    }

    public InvestorPortfolio updatedOn(Instant updatedOn) {
        this.setUpdatedOn(updatedOn);
        return this;
    }

    public void setUpdatedOn(Instant updatedOn) {
        this.updatedOn = updatedOn;
    }

    public Investor getInvestor() {
        return this.investor;
    }

    public void setInvestor(Investor investor) {
        if (this.investor != null) {
            this.investor.setPortfolio(null);
        }
        if (investor != null) {
            investor.setPortfolio(this);
        }
        this.investor = investor;
    }

    public InvestorPortfolio investor(Investor investor) {
        this.setInvestor(investor);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InvestorPortfolio)) {
            return false;
        }
        return id != null && id.equals(((InvestorPortfolio) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InvestorPortfolio{" +
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
