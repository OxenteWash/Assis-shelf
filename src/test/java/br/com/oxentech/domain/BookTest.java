package br.com.oxentech.domain;

import static br.com.oxentech.domain.BookTestSamples.*;
import static br.com.oxentech.domain.CategoryTestSamples.*;
import static br.com.oxentech.domain.ReservationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.com.oxentech.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class BookTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Book.class);
        Book book1 = getBookSample1();
        Book book2 = new Book();
        assertThat(book1).isNotEqualTo(book2);

        book2.setId(book1.getId());
        assertThat(book1).isEqualTo(book2);

        book2 = getBookSample2();
        assertThat(book1).isNotEqualTo(book2);
    }

    @Test
    void categoryTest() {
        Book book = getBookRandomSampleGenerator();
        Category categoryBack = getCategoryRandomSampleGenerator();

        book.addCategory(categoryBack);
        assertThat(book.getCategories()).containsOnly(categoryBack);

        book.removeCategory(categoryBack);
        assertThat(book.getCategories()).doesNotContain(categoryBack);

        book.categories(new HashSet<>(Set.of(categoryBack)));
        assertThat(book.getCategories()).containsOnly(categoryBack);

        book.setCategories(new HashSet<>());
        assertThat(book.getCategories()).doesNotContain(categoryBack);
    }

    @Test
    void reservationTest() {
        Book book = getBookRandomSampleGenerator();
        Reservation reservationBack = getReservationRandomSampleGenerator();

        book.addReservation(reservationBack);
        assertThat(book.getReservations()).containsOnly(reservationBack);
        assertThat(reservationBack.getBook()).isEqualTo(book);

        book.removeReservation(reservationBack);
        assertThat(book.getReservations()).doesNotContain(reservationBack);
        assertThat(reservationBack.getBook()).isNull();

        book.reservations(new HashSet<>(Set.of(reservationBack)));
        assertThat(book.getReservations()).containsOnly(reservationBack);
        assertThat(reservationBack.getBook()).isEqualTo(book);

        book.setReservations(new HashSet<>());
        assertThat(book.getReservations()).doesNotContain(reservationBack);
        assertThat(reservationBack.getBook()).isNull();
    }
}
