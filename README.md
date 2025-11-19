# Лабораторна робота 4. Аутентифікація за допомогою JWT.


##  Структура проєкту

```
src/main/java/com/example/travel_destinations/
├── security/
│   ├── config/
│   │   └── SecurityConfig.java              # Конфігурація Spring Security
│   ├── util/
│   │   └── JwtUtil.java                     # Утиліта для роботи з JWT
│   ├── JwtAuthenticationFilter.java         # Фільтр для перевірки токенів
|   ├── AuthController.java                  # Контролер аутентифікації
│   └── CustomUserDetailsService.java        # Сервіс користувачів (hardcoded)
│   ├── AuthRequest.java                     # для запиту
│   └── AuthResponse.java                    # для відповіді
└── ...
├── controller/
│   ├── DestinationController.java           # Контролер дестинацій (оновлено)
│   ├── TripController.java                  # Контролер поїздок (оновлено)
│   └── TravelerController.java              # Контролер мандрівників (оновлено)
└── 
```

---

## Тестові користувачі


| Username | Password    | Role         | Права доступу |
|----------|-------------|--------------|---------------|
| `admin`  | `admin123`  | ROLE_ADMIN   | Повний доступ (view, create, update, delete) |
| `manager`| `manager123`| ROLE_MANAGER | Перегляд, створення, оновлення, окрім travelers |
| `user`   | `user123`   | ROLE_USER    | Тільки перегляд |

---

##  Налаштування проєкту

### 1. Залежності (pom.xml)

```xml
<!-- Spring Security -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>

<!-- JWT -->
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
    <version>0.12.3</version>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-impl</artifactId>
    <version>0.12.3</version>
    <scope>runtime</scope>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-jackson</artifactId>
    <version>0.12.3</version>
    <scope>runtime</scope>
</dependency>
```

### 2. Конфігурація (application.properties)

```properties
# JWT Configuration
jwt.secret=
jwt.expiration=

```

---

## Принцип роботи JWT Authentication

### 1. Отримання токена

Користувач відправляє username і password на `/api/auth/login`:

```
POST /api/auth/login
{
  "username": "admin",
  "password": "admin123"
}
```

**Відповідь:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiUk9MRV9BRE1JTiIsInN1YiI6ImFkbWluIn0...",
  "username": "admin",
  "role": "ROLE_ADMIN"
}
```

### 2. Використання токена

Для доступу до захищених ендпоінтів токен додається в заголовок:

```
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

### 3. Перевірка токена

`JwtAuthenticationFilter` автоматично:
- Витягує токен з заголовка
- Перевіряє підпис і термін дії
- Встановлює аутентифікацію в Spring Security Context

### 4. Перевірка ролей

Spring Security перевіряє чи має користувач необхідну роль через:
- `@PreAuthorize` анотації в контролерах
- Правила в `SecurityConfig`

---


## Тестування через Postman

### 1. Отримання JWT токена

**Запит:**
```
POST http://localhost:8080/api/auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "admin123"
}
```

**Відповідь (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "username": "admin",
  "role": "ROLE_ADMIN"
}
```


---

### 2. Публічні ендпоінти (без токена)

**Запит:**
```
GET http://localhost:8080/api/destinations/public/all
```

**Результат:**  200 OK (токен не потрібен)

---

### 3. Захищені ендпоінти (з токеном)

**Запит:**
```
GET http://localhost:8080/api/destinations/manager/count
Authorization: Bearer {{jwt_token}}
```

**Результати:**
- З токеном MANAGER або ADMIN: -200 OK
- З токеном USER: - 403 Forbidden
- Без токена: - 401 Unauthorized

---
###  Нові файли:

1. **security/config/SecurityConfig.java** - конфігурація Spring Security
2. **security/util/JwtUtil.java** - утиліта для JWT токенів
3. **security/JwtAuthenticationFilter.java** - фільтр перевірки токенів
4. **security/CustomUserDetailsService.java** - сервіс користувачів
5. **security/AuthController.java** - контролер аутентифікації
6. **security/AuthRequest.java** - DTO запиту логіну
7. **security/AuthResponse.java** - DTO відповіді з токеном

###  Оновлені файли:

1. **controller/DestinationController.java** - додано `@PreAuthorize` анотації
2. **controller/TripController.java** - додано контроль доступу
3. **controller/TravelerController.java** - додано контроль доступу
4. **pom.xml** - додано залежності Spring Security і JWT
5. **application.properties** - додано JWT конфігурацію
