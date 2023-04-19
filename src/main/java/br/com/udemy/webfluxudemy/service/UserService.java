package br.com.udemy.webfluxudemy.service;

import br.com.udemy.webfluxudemy.entity.User;
import br.com.udemy.webfluxudemy.mapper.UserMapper;
import br.com.udemy.webfluxudemy.model.request.UserRequest;
import br.com.udemy.webfluxudemy.repository.UserRepository;
import br.com.udemy.webfluxudemy.service.exception.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static java.lang.String.format;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final UserMapper mapper;

    public Mono<User> save (final UserRequest request){
        return repository.save(mapper.toEntity(request));
    }
    public Mono<User> findById(final String id){
        return repository.findById(id).switchIfEmpty(Mono.error(new ObjectNotFoundException(
                format("Object not found. Id: %s, Type: %s", id, User.class.getSimpleName())
        )));
    }



}
