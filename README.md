# Travel Destinations REST API
Лабораторна робота: Основи побудови RESTful інтерфейсів для серверів застосунків

---

## Опис проєкту
REST API для управління подорожами, що дозволяє:
- Створювати та управляти профілями мандрівників
- Додавати напрямки подорожей з різними категоріями
- Планувати подорожі з декількома напрямками
- Фільтрувати дані за різними критеріями

### Entities:
**Traveler**
- ID
- Name
- Email
- Phone
- Registration Date
- Favorite Destination
- Travel Style(SOLO, COUPLE, FAMILY, GROUP)

**Destination**
- ID
- Country
- City
- Description
- Rating(1.0 - 5.0)
- Category(BEACH, MOUNTAINS, CITY, CULTURE, ADVENTURE, NATURE, HISTORIC)
- Season (SPRING,SUMMER,AUTUMN,WINTER)

**Trip**
- ID
- Name
- Start and End Dates
- Description
- Budget
- Status (PLANNED, IN_PROGRESS, COMPLETED, CANCELLED)
- Зв'язок з traveler та destinations.

### Relationships (Зв'язки)
Traveler (1) ──────< (Many) Trip  
         │  
         │ Many-to-Many  
         ▼  
        Destination (Many)  

- Traveler → Trip: One-to-Many (один мандрівник може мати багато подорожей)  
- Trip ↔ Destination: Many-to-Many (одна подорож може включати багато напрямків, і навпаки)

---

## Технології

- Java 21 
- Spring Boot 3.4.10
- Spring Data JPA 
-  Hibernate
- H2 Database 
- Maven 


## Тестування
Через Swagger UI:
1. Відкрий http://localhost:8080/swagger-ui.html  
2. Вибери Controller (наприклад, "Travelers")  
3. Клікни на endpoint (наприклад, GET /api/travelers)  
4. Натисни "Try it out"  
5. Заповни параметри  
6. Натисни "Execute"  
7. Переглянь Response  

---

## Тестові дані
При запуску автоматично завантажуються:
- 4 мандрівники (Олег, Іра, Тарас, Катя)  
- 8 напрямків (Львів, Одеса, Карпати, Париж, Барселона, Альпи, Токіо, Мальдіви)  
- 6 подорожей з різними статусами та напрямками  
