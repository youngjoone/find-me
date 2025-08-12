# Payment Service

## Environment Variables

The following environment variables are required for the Payment Service:

- `DB_URL`: PostgreSQL database connection URL (e.g., `jdbc:postgresql://db-payment:5432/payment`)
- `DB_USER`: PostgreSQL database username
- `DB_PASS`: PostgreSQL database password

## API Endpoints

### Subscribe

**POST /pay/subscribe**

Initiates a subscription process (stub).

**Request Body:**
```json
{
  "userId": 1,
  "plan": "premium"
}
```

**Example cURL:**
```bash
curl -X POST http://localhost/pay/subscribe \
-H "Content-Type: application/json" \
-d '{
  "userId": 1,
  "plan": "premium"
}'
```

**Response Body (Success):**
```json
"stub-1234567890"
```

### Webhook

**POST /pay/webhook**

Handles webhook notifications to update subscription status (stub).

**Request Body:**
```json
{
  "userId": 1,
  "eventType": "payment_success"
}
```

**Example cURL:**
```bash
curl -X POST http://localhost/pay/webhook \
-H "Content-Type: application/json" \
-d '{
  "userId": 1,
  "eventType": "payment_success"
}'
```

### Get Subscription Status

**GET /pay/status**

Retrieves the current subscription status for a user.

**Query Parameters:**
- `userId`: The ID of the user.

**Example cURL:**
```bash
curl http://localhost/pay/status?userId=1
```

**Response Body (Success):**
```json
{
  "plan": "premium",
  "status": "active"
}
```

### Health Check

**GET /pay/health**

Checks the health of the Payment Service.

**Example cURL:**
```bash
curl http://localhost/pay/health
```

