package com.finance.investment.micro.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.finance.investment.micro.domain.enumeration.Gender;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Investor.
 */
@Entity
@Table(name = "investor")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Investor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    private Gender gender;

    @NotNull
    @Column(name = "phone", nullable = false)
    private String phone;

    @NotNull
    @Column(name = "address_line_1", nullable = false)
    private String addressLine1;

    @Column(name = "address_line_2")
    private String addressLine2;

    @NotNull
    @Column(name = "city", nullable = false)
    private String city;

    @NotNull
    @Column(name = "country", nullable = false)
    private String country;

    @Column(name = "created_on")
    private Instant createdOn;

    @JsonIgnoreProperties(value = { "investor" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private InvestorPortfolio portfolio;

    @OneToMany(mappedBy = "investor")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "investor" }, allowSetters = true)
    private Set<InvestorAccount> accounts = new HashSet<>();

    @OneToMany(mappedBy = "investor")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "investor" }, allowSetters = true)
    private Set<Transaction> transactions = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Investor id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Investor name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return this.email;
    }

    public Investor email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Gender getGender() {
        return this.gender;
    }

    public Investor gender(Gender gender) {
        this.setGender(gender);
        return this;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return this.phone;
    }

    public Investor phone(String phone) {
        this.setPhone(phone);
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddressLine1() {
        return this.addressLine1;
    }

    public Investor addressLine1(String addressLine1) {
        this.setAddressLine1(addressLine1);
        return this;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return this.addressLine2;
    }

    public Investor addressLine2(String addressLine2) {
        this.setAddressLine2(addressLine2);
        return this;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getCity() {
        return this.city;
    }

    public Investor city(String city) {
        this.setCity(city);
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return this.country;
    }

    public Investor country(String country) {
        this.setCountry(country);
        return this;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Instant getCreatedOn() {
        return this.createdOn;
    }

    public Investor createdOn(Instant createdOn) {
        this.setCreatedOn(createdOn);
        return this;
    }

    public void setCreatedOn(Instant createdOn) {
        this.createdOn = createdOn;
    }

    public InvestorPortfolio getPortfolio() {
        return this.portfolio;
    }

    public void setPortfolio(InvestorPortfolio investorPortfolio) {
        this.portfolio = investorPortfolio;
    }

    public Investor portfolio(InvestorPortfolio investorPortfolio) {
        this.setPortfolio(investorPortfolio);
        return this;
    }

    public Set<InvestorAccount> getAccounts() {
        return this.accounts;
    }

    public void setAccounts(Set<InvestorAccount> investorAccounts) {
        if (this.accounts != null) {
            this.accounts.forEach(i -> i.setInvestor(null));
        }
        if (investorAccounts != null) {
            investorAccounts.forEach(i -> i.setInvestor(this));
        }
        this.accounts = investorAccounts;
    }

    public Investor accounts(Set<InvestorAccount> investorAccounts) {
        this.setAccounts(investorAccounts);
        return this;
    }

    public Investor addAccounts(InvestorAccount investorAccount) {
        this.accounts.add(investorAccount);
        investorAccount.setInvestor(this);
        return this;
    }

    public Investor removeAccounts(InvestorAccount investorAccount) {
        this.accounts.remove(investorAccount);
        investorAccount.setInvestor(null);
        return this;
    }

    public Set<Transaction> getTransactions() {
        return this.transactions;
    }

    public void setTransactions(Set<Transaction> transactions) {
        if (this.transactions != null) {
            this.transactions.forEach(i -> i.setInvestor(null));
        }
        if (transactions != null) {
            transactions.forEach(i -> i.setInvestor(this));
        }
        this.transactions = transactions;
    }

    public Investor transactions(Set<Transaction> transactions) {
        this.setTransactions(transactions);
        return this;
    }

    public Investor addTransaction(Transaction transaction) {
        this.transactions.add(transaction);
        transaction.setInvestor(this);
        return this;
    }

    public Investor removeTransaction(Transaction transaction) {
        this.transactions.remove(transaction);
        transaction.setInvestor(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Investor)) {
            return false;
        }
        return id != null && id.equals(((Investor) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Investor{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", email='" + getEmail() + "'" +
            ", gender='" + getGender() + "'" +
            ", phone='" + getPhone() + "'" +
            ", addressLine1='" + getAddressLine1() + "'" +
            ", addressLine2='" + getAddressLine2() + "'" +
            ", city='" + getCity() + "'" +
            ", country='" + getCountry() + "'" +
            ", createdOn='" + getCreatedOn() + "'" +
            "}";
    }
}
