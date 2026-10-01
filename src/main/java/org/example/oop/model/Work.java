package org.example.oop.model;

public record Work(String name, double price, double standardHours) {

    public Work {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Имя работы не может быть пустым");
        }
        if (price < 0) {
            throw new IllegalArgumentException("Цена не может быть отрицательной");
        }
        if (standardHours < 0) {
            throw new IllegalArgumentException("Нормо-часы не могут быть отрицательными");
        }
    }
}
