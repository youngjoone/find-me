package com.vibe.payment.payment.dto;

import lombok.Data;

@Data
public class SubscribeRequest {
    private Long userId;
    private String plan; // e.g., "premium"
}
