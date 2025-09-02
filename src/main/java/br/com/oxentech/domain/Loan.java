package br.com.oxentech.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Loan.
 */
@Entity
@Table(name = "loan")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Loan implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "loan_time")
    private Instant loanTime;

    @Column(name = "return_time_expected")
    private Instant returnTimeExpected;

    @Column(name = "real_return_time")
    private Instant realReturnTime;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Loan id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getLoanTime() {
        return this.loanTime;
    }

    public Loan loanTime(Instant loanTime) {
        this.setLoanTime(loanTime);
        return this;
    }

    public void setLoanTime(Instant loanTime) {
        this.loanTime = loanTime;
    }

    public Instant getReturnTimeExpected() {
        return this.returnTimeExpected;
    }

    public Loan returnTimeExpected(Instant returnTimeExpected) {
        this.setReturnTimeExpected(returnTimeExpected);
        return this;
    }

    public void setReturnTimeExpected(Instant returnTimeExpected) {
        this.returnTimeExpected = returnTimeExpected;
    }

    public Instant getRealReturnTime() {
        return this.realReturnTime;
    }

    public Loan realReturnTime(Instant realReturnTime) {
        this.setRealReturnTime(realReturnTime);
        return this;
    }

    public void setRealReturnTime(Instant realReturnTime) {
        this.realReturnTime = realReturnTime;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Loan user(User user) {
        this.setUser(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Loan)) {
            return false;
        }
        return getId() != null && getId().equals(((Loan) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Loan{" +
            "id=" + getId() +
            ", loanTime='" + getLoanTime() + "'" +
            ", returnTimeExpected='" + getReturnTimeExpected() + "'" +
            ", realReturnTime='" + getRealReturnTime() + "'" +
            "}";
    }
}
