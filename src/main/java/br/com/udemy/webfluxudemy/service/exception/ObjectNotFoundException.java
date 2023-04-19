package br.com.udemy.webfluxudemy.service.exception;

public class ObjectNotFoundException extends RuntimeException {
    public ObjectNotFoundException(String message) {
        super(message);
    }
}
