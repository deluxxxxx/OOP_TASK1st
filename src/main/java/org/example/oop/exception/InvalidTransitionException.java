package org.example.oop.exception;

/**
 * Исключение, выбрасываемое при попытке недопустимого перехода
 * между статусами заявки (например, ACCEPTED → REPAIR).
 */
public class InvalidTransitionException extends RuntimeException {

    /**
     * Создаёт исключение с указанным сообщением.
     * @param message описание причины ошибки
     */
    public InvalidTransitionException(String message) {
        super(message);
    }
}