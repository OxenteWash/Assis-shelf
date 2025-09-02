package br.com.oxentech.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link br.com.oxentech.domain.Loan} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LoanDTO implements Serializable {

    private Long id;

    private Instant loanTime;

    private Instant returnTimeExpected;

    private Instant realReturnTime;

    private UserDTO user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getLoanTime() {
        return loanTime;
    }

    public void setLoanTime(Instant loanTime) {
        this.loanTime = loanTime;
    }

    public Instant getReturnTimeExpected() {
        return returnTimeExpected;
    }

    public void setReturnTimeExpected(Instant returnTimeExpected) {
        this.returnTimeExpected = returnTimeExpected;
    }

    public Instant getRealReturnTime() {
        return realReturnTime;
    }

    public void setRealReturnTime(Instant realReturnTime) {
        this.realReturnTime = realReturnTime;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LoanDTO)) {
            return false;
        }

        LoanDTO loanDTO = (LoanDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, loanDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LoanDTO{" +
            "id=" + getId() +
            ", loanTime='" + getLoanTime() + "'" +
            ", returnTimeExpected='" + getReturnTimeExpected() + "'" +
            ", realReturnTime='" + getRealReturnTime() + "'" +
            ", user=" + getUser() +
            "}";
    }
}
