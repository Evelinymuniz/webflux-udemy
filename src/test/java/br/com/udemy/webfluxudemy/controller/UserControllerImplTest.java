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
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureWebTestClient // fazer teste de integracao batendo no endpoint
class UserControllerImplTest {
    @Autowired
    private WebTestClient webTestClient;
    @MockBean
    private UserService service;
    @MockBean
    private UserMapper mapper;
    @MockBean
    private MongoClient mongoClient;// conexao
    @Test
    @DisplayName("Test endpoint save with success") //
    void testSaveWithSuccess() {

        UserRequest request = new UserRequest("Eveliny", "eveliny01@gmail.com","123");
        Mockito.when(service.save(any(UserRequest.class))).thenReturn(Mono.just(User.builder().build()));

        webTestClient.post().uri("/users")
                .contentType(APPLICATION_JSON)
                .body(BodyInserters.fromValue(request))
                .exchange().expectStatus().isCreated();
        Mockito.verify(service, times(1)).save(any(UserRequest.class));
    }
    @Test
    @DisplayName("Test endpoint save with bad request")
    void testSaveWithBadRequest() {

        UserRequest request = new UserRequest(" Eveliny", "eveliny01@gmail.com","123");

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
        final var id = "123456";
        final var userResponse = new UserResponse(id,"Eveliny", "eveliny01@gmail.com","123");

        when(service.findById(anyString())).thenReturn(Mono.just(User.builder().build()));
        when(mapper.toResponse(any(User.class))).thenReturn(userResponse);

        webTestClient.get().uri("/users/"+ id)
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(id)
                .jsonPath("$.name").isEqualTo("Eveliny")
                .jsonPath("$.email").isEqualTo("eveliny01@gmail.com")
                .jsonPath("$.password").isEqualTo("123");


    }

    @Test
    void findall() {
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }
}