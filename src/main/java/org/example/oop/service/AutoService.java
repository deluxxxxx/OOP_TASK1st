package org.example.oop.service;

import org.example.oop.model.Car;
import org.example.oop.model.RepairRequest;
import org.example.oop.model.RequestStatus;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Сервис автосервиса. Хранит заявки и автомобили в памяти.
 */
public class AutoService {

    private final List<RepairRequest> requests = new ArrayList<>();
    private final List<Car> cars = new ArrayList<>();

    /**
     * Добавляет автомобиль. Если с таким VIN уже есть — ничего не делает.
     */
    public void registerCar(Car car) {
        if (car == null) {
            throw new IllegalArgumentException("Автомобиль не может быть null");
        }
        for (Car c : cars) {
            if (c.getVin().equals(car.getVin())) {
                return;
            }
        }
        cars.add(car);
    }

    /**
     * Создаёт новую заявку на ремонт.
     * @param id           идентификатор заявки
     * @param car          автомобиль
     * @param promisedDate обещанная дата выдачи
     * @return созданная заявка
     */
    public RepairRequest registerRequest(Long id, Car car, LocalDate promisedDate) {
        if (car == null) {
            throw new IllegalArgumentException("Автомобиль не может быть null");
        }
        registerCar(car);

        for (RepairRequest r : requests) {
            if (r.getId().equals(id)) {
                throw new IllegalArgumentException("Заявка с id=" + id + " уже существует");
            }
        }

        RepairRequest request = new RepairRequest(id, car, promisedDate);
        requests.add(request);
        return request;
    }

    /**
     * Ищет автомобиль по VIN.
     * @return найденный автомобиль или null
     */
    public Car findCarByVin(String vin) {
        if (vin == null || vin.isBlank()) {
            return null;
        }
        for (Car c : cars) {
            if (c.getVin().equals(vin)) {
                return c;
            }
        }
        return null;
    }

    /**
     * Ищет заявку по id.
     * @return найденная заявка или null
     */
    public RepairRequest findRequestById(Long id) {
        for (RepairRequest r : requests) {
            if (r.getId().equals(id)) {
                return r;
            }
        }
        return null;
    }

    /**
     * Возвращает заявки, находящиеся в работе на указанную дату.
     * В работе — созданы до даты и не выданы/отменены.
     */
    public List<RepairRequest> getActiveRequestsOn(LocalDate date) {
        List<RepairRequest> result = new ArrayList<>();
        for (RepairRequest r : requests) {
            boolean createdBefore = !r.getCreatedDate().isAfter(date);
            boolean notIssued = r.getStatus() != RequestStatus.ISSUED;
            boolean notCancelled = r.getStatus() != RequestStatus.CANCELLED;
            if (createdBefore && notIssued && notCancelled) {
                result.add(r);
            }
        }
        return result;
    }

    /**
     * Возвращает копию списка всех заявок.
     */
    public List<RepairRequest> getAllRequests() {
        return new ArrayList<>(requests);
    }

    /**
     * Возвращает копию списка всех автомобилей.
     */
    public List<Car> getAllCars() {
        return new ArrayList<>(cars);
    }
}