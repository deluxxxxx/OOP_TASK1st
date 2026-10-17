package org.example.oop.model;

import org.example.oop.exception.InvalidTransitionException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Заявка на ремонт автомобиля.
 * Управляет жизненным циклом через {@link RequestStatus} и хранит список работ.
 * Ведёт журнал переходов между статусами.
 */
public class RepairRequest implements Identifiable {

    private final Long id;
    private final Car car;
    private final LocalDate createdDate;
    private final LocalDate promisedDate;
    private RequestStatus status;
    private LocalDate actualIssueDate;
    private final List<Work> works = new ArrayList<>();
    private final List<StatusChange> history = new ArrayList<>();

    /**
     * Создаёт новую заявку в статусе {@link RequestStatus#ACCEPTED}.
     *
     * @param id           уникальный идентификатор заявки
     * @param car          автомобиль клиента
     * @param promisedDate обещанная дата выдачи
     */
    public RepairRequest(Long id, Car car, LocalDate promisedDate) {
        if (id == null) {
            throw new IllegalArgumentException("ID заявки не может отсутствовать");
        }
        if (car == null) {
            throw new IllegalArgumentException("Автомобиль не может отсутствовать");
        }
        if (promisedDate == null) {
            throw new IllegalArgumentException("Обещанная дата не может отсутствовать");
        }
        this.id = id;
        this.car = car;
        this.promisedDate = promisedDate;
        this.createdDate = LocalDate.now();
        this.status = RequestStatus.ACCEPTED;
    }

    /** Идентификатор заявки */
    public Long getId() { return id; }

    /** Автомобиль */
    public Car getCar() { return car; }

    /** Дата создания заявки */
    public LocalDate getCreatedDate() { return createdDate; }

    /** Обещанная дата выдачи */
    public LocalDate getPromisedDate() { return promisedDate; }

    /** Фактическая дата выдачи или {@code null}, если ещё не выдана */
    public LocalDate getActualIssueDate() { return actualIssueDate; }

    /** Текущий статус заявки */
    public RequestStatus getStatus() { return status; }

    /**
     * Неизменяемая копия списка работ
     */
    public List<Work> getWorks() {
        return Collections.unmodifiableList(works);
    }

    /**
     * Неизменяемая копия журнала переходов
     */
    public List<StatusChange> getHistory() {
        return Collections.unmodifiableList(history);
    }

    /**
     * Добавляет работу в заявку.
     * Разрешено только в статусах ACCEPTED или DIAGNOSTICS.

     */
    public void addWork(Work work) {
        if (work == null) {
            throw new IllegalArgumentException("Работа не может быть null");
        }
        if (status != RequestStatus.ACCEPTED && status != RequestStatus.DIAGNOSTICS) {
            throw new IllegalStateException(
                    "Нельзя добавить работу в статусе: " + status.getDescription()
            );
        }
        works.add(work);
    }

    /**
     * Переводит заявку в новый статус.
     * Каждый переход фиксируется в журнале с текущим временем.
     * newStatus целевой статус
     * InvalidTransitionException если переход недопустим
     */
    public void transitionTo(RequestStatus newStatus) {
        if (!this.status.canTransitionTo(newStatus)) {
            throw new InvalidTransitionException(
                    "Недопустимый переход: " + this.status.getDescription()
                            + " -> " + newStatus.getDescription()
            );
        }
        history.add(new StatusChange(this.status, newStatus, LocalDateTime.now()));
        this.status = newStatus;
        if (newStatus == RequestStatus.ISSUED) {
            this.actualIssueDate = LocalDate.now();
        }
    }

    /**
     * Считает полную стоимость всех работ в заявке.
     * Суммарная стоимость в рублях
     */
    public double calculateTotalCost() {
        return works.stream()
                .mapToDouble(Work::price)
                .sum();
    }

    /**
     * Фактический срок ремонта в днях — от создания до выдачи.
     * Если заявка ещё не выдана — возвращает -1.
     *
     * Количество дней ремонта или -1, если заявка не завершена
     */
    public long getActualDurationDays() {
        if (actualIssueDate == null) {
            return -1;
        }
        return ChronoUnit.DAYS.between(createdDate, actualIssueDate);
    }

    @Override
    public String toString() {
        return "Заявка#" + id + " [" + status.getDescription() + "] " + car
                + ", работ: " + works.size()
                + ", стоимость: " + calculateTotalCost() + " руб.";
    }
}