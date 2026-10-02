package org.example.oop.model;

import java.time.LocalDate;
import java.util.ArrayList;
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
}
