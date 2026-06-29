<<<<<<< HEAD
# Booking System

A RESTful booking system built with Spring Boot, PostgreSQL and JWT authentication.

---

## Tech Stack

- Java 17, Spring Boot 3.3
- PostgreSQL
- Spring Security + JWT
- Lombok, Springdoc OpenAPI

---

## Prerequisites

- Java 17+
- Maven 3.8+
- PostgreSQL

---

## How to Run

**Step 1** — Create database
```sql
CREATE DATABASE booking_db;
```

**Step 2** — Update `application.properties`
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/booking_db
spring.datasource.username=postgres
spring.datasource.password=yourpassword

app.jwt.secret=404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970
app.jwt.expiration-ms=86400000
```

**Step 3** — Run
```bash
./mvnw spring-boot:run
```

- App: `http://localhost:8080`
- Swagger: `http://localhost:8080/swagger-ui.html`

---

## Seed Users

Created automatically on first boot.

| Username | Password | Role  |
|----------|----------|-------|
| admin    | admin123 | ADMIN |
| user     | user123  | USER  |

---

## Authentication

**Login**
```
POST /auth/login
```
```json
{
  "username": "admin",
  "password": "admin123"
}
```
**Response**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "username": "admin",
  "role": "ADMIN"
}
```

**Use token in every request**
```
Authorization: Bearer <token>
```

---

## API Usage

### Resources

**Get all resources**
```
GET /resources
```

**Get single resource**
```
GET /resources/{id}
```

**Create resource** — ADMIN only
```
POST /resources
```
```json
{
  "name": "Conference Room A",
  "type": "ROOM",
  "description": "10 person meeting room",
  "capacity": 10,
  "active": true
}
```

**Update resource** — ADMIN only
```
PUT /resources/{id}
```
```json
{
  "name": "Conference Room A",
  "type": "ROOM",
  "description": "Updated description",
  "capacity": 15,
  "active": true
}
```

**Delete resource** — ADMIN only
```
DELETE /resources/{id}
```

---

### Reservations

**Get all reservations**
```
GET /reservations
```
- ADMIN sees all
- USER sees only their own

**Filter reservations**
```
GET /reservations?status=PENDING&minPrice=100&maxPrice=500&page=0&size=10&sort=createdAt,desc
```

| Param | Example |
|-------|---------|
| status | PENDING, CONFIRMED, CANCELLED |
| minPrice | 100 |
| maxPrice | 500 |
| page | 0 |
| size | 10 |
| sort | createdAt,desc |

**Get single reservation**
```
GET /reservations/{id}
```

**Create reservation**
```
POST /reservations
```
```json
{
  "resourceId": 1,
  "price": 150.00,
  "startTime": "2026-07-01T09:00:00Z",
  "endTime": "2026-07-01T11:00:00Z"
}
```

**Update reservation**
```
PUT /reservations/{id}
```
```json
{
  "status": "CONFIRMED",
  "price": 200.00,
  "startTime": "2026-07-01T09:00:00Z",
  "endTime": "2026-07-01T11:00:00Z"
}
```

**Delete reservation**
```
DELETE /reservations/{id}
```

---

## Roles & Access

| Action | ADMIN | USER |
|--------|-------|------|
| Login | ✅ | ✅ |
| View resources | ✅ | ✅ |
| Create / Edit / Delete resource | ✅ | ❌ |
| View all reservations | ✅ | ❌ |
| View own reservations | ✅ | ✅ |
| Create reservation | ✅ | ✅ |
| Confirm reservation | ✅ | ❌ |
| Cancel own reservation | ✅ | ✅ |

---

## Assumptions & Trade-offs

- Overlap check only applies to CONFIRMED reservations. Two PENDING bookings for the same slot are allowed.
- Hard delete on resources. Soft delete (active = false) would be better in production.
- userId on POST /reservations always comes from JWT — not from request body.
- Single role per user since only ADMIN and USER are required.
=======
# Booking-System-for-Technical-Round
>>>>>>> fec0a59a0d9a881e0739685ad19be135eaf312b6
