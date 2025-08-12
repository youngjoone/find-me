package com.vibe.payment.payment.dto;

import lombok.Data;

@Data
public class WebhookRequest {
    private Long userId;
    private String eventType; // e.g., "payment_success"
    // In a real webhook, there would be more fields like transaction ID, amount, etc.
}
