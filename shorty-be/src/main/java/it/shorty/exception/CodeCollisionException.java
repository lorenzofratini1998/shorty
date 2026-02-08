package it.shorty.exception;

public class CodeCollisionException extends RuntimeException {
    public CodeCollisionException(String message) {
        super(message);
    }
}
