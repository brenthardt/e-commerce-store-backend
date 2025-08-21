see project here: https://e-commerce-store-by-baxrom.netlify.app/


# E-Commerce Store Backend

This is the backend for an **E-Commerce Store** built with **Spring Boot**, featuring **JWT authentication**, **role-based access control**, and **CORS configuration**.

---

## 🔐 Security Configuration

The security is handled inside [`SecurityConfig.java`](src/main/java/org/example/ecommercestore_backend/config/SecurityConfig.java).  
It uses **Spring Security**, **JWT filters**, and **CORS** to protect the APIs.

### Key Features
- **JWT Authentication**: Every request is filtered through a custom `JwtFilter`.
- **Password Encoding**: Uses `BCryptPasswordEncoder` for secure password storage.
- **CORS Support**: Configured to allow requests from any origin (`*`), supporting headers `Authorization` and `Content-Type`.
- **CSRF Disabled**: Disabled since JWT is used.
- **Role-Based Access Control**:
  - `ROLE_ADMIN` required for certain update operations.
  - Public access allowed for signup, login, refresh, and product browsing.

---

## 📌 API Authorization Rules

| Method   | Endpoint(s)                                                                                   | Access         |
|----------|------------------------------------------------------------------------------------------------|----------------|
| `POST`   | `/users/signup`, `/users/login`, `/users/refresh`                                             | Public         |
| `GET`    | `/users/**`, `/product/**`, `/image/**`, `/files/**`, `/cart/**`, `/wishlist/**`, `/checkout/**`, `/products/{productId}/ratings` | Public         |
| `POST`   | `/users/**`, `/mail/**`, `/product/**`, `/cart/**`, `/wishlist/**`, `/checkout/**`, `/products/{productId}/ratings` | Public         |
| `PUT`    | `/users/**`, `/product/**`, `/cart/**`, `/wishlist/**`, `/checkout/**`                        | `ROLE_ADMIN`   |
| `DELETE` | `/users/**`, `/product/**`, `/cart/**`, `/wishlist/**`, `/checkout/**`, `/files/**`           | Public         |
| `OPTIONS`| `/**`                                                                                         | Public         |
| Any      | Others                                                                                        | Authenticated  |

---

## ⚙️ Tech Stack
- **Spring Boot 3**
- **Spring Security**
- **JWT Authentication**
- **Lombok**
- **PostgreSQL (typical setup)**
- **BCrypt for password hashing**



