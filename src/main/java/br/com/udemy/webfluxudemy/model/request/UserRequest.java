package br.com.udemy.webfluxudemy.model.request;

public record UserRequest(
        String name,
        String email,
        String password
) {
}
