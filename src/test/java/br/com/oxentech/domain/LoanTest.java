package br.com.oxentech.domain;

import static br.com.oxentech.domain.LoanTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.com.oxentech.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LoanTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Loan.class);
        Loan loan1 = getLoanSample1();
        Loan loan2 = new Loan();
        assertThat(loan1).isNotEqualTo(loan2);

        loan2.setId(loan1.getId());
        assertThat(loan1).isEqualTo(loan2);

        loan2 = getLoanSample2();
        assertThat(loan1).isNotEqualTo(loan2);
    }
}
