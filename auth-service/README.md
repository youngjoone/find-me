# Auth Service

## Environment Variables

The following environment variables are required for the Auth Service:

- `DB_URL`: PostgreSQL database connection URL (e.g., `jdbc:postgresql://db-auth:5432/auth`)
- `DB_USER`: PostgreSQL database username
- `DB_PASS`: PostgreSQL database password
- `JWT_SECRET`: Secret key for JWT signing (base64 encoded, at least 32 characters long)
- `JWT_ISSUER`: Issuer of the JWT (e.g., `http://auth.local`)

## API Endpoints

### Register User

**POST /auth/register**

Registers a new user.

**Request Body:**
```json
{
  "email": "test@example.com",
  "password": "password123",
  "nickname": "TestUser"
}
```

**Example cURL:**
```bash
curl -X POST http://localhost/auth/register \
-H "Content-Type: application/json" \
-d '{
  "email": "test@example.com",
  "password": "password123",
  "nickname": "TestUser"
}'
```

### Login User

**POST /auth/login**

Authenticates a user and returns JWT tokens.

**Request Body:**
```json
{
  "email": "test@example.com",
  "password": "password123"
}
```

**Example cURL:**
```bash
curl -X POST http://localhost/auth/login \
-H "Content-Type: application/json" \
-d '{
  "email": "test@example.com",
  "password": "password123"
}'
```

**Response Body (Success):**
```json
{
  "accessToken": "...",
  "refreshToken": "...",
  "expiresIn": 3600000
}
```

### JWKS Endpoint

**GET /.well-known/jwks.json**

Returns the JSON Web Key Set (JWKS) containing the public key used for JWT verification.

**Example cURL:**
```bash
curl http://localhost/.well-known/jwks.json
```

### Health Check

**GET /auth/health**

Checks the health of the Auth Service.

**Example cURL:**
```bash
curl http://localhost/auth/health
```