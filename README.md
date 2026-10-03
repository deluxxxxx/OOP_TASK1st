# Автосервис

Учебный проект по ООП на Java 21: модель автосервиса с заявками на ремонт.

## Структура проекта

```
src/main/java/org/example/
    Main.java                        Демонстрация работы модели
    oop/
        exception/
            InvalidTransitionException.java     Кастомное исключение
        model/
            Car.java                 Автомобиль (ключ равенства — VIN)
            Client.java              Клиент (ключ равенства — id)
            Identifiable.java        Интерфейс для сущностей с id
            RepairRequest.java       Заявка на ремонт
            RequestStatus.java       Статус заявки
            Work.java                Работа
        service/
            AutoService.java         Сервис: регистрация, поиск, отчёты

    src/test/java/org/example/oop/
        AutoServiceTest.java             22 JUnit-теста
```
## Модель

- **Client** - клиент автосервиса, ключ равенства - id.
- **Car** - автомобиль, ключ равенства - VIN.
- **Work** - работа: название, цена, нормо-часы.
- **RequestStatus** - статусы заявки с логикой переходов.
- **RepairRequest** - заявка на ремонт, управляет жизненным циклом.
- **InvalidTransitionException** - кастомное исключение для недопустимых переходов.
- **Identifiable** - интерфейс для сущностей с id.
- **AutoService** - сервис: регистрация, поиск, отчёты.

## Жизненный цикл заявки

```
ACCEPTED -> DIAGNOSTICS -> APPROVAL -> REPAIR -> READY -> ISSUED
```
Из любого промежуточного статуса возможна отмена в `CANCELLED`(соответственно, кроме ISSUED).

## Запуск

- **Сборка:** `mvn clean package`
- **Тесты:** `mvn test`
- **Демонстрация:** запустить `Main` в IDE

## Стек

Java 21, Maven, JUnit 5.