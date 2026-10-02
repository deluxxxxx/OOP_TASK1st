package org.example.oop.model;

import java.util.Set;

/**
 * Жизненный цикл заявки:
 * ACCEPTED -> DIAGNOSTICS -> APPROVAL -> REPAIR -> READY -> ISSUED
 * Из любого промежуточного статуса (кроме ISSUED) возможна отмена в CANCELLED
 * Статусы ISSUED и CANCELLED - конечные, из них переход невозможен.
 */
public enum RequestStatus {

    ACCEPTED("Принята"),
    DIAGNOSTICS("Диагностика"),
    APPROVAL("Согласование"),
    REPAIR("Ремонт"),
    READY("Готова"),
    ISSUED("Выдана"),
    CANCELLED("Отменена");

    private final String description;

    RequestStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Проверяет, допустим ли переход из текущего статуса в целевой.
     *
     * @param target целевой статус
     * @return {@code true}, если переход разрешён, иначе {@code false}
     */
    public boolean canTransitionTo(RequestStatus target) {
        if (target == null) {
            return false;
        }
        return switch (this) {
            case ACCEPTED    -> Set.of(DIAGNOSTICS, CANCELLED).contains(target);
            case DIAGNOSTICS -> Set.of(APPROVAL, CANCELLED).contains(target);
            case APPROVAL    -> Set.of(REPAIR, CANCELLED).contains(target);
            case REPAIR      -> Set.of(READY, CANCELLED).contains(target);
            case READY       -> Set.of(ISSUED, CANCELLED).contains(target);
            case ISSUED, CANCELLED -> false;
        };
    }

    @Override
    public String toString() {
        return description;
    }
}