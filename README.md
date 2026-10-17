# Personal Expense Tracker

## 1. Project Overview

A personal expense tracking REST API built using Java and Spring Boot. The application allows users to record their income and expenses, track their calculated balance, view transaction history, and analyze their spending.

The application relies on manually entered transactions. It does not connect to bank accounts or verify real-world financial transactions.

## 2. MVP Goals

- User registration and login.
- Role-based access control for users and administrators.
- JWT authentication using access and refresh tokens.
- Create, read, update, and delete expenses.
- Record incoming money, including an initial balance entry.
- Calculate the current balance from transaction history.
- View transaction history with pagination and filtering.
- Provide basic financial analysis.

## 3. Technology Stack

- **Language:** Java
- **Backend:** Spring Boot
- **Database:** MySQL
- **ORM:** Spring Data JPA / Hibernate
- **Build Tool:** Maven
- **Authentication:** Spring Security + JWT
- **API Testing:** Postman
- **Additional Library:** Lombok (optional)

PostgreSQL may be explored in the future. MySQL will be used for the MVP.

## 4. Domain Rules

1. Users manually record their income and expenses.
2. The application tracks reported financial activity; it does not verify actual bank balances.
3. Users can record expenses regardless of their current calculated balance.
4. Negative balances are allowed.
5. The current balance is derived from the recorded transaction history.
6. Editing or deleting a transaction changes the calculated balance accordingly.
7. No independently stored current balance is required for the MVP.
8. Currency defaults to INR (₹). Multiple currencies are outside the initial scope.
10. Users must not access or modify another user's financial records unless explicitly authorized.

### Balance Calculation

**Current Balance = Total Credits − Total Expenses**

The initial balance is represented by a credit entry created by the user when they start tracking their finances.

Example:

| Transaction | Amount |
|---|---:|
| Initial credit | ₹10,000 |
| Salary credit | ₹5,000 |
| Food expense | ₹1,000 |
| Travel expense | ₹500 |
| **Current balance** | **₹13,500** |

The balance will be calculated using database aggregate queries rather than fetching the entire transaction history into Java memory.

## 5. Database Entities

### 5.1 User

Represents an application user.

| Field | Description |
|---|---|
| `id` | Primary key |
| `username` | Unique username |
| `password` | Hashed password |
| `role_id` | Foreign key referencing Role |


### 5.2 Role

Represents a user's application role.

| Field | Description |
|---|---|
| `id` | Primary key |
| `name` | Role name, such as `USER` or `ADMIN` |


### 5.3 Token

Stores refresh-token information for authentication.

| Field | Description |
|---|---|
| `id` | Primary key |
| `user_id` | Foreign key referencing User |
| `token_hash` | Hash of the refresh token |
| `expires_at` | Token expiration timestamp |



**Design note:** Use suitable database constraints and indexes, particularly for username uniqueness, foreign keys, and transaction queries filtered by user and date.

## 6. API Endpoints

The following endpoints define the initial API scope.

### 6.1 Authentication

| Method | Endpoint | Description |
|---|---|---|
| POST | `/auth/v1/register` | Register a user |
| POST | `/auth/v1/login` | Authenticate a user |
| POST | `/auth/v1/refresh-token` | Refresh an access token |


## 7. Suggested Spring Boot Package Structure

```text
com.example.expensetracker
├── config
├── controller
├── dto
├── entity
├── exception
├── repository
├── service
├── service.impl
├── security
└── ExpenseTrackerApplication.java
```

Responsibilities:

- **controller:** REST endpoints and HTTP request handling.
- **dto:** Request and response objects.
- **entity:** JPA entities and database relationships.
- **repository:** Database access and aggregate queries.
- **service:** Business rules and application logic.
- **exception:** Custom exceptions and centralized exception handling.
- **security:** JWT processing, authentication, and authorization.
- **config:** Application and infrastructure configuration.

## 8. Implementation Plan

### Phase 1: Project Setup

- Create the Spring Boot project.
- Configure MySQL and Maven.
- Add Spring Web, Spring Data JPA, MySQL Driver, Validation, and Lombok if desired.
- Configure database properties.
- Create the initial entities and relationships.

### Phase 2: Expense and Credit APIs

- Create repositories and service classes.
- Implement create, read, update, and delete operations.
- Introduce request and response DTOs.
- Add input validation and exception handling.
- Test APIs using Postman.

### Phase 3: Balance and Analysis

- Calculate total credits and expenses using database queries.
- Implement the current balance calculation.
- Add pagination, filtering, and sorting.
- Implement basic category and period summaries.

### Phase 4: Authentication

- Implement user registration and login.
- Hash passwords securely.
- Integrate Spring Security and JWT.
- Implement access-token validation.
- Implement refresh-token expiration, rotation, and revocation.

### Phase 5: Authorization and Ownership

- Introduce role-based access control.
- Restrict administrative endpoints to administrators.
- Ensure users can only access their own financial records.
- Test unauthorized and cross-user access attempts.

### Phase 6: Testing and Finalization

- Test all CRUD endpoints.
- Test balance calculations after creating, editing, and deleting transactions.
- Test negative-balance scenarios.
- Test pagination and filters.
- Test authentication, authorization, and refresh-token behavior.
- Document API usage and setup instructions.

## 9. Out of Scope for the MVP

The following features may be considered later:

- Bank account integration and automatic transaction imports.
- Multiple currencies.
- User-defined categories.
- Budgeting and spending limits.
- Recurring transactions.
- Financial recovery goals and advanced insights.
- Persisted or cached current balances.
- Balance reconciliation and performance optimizations.
- Advanced permission management.
- Microservices architecture.

## 10. Definition of Done

The MVP is considered functional when:

- Users can register and authenticate.
- Access and refresh tokens work according to the defined security rules.
- Users can create, view, update, and delete credits and expenses.
- Users cannot access another user's records.
- Current balance is calculated correctly from recorded transactions.
- Negative balances are permitted.
- Transaction edits and deletions are reflected in balance calculations.
- Users can paginate and filter their transaction history.
- Basic financial summaries are available through the API.
- The application runs locally with MySQL and has documented setup instructions.

---

**Current priority:** Build the basic Spring Boot application with MySQL, implement the transaction APIs, and calculate balances from the transaction history. Add authentication and authorization after the basic API flow is working.
