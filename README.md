# Restaurant Review API

API REST desenvolvida com **Spring Boot** onde os utilizadores podem registar-se, iniciar sessão, adicionar restaurantes e avaliá-los consoante o cuidado com alergias alimentares (amendoim, ovo e lacticínios). As avaliações são moderadas por um administrador antes de contarem para as pontuações de um restaurante.

---

## Funcionalidades

- Registo e login de utilizadores com autenticação **JWT** (stateless)
- Palavras-passe com hashing **BCrypt**
- Papéis: `USER` e `ADMIN`
- Os utilizadores podem definir a cidade, o código postal e os seus interesses em alergias
- Adicionar restaurantes e pesquisá-los por nome + código postal
- Submeter avaliações (pontuações de 1 a 5 para amendoim, ovo e lacticínios + comentário opcional)
- O administrador aprova ou rejeita avaliações pendentes
- As pontuações dos restaurantes são recalculadas apenas a partir das avaliações **aceites**
- Pesquisa de restaurantes num código postal, ordenados por pontuação de alergia

## Stack tecnológica

| Área | Tecnologia |
|------|------------|
| Linguagem | Java 17+ |
| Framework | Spring Boot (Web, Data JPA, Validation, Security) |
| Autenticação | JWT (`com.auth0:java-jwt`) |
| Base de dados | H2 (desenvolvimento) |
| Boilerplate | Lombok |
| Build | Maven |

## Estrutura do projeto

```
src/main/java/com/netchitchi/dining
├── DiningApplication.java      # Ponto de entrada
├── config/                     # SecurityConfig (regras, CORS, password encoder)
├── controller/                 # Camada HTTP: recebe pedidos, devolve respostas
├── dto/                        # Objetos de pedido/resposta (o que entra/sai da API)
├── model/                      # Entidades JPA + enums (tabelas da base de dados)
├── repository/                 # Interfaces Spring Data JPA
├── security/                   # JwtFilter + TokenService
└── service/                    # Lógica de negócio
```

**Fluxo de um pedido:** `Controller → Service → Repository → Base de dados`. Os DTOs são usados nas fronteiras da API para que as entidades (por exemplo, o hash da palavra-passe) nunca sejam expostas.

## Como começar

### Clonar o projeto

```bash
git clone https://github.com/<your-username>/restaurant-review.git
cd restaurant-review
cp src/main/resources/application-example.properties src/main/resources/application.properties
```

### Pré-requisitos

- JDK 17 ou superior
- Maven (ou o wrapper `mvnw` incluído)

### Configuração

Cria o ficheiro `src/main/resources/application.properties` (este ficheiro **não** é commitado — vê o `.gitignore`). Usa o `application-example.properties` como modelo:

```properties
spring.datasource.url=jdbc:h2:mem:diningdb
spring.jpa.hibernate.ddl-auto=update
spring.h2.console.enabled=true

# Segredo usado para assinar os JWTs. Usa um valor aleatório e longo e NUNCA faças commit do valor real.
api.security.token.secret=${JWT_SECRET:change-me-to-a-long-random-string}

# Origem do frontend permitida pelo CORS
app.cors.allowed-origin=http://localhost:5173
```

### Executar

```bash
./mvnw spring-boot:run
```

A API arranca em `http://localhost:8080`.

## Autenticação

1. Registo: `POST /api/auth/register`
2. Login: `POST /api/auth/login` → devolve um token
3. Envia o token em todos os pedidos protegidos:

```
Authorization: Bearer <your-token>
```

Os tokens expiram ao fim de **2 horas**.

## Endpoints da API

### Auth (público)

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/api/auth/register` | Criar uma conta |
| POST | `/api/auth/login` | Iniciar sessão e receber um JWT |

### Utilizadores (autenticado)

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/api/users/me` | Obter o meu perfil |
| PUT | `/api/users/updateUser/me` | Atualizar a minha cidade, código postal e interesses em alergias |
| GET | `/api/users/users` | Listar todos os utilizadores (admin) |

### Restaurantes (autenticado)

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/api/restaurants/new` | Adicionar um restaurante |
| GET | `/api/restaurants/details?name=&zipCode=` | Obter um restaurante |
| GET | `/api/restaurants/search?zipCode=&allergy=` | Ordenar restaurantes pela pontuação de `peanut`, `egg` ou `dairy` |

### Avaliações (autenticado)

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/api/reviews/addReview` | Submeter uma avaliação (começa como `PENDING`) |
| GET | `/api/reviews/pendingReviews` | Listar avaliações pendentes (admin) |
| PUT | `/api/reviews/{id}` | Aceitar ou rejeitar uma avaliação (admin) |

### Exemplos de pedidos

**Registo**
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

**Adicionar uma avaliação**
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

**Aprovar uma avaliação (admin)**
```json
PUT /api/reviews/1
{ "acceptedReview": true }
```

## Regras de validação

- Formato do código postal: `1000-001`
- Pontuações: números inteiros de 1 a 5
- Comentário: até 200 caracteres
- Nome: 3–40 caracteres · Palavra-passe: 8–20 caracteres

## Roadmap

- [ ] Frontend em React + Shadcn UI
- [ ] Testes unitários e de integração
- [ ] Migrar de H2 para uma base de dados persistente (PostgreSQL)
- [ ] Global exception handler que devolva `ApiError`

## Autor

**Edson** — [netchitchiofc@gmail.com](mailto:netchitchiofc@gmail.com)
