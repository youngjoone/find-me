CREATE TABLE subscriptions (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE,
    plan VARCHAR(255) NOT NULL,
    status VARCHAR(255) NOT NULL,
    current_period_end TIMESTAMP NOT NULL
);
