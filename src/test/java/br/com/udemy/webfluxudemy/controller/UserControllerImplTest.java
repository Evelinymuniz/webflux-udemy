package br.com.udemy.webfluxudemy.controller;

import br.com.udemy.webfluxudemy.entity.User;
import br.com.udemy.webfluxudemy.mapper.UserMapper;
import br.com.udemy.webfluxudemy.model.request.UserRequest;
import br.com.udemy.webfluxudemy.model.response.UserResponse;
import br.com.udemy.webfluxudemy.service.UserService;
import com.mongodb.reactivestreams.client.MongoClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureWebTestClient // fazer teste de integracao batendo no endpoint
class UserControllerImplTest {
    public static final String ID = "123456";
    public static final String NAME = "Eveliny";
    public static final String EMAIL = "eveliny01@gmail.com";
    public static final String PASSWORD = "123";
    @Autowired
    private WebTestClient webTestClient;
    @MockBean
    private UserService service;
    @MockBean
    private UserMapper mapper;
    @MockBean
    private MongoClient mongoClient; // conexao
    @Test
    @DisplayName("Test endpoint save with success") //
    void testSaveWithSuccess() {

        UserRequest request = new UserRequest(NAME, EMAIL,PASSWORD);
        Mockito.when(service.save(any(UserRequest.class))).thenReturn(Mono.just(User.builder().build()));

        webTestClient.post().uri("/users")
                .contentType(APPLICATION_JSON)
                .body(BodyInserters.fromValue(request))
                .exchange().expectStatus().isCreated();
        verify(service, times(1)).save(any(UserRequest.class));
    }
    @Test
    @DisplayName("Test endpoint save with bad request")
    void testSaveWithBadRequest() {

        UserRequest request = new UserRequest(NAME.concat(" "), EMAIL,PASSWORD);

        webTestClient.post().uri("/users")
                .contentType(APPLICATION_JSON)
                .body(BodyInserters.fromValue(request))
                .exchange().expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.path").isEqualTo("/users")
                .jsonPath("$.status").isEqualTo(HttpStatus.BAD_REQUEST.value())
                .jsonPath("$.error").isEqualTo("validation error")
                .jsonPath("$.message").isEqualTo(" Error on validation attributes")
                .jsonPath("$.errors[0].fieldName").isEqualTo("name")
                .jsonPath("$.errors[0].message").isEqualTo("field connot have blank spaces at the beginning or at end");


    }

    @Test
    @DisplayName("Test find by id endpoint with success")
    void testfindByIdWithSuccess() {

        final var userResponse = new UserResponse(ID, NAME, EMAIL, PASSWORD);

        when(service.findById(anyString())).thenReturn(Mono.just(User.builder().build()));
        when(mapper.toResponse(any(User.class))).thenReturn(userResponse);

        webTestClient.get().uri("/users/"+ "123456")
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(ID)
                .jsonPath("$.name").isEqualTo(NAME)
                .jsonPath("$.email").isEqualTo(EMAIL)
                .jsonPath("$.password").isEqualTo(PASSWORD);

        verify(service).findById(anyString());
        verify(mapper).toResponse(any(User.class));
    }


    @Test
    @DisplayName("Test endpoint findAll with with success")
    void testFindAllWithSuccess() {

        final var userResponse = new UserResponse(ID, NAME, EMAIL, PASSWORD);

        when(service.findAll()).thenReturn(Flux.just(User.builder().build()));
        when(mapper.toResponse(any(User.class))).thenReturn(userResponse);

        webTestClient.get().uri("/users")
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.[0].id").isEqualTo(ID)
                .jsonPath("$.[0].name").isEqualTo(NAME)
                .jsonPath("$.[0].email").isEqualTo(EMAIL)
                .jsonPath("$.[0].password").isEqualTo(PASSWORD);

        verify(service).findAll();
        verify(mapper).toResponse(any(User.class));
    }

    @Test
    @DisplayName("Test endpoint update with with success")
    void testUpdateWithSuccess() {

        final var userResponse = new UserResponse(ID, NAME, EMAIL, PASSWORD);
        UserRequest request = new UserRequest(NAME, EMAIL,PASSWORD);

        when(service.update(anyString(), any(UserRequest.class)))
                .thenReturn(Mono.just(User.builder().build()));
        when(mapper.toResponse(any(User.class))).thenReturn(userResponse);

        webTestClient.patch().uri("/users/" + ID)
                .contentType(APPLICATION_JSON)
                .body(BodyInserters.fromValue(request))
                .exchange().expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(ID)
                .jsonPath("$.name").isEqualTo(NAME)
                .jsonPath("$.email").isEqualTo(EMAIL)
                .jsonPath("$.password").isEqualTo(PASSWORD);

        verify(service).update(anyString(),any(UserRequest.class));
        verify(mapper).toResponse(any(User.class));
    }

    @Test
    @DisplayName("Test endpoint delete with with success")
    void testDeleteWithSuccess() {

        when(service.delete(anyString()))
                .thenReturn(Mono.just(User.builder().build()));

        webTestClient.delete().uri("/users/" + ID)
                    .exchange().expectStatus().isOk();

        verify(service).delete(anyString());

    }
}