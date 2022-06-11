package com.finance.investment.micro.service.dto;

import com.finance.investment.micro.domain.enumeration.PROMOTIONTYPE;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.finance.investment.micro.domain.PromotionsAudit} entity.
 */
public class PromotionsAuditDTO implements Serializable {

    private Long id;

    private String description;

    private PROMOTIONTYPE type;

    private BigDecimal amount;

    private Instant createdOn;

    private Instant updatedOn;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public PROMOTIONTYPE getType() {
        return type;
    }

    public void setType(PROMOTIONTYPE type) {
        this.type = type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
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
        if (!(o instanceof PromotionsAuditDTO)) {
            return false;
        }

        PromotionsAuditDTO promotionsAuditDTO = (PromotionsAuditDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, promotionsAuditDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PromotionsAuditDTO{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", type='" + getType() + "'" +
            ", amount=" + getAmount() +
            ", createdOn='" + getCreatedOn() + "'" +
            ", updatedOn='" + getUpdatedOn() + "'" +
            "}";
    }
}
