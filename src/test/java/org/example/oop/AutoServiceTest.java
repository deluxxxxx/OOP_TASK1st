package org.example.oop;

import org.example.oop.exception.InvalidTransitionException;
import org.example.oop.model.*;
import org.example.oop.service.AutoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class AutoServiceTest {

    private AutoService service;
    private Client client;
    private Car car;

    @BeforeEach
    void setUp() {
        service = new AutoService();
        client = new Client(1L, "Иван Иванов", "+7-900-000-00-00");
        car = new Car("VIN123456789", "Toyota", "Camry", client);
    }

    @Test
    void workCreatedWithValidData() {
        Work work = new Work("Замена масла", 1500, 1.0);
        assertEquals("Замена масла", work.name());
        assertEquals(1500, work.price());
    }

    @Test
    void workThrowsOnNegativePrice() {
        assertThrows(IllegalArgumentException.class, () ->
                new Work("Замена", -100, 1.0)
        );
    }

    @Test
    void workThrowsOnBlankName() {
        assertThrows(IllegalArgumentException.class, () ->
                new Work("   ", 100, 1.0)
        );
    }

    @Test
    void clientEqualityById() {
        Client a = new Client(1L, "Иван", "+7-111");
        Client b = new Client(1L, "Пётр", "+7-222");
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void clientNotEqualWithDifferentId() {
        Client a = new Client(1L, "Иван", "+7-111");
        Client b = new Client(2L, "Иван", "+7-111");
        assertNotEquals(a, b);
    }

    @Test
    void carEqualityByVin() {
        Car a = new Car("VIN-XYZ", "Toyota", "Camry", client);
        Car b = new Car("VIN-XYZ", "Kia", "Rio", client);
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void carNotEqualWithDifferentVin() {
        Car a = new Car("VIN-1", "Toyota", "Camry", client);
        Car b = new Car("VIN-2", "Toyota", "Camry", client);
        assertNotEquals(a, b);
    }

    @Test
    void registerCarAddsCar() {
        service.registerCar(car);
        assertEquals(1, service.getAllCars().size());
    }

    @Test
    void registerCarDoesNotAddDuplicateVin() {
        Car sameVin = new Car("VIN123456789", "Другая", "Модель", client);
        service.registerCar(car);
        service.registerCar(sameVin);
        assertEquals(1, service.getAllCars().size());
    }

    @Test
    void registerCarThrowsOnNull() {
        assertThrows(IllegalArgumentException.class, () ->
                service.registerCar(null)
        );
    }

    @Test
    void registerRequestCreatesRequest() {
        RepairRequest r = service.registerRequest(100L, car, LocalDate.now().plusDays(3));
        assertNotNull(r);
        assertEquals(RequestStatus.ACCEPTED, r.getStatus());
    }

    @Test
    void registerRequestThrowsOnDuplicateId() {
        service.registerRequest(100L, car, LocalDate.now().plusDays(3));
        assertThrows(IllegalArgumentException.class, () ->
                service.registerRequest(100L, car, LocalDate.now().plusDays(3))
        );
    }

    @Test
    void findCarByVinReturnsCar() {
        service.registerCar(car);
        Car found = service.findCarByVin("VIN123456789");
        assertNotNull(found);
        assertEquals(car, found);
    }

    @Test
    void findCarByVinReturnsNullIfNotFound() {
        service.registerCar(car);
        assertNull(service.findCarByVin("НЕТ"));
    }

    @Test
    void findRequestByIdReturnsNullIfNotFound() {
        assertNull(service.findRequestById(999L));
    }

    @Test
    void calculateTotalCostSumsWorks() {
        RepairRequest r = service.registerRequest(100L, car, LocalDate.now().plusDays(3));
        r.addWork(new Work("Работа 1", 1000, 1.0));
        r.addWork(new Work("Работа 2", 500, 0.5));
        assertEquals(1500, r.calculateTotalCost());
    }

    @Test
    void addWorkThrowsWhenStatusIsRepair() {
        RepairRequest r = service.registerRequest(100L, car, LocalDate.now().plusDays(3));
        r.transitionTo(RequestStatus.DIAGNOSTICS);
        r.transitionTo(RequestStatus.APPROVAL);
        r.transitionTo(RequestStatus.REPAIR);
        assertThrows(IllegalStateException.class, () ->
                r.addWork(new Work("Поздняя", 100, 1.0))
        );
    }

    @Test
    void validTransitionWorks() {
        RepairRequest r = service.registerRequest(100L, car, LocalDate.now().plusDays(3));
        r.transitionTo(RequestStatus.DIAGNOSTICS);
        assertEquals(RequestStatus.DIAGNOSTICS, r.getStatus());
    }

    @Test
    void invalidTransitionThrows() {
        RepairRequest r = service.registerRequest(100L, car, LocalDate.now().plusDays(3));
        assertThrows(InvalidTransitionException.class, () ->
                r.transitionTo(RequestStatus.REPAIR)
        );
    }

    @Test
    void issuedStatusSetsActualDate() {
        RepairRequest r = service.registerRequest(100L, car, LocalDate.now().plusDays(3));
        r.transitionTo(RequestStatus.DIAGNOSTICS);
        r.transitionTo(RequestStatus.APPROVAL);
        r.transitionTo(RequestStatus.REPAIR);
        r.transitionTo(RequestStatus.READY);
        r.transitionTo(RequestStatus.ISSUED);
        assertNotNull(r.getActualIssueDate());
    }

    @Test
    void getWorksReturnsUnmodifiableList() {
        RepairRequest r = service.registerRequest(100L, car, LocalDate.now().plusDays(3));
        r.addWork(new Work("Работа", 500, 1.0));
        assertThrows(UnsupportedOperationException.class, () ->
                r.getWorks().add(new Work("Взлом", 0, 0))
        );
    }

    @Test
    void getAllRequestsReturnsCopy() {
        service.registerRequest(100L, car, LocalDate.now().plusDays(3));
        assertEquals(1, service.getAllRequests().size());
        service.getAllRequests().clear();
        assertEquals(1, service.getAllRequests().size());
    }
}