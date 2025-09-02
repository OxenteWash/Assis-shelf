package br.com.oxentech.domain;

import static br.com.oxentech.domain.BookTestSamples.*;
import static br.com.oxentech.domain.CategoryTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.com.oxentech.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CategoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Category.class);
        Category category1 = getCategorySample1();
        Category category2 = new Category();
        assertThat(category1).isNotEqualTo(category2);

        category2.setId(category1.getId());
        assertThat(category1).isEqualTo(category2);

        category2 = getCategorySample2();
        assertThat(category1).isNotEqualTo(category2);
    }

    @Test
    void bookTest() {
        Category category = getCategoryRandomSampleGenerator();
        Book bookBack = getBookRandomSampleGenerator();

        category.addBook(bookBack);
        assertThat(category.getBooks()).containsOnly(bookBack);
        assertThat(bookBack.getCategories()).containsOnly(category);

        category.removeBook(bookBack);
        assertThat(category.getBooks()).doesNotContain(bookBack);
        assertThat(bookBack.getCategories()).doesNotContain(category);

        category.books(new HashSet<>(Set.of(bookBack)));
        assertThat(category.getBooks()).containsOnly(bookBack);
        assertThat(bookBack.getCategories()).containsOnly(category);

        category.setBooks(new HashSet<>());
        assertThat(category.getBooks()).doesNotContain(bookBack);
        assertThat(bookBack.getCategories()).doesNotContain(category);
    }
}
