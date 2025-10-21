### Лабораторна робота 3. Взаємодія додатків засобами черг повідомлень.

## Опис проекту

Spring Boot додаток для управління туристичними напрямками (Travel Destinations) з інтеграцією RabbitMQ для асинхронної обробки подій.РДодаток реалізує шаблон Publisher–Subscriber через чергу повідомлень(JSON format).

##  Архітектура

```
REST API → Service Layer → Database (H2)
              ↓
         MessageProducer → RabbitMQ → MessageConsumer
```

##  Структура проекту

```
src/main/java/com/example/travel_destinations/
├── config/
│   ├── DataLoader.java           # Початкове завантаження даних
│   └── RabbitConfig.java         # Конфігурація RabbitMQ
│
├── controller/
│   ├── DestinationController.java  # REST API для напрямків (відредаговано під 3 лабу)
│   ├── TravelerController.java     # REST API для мандрівників
│   └── TripController.java         # REST API для поїздок
│
├── entity/
│   ├── Destination.java            # Сутність напрямку
│   ├── Message.java                # Модель повідомлення RabbitMQ
│   ├── Traveler.java               # Сутність мандрівника
│   └── Trip.java                   # Сутність поїздки
│
├── repository/                     # JPA репозиторії
│
├── service/
│   ├── DestinationService.java     # Бізнес-логіка напрямків
│   ├── MessageConsumer.java        # RabbitMQ Consumer
│   └── MessageProducer.java        # RabbitMQ Producer
│
└── TravelDestinationsApplication.java
```

##  Встановлення та запуск

### 1. Запуск RabbitMQ через Docker

```
docker run -d --hostname my-rabbit --name some-rabbit \
  -p 15672:15672 -p 5672:5672 rabbitmq:3-management
```

**Перевірка:**
```
docker ps
```

### 2. Запуск Spring Boot додатку

```bash
mvn spring-boot:run
```



### 3. Доступ до інтерфейсів

- **REST API:** http://localhost:8080/api/destinations
- **RabbitMQ Management:** http://localhost:15672 (guest/guest)

##  RabbitMQ конфігурація

```
Exchange:     travel.exchange (Topic)
Queue:        travel.queue
Routing Key:  travel.routingkey
Message Format: JSON
```

##  Приклад Тестування (Postman)

### 1. CREATE Destination (з RabbitMQ)

**Запит:**
```
POST http://localhost:8080/api/destinations
Content-Type: application/json

{
  "country": "Ukraine",
  "city": "Kyiv",
  "description": "Capital of Ukraine",
  "rating": 4.8,
  "category": "CULTURAL",
  "bestSeason": "SUMMER"
}
```

**Очікуваний результат в консолі:**
```
Service: Створюємо новий напрямок: Capital of Ukraine
Producer: Відправляємо повідомлення до RabbitMQ
Producer: Повідомлення відправлено!
Consumer: Отримано повідомлення від RabbitMQ
Action: CREATE - Новий напрямок додано: Capital of Ukraine
Consumer: Повідомлення оброблено
```



## Технології

- **Spring Boot** 
- **Spring AMQP** (RabbitMQ)
- **H2 Database**
- **Spring Data JPA**
- **Swagger/OpenAPI**
- **Docker** (для RabbitMQ)

##  Dependencies

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-amqp</artifactId>
</dependency>
```
