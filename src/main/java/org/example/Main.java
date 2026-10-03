package org.example;

import org.example.oop.model.*;
import org.example.oop.service.AutoService;

import java.time.LocalDate;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        AutoService service = new AutoService();

        System.out.println("1. Создание клиентов");
        Client[] clients = new Client[10];
        for (int i = 0; i < 10; i++) {
            clients[i] = new Client(
                    (long) (i + 1),
                    "Клиент №" + (i + 1),
                    "+7-900-000-00-" + i
            );
            System.out.println("  " + clients[i]);
        }

        System.out.println("\n2. Создание и регистрация автомобилей");
        Car[] cars = new Car[10];
        for (int i = 0; i < 10; i++) {
            cars[i] = new Car(
                    "VIN-" + (i + 1),
                    "Марка" + (i + 1),
                    "Модель" + (i + 1),
                    clients[i]
            );
            service.registerCar(cars[i]);
            System.out.println("  Зарегистрирована: " + cars[i]);
        }
        System.out.println("Всего машин: " + service.getAllCars().size());

        System.out.println("\n3. Создание заявок");
        RepairRequest[] requests = new RepairRequest[10];
        for (int i = 0; i < 10; i++) {
            requests[i] = service.registerRequest(
                    (long) (100 + i),
                    cars[i],
                    LocalDate.now().plusDays(3)
            );
            System.out.println("  " + requests[i]);
        }

        System.out.println("\n4. Добавление работ в заявки");
        for (int i = 0; i < 10; i++) {
            requests[i].addWork(new Work("Диагностика", 500, 0.5));
            requests[i].addWork(new Work("Замена масла", 1500, 1.0));
            if (i % 2 == 0) {
                requests[i].addWork(new Work("Замена колодок", 3000, 1.5));
            }
        }

        System.out.println("\n5. Стоимость заявок");
        for (RepairRequest r : requests) {
            System.out.printf("  Заявка#%d: работ %d, итого %.2f руб.%n",
                    r.getId(), r.getWorks().size(), r.calculateTotalCost());
        }

        System.out.println("\n6. Цикл заявок");

        for (int i = 0; i < 3; i++) {
            requests[i].transitionTo(RequestStatus.DIAGNOSTICS);
            requests[i].transitionTo(RequestStatus.APPROVAL);
            requests[i].transitionTo(RequestStatus.REPAIR);
            requests[i].transitionTo(RequestStatus.READY);
            requests[i].transitionTo(RequestStatus.ISSUED);
            System.out.println("  Заявка#" + requests[i].getId() + " → " + requests[i].getStatus());
        }

        for (int i = 3; i < 6; i++) {
            requests[i].transitionTo(RequestStatus.DIAGNOSTICS);
            requests[i].transitionTo(RequestStatus.APPROVAL);
            requests[i].transitionTo(RequestStatus.REPAIR);
            System.out.println("  Заявка#" + requests[i].getId() + " → " + requests[i].getStatus());
        }

        requests[6].transitionTo(RequestStatus.CANCELLED);
        System.out.println("  Заявка#" + requests[6].getId() + " → " + requests[6].getStatus());

        System.out.println("\n7. Поиск машины по VIN");
        Car found = service.findCarByVin("VIN0000000000005");
        System.out.println("  Найдена: " + found);

        Car notFound = service.findCarByVin("XXXX-NOPE");
        System.out.println("  Ненайденная: " + notFound);

        System.out.println("\n8. Активные заявки на сегодня");
        List<RepairRequest> active = service.getActiveRequestsOn(LocalDate.now());
        System.out.println("  Активных: " + active.size());
        for (RepairRequest r : active) {
            System.out.println("    " + r);
        }

        System.out.println("\n9. Итоги");
        System.out.println("  Клиентов: " + clients.length);
        System.out.println("  Машин: " + service.getAllCars().size());
        System.out.println("  Заявок: " + service.getAllRequests().size());
        System.out.println("  Дата выдачи заявки#100: " + requests[0].getActualIssueDate());
        System.out.println("  Общая стоимость всех заявок: "
                + service.getAllRequests().stream()
                .mapToDouble(RepairRequest::calculateTotalCost)
                .sum() + " руб.");
    }
}