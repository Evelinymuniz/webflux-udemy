package br.com.udemy.webfluxudemy.service;

import br.com.udemy.webfluxudemy.entity.User;
import br.com.udemy.webfluxudemy.mapper.UserMapper;
import br.com.udemy.webfluxudemy.model.request.UserRequest;
import br.com.udemy.webfluxudemy.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository repository;
    @Mock
    private UserMapper mapper;

    @InjectMocks
    private UserService service;
    @Test
    void testsave() {
        UserRequest request = new UserRequest("eveliny","eveliny@gmail.com","2342");
        User entity = User.builder().build();

        when(mapper.toEntity(any(UserRequest.class))).thenReturn(entity);
        when(repository.save(any(User.class))).thenReturn(Mono.just(User.builder().build()));

        Mono<User> result = service.save(request);

        StepVerifier.create(result).expectNextMatches(user -> user.getClass() == User.class)
                .expectComplete()
                .verify();
        Mockito.verify(repository, times(1)).save(any(User.class));
    }
    @Test
    void testFindById(){
        when(repository.findById(anyString())).thenReturn(Mono.just(User.builder()
                .build()));

        Mono<User> result = service.findById("123");

        StepVerifier.create(result).expectNextMatches(user -> user.getClass() == User.class)
                .expectComplete()
                .verify();
        Mockito.verify(repository, times(1)).findById(anyString());
    }
    @Test
    void testFindAll(){
        when(repository.findAll()).thenReturn(Flux.just(User.builder()
                .build()));

        Flux<User> result = service.findAll();

        StepVerifier.create(result).expectNextMatches(user -> user.getClass() == User.class)
                .expectComplete()
                .verify();
        Mockito.verify(repository, times(1)).findAll();
    }

    @Test
    void testUpdate(){
        UserRequest request = new UserRequest("eveliny","eveliny@gmail.com","2342");
        User entity = User.builder().build();

        when(mapper.toEntity(any(UserRequest.class),any(User.class))).thenReturn(entity);
        when(repository.findById(anyString())).thenReturn(Mono.just(entity));
        when(repository.save(any(User.class))).thenReturn(Mono.just(entity));

        Mono<User> result = service.update("123",request);

        StepVerifier.create(result).expectNextMatches(user -> user.getClass() == User.class)
                .expectComplete()
                .verify();
        Mockito.verify(repository, times(1)).save(any(User.class));
    }

    }

