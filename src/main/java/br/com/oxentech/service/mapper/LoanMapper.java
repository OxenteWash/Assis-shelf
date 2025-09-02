package br.com.oxentech.service.mapper;

import br.com.oxentech.domain.Loan;
import br.com.oxentech.domain.User;
import br.com.oxentech.service.dto.LoanDTO;
import br.com.oxentech.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Loan} and its DTO {@link LoanDTO}.
 */
@Mapper(componentModel = "spring")
public interface LoanMapper extends EntityMapper<LoanDTO, Loan> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userId")
    LoanDTO toDto(Loan s);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);
}
