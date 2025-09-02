package br.com.oxentech.service.mapper;

import br.com.oxentech.domain.Book;
import br.com.oxentech.domain.Reservation;
import br.com.oxentech.domain.User;
import br.com.oxentech.service.dto.BookDTO;
import br.com.oxentech.service.dto.ReservationDTO;
import br.com.oxentech.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Reservation} and its DTO {@link ReservationDTO}.
 */
@Mapper(componentModel = "spring")
public interface ReservationMapper extends EntityMapper<ReservationDTO, Reservation> {
    @Mapping(target = "book", source = "book", qualifiedByName = "bookId")
    @Mapping(target = "user", source = "user", qualifiedByName = "userId")
    ReservationDTO toDto(Reservation s);

    @Named("bookId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    BookDTO toDtoBookId(Book book);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);
}
