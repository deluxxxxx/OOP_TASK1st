package org.example.oop.service;

import org.example.oop.model.Car;
import org.example.oop.model.RepairRequest;
import org.example.oop.model.RequestStatus;
import org.example.oop.model.Work;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

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
     * Возвращает заявки, просроченные относительно обещанной даты выдачи.
     * Просроченная — обещанная дата уже прошла, заявка ещё не выдана и не отменена.
     * @param today текущая дата
     * @return список просроченных заявок
     */
    public List<RepairRequest> getOverdueRequests(LocalDate today) {
        if (today == null) {
            throw new IllegalArgumentException("Дата не может быть null");
        }
        List<RepairRequest> result = new ArrayList<>();
        for (RepairRequest r : requests) {
            boolean pastPromise = r.getPromisedDate().isBefore(today);
            boolean notIssued = r.getStatus() != RequestStatus.ISSUED;
            boolean notCancelled = r.getStatus() != RequestStatus.CANCELLED;
            if (pastPromise && notIssued && notCancelled) {
                result.add(r);
            }
        }
        return result;
    }

    /**
     * Считает средний фактический срок ремонта (в днях) по всем завершённым заявкам.
     * @return средний срок в днях или 0, если завершённых заявок нет
     */
    public double getAverageRepairDurationDays() {
        long sum = 0;
        int count = 0;
        for (RepairRequest r : requests) {
            long duration = r.getActualDurationDays();
            if (duration >= 0) {
                sum += duration;
                count++;
            }
        }
        if (count == 0) {
            return 0;
        }
        return (double) sum / count;
    }

    /**
     * Считает средний срок ремонта (в днях) по заявкам, где есть указанный вид работ.
     * Учитываются только завершённые заявки.
     * @param workName название работы
     * @return средний срок в днях или 0, если подходящих завершённых заявок нет
     */
    public double getAverageDurationByWork(String workName) {
        if (workName == null || workName.isBlank()) {
            throw new IllegalArgumentException("Название работы не может быть пустым");
        }
        long sum = 0;
        int count = 0;
        for (RepairRequest r : requests) {
            long duration = r.getActualDurationDays();
            if (duration < 0) {
                continue;
            }
            boolean hasWork = false;
            for (Work w : r.getWorks()) {
                if (w.name().equals(workName)) {
                    hasWork = true;
                    break;
                }
            }
            if (hasWork) {
                sum += duration;
                count++;
            }
        }
        if (count == 0) {
            return 0;
        }
        return (double) sum / count;
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
    /**
     * Возвращает средний срок ремонта (в днях) по каждому виду работ.
     * Ключ — название работы, значение — средний срок по заявкам, где эта работа встречается.
     * Учитываются только завершённые заявки.
     * @return Map: название работы → средний срок в днях
     */
    public Map<String, Double> getAverageDurationByAllWorks() {
        Map<String, Long> sums = new HashMap<>();
        Map<String, Integer> counts = new HashMap<>();

        for (RepairRequest r : requests) {
            long duration = r.getActualDurationDays();
            if (duration < 0) {
                continue;
            }
            for (Work w : r.getWorks()) {
                String name = w.name();
                sums.put(name, sums.getOrDefault(name, 0L) + duration);
                counts.put(name, counts.getOrDefault(name, 0) + 1);
            }
        }

        Map<String, Double> result = new HashMap<>();
        for (String name : sums.keySet()) {
            result.put(name, (double) sums.get(name) / counts.get(name));
        }
        return result;
    }
}