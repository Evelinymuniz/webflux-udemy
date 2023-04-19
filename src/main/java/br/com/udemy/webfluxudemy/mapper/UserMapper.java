package br.com.udemy.webfluxudemy.mapper;

import br.com.udemy.webfluxudemy.entity.User;
import br.com.udemy.webfluxudemy.model.request.UserRequest;
import br.com.udemy.webfluxudemy.model.response.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import static org.mapstruct.NullValueCheckStrategy.ALWAYS;
import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = IGNORE,
        nullValueCheckStrategy = ALWAYS
        )
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    User toEntity(final UserRequest request);


    UserResponse toResponse (final User entity);


}
