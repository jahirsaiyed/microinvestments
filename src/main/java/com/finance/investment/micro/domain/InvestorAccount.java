package com.finance.investment.micro.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A InvestorAccount.
 */
@Entity
@Table(name = "investor_account")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class InvestorAccount implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "account_no")
    private String accountNo;

    @Column(name = "i_ban")
    private String iBAN;

    @Column(name = "type")
    private String type;

    @Column(name = "wallet_address")
    private String walletAddress;

    @Column(name = "wallet_network")
    private String walletNetwork;

    @ManyToOne
    @JsonIgnoreProperties(value = { "portfolio", "accounts", "transactions" }, allowSetters = true)
    private Investor investor;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public InvestorAccount id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountNo() {
        return this.accountNo;
    }

    public InvestorAccount accountNo(String accountNo) {
        this.setAccountNo(accountNo);
        return this;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getiBAN() {
        return this.iBAN;
    }

    public InvestorAccount iBAN(String iBAN) {
        this.setiBAN(iBAN);
        return this;
    }

    public void setiBAN(String iBAN) {
        this.iBAN = iBAN;
    }

    public String getType() {
        return this.type;
    }

    public InvestorAccount type(String type) {
        this.setType(type);
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getWalletAddress() {
        return this.walletAddress;
    }

    public InvestorAccount walletAddress(String walletAddress) {
        this.setWalletAddress(walletAddress);
        return this;
    }

    public void setWalletAddress(String walletAddress) {
        this.walletAddress = walletAddress;
    }

    public String getWalletNetwork() {
        return this.walletNetwork;
    }

    public InvestorAccount walletNetwork(String walletNetwork) {
        this.setWalletNetwork(walletNetwork);
        return this;
    }

    public void setWalletNetwork(String walletNetwork) {
        this.walletNetwork = walletNetwork;
    }

    public Investor getInvestor() {
        return this.investor;
    }

    public void setInvestor(Investor investor) {
        this.investor = investor;
    }

    public InvestorAccount investor(Investor investor) {
        this.setInvestor(investor);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InvestorAccount)) {
            return false;
        }
        return id != null && id.equals(((InvestorAccount) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InvestorAccount{" +
            "id=" + getId() +
            ", accountNo='" + getAccountNo() + "'" +
            ", iBAN='" + getiBAN() + "'" +
            ", type='" + getType() + "'" +
            ", walletAddress='" + getWalletAddress() + "'" +
            ", walletNetwork='" + getWalletNetwork() + "'" +
            "}";
    }
}
