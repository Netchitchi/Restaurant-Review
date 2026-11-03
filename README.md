# Restaurant Review API

A REST API built with **Spring Boot** where users can register, log in, add restaurants and review them based on how well they handle food allergies (peanut, egg and dairy). Reviews are moderated by an admin before they count towards a restaurant's scores.

> 🇵🇹 **Resumo:** API REST em Spring Boot para avaliar restaurantes consoante o cuidado com alergias (amendoim, ovo, lacticínios). Autenticação com JWT; as avaliações têm de ser aprovadas por um administrador.

---

## Features / Funcionalidades

- User registration and login with **JWT** authentication (stateless)
- Passwords hashed with **BCrypt**
- Roles: `USER` and `ADMIN`
- Users can set their city, zip code and allergy interests
- Add restaurants and look them up by name + zip code
- Submit reviews (scores 1–5 for peanut, egg and dairy + optional comment)
- Admin approves or rejects pending reviews
- Restaurant scores are recalculated from **accepted** reviews only
- Search restaurants in a zip code ranked by allergy score

## Tech stack

| Area | Technology |
|------|------------|
| Language | Java 17+ |
| Framework | Spring Boot (Web, Data JPA, Validation, Security) |
| Auth | JWT (`com.auth0:java-jwt`) |
| Database | H2 (development) |
| Boilerplate | Lombok |
| Build | Maven |

## Project structure

```
src/main/java/com/netchitchi/dining
├── DiningApplication.java      # Entry point
├── config/                     # SecurityConfig (rules, CORS, password encoder)
├── controller/                 # HTTP layer: receives requests, returns responses
├── dto/                        # Request/response objects (what goes in/out of the API)
├── model/                      # JPA entities + enums (database tables)
├── repository/                 # Spring Data JPA interfaces
├── security/                   # JwtFilter + TokenService
└── service/                    # Business logic
```

**Request flow:** `Controller → Service → Repository → Database`. DTOs are used at the edges so entities (e.g. the password hash) are never exposed.

## Getting started

### Clone the project

```bash
git clone https://github.com/<your-username>/restaurant-review.git
cd restaurant-review
cp src/main/resources/application-example.properties src/main/resources/application.properties
```

### Prerequisites

- JDK 17 or newer
- Maven (or use the included `mvnw` wrapper)

### Configuration

Create `src/main/resources/application.properties` (this file is **not** committed — see `.gitignore`). Use `application-example.properties` as a template:

```properties
spring.datasource.url=jdbc:h2:mem:diningdb
spring.jpa.hibernate.ddl-auto=update
spring.h2.console.enabled=true

# Secret used to sign JWTs. Use a long random value and NEVER commit the real one.
api.security.token.secret=${JWT_SECRET:change-me-to-a-long-random-string}

# Frontend origin allowed by CORS
app.cors.allowed-origin=http://localhost:5173
```

### Run

```bash
./mvnw spring-boot:run
```

The API starts on `http://localhost:8080`.

## Authentication

1. Register: `POST /api/auth/register`
2. Log in: `POST /api/auth/login` → returns a token
3. Send the token on every protected request:

```
Authorization: Bearer <your-token>
```

Tokens expire after **2 hours**.

## API endpoints

### Auth (public)

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/auth/register` | Create an account |
| POST | `/api/auth/login` | Log in and receive a JWT |

### Users (authenticated)

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/users/me` | Get my profile |
| PUT | `/api/users/updateUser/me` | Update my city, zip code and allergy interests |
| GET | `/api/users/users` | List all users (admin) |

### Restaurants (authenticated)

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/restaurants/new` | Add a restaurant |
| GET | `/api/restaurants/details?name=&zipCode=` | Get one restaurant |
| GET | `/api/restaurants/search?zipCode=&allergy=` | Rank restaurants by `peanut`, `egg` or `dairy` score |

### Reviews (authenticated)

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/reviews/addReview` | Submit a review (starts as `PENDING`) |
| GET | `/api/reviews/pendingReviews` | List pending reviews (admin) |
| PUT | `/api/reviews/{id}` | Accept or reject a review (admin) |

### Example requests

**Register**
```json
POST /api/auth/register
{
  "name": "Maria Silva",
  "password": "supersecret1",
  "city": "Lisboa",
  "state": "Lisboa",
  "zipCode": "1000-001",
  "peanutAllergiesInterest": true,
  "eggAllergiesInterest": false,
  "dairyAllergiesInterest": false
}
```

**Add a review**
```json
POST /api/reviews/addReview
{
  "restaurantId": 1,
  "peanutScore": 5,
  "eggScore": 4,
  "dairyScore": 3,
  "commentary": "Staff were very aware of allergens."
}
```

**Approve a review (admin)**
```json
PUT /api/reviews/1
{ "acceptedReview": true }
```

## Validation rules

- Zip code format: `1000-001`
- Scores: integers from 1 to 5
- Comment: up to 200 characters
- Name: 3–40 characters · Password: 8–20 characters

## Roadmap

- [ ] React + Shadcn UI frontend
- [ ] Unit and integration tests
- [ ] Switch from H2 to a persistent database (PostgreSQL)
- [ ] Global exception handler returning `ApiError`

## Author

**Edson** — [netchitchiofc@gmail.com](mailto:netchitchiofc@gmail.com)