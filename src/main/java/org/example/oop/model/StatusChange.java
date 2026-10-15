package org.example.oop.model;

import java.time.LocalDateTime;

/**
 * Одна запись в журнале переходов заявки.
 * Фиксирует, из какого статуса в какой перешла заявка и когда.
 * @param from      статус, из которого перешли
 * @param to        статус, в который перешли
 * @param timestamp дата и время перехода
 */
public record StatusChange(RequestStatus from, RequestStatus to, LocalDateTime timestamp) {
}