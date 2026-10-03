package org.example.oop.model;

import java.util.Objects;

/**
 * Клиент автосервиса
 * Ключ равенства - id
 */
public class Client implements Identifiable {

    private final Long id;
    private final String fullName;
    private final String phone;

    /**
     * Создание клиента(конструктор)
     */
    public Client(Long id, String fullName, String phone) {
        if (id == null) {
            throw new IllegalArgumentException("ID клиента не может быть пустым");
        }
        if (fullName == null || fullName.isBlank()) {
            throw new IllegalArgumentException("ФИО клиента не может быть пустым");
        }
        if (phone == null || phone.isBlank()) {
            throw new IllegalArgumentException("Телефон клиента не может быть пустым");
        }
        this.id = id;
        this.fullName = fullName;
        this.phone = phone;
    }

    /**
     * Геттер id клиента
     */
    public Long getId() {
        return id;
    }

    /**
     * Геттер полного имени (ФИО) клиента
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * Геттер номера телефона клиента
     */
    public String getPhone() {
        return phone;
    }

    /**
     *
     * Равенство по id (два клиента считаются одним и тем же клиентом, если у них одно id,
     * даже если ФИО или номер отличаются(например, если сменился номер))
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return Objects.equals(id, client.id);
    }

    /**
     * Хеш-код на основе id
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /**
     * Строковое представление клиента
     */
    @Override
    public String toString() {
        return "Client{id=" + id + ", name='" + fullName + "', phone='" + phone + "'}";
    }
}