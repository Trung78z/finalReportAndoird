# FastFood Management System

This repository contains a multi-module system for managing a fast food business, including a web client, backend server, and management tools.

## Project Structure

- **fastFoodClient/**: Frontend web application built with React, TypeScript, and Vite.
- **fastFoodServer/**: Backend REST API built with Spring Boot and Java.
- **management/**: Android management app (Java/Kotlin, Gradle).
- **db.sql**: Database schema and initialization scripts.

## Getting Started

### Prerequisites

- Java 21+
- Node.js 18+
- npm or yarn
- Android Studio (for management app)
- PostgreSQL

### Backend Setup (`fastFoodServer`)

1. Install dependencies and build:
    ```sh
    cd fastFoodServer
    make start
    ```
2. The server runs on `http://localhost:8080`.

#### Useful Endpoints

- `POST /api/auth/login` — User login
- `POST /api/auth/register` — User registration
- `GET /api/user/me` — Get current user info (requires JWT)
- `POST /api/auth/refresh` — Refresh access token

See [fastFoodServer/.rest](fastFoodServer/.rest) for example requests.

### Frontend Setup (`fastFoodClient`)

1. Install dependencies:
    ```sh
    cd fastFoodClient
    npm install
    ```
2. Start the development server:
    ```sh
    npm run dev
    ```
3. The app runs on `http://localhost:5173`.

### Management App (`management`)

1. Open `management/` in Android Studio.
2. Build and run on an emulator or device.

## Database

- The backend uses PostgreSQL.
- See [fastFoodServer/Supabase PostgreSQL.session.sql](fastFoodServer/Supabase%20PostgreSQL.session.sql) and [db.sql](db.sql) for schema and sample data.

## Development

- Backend uses Spring Boot, JPA, and JWT for authentication.
- Frontend uses React, TypeScript, and Vite.
- Management app is an Android application.

## License

This project is for educational purposes.
