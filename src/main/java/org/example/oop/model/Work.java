package org.example.oop.model;

/**
 * Работа, которую необходимо выполнить в рамках заявки на ремонт
 * @param name   Название работы(например, балансировка колес, замена масла и т.п.)
 * @param price  Стоимость работы в рублях
 * @param standardHours  Время, которое по нормативу должна занимать работа
 */
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
