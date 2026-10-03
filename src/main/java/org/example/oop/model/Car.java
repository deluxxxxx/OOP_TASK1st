package org.example.oop.model;

import java.util.Objects;

/**
 * Автомобиль клиента.
 * Ключ равенства - VIN (Vehicle Identification Number).
 */
public class Car {

    private final String vin;
    private final String brand;
    private final String model;
    private final Client owner;

    /** Конструктор машины (создание) */
    public Car(String vin, String brand, String model, Client owner) {
        if (vin == null || vin.isBlank()) {
            throw new IllegalArgumentException("VIN не может быть пустым");
        }
        if (brand == null || brand.isBlank()) {
            throw new IllegalArgumentException("Марка не может быть пустой");
        }
        if (model == null || model.isBlank()) {
            throw new IllegalArgumentException("Модель не может быть пустой");
        }
        if (owner == null) {
            throw new IllegalArgumentException("Владелец не может отсутствовать");
        }
        this.vin = vin;
        this.brand = brand;
        this.model = model;
        this.owner = owner;
    }

    /** Геттер вин-номера машины */
    public String getVin() {
        return vin;
    }

    /** Геттер марки машины */
    public String getBrand() {
        return brand;
    }

    /** Геттер модели машины */
    public String getModel() {
        return model;
    }

    /** Геттер владельца машины */
    public Client getOwner() {
        return owner;
    }

    /**
     * Равенство по VIN - два автомобиля с одинаковым VIN считаются одним и тем же,
     * даже если марка, модель или владелец отличаются (например, сменился владелец).
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return Objects.equals(vin, car.vin);
    }
    /** Хеш-код по vin */
    @Override
    public int hashCode() {
        return Objects.hash(vin);
    }
    /** Строковое представление машины */
    @Override
    public String toString() {
        return brand + " " + model + " (VIN: " + vin + ")";
    }
}