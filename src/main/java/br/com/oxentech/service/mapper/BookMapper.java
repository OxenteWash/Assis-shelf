package br.com.oxentech.service.mapper;

import br.com.oxentech.domain.Book;
import br.com.oxentech.domain.Category;
import br.com.oxentech.service.dto.BookDTO;
import br.com.oxentech.service.dto.CategoryDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Book} and its DTO {@link BookDTO}.
 */
@Mapper(componentModel = "spring")
public interface BookMapper extends EntityMapper<BookDTO, Book> {
    @Mapping(target = "categories", source = "categories", qualifiedByName = "categoryIdSet")
    BookDTO toDto(Book s);

    @Mapping(target = "removeCategory", ignore = true)
    Book toEntity(BookDTO bookDTO);

    @Named("categoryId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CategoryDTO toDtoCategoryId(Category category);

    @Named("categoryIdSet")
    default Set<CategoryDTO> toDtoCategoryIdSet(Set<Category> category) {
        return category.stream().map(this::toDtoCategoryId).collect(Collectors.toSet());
    }
}
