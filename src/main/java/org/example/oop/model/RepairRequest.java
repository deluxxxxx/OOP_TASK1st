package org.example.oop.model;

import org.example.oop.exception.InvalidTransitionException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RepairRequest implements Identifiable {

    private final Long id;
    private final Car car;
    private final LocalDate createdDate;
    private final LocalDate promisedDate;
    private RequestStatus status;
    private LocalDate actualIssueDate;
    private final List<Work> works = new ArrayList<>();

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

    public Long getId() { return id; }
    public Car getCar() { return car; }
    public LocalDate getCreatedDate() { return createdDate; }
    public LocalDate getPromisedDate() { return promisedDate; }
    public LocalDate getActualIssueDate() { return actualIssueDate; }
    public RequestStatus getStatus() { return status; }

    /**
     * Возвращает неизменяемую копию списка работ.
     */
    public List<Work> getWorks() {
        return Collections.unmodifiableList(works);
    }
    /**
     * Добавляет работу в заявку.
     * Разрешено только в статусах ACCEPTED или DIAGNOSTICS.
     *
     * @param work работа
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
     *
     * @param newStatus целевой статус
     * @throws InvalidTransitionException если переход недопустим
     */
    public void transitionTo(RequestStatus newStatus) {
        if (!this.status.canTransitionTo(newStatus)) {
            throw new InvalidTransitionException("Недопустимый переход: " + this.status.getDescription() + " -> " + newStatus.getDescription());
        }
        this.status = newStatus;
        if (newStatus == RequestStatus.ISSUED) {
            this.actualIssueDate = LocalDate.now();
        }
    }
    /**
     * Считает полную стоимость всех работ в заявке.
     * @return суммарная стоимость в рублях
     */
    public double calculateTotalCost() {
        return works.stream().mapToDouble(Work::price).sum();
    }

    @Override
    public String toString() {
        return "Заявка#" + id + " [" + status.getDescription() + "] " + car
                + ", работ: " + works.size()
                + ", стоимость: " + calculateTotalCost() + " руб.";
    }
}