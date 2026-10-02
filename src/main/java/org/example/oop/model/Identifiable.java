package org.example.oop.model;

/**
 * Интерфейс для всех сущностей, имеющих id.
 */
public interface Identifiable {
    /**
     * @return уникальный идентификатор сущности
     */
    Long getId();
}