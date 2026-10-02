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

    public String getVin() {
        return vin;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

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

    @Override
    public int hashCode() {
        return Objects.hash(vin);
    }

    @Override
    public String toString() {
        return brand + " " + model + " (VIN: " + vin + ")";
    }
}