# Vibe MSA Project

This is a monorepo for the Vibe Microservices Architecture (MSA) project, implementing a survey-based profiling and AI coaching web service.

## Project Structure

```
/vibe-msaproject
  /gateway/              # Nginx (API Gateway)
  /web/                  # Next.js (Frontend)
  /auth-service/         # Spring Boot (Auth, User Management)
  /profile-service/      # FastAPI (Survey Analysis, AI Integration)
  /payment-service/      # Spring Boot (Payment, Subscription)
  /ops/                  # Docker Compose, operational scripts
  /docs/                 # API documentation, Postman collection, etc.
```

## Getting Started (Local Development)

### Prerequisites

- Docker Desktop (or Docker Engine) installed and running.

### Setup

1.  **Clone the repository:**
    ```bash
    git clone https://github.com/youngjoone/find-me.git
    cd find-me/vibe-msaproject
    ```
2.  **Create `.env` files:**
    Copy the `.env.sample` files to `.env` (or `.env.local` for web) in each service directory and fill in the required values.

    ```bash
    cp auth-service/.env.sample auth-service/.env
    cp profile-service/.env.sample profile-service/.env
    cp payment-service/.env.sample payment-service/.env
    cp web/.env.local.sample web/.env.local
    ```
    *Note: For `JWT_SECRET` in `auth-service/.env`, generate a secure base64 encoded string (e.g., `openssl rand -base64 32`).*

3.  **Start Services:**
    Navigate to the `ops` directory and start all services using Docker Compose:
    ```bash
    cd ops
    docker compose up --build -d
    ```
    This will build the Docker images for each service and start them in the background.

### Accessing Services

-   **Web Frontend:** `http://localhost:3000`
-   **API Gateway:** `http://localhost`

## Health Checks

You can check the health of individual services via the API Gateway:

-   **Auth Service:**
    ```bash
    curl http://localhost/auth/health
    ```
-   **Profile Service:**
    ```bash
    curl http://localhost/profile/health
    ```
-   **Payment Service:**
    ```bash
    curl http://localhost/pay/health
    ```

## API Documentation

A Postman/Insomnia collection is available in `docs/api-collection.json`. You can import this file into your API client to test the endpoints.

## Load Testing (Example)

For basic load testing, you can use tools like `ApacheBench` (`ab`) or `k6`.

**Example with `ab` (for health check endpoint):**
```bash
# Install ApacheBench if not already installed (e.g., `brew install apache-httpd` on macOS)
ab -n 100 -c 10 http://localhost/profile/health
```
*Note: Ensure the target service is running before performing load tests.*

## Rollback Guide

In case of issues after a deployment, you can rollback to a previous stable version by:

1.  **Stopping current services:**
    ```bash
    cd ops
    docker compose down
    ```
2.  **Checking out previous Git commit:**
    ```bash
    git checkout <previous_commit_hash>
    ```
3.  **Rebuilding and restarting services:**
    ```bash
    docker compose up --build -d
    ```
*Note: This is a simplified rollback. For production, consider proper versioning of Docker images and orchestrated rollbacks.*

## Known Issues / Constraints

-   **Spring Boot `jarfile` issue**: `auth-service` and `payment-service` currently fail to start with `Error: Unable to access jarfile /app.jar` in the Docker environment. This prevents full end-to-end testing of authentication and payment functionalities. This issue appears to be related to the local Docker environment configuration.
-   **JWT JWKS Implementation**: The `auth-service` currently uses HS256 for JWT signing. The specification requires RSA/ECDSA public key exposure via JWKS. This requires a change in the JWT implementation to use asymmetric keys.
-   **Payment Service Stub**: The `payment-service` is currently a stub and does not integrate with a real payment gateway.
-   **AI Integration**: The AI features in `profile-service` are not yet fully implemented and integrated with real LLM APIs.
