package br.com.oxentech.domain;

import static br.com.oxentech.domain.BookTestSamples.*;
import static br.com.oxentech.domain.ReservationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.com.oxentech.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReservationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Reservation.class);
        Reservation reservation1 = getReservationSample1();
        Reservation reservation2 = new Reservation();
        assertThat(reservation1).isNotEqualTo(reservation2);

        reservation2.setId(reservation1.getId());
        assertThat(reservation1).isEqualTo(reservation2);

        reservation2 = getReservationSample2();
        assertThat(reservation1).isNotEqualTo(reservation2);
    }

    @Test
    void bookTest() {
        Reservation reservation = getReservationRandomSampleGenerator();
        Book bookBack = getBookRandomSampleGenerator();

        reservation.setBook(bookBack);
        assertThat(reservation.getBook()).isEqualTo(bookBack);

        reservation.book(null);
        assertThat(reservation.getBook()).isNull();
    }
}
