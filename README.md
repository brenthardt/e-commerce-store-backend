see project ui here: https://e-commerce-store-by-baxrom.netlify.app/ <br/>
see frontend here: https://github.com/brenthardt/e-commerce-store-frontend.git <br/>
see figma here : https://www.figma.com/design/xyT6B6th4ucBzsheOmXLyK/Full-E-Commerce-Website-UI-UX-Design--Community-?node-id=34-213&t=zch2BaIb6VoTTgxl-0 <br/>


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


---

<p align="center">
  <img src="https://drive.google.com/uc?export=view&id=1tqhH4ZVXCbhvZkLP4c_8IqIIQPCDxvBF" alt="Home Page" width="400"/>
  <img src="https://drive.google.com/uc?export=view&id=1OXznc4NtUsdu3-pTlGLqKQYFGMG9_OGV" alt="Image 2" width="400"/>
</p>

<p align="center">
  <img src="https://drive.google.com/uc?export=view&id=1Czu3xlreq2HDARvFgUctPgZduzBuMKIg" alt="Image 3" width="400"/>
  <img src="https://drive.google.com/uc?export=view&id=1ub8wg8cqaAw8mFBFts7Ii_PMQN-ZqNy6" alt="Image 4" width="400"/>
</p>

<p align="center">
  <img src="https://drive.google.com/uc?export=view&id=1aizLZjf052i2gh_grl9rLdsZyCOqmseu" alt="Image 5" width="400"/>
  <img src="https://drive.google.com/uc?export=view&id=1c6J2o5M1RHoYODb0YTVy9LYnodfGYTK9" alt="Image 6" width="400"/>
</p>

<p align="center">
  <img src="https://drive.google.com/uc?export=view&id=1SZa7p-P35hf8vhuistaukYGExygxhui8" alt="Image 7" width="400"/>
  <img src="https://drive.google.com/uc?export=view&id=1rRR7x8EvezTD5lSX2UlAG8Kg8JrEnDzE" alt="Image 8" width="400"/>
</p>

<p align="center">
  <img src="https://drive.google.com/uc?export=view&id=1nBFCATS3pEImonMWo6aUkg1CFAjm-c2p" alt="Image 9" width="400"/>
  <img src="https://drive.google.com/uc?export=view&id=1OSLBLH3X5SXx5IQ__QXcfCJN2oLKmScl" alt="Image 10" width="400"/>
</p>

<p align="center">
  <img src="https://drive.google.com/uc?export=view&id=17bvq8PKWig4Jj7Sih07zVMVL1ZXQCXaR" alt="Image 11" width="400"/>
</p>

---

## ⚙️ Tech Stack
- **Spring Boot 3**
- **Spring Security**
- **JWT Authentication**
- **Lombok**
- **PostgreSQL (typical setup)**
- **BCrypt for password hashing**



