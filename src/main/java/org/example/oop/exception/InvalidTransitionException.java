package org.example.oop.exception;

/**
 * Исключение, выбрасываемое при попытке недопустимого перехода
 * между статусами заявки (например, ACCEPTED -> REPAIR).
 */
public class InvalidTransitionException extends RuntimeException {

    public InvalidTransitionException(String message) {
        super(message);
    }
}