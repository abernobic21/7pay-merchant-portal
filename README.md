# Merchant Portal

A full-stack merchant portal for viewing payment data: merchants, sale points, terminals and transactions. It also covers user management, reports and two-factor authentication.

It is built as a set of Spring Boot microservices behind an Nginx gateway, with a React + TypeScript frontend. Merchant and transaction data come from an external merchant test API; the portal stores only its own users and their auth data.

> **Note:** This repository is a showcase of the code. The app depends on a private merchant test API that isn't publicly available, so it can't be run as-is.

## Features

- **Authentication**: JWT access and refresh tokens in HTTP-only cookies, token blacklisting on logout, and account lockout after repeated failed logins (all held in Redis)
- **Two-factor authentication**: TOTP setup with a QR code, plus one-time backup codes. TOTP secrets are encrypted with AES-GCM
- **Password flows**: set a password on first login, forgot password, reset password
- **Dashboard**: transaction statistics and charts
- **Merchants, sale points, terminals, transactions**: list and detail views with filtering
- **Reports**: generate transaction reports as **CSV, JSON or PDF**
- **User management**: create, edit, deactivate and delete users, with `ADMIN` / `USER` roles and `MERCHANT` / `BRANCH` access levels
- **i18n**: English and Croatian

## Architecture

```
                    ┌──────────────┐
  Browser ────────► │ Nginx :8080  │
                    └──────┬───────┘
     ┌──────────┬──────────┼──────────────┬───────────────┐
     ▼          ▼          ▼              ▼               ▼
 frontend   auth-service  user-mgmt    core-service   report-service
  :5173       :8081       :8083          :8082           :8084
               │  │         │              │               │
               │  └─ Redis ─┘──────────────┘               │
               └── PostgreSQL ┘            └── Merchant test API ──┘
```

| Service | Responsibility |
|---|---|
| `frontend` | React SPA |
| `auth-service` | Login, tokens, 2FA, password flows. Other services call it to authenticate each request |
| `user-management-service` | User CRUD and profile |
| `core-service` | Dashboard, merchants, sale points, terminals, transactions (proxies the merchant API) |
| `report-service` | Report generation |
| PostgreSQL | Users, password resets, 2FA secrets and backup codes ([schema](database/init.sql)) |
| Redis | Refresh tokens, token blacklist, login lockout |

Nginx routes `/api/v1/auth`, `/api/v1/users` and `/api/v1/reports` to their services, sends the rest of `/api/v1` to `core-service`, and serves the frontend at `/`. Everything runs with Docker Compose, and all secrets are read from environment variables.

## Tech stack

**Backend:** Java 21, Spring Boot 4, Spring Security, Spring Data JPA, jjwt, Redis, PostgreSQL, Lombok
**Frontend:** React 18, TypeScript, Vite, TanStack Query, React Router, Axios, Recharts, i18next
**Infra:** Docker Compose, Nginx

## API overview

All routes are under `/api/v1`.

| Area | Endpoints |
|---|---|
| Auth | `POST auth/login`, `auth/logout`, `auth/refresh`, `auth/forgot-password`, `auth/reset-password`, `auth/set-password`, `GET auth/validate` |
| 2FA | `POST auth/2fa/setup`, `auth/2fa/verify-setup`, `auth/2fa/validate`, `auth/2fa/validate-backup`, `auth/2fa/disable` |
| Users | `GET/POST users`, `GET/PUT/DELETE users/{id}`, `PATCH users/{id}/status`, `GET users/me`, `PUT users/me/password` |
| Dashboard | `GET dashboard/stats` |
| Merchants | `GET merchants`, `GET merchants/{oib}` |
| Sale points | `GET salepoints`, `GET salepoints/{mid}` |
| Terminals | `GET terminals`, `GET terminals/{tid}` |
| Transactions | `GET transactions`, `GET transactions/{transactionId}` |
| Reports | `POST reports/generate` |

## Project structure

```
├── database/            # PostgreSQL schema
├── frontend/            # React + Vite app (feature-based folders under src/features)
├── nginx/               # Gateway config
├── services/
│   ├── auth-service/
│   ├── core-service/
│   ├── report-service/
│   └── user-management-service/
└── docker-compose.yaml
```
