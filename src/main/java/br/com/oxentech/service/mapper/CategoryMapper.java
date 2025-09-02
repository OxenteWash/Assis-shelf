package br.com.oxentech.service.mapper;

import br.com.oxentech.domain.Book;
import br.com.oxentech.domain.Category;
import br.com.oxentech.service.dto.BookDTO;
import br.com.oxentech.service.dto.CategoryDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Category} and its DTO {@link CategoryDTO}.
 */
@Mapper(componentModel = "spring")
public interface CategoryMapper extends EntityMapper<CategoryDTO, Category> {
    @Mapping(target = "books", source = "books", qualifiedByName = "bookIdSet")
    CategoryDTO toDto(Category s);

    @Mapping(target = "books", ignore = true)
    @Mapping(target = "removeBook", ignore = true)
    Category toEntity(CategoryDTO categoryDTO);

    @Named("bookId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    BookDTO toDtoBookId(Book book);

    @Named("bookIdSet")
    default Set<BookDTO> toDtoBookIdSet(Set<Book> book) {
        return book.stream().map(this::toDtoBookId).collect(Collectors.toSet());
    }
}
