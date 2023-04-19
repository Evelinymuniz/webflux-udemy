package br.com.udemy.webfluxudemy.controller.impl;

import br.com.udemy.webfluxudemy.controller.UserController;
import br.com.udemy.webfluxudemy.mapper.UserMapper;
import br.com.udemy.webfluxudemy.model.request.UserRequest;
import br.com.udemy.webfluxudemy.model.response.UserResponse;
import br.com.udemy.webfluxudemy.repository.UserRepository;
import br.com.udemy.webfluxudemy.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/users")
public class UserControllerImpl implements UserController {

    private final UserService service;
    private final UserMapper mapper;


    @Override
    public ResponseEntity<Mono<Void>> save(final UserRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.save(request).then());
    }

    @Override
    public ResponseEntity<Mono<UserResponse>> findById(String id) {
        return ResponseEntity.ok().body(service.findById(id).map(obj -> mapper.toResponse(obj)));
        //.map(mapper::toResponse)
    }

    @Override
    public ResponseEntity<Flux<UserResponse>> findall() {
        return ResponseEntity.ok()
                .body(service.findAll()
                .map(mapper::toResponse));
        //map(obj -> mapper.toResponse(obj))) pode ser assim tbm.
    }

    @Override
    public ResponseEntity<Mono<UserResponse>> update(String id, UserRequest request) {
        return null;
    }

    @Override
    public ResponseEntity<Mono<Void>> delete(String id) {
        return null;
    }
}
